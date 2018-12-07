package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.AccountBankCard;
import java_all.mybatis_domin.service.AccountBankCardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhijing on 2018/10/22.
 */



@Controller
public class BankListController {

    @RequestMapping(value = "/banklist", method = RequestMethod.POST)
    @ResponseBody
    public void bankList(HttpServletRequest request, HttpServletResponse response){

        try {

            Map<String,Object> map = new HashMap<String, Object>();

            String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String accountId = jsonObject.getString("accountId");
            if (accountId == null){
                map.put(HttpResponseConstant.RSPCODE, "1001");
                map.put(HttpResponseConstant.RSPMSG, "缺少参数");
                responseAction(response,map);
                return;
            }

            if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){
                map.put(HttpResponseConstant.RSPCODE, "1002");
                map.put(HttpResponseConstant.RSPMSG, "账号不存在");
                responseAction(response, map);
                return;
            }

            AccountBankCardService accountBankCardService = new AccountBankCardService();
            List<AccountBankCard> list = accountBankCardService.selectAllBankCardByAccountId(accountId);


            List responseList = new ArrayList();
            if (list != null){
                for (AccountBankCard accountBankCard: list
                        ) {

                    Map<String, Object> accountBankCardMap = new HashMap<String, Object>();
                    accountBankCardMap.put("accountId", accountBankCard.getAccountId());
                    accountBankCardMap.put("bankNo", accountBankCard.getBankNo());
                    accountBankCardMap.put("bankName", accountBankCard.getBankName());
                    accountBankCardMap.put("name", accountBankCard.getName());
                    accountBankCardMap.put("bankAboutMobile", accountBankCard.getBankAboutMobile());
                    accountBankCardMap.put("bankCode", accountBankCard.getBankCode());
                    accountBankCardMap.put("cardType", accountBankCard.getCardType());
                    responseList.add(accountBankCardMap);


                }
            }


            map.put(HttpResponseConstant.RSPCODE,"0000");
            map.put(HttpResponseConstant.RSPMSG,"成功");
            map.put("list", responseList);
            responseAction(response,map);
            return;



        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void responseAction(HttpServletResponse response,  Map map){

        try {
            response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
            response.setContentType(HttpResponseConstant.CONTENT_TYPE);
            response.getWriter().print(JSON.toJSONString(map));
        }catch (Exception e){
            e.printStackTrace();
        }



    }




}
