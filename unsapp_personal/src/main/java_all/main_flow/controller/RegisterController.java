package java_all.main_flow.controller;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.deploy.net.HttpResponse;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.AccountIdStorage;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.service.AccountIdStorageService;
import java_all.mybatis_domin.service.BaseMsgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * Created by lizhijing on 2018/9/7.
 */


@Controller
public class RegisterController {



    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @ResponseBody
    public void register(HttpServletRequest request, HttpServletResponse response) throws Exception{

        System.out.print("/register : 进来了");

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);


        String tel = jsonObject.getString("tel");
        String pwd = jsonObject.getString("pwd");

        Map map = new HashMap();

        if (tel.length() == 0){

            map.put("rspCode","1001");
            map.put("rspMsg","手机号码为空");

        }else if (pwd.length() == 0){

            map.put("rspCode","1002");
            map.put("rspMsg","密码值为空");

        }else {

            BaseMsg baseMsg = new BaseMsg();
            baseMsg.setTel(tel);
            baseMsg.setPwd(pwd);

            BaseMsgService baseMsgService = new BaseMsgService();
            List<BaseMsg> list =  baseMsgService.selectBaseMsgByTel(baseMsg.getTel());

            if (list.size() == 0){
                AccountIdStorageService accountIdStorageService = new AccountIdStorageService();
               AccountIdStorage accountIdStorage = accountIdStorageService.selectOneValid();
               if (accountIdStorage != null && accountIdStorage.getValid()){
                   baseMsg.setAccountId(String.valueOf(accountIdStorage.getAccountId()));

                  Boolean result = accountIdStorageService.updateAccountIdStorageToInvalid(accountIdStorage);
                  if (result){
                      baseMsgService.registerBaseMsg(baseMsg);

                      map.put("rspCode","0000");
                      map.put("rspMsg","成功");

                  }else {

                      accountIdStorageService.updateAccountIdStorageToValid(accountIdStorage);
                      map.put("rspCode","4444");
                      map.put("rspMsg","账号储存状态修改失败");

                  }



               }else {
                   map.put("rspCode","3333");
                   map.put("rspMsg","账号生成失败");
               }


            }else {
                map.put("rspCode","1111");
                map.put("rspMsg","手机号码已被注册");
            }

        }



        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(JSON.toJSONString(map));


    }



}
