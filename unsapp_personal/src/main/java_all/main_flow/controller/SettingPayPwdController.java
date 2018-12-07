package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpResponse;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.DateUtils;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.AccountProperty;
import java_all.mybatis_domin.service.AccountPropertyService;
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
 * Created by lizhijing on 2018/10/9.
 */


@Controller
public class SettingPayPwdController {


    @RequestMapping(value = "/setting/pay/passoword",method = RequestMethod.POST)
    @ResponseBody

    public void settingPayPassword(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        String accountId = jsonObject.getString("accountId");
        String password = jsonObject.getString("payPwd");

        Map<String,Object> map = new HashMap<String, Object>();

        if (accountId == null || password == null){
            map.put(HttpResponseConstant.RSPCODE,"1001");
            map.put(HttpResponseConstant.RSPMSG,"参数不全");
            toResponse(response,map);
            return;
        }


       if (AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){

           AccountPropertyService accountPropertyService = new AccountPropertyService();


           AccountProperty accountProperty;
           boolean result;
           if (AccountMessageUtil.whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(accountId)){
               accountProperty = accountPropertyService.gainAccountPropertyMessageBy(accountId);
               accountProperty.setPayPwd(password);
               accountProperty.setUpdateTime(DateUtils.nowDate());
               result =  accountPropertyService.updatePayPwd(accountProperty);
            }else{

              accountProperty = new AccountProperty();
               accountProperty.setAccountId(accountId);
               accountProperty.setPayPwd(password);
               accountProperty.setUpdateTime(DateUtils.nowDate());
               result = accountPropertyService.insertPayPwd(accountProperty);
            }

            if (result){
                map.put(HttpResponseConstant.RSPCODE,"0000");
                map.put(HttpResponseConstant.RSPMSG,"成功");
            }else {
                map.put(HttpResponseConstant.RSPCODE,"1003");
                map.put(HttpResponseConstant.RSPMSG,"异常情况");
            }

            toResponse(response,map);
            return;


       }else {
           map.put(HttpResponseConstant.RSPCODE,"1002");
           map.put(HttpResponseConstant.RSPMSG,"账号不存在");
           toResponse(response,map);
           return;
       }




    }

    static void toResponse(HttpServletResponse response, Map map){

        try {
            response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
            response.setContentType(HttpResponseConstant.CONTENT_TYPE);
            response.getWriter().print(JSON.toJSONString(map));
        }catch (Exception e){
            System.out.print(e);
        }

    }

}
