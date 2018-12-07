package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
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
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by lizhijing on 2018/10/11.
 */


@Controller
public class AddBankCardController {

    @RequestMapping(value = "/addbankcard", method = RequestMethod.POST)
    @ResponseBody

    public void addBankCard(HttpServletRequest request, HttpServletResponse response) throws IOException{


      AccountBankCard accountBankCard = gainBankCardParams(request);
      Map<String,Object> map = new HashMap<String, Object>();

      if (accountBankCard == null){
          map.put(HttpResponseConstant.RSPCODE,"1001");
          map.put(HttpResponseConstant.RSPMSG,"参数不全");
          toResponse(response,map);
          return;
      }

      if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountBankCard.getAccountId())){
          map.put(HttpResponseConstant.RSPCODE,"1002");
          map.put(HttpResponseConstant.RSPMSG,"账号不存在");
          toResponse(response,map);
          return;
      }

      if (AccountMessageUtil.whetherTheBankCardIsExistsOrNotByBankNo(accountBankCard.getBankNo())){
          map.put(HttpResponseConstant.RSPCODE,"1003");
          map.put(HttpResponseConstant.RSPMSG,"银行卡已存在");
          toResponse(response,map);
          return;
      }

        AccountBankCardService accountBankCardService = new AccountBankCardService();
     boolean result = accountBankCardService.insertBankCard(accountBankCard);
     if (result){
         map.put(HttpResponseConstant.RSPCODE,"0000");
         map.put(HttpResponseConstant.RSPMSG,"添加成功");
         toResponse(response,map);
     }else {
         map.put(HttpResponseConstant.RSPCODE,"1004");
         map.put(HttpResponseConstant.RSPMSG,"添加失败");
         toResponse(response,map);
     }



    }

    AccountBankCard gainBankCardParams(HttpServletRequest request){

        try{

            String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            AccountBankCard accountBankCard = JSONObject.toJavaObject(jsonObject,AccountBankCard.class);
            if (accountBankCard.getAccountId() != null &&
                    accountBankCard.getBankNo() != null &&
                    accountBankCard.getName() != null &&
                    accountBankCard.getBankAboutMobile() != null &&
                    accountBankCard.getCardType() != null &&
                    accountBankCard.getName() != null &&
                    accountBankCard.getBankCode() != null){
                return accountBankCard;
            }
            return null;
        }catch (Exception e){
            throw new RuntimeException(e);
        }


    }


    void toResponse(HttpServletResponse response, Map map) throws IOException{
        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.getWriter().print(JSON.toJSONString(map));
    }

}
