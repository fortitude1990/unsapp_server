package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.AccountDeal;
import java_all.mybatis_domin.mapper_class.AccountDealQuery;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.service.AccountDealService;
import java_all.mybatis_domin.service.BaseMsgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhijing on 2018/11/1.
 */


@Controller
@RequestMapping(value = "/query", method = RequestMethod.POST)
public class QueryDealRecordsController {



    @RequestMapping(value = "/deal")
    @ResponseBody
    public void queryDeal(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String accountId = jsonObject.getString("accountId");
        String page = jsonObject.getString("page");
        String pageSize = jsonObject.getString("pageSize");
        String queryType = jsonObject.getString("queryType");

        Map<String, Object> map = new HashMap<String, Object>();

        if (accountId == null ||
                page == null ||
                pageSize == null ||
                queryType == null) {
            map.put(HttpResponseConstant.RSPCODE, "1001");
            map.put(HttpResponseConstant.RSPMSG, "缺少参数");
            toResponse(response, map);
            return;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){
            map.put(HttpResponseConstant.RSPCODE, "1002");
            map.put(HttpResponseConstant.RSPMSG, "账户不存在");
            toResponse(response, map);
            return;
        }


        AccountDealQuery accountDealQuery = new AccountDealQuery();
        accountDealQuery.setAccountId(accountId);
        accountDealQuery.setQueryLength(Integer.valueOf(pageSize));
        accountDealQuery.setStartIndex((Integer.valueOf(page) - 1) * Integer.valueOf(pageSize));


        List<AccountDeal> list = null;
        switch (Integer.valueOf(queryType)){
            case 0:
                accountDealQuery.setDealType("0");
                list = queryRecharge(accountDealQuery);
                break;
            case 1:
                accountDealQuery.setDealType("1");
                list = queryWithdraw(accountDealQuery);
                break;
            case 2:
                accountDealQuery.setDealType("2");
                list = queryTransfer(accountDealQuery);
                break;
            case 3:
                list = queryAllDeal(accountDealQuery);
                break;
        }

        List responseList = new ArrayList();
        if (list != null){
            for (AccountDeal accountDeal: list) {

                Map<String, Object> accountDealMap = new HashMap<String, Object>();
                accountDealMap.put("dealTime", accountDeal.getDealTime());
                accountDealMap.put("accountId", accountDeal.getAccountId());
                accountDealMap.put("amount", accountDeal.getAmount());
                accountDealMap.put("dealType", accountDeal.getDealType());
                accountDealMap.put("payType", accountDeal.getPayType());
                accountDealMap.put("transferType", accountDeal.getTransferType());
                accountDealMap.put("bankNo", accountDeal.getBankNo());
                accountDealMap.put("name", accountDeal.getName());
                accountDealMap.put("toAccountId", accountDeal.getToAccountId());
                accountDealMap.put("toBankNo", accountDeal.getToBankNo());
                accountDealMap.put("toName", accountDeal.getToName());
                accountDealMap.put("des", accountDeal.getDes());
                accountDealMap.put("orderNo", accountDeal.getOrderNo());
                accountDealMap.put("bankName", accountDeal.getBankName());
                accountDealMap.put("toBankName", accountDeal.getToBankName());
                accountDealMap.put("status", accountDeal.getStatus());

                if (accountDeal.getToAccountId() != null){
                    BaseMsgService baseMsgService = new BaseMsgService();
                    List<BaseMsg> list1 = baseMsgService.selectBaseMsgByAccountId(accountDeal.getToAccountId());
                    if (list1 != null && list1.size() > 0){
                        BaseMsg baseMsg = list1.get(0);
                        accountDealMap.put("toTel", baseMsg.getTel());
                    }
                }

               responseList.add(accountDealMap);
            }
        }
        map.put("list", responseList);
        map.put(HttpResponseConstant.RSPCODE, "0000");
        map.put(HttpResponseConstant.RSPMSG, "查询成功");
        toResponse(response, map);
    }


    public List<AccountDeal> queryAllDeal(AccountDealQuery accountDealQuery){

        AccountDealService accountDealService = new AccountDealService();
        return  accountDealService.selectAllBySize(accountDealQuery);
    }

    public List<AccountDeal> queryRecharge(AccountDealQuery accountDealQuery){

        AccountDealService accountDealService = new AccountDealService();
        return  accountDealService.selectBySize(accountDealQuery);
    }

    public List<AccountDeal> queryWithdraw(AccountDealQuery accountDealQuery){

        AccountDealService accountDealService = new AccountDealService();
        return  accountDealService.selectBySize(accountDealQuery);
    }
    public List<AccountDeal> queryTransfer(AccountDealQuery accountDealQuery){

        AccountDealService accountDealService = new AccountDealService();
        return  accountDealService.selectBySize(accountDealQuery);
    }



    public void toResponse(HttpServletResponse response, Map map){
        try {
            response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
            response.getWriter().print(JSON.toJSONString(map));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
