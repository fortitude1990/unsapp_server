package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
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
 * Created by lizhijing on 2018/10/11.
 */

@Controller
public class VerifyPayPwdController {

    @RequestMapping(value = "/verify/paypwd",method = RequestMethod.POST)
    @ResponseBody

    public void verifyPayPwd(HttpServletRequest request,
                             HttpServletResponse response) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String accountId = jsonObject.getString("accountId");
        String payPwd = jsonObject.getString("payPwd");

        Map<String,Object> map = new HashMap<String, Object>();

        if (accountId == null || payPwd == null){
            map.put(HttpResponseConstant.RSPCODE,"1001");
            map.put(HttpResponseConstant.RSPMSG, "缺少参数");
            toResponse(response,map);
            return;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){
            map.put(HttpResponseConstant.RSPCODE,"1002");
            map.put(HttpResponseConstant.RSPMSG, "账号不存在");
            toResponse(response,map);
            return;
        }

        if(AccountMessageUtil.whetherThePayPasswordIsSetByAccountId(accountId)){

            AccountPropertyService service = new AccountPropertyService();
            AccountProperty accountProperty = service.gainAccountPropertyMessageBy(accountId);
            if (accountProperty.getPayPwd().equals(payPwd)){
                map.put(HttpResponseConstant.RSPCODE,"0000");
                map.put(HttpResponseConstant.RSPMSG, "密码正确");
            }else {
                map.put(HttpResponseConstant.RSPCODE,"1004");
                map.put(HttpResponseConstant.RSPMSG, "密码错误");
            }

        }else {
            map.put(HttpResponseConstant.RSPCODE,"1003");
            map.put(HttpResponseConstant.RSPMSG, "账户支付密码未设置");
        }

        toResponse(response,map);


    }

    void toResponse(HttpServletResponse response, Map map) throws IOException{
        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.getWriter().print(JSON.toJSONString(map));
    }
}
