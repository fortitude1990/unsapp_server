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
import java_all.mybatis_domin.service.RealNameMsgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by lizhijing on 2018/10/11.
 */


@Controller
public class RechargeController {

    private  String nowDate;

    @RequestMapping(value = "/recharge", method = RequestMethod.POST)
    @ResponseBody

    public void recharge(HttpServletRequest request, HttpServletResponse response) throws IOException{

        nowDate = DateUtils.nowDate();

       String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        final String accountId = jsonObject.getString("accountId");
        final String bankNo = jsonObject.getString("bankNo");
        final String amountOrigin = jsonObject.getString("amount");
        final String bankName = jsonObject.getString("bankName");

        Double amountDouble = Double.parseDouble(amountOrigin);
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        final String amount = decimalFormat.format(amountDouble);



        Map<String,Object> map = new HashMap<String, Object>();

        if (accountId == null || bankNo == null || amount == null || bankName == null){
            map.put(HttpResponseConstant.RSPCODE,"1001");
            map.put(HttpResponseConstant.RSPMSG,"缺少参数");
            toResponse(response,map);
            return;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){
            map.put(HttpResponseConstant.RSPCODE,"1002");
            map.put(HttpResponseConstant.RSPMSG,"账户不存在");
            toResponse(response,map);
            return;
        }

        if (!AccountMessageUtil.whetherTheAccountIsRealNameOrNotByAccountId(accountId)){
            map.put(HttpResponseConstant.RSPCODE,"1003");
            map.put(HttpResponseConstant.RSPMSG,"账户未进行实名认证");
            toResponse(response,map);
            return;
        }

        AccountPropertyService service = new AccountPropertyService();


        AccountProperty accountProperty = new AccountProperty();
        accountProperty.setAccountId(accountId);

        boolean result;
        if (AccountMessageUtil.whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(accountId)){
            AccountProperty accountProperty1 =  service.gainAccountPropertyMessageBy(accountId);
            float totalProperty;
            if (accountProperty1.getTotalProperty() == null){
                totalProperty =  Float.parseFloat(amount);
            }else {
                totalProperty  = Float.parseFloat(accountProperty1.getTotalProperty()) + Float.parseFloat(amount);
            }
            float availableProperty = totalProperty;

            accountProperty.setTotalProperty(DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(totalProperty)));
            accountProperty.setAvailableProperty(DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(availableProperty)));

            if (! AccountMessageUtil.whetherTheAccountPropertyCanClearAllMonthlyIncomeOrSpending(accountProperty1) &&
                    accountProperty1.getMonthlyIncome() != null){
                double monthlyIncome = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountProperty1.getMonthlyIncome())
                        + DataFormatUtils.getTwoDecimalPlacesDoubleFormat(amount);
                accountProperty.setMonthlyIncome(DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(monthlyIncome)));
                accountProperty.setMonthlySpending(accountProperty1.getMonthlySpending());
            }else {
                accountProperty.setMonthlyIncome(DataFormatUtils.twoDecimalPlacesFormat(amount));
                accountProperty.setMonthlySpending("0.00");
            }

            accountProperty.setUpdateTime(nowDate);
            result = service.updateAccountProperty(accountProperty);


        }else {
            accountProperty.setTotalProperty(amount);
            accountProperty.setAvailableProperty(amount);
            result = service.insertAccountProperty(accountProperty);
        }

        if (result){
            map.put(HttpResponseConstant.RSPCODE,"0000");
            map.put(HttpResponseConstant.RSPMSG,"充值成功");

            ExecutorService executorService = Executors.newFixedThreadPool(1);
            executorService.execute(new Runnable() {
                @Override
                public void run() {

                    AccountDeal accountDeal = new AccountDeal();
                    accountDeal.setAccountId(accountId);
                    accountDeal.setAmount(amount);
                    accountDeal.setBankNo(bankNo);
                    accountDeal.setDealTime(nowDate);
                    accountDeal.setOrderNo(OrderNoUtils.orderNo());
                    accountDeal.setDealType("0");
                    accountDeal.setStatus("0");
                    accountDeal.setBankName(bankName);

                    RealNameMsgService realNameMsgService = new RealNameMsgService();
                    List<RealNameMsg> list = realNameMsgService.selectByAccountId(accountId);
                    RealNameMsg realNameMsg = list.get(0);

                    accountDeal.setName(realNameMsg.getName());

                    AccountDealService accountDealService = new AccountDealService();
                    boolean result = accountDealService.insertAccountDeal(accountDeal);
                    if (!result){
                        System.out.print("充值记录插入失败");
                    }

                }
            });


        }else {
            map.put(HttpResponseConstant.RSPCODE,"2001");
            map.put(HttpResponseConstant.RSPMSG,"充值失败");
        }

        toResponse(response,map);

    }

    void toResponse(HttpServletResponse response, Map map) throws IOException{
        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.getWriter().print(JSON.toJSONString(map));
    }

}



