package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.*;
import java_all.mybatis_domin.mapper_class.AccountDeal;
import java_all.mybatis_domin.mapper_class.AccountProperty;
import java_all.mybatis_domin.mapper_class.RealNameMsg;
import java_all.mybatis_domin.service.AccountDealService;
import java_all.mybatis_domin.service.AccountPropertyService;
import java_all.mybatis_domin.service.BaseMsgService;
import java_all.mybatis_domin.service.RealNameMsgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhijing on 2018/10/17.
 */

@Controller
public class TransferController {

    private String nowDate;

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    @ResponseBody

    public void transfer(HttpServletRequest request, HttpServletResponse response) throws IOException{

        nowDate = DateUtils.nowDate();

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        String transferType = jsonObject.getString("transferType");


        Map<String,Object> map;

        if (transferType == null){
            map = new HashMap<String, Object>();
            map.put(HttpResponseConstant.RSPCODE, "1001");
            map.put(HttpResponseConstant.RSPMSG,"缺少transferType参数");
        }else if (transferType.equals("0")){
          map = transferToAccount(jsonObject);
        }else {
            map = transferToBankCard(jsonObject);
        }

        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.getWriter().print(JSON.toJSONString(map));

    }

    public Map<String, Object> transferToAccount(JSONObject jsonObject){

        Map<String, Object> map = new HashMap<String, Object>();

        final String accountId = jsonObject.getString("accountId");
//        String name = jsonObject.getString("name");
        final String toAccountId = jsonObject.getString("toAccountId");
        String toName = jsonObject.getString("toName");
        final String des = jsonObject.getString("des");
        final String amount = jsonObject.getString("amount");
        final String payType = jsonObject.getString("payType");
        final String transferType = jsonObject.getString("transferType");
        String bankNo;
        if (payType != null && payType.equals("1")){
             bankNo = jsonObject.getString("bankNo");
        }else {
            bankNo = null;
        }

        if (accountId == null || toAccountId == null || toName == null || amount == null || payType == null ||
                (payType.equals("1") && bankNo == null)){
            map.put(HttpResponseConstant.RSPCODE, "1001");
            map.put(HttpResponseConstant.RSPMSG,"缺少参数");
            return map;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId) ||
                     !AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(toAccountId)){
            map.put(HttpResponseConstant.RSPCODE, "1002");
            map.put(HttpResponseConstant.RSPMSG,"账户不存在");
            return map;
        }

        if (accountId.equals(toAccountId)){
            map.put(HttpResponseConstant.RSPCODE, "1003");
            map.put(HttpResponseConstant.RSPMSG,"不可自己相互转账");
            return map;
        }


        AccountPropertyService accountPropertyService = new AccountPropertyService();
        final AccountProperty accountProperty = accountPropertyService.gainAccountPropertyMessageBy(accountId);
        AccountProperty reserveAccountProperty = null;
        try{
            reserveAccountProperty = (AccountProperty) accountProperty.clone();
        }catch (Exception e){
            e.printStackTrace();
        }

        boolean result;
        boolean result1;

        if (payType.equals("0")){

            if (!AccountMessageUtil.whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(accountId) ||
                    accountProperty.getAvailableProperty() == null ||
                    Float.parseFloat(amount) > Float.parseFloat(accountProperty.getAvailableProperty())){
                map.put(HttpResponseConstant.RSPCODE, "1003");
                map.put(HttpResponseConstant.RSPMSG,"账户余额不足");
                return map;
            }else {

              result = toDealAccountProperty(accountPropertyService, accountProperty, amount);

            }

        }else {
            //假设银行扣款成功
            result = true;
        }


        if (result){

            if (!AccountMessageUtil.whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(toAccountId)){

                AccountProperty accountProperty1 = new AccountProperty();
                accountProperty1.setAccountId(toAccountId);
                accountProperty1.setAvailableProperty(DataFormatUtils.twoDecimalPlacesFormat(amount));
                accountProperty1.setTotalProperty(DataFormatUtils.twoDecimalPlacesFormat(amount));
                accountProperty1.setMonthlyIncome(DataFormatUtils.twoDecimalPlacesFormat(amount));
                accountProperty1.setMonthlySpending("0.00");
                accountProperty1.setUpdateTime(nowDate);
                result1 = accountPropertyService.insertAccountProperty(accountProperty1);

            }else {
                AccountProperty accountProperty1 = accountPropertyService.gainAccountPropertyMessageBy(toAccountId);
                result1 = toDealToaccountProperty(accountPropertyService, accountProperty1, amount);
            }

        }else {

            result1 = false;

        }


        String orderNo = OrderNoUtils.orderNo();

        if (result1){
            map.put(HttpResponseConstant.RSPCODE, "0000");
            map.put(HttpResponseConstant.RSPMSG,"转账成功");
            map.put("orderNo", orderNo);
            map.put("dealTime", nowDate);

            insertDealRecord(jsonObject,orderNo, true);

        }else {

            map.put(HttpResponseConstant.RSPCODE, "2222");
            map.put(HttpResponseConstant.RSPMSG,"出现异常");
            accountPropertyService.updateAccountProperty(reserveAccountProperty);

        }

        return map;
    }

    public Map<String, Object> transferToBankCard(JSONObject jsonObject){
        Map<String, Object> map = new HashMap<String, Object>();


        AccountDeal deal = JSONObject.toJavaObject(jsonObject, AccountDeal.class);

        if (deal.getAccountId() == null ||
                deal.getAmount() == null ||
                deal.getPayType() == null ||
                deal.getToBankNo() == null ||
                deal.getToBankName() == null ||
                deal.getToName() == null ||
                (deal.getPayType().equals("1") && (deal.getBankNo() == null || deal.getBankName() == null))){

            map.put(HttpResponseConstant.RSPCODE, "1001");
            map.put(HttpResponseConstant.RSPMSG,"缺少参数");
            return map;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(deal.getAccountId())){
            map.put(HttpResponseConstant.RSPCODE, "1002");
            map.put(HttpResponseConstant.RSPMSG,"账户不存在");
            return map;
        }

        AccountPropertyService accountPropertyService = new AccountPropertyService();
        final AccountProperty accountProperty = accountPropertyService.gainAccountPropertyMessageBy(deal.getAccountId());
        AccountProperty reserveAccountProperty = null;
        try{
            reserveAccountProperty = (AccountProperty) accountProperty.clone();
        }catch (Exception e){
            e.printStackTrace();
        }


        boolean result;
        boolean result1;

        if (deal.getPayType().equals("0")){

            if (!AccountMessageUtil.whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(deal.getAccountId()) ||
                    accountProperty.getAvailableProperty() == null ||
                    Float.parseFloat(deal.getAmount()) > Float.parseFloat(accountProperty.getAvailableProperty())){
                map.put(HttpResponseConstant.RSPCODE, "1003");
                map.put(HttpResponseConstant.RSPMSG,"账户余额不足");
                return map;
            }else {
                result = toDealAccountProperty(accountPropertyService, accountProperty, deal.getAmount());
            }

        }else {
            //假设银行扣款成功
            result = true;
        }


        if (result){
           result1 = true;
        }else {
            result1 = false;
        }


        String orderNo = OrderNoUtils.orderNo();

        if (result1){

            map.put(HttpResponseConstant.RSPCODE, "0000");
            map.put(HttpResponseConstant.RSPMSG,"转账成功");
            map.put("orderNo", orderNo);
            map.put("dealTime", nowDate);

            insertDealRecord(jsonObject,orderNo, true);

        }else {

            map.put(HttpResponseConstant.RSPCODE, "2222");
            map.put(HttpResponseConstant.RSPMSG,"出现异常");
            accountPropertyService.updateAccountProperty(reserveAccountProperty);

        }


        return map;
    }

    public void insertDealRecord(JSONObject jsonObject, final String orderNo, final boolean success){


        final String accountId = jsonObject.getString("accountId");
        final String toAccountId = jsonObject.getString("toAccountId");
        final String des = jsonObject.getString("des");
        final String amount = jsonObject.getString("amount");
        final String payType = jsonObject.getString("payType");
        final String transferType = jsonObject.getString("transferType");
        final String bankNo = jsonObject.getString("bankNo");
        final String toBankNo = jsonObject.getString("toBankNo");
        final String toBankName = jsonObject.getString("toBankName");
        final String bankName = jsonObject.getString("bankName");
        final String toName = jsonObject.getString("toName");

        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(new Runnable() {
            @Override
            public void run() {

                RealNameMsgService realNameMsgService = new RealNameMsgService();

                AccountDeal accountDeal = new AccountDeal();
                accountDeal.setOrderNo(orderNo);
                accountDeal.setAccountId(accountId);
                accountDeal.setDealTime(nowDate);
                accountDeal.setAmount(DataFormatUtils.twoDecimalPlacesFormat(amount));
                accountDeal.setPayType(payType);
                accountDeal.setTransferType(transferType);
                accountDeal.setDes(des);
                accountDeal.setBankNo(bankNo);
                accountDeal.setToAccountId(toAccountId);
                accountDeal.setBankName(bankName);
                accountDeal.setToBankNo(toBankNo);
                accountDeal.setToBankName(toBankName);
                accountDeal.setDealType("2");

                if (AccountMessageUtil.whetherTheAccountIsRealNameOrNotByAccountId(accountId)){
                    List<RealNameMsg> list = realNameMsgService.selectByAccountId(accountId);
                    if (list != null && list.size() > 0){
                        RealNameMsg realNameMsg = list.get(0);
                        accountDeal.setName(realNameMsg.getName());
                    }

                }

                if (toAccountId != null && AccountMessageUtil.whetherTheAccountIsRealNameOrNotByAccountId(toAccountId)){
                    List<RealNameMsg> list = realNameMsgService.selectByAccountId(toAccountId);
                    if (list != null && list.size() > 0){
                        RealNameMsg realNameMsg = list.get(0);
                        accountDeal.setToName(realNameMsg.getName());
                    }
                }else {
                    accountDeal.setToName(toName);
                }

                if (success){
                    accountDeal.setStatus("0");
                }else {
                    accountDeal.setStatus("1");
                }

                AccountDealService accountDealService = new AccountDealService();
                boolean result2 = accountDealService.insertAccountDeal(accountDeal);
                if (!result2){
                    System.out.print("交易记录插入失败, 交易编号：" + accountDeal.getOrderNo());
                }

            }
        });

    }


    public boolean toDealAccountProperty(AccountPropertyService accountPropertyService, AccountProperty accountProperty, String amount){


        double amountDouble = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(amount);
        double availableAmountDouble = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountProperty.getAvailableProperty());
        double residueDouble = availableAmountDouble - amountDouble;

        String residueString = DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(residueDouble));
        accountProperty.setAvailableProperty(residueString);
        accountProperty.setTotalProperty(residueString);

        String monthlySpending;
        String monthlyIncome;

        if (AccountMessageUtil.whetherTheAccountPropertyCanClearAllMonthlyIncomeOrSpending(accountProperty)){
            monthlySpending = DataFormatUtils.twoDecimalPlacesFormat(amount);
            monthlyIncome = "0.00";
            accountProperty.setMonthlyIncome(monthlyIncome);
            accountProperty.setMonthlySpending(monthlySpending);
        }else {

            if (accountProperty.getMonthlySpending() != null){
                double monthlySpendingFloat = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountProperty.getMonthlySpending())
                        + DataFormatUtils.getTwoDecimalPlacesDoubleFormat(amount);
                monthlySpending = DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(monthlySpendingFloat)) ;
            }else {
                monthlySpending = DataFormatUtils.twoDecimalPlacesFormat(amount);
            }
            accountProperty.setMonthlySpending(monthlySpending);

        }
        accountProperty.setUpdateTime(nowDate);

        return accountPropertyService.updateAccountProperty(accountProperty);

    }


    public boolean toDealToaccountProperty(AccountPropertyService accountPropertyService, AccountProperty accountProperty, String amount){


        double amountDouble = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(amount);
        double availableAmountDouble = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountProperty.getAvailableProperty());
        double residueDouble = availableAmountDouble + amountDouble;

        String residueString = DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(residueDouble)) ;
        accountProperty.setAvailableProperty(residueString);
        accountProperty.setTotalProperty(residueString);

        String monthlySpending;
        String monthlyIncome;

        if (AccountMessageUtil.whetherTheAccountPropertyCanClearAllMonthlyIncomeOrSpending(accountProperty)){
            monthlyIncome = DataFormatUtils.twoDecimalPlacesFormat(amount);
            monthlySpending = "0.00";
            accountProperty.setMonthlyIncome(monthlyIncome);
            accountProperty.setMonthlySpending(monthlySpending);
        }else {

            if (accountProperty.getMonthlyIncome() != null){
                double monthlyIncomeFloat = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountProperty.getMonthlyIncome())
                        + DataFormatUtils.getTwoDecimalPlacesDoubleFormat(amount);
                monthlyIncome = DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(monthlyIncomeFloat));
            }else {
                monthlyIncome = DataFormatUtils.twoDecimalPlacesFormat(amount);
            }
            accountProperty.setMonthlyIncome(monthlyIncome);

        }
        accountProperty.setUpdateTime(nowDate);

        return accountPropertyService.updateAccountProperty(accountProperty);
    }

}
