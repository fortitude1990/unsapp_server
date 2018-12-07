package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.BaseMsg;
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
 * Created by lizhijing on 2018/9/10.
 */


@Controller
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody

    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException{


        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        String tel = jsonObject.getString("tel");
        String pwd = jsonObject.getString("pwd");

        String rspCode = "rspCode";
        String rspMsg = "rspMsg";

        Map<String,String> map = new HashMap<String, String>();

        if (tel.length() == 0){
            map.put(rspCode,"1001");
            map.put(rspMsg,"手机号码为空");
        }else if (pwd.length() == 0){
            map.put(rspCode,"1001");
            map.put(rspMsg,"登陆密码为空");
        }else {

            BaseMsg msg = new BaseMsg();
            msg.setTel(tel);
            msg.setPwd(pwd);

            BaseMsgService baseMsgService = new BaseMsgService();
            List<BaseMsg> list = baseMsgService.selectBaseMsgByTel(msg.getTel());
            if (list.size() == 0){
                map.put(rspCode,"1111");
                map.put(rspMsg,"手机号码或密码错误");
            }else if (list.size() == 1){
               BaseMsg baseMsg = list.get(0);

               if (baseMsg.getPwd().equals(msg.getPwd())){

                   map.put("accountId",baseMsg.getAccountId());
                   map.put(rspCode,"0000");
                   map.put(rspMsg,"登陆成功");

               }else {
                   map.put(rspCode,"1111");
                   map.put(rspMsg,"手机号码或密码错误");
               }

            }else {
                map.put(rspCode,"2222");
                map.put(rspMsg,"登陆异常");
            }


        }

        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(JSON.toJSONString(map));


    }

    public Map<String,String> result(BaseMsg baseMsg){

        Map<String, String> map = new HashMap<String, String>();
        return map;
    }


}
