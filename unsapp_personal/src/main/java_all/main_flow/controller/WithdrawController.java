package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.*;
import java_all.mybatis_domin.mapper_class.AccountBankCard;
import java_all.mybatis_domin.mapper_class.AccountDeal;
import java_all.mybatis_domin.mapper_class.AccountProperty;
import java_all.mybatis_domin.service.AccountBankCardService;
import java_all.mybatis_domin.service.AccountDealService;
import java_all.mybatis_domin.service.AccountPropertyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lizhijing on 2018/10/22.
 */
@Controller
public class WithdrawController {

    private  String nowDate;

    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    @ResponseBody

    public void withdraw(HttpServletRequest request, HttpServletResponse response){

        try {

            nowDate = DateUtils.nowDate();

          Map<String, Object> map = new HashMap<String, Object>();


          String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
          JSONObject jsonObject = JSON.parseObject(jsonString);
            final AccountDeal accountDeal = JSONObject.toJavaObject(jsonObject, AccountDeal.class);

            if (accountDeal.getAccountId() == null ||
                    accountDeal.getAmount() == null ||
                    accountDeal.getToBankNo() == null ||
                    accountDeal.getToBankName() == null){
                map.put(HttpResponseConstant.RSPCODE, "1001");
                map.put(HttpResponseConstant.RSPMSG, "缺少参数");
                toResponse(response, map);
                return;
            }

            if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountDeal.getAccountId())){
                map.put(HttpResponseConstant.RSPCODE, "1002");
                map.put(HttpResponseConstant.RSPMSG, "账号不正确");
                toResponse(response, map);
                return;
            }

            final AccountBankCardService accountBankCardService = new AccountBankCardService();
            AccountBankCard accountBankCard = accountBankCardService.selectOneBankCardByBankNo(accountDeal.getToBankNo());
            if (accountBankCard == null || ! accountBankCard.getAccountId().equals(accountDeal.getAccountId()) ){
                map.put(HttpResponseConstant.RSPCODE, "1003");
                map.put(HttpResponseConstant.RSPMSG, "银行卡未被绑定");
                toResponse(response, map);
                return;
            }

            if (AccountMessageUtil.whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(accountDeal.getAccountId())){

                AccountPropertyService accountPropertyService = new AccountPropertyService();
                AccountProperty accountProperty = accountPropertyService.gainAccountPropertyMessageBy(accountDeal.getAccountId());
                AccountProperty backupAccountProperty = (AccountProperty) accountProperty.clone();


                String availableAmount = accountProperty.getAvailableProperty();

                if (availableAmount != null && Float.parseFloat(availableAmount) - Float.parseFloat(accountDeal.getAmount()) >= 0){


                    double residue = DataFormatUtils.getTwoDecimalPlacesDoubleFormat(availableAmount)
                            - DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountDeal.getAmount());
                    accountProperty.setAvailableProperty(String.valueOf(residue));
                    accountProperty.setTotalProperty(String.valueOf(residue));
                    if (AccountMessageUtil.whetherTheAccountPropertyCanClearAllMonthlyIncomeOrSpending(accountProperty)){
                        accountProperty.setMonthlySpending("0.00");
                        accountProperty.setMonthlyIncome("0.00");
                    }
                    accountProperty.setUpdateTime(nowDate);

                    boolean result = accountPropertyService.updateAccountProperty(accountProperty);
                    boolean result1 = false;
                    if (result){

                        result1 = true; //打进银行卡成功

                    }else {

                        map.put(HttpResponseConstant.RSPCODE, "2001");
                        map.put(HttpResponseConstant.RSPMSG, "异常情况");
                        toResponse(response, map);
                        return;

                    }


                    if (result1){
                        accountDeal.setStatus("0"); //交易成功

                        map.put("dealTime", nowDate);
                        map.put(HttpResponseConstant.RSPCODE, "0000");
                        map.put(HttpResponseConstant.RSPMSG, "提现成功");

                    }else {

                        accountPropertyService.updateAccountProperty(backupAccountProperty);
                        accountDeal.setStatus("1");

                        map.put(HttpResponseConstant.RSPCODE, "2001");
                        map.put(HttpResponseConstant.RSPMSG, "提现失败");

                    }

                    accountDeal.setDealType("1");
                    accountDeal.setDealTime(nowDate);
                    accountDeal.setOrderNo(OrderNoUtils.orderNo());

                    ExecutorService executorService = Executors.newFixedThreadPool(1);
                    executorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            AccountDealService accountDealService = new AccountDealService();
                            boolean result2 = accountDealService.insertAccountDeal(accountDeal);
                            if (!result2){
                                System.out.print("交易记录插入失败");
                            }
                        }
                    });


                    toResponse(response, map);
                    return;


                }else {
                    map.put(HttpResponseConstant.RSPCODE, "1004");
                    map.put(HttpResponseConstant.RSPMSG, "余额不足");
                    toResponse(response, map);
                    return;
                }

            }else {
                map.put(HttpResponseConstant.RSPCODE, "1004");
                map.put(HttpResponseConstant.RSPMSG, "余额不足");
                toResponse(response, map);
                return;
            }





        }catch (Exception e){
            e.printStackTrace();
        }

    }



    public void toResponse(HttpServletResponse response, Map map){
        try {
            response.setContentType(HttpResponseConstant.CONTENT_TYPE);
            response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
            response.getWriter().print(JSON.toJSONString(map));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}
