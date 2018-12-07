package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.DataFormatUtils;
import java_all.main_flow.utils.DateUtils;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.AccountDeal;
import java_all.mybatis_domin.mapper_class.AccountDealQuery;
import java_all.mybatis_domin.mapper_class.AccountProperty;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.service.AccountDealService;
import java_all.mybatis_domin.service.AccountPropertyService;
import java_all.mybatis_domin.service.BaseMsgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhijing on 2018/10/8.
 */

@Controller
public class MainMessageController {

    @RequestMapping(value = "/main_message", method = RequestMethod.POST)
    @ResponseBody
    public void  mainMessage(HttpServletRequest request, HttpServletResponse response) throws IOException{


        String rspCode = "rspCode";
        String rspMsg = "rspMsg";
        String totalProperty = "totalProperty";
        String availableProperty = "availableProperty";
        String monthlySpending = "monthlySpending";
        String monthlyIncome = "monthlyIncome";
        String yesterdaySpending = "yesterdaySpending";
        String yesterdayIncome = "yesterdayIncome";


        System.out.print(request.getQueryString());

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String accountId = jsonObject.getString("accountId");


        Map<String,String> map = new HashMap<String, String>();

        if (accountId == null){

            map.put(rspCode,"1001");
            map.put(rspMsg,"缺少accountId参数");

        }else {

            BaseMsgService baseMsgService = new BaseMsgService();
            List<BaseMsg> list = baseMsgService.selectBaseMsgByAccountId(accountId);
            if (list != null && list.size() != 0){

                AccountPropertyService service = new AccountPropertyService();
                AccountProperty accountProperty = service.gainAccountPropertyMessageBy(accountId);
                if (accountProperty == null){

                    map.put(rspCode,"0000");
                    map.put(rspMsg,"无相关数据");
                    map.put(totalProperty,"0.00");
                    map.put(availableProperty,"0.00");
                    map.put(monthlySpending,"0.00");
                    map.put(monthlyIncome,"0.00");

                }else {

                    map.put(rspCode,"0000");
                    map.put(rspMsg,"查询成功");

                    map.put(totalProperty,accountProperty.getTotalProperty());
                    map.put(availableProperty,accountProperty.getAvailableProperty());
                    map.put(monthlySpending,accountProperty.getMonthlySpending());
                    map.put(monthlyIncome,accountProperty.getMonthlyIncome());

                    String dealTime = accountProperty.getUpdateTime();
                    String formatTime = DateUtils.transferFormat("yyyyMMddHHmmss", "yyyyMMdd", dealTime);
                    String nowTime = DateUtils.nowDate("yyyyMMdd");


//                    if (formatTime != null && formatTime.equals(nowTime)){
//                        map.put(yesterdayIncome,accountProperty.getYesterdayIncome());
//                        map.put(yesterdaySpending,accountProperty.getYesterdaySpending());
//                    }else {
//
//
//
//                    }

                    String timeval = DateUtils.date2TimeStamp(nowTime, "yyyyMMdd");
                    String timeval1 = String.valueOf(Integer.parseInt(timeval) - 24 * 60 * 60);
                    String yesterday = DateUtils.timeStamp2Date(timeval1, "yyyyMMdd");

                    AccountDealService accountDealService = new AccountDealService();

                    AccountDealQuery accountDealQuery = new AccountDealQuery();
                    accountDealQuery.setAccountId(accountId);
                    accountDealQuery.setDealType("0");
                    accountDealQuery.setStartTime(yesterday + "000000");
                    accountDealQuery.setEndTime(yesterday + "235959");

                    List<AccountDeal> list1 = accountDealService.selectByTime(accountDealQuery);
                    double totalAmount = 0.00;
                    for (AccountDeal accountdeal: list1) {
                        totalAmount += DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountdeal.getAmount());
                    }
                    accountProperty.setYesterdayIncome(DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(totalAmount)));


                    accountDealQuery.setDealType("2");
                    List<AccountDeal> list2 = accountDealService.selectByTime(accountDealQuery);
                    totalAmount = 0.00;
                    for (AccountDeal accountdeal: list2) {
                        totalAmount += DataFormatUtils.getTwoDecimalPlacesDoubleFormat(accountdeal.getAmount());
                    }
                    accountProperty.setYesterdaySpending(DataFormatUtils.twoDecimalPlacesFormat(String.valueOf(totalAmount)));

                    map.put(yesterdayIncome, accountProperty.getYesterdayIncome());
                    map.put(yesterdaySpending, accountProperty.getYesterdaySpending());

                    accountProperty.setUpdateTime(DateUtils.nowDate());
                    boolean result = service.updateAccountProperty(accountProperty);
                    if (result){
                        System.out.print("数据更新有误");
                    }

                }

            }else {
                map.put(rspCode,"1002");
                map.put(rspMsg,"账号不存在");
            }

        }



        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.getWriter().print(JSON.toJSONString(map));


    }



}
