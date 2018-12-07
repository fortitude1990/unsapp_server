package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.mapper_class.RealNameMsg;
import java_all.mybatis_domin.service.BaseMsgService;
import java_all.mybatis_domin.service.RealNameMsgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sun.org.mozilla.javascript.internal.json.JsonParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhijing on 2018/10/9.
 */

@Controller
public class ObtainCheckStatusController {


    @RequestMapping(value = "/checkstatus", method = RequestMethod.POST)
    @ResponseBody

    public void checkstatus(HttpServletRequest request, HttpServletResponse response){



        try {

            String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
            System.out.print(jsonString);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            String accountId = jsonObject.getString("accountId");


            Map<String,String> map = new HashMap<String, String>();

            if (accountId == null){
                map.put(HttpResponseConstant.RSPCODE,"1001");
                map.put(HttpResponseConstant.RSPMSG,"缺少accountId参数");
            }else {
                BaseMsgService baseMsgService = new BaseMsgService();
                List<BaseMsg> list = baseMsgService.selectBaseMsgByAccountId(accountId);
                if (list != null && list.size() > 0){

                    RealNameMsgService realNameMsgService = new RealNameMsgService();
                    List<RealNameMsg> realNameMsgList = realNameMsgService.selectByAccountId(accountId);
                    if (realNameMsgList != null && realNameMsgList.size() > 0){

                        String rspMsg;
                        String status;
                        RealNameMsg realNameMsg = realNameMsgList.get(0);
                        status = realNameMsg.getStatus();
                        switch ( Integer.valueOf(status)){
                            case 0:
                                rspMsg = "审核中";
                                break;
                            case 1:
                                rspMsg = "审核通过";
                                break;
                            case 2:
                                rspMsg = "审核未通过";
                                break;
                            default:
                                rspMsg = "未知状态";
                                break;
                        }

                        map.put(HttpResponseConstant.RSPCODE,"0000");
                        map.put(HttpResponseConstant.RSPMSG,rspMsg);
                        map.put("status",status);

                    }else {
                        map.put(HttpResponseConstant.RSPCODE,"1003");
                        map.put(HttpResponseConstant.RSPMSG,"未进行实名认证");
                    }

                }else {
                    map.put(HttpResponseConstant.RSPCODE,"1002");
                    map.put(HttpResponseConstant.RSPMSG,"账号不存在");
                }
            }

            response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
            response.setContentType(HttpResponseConstant.CONTENT_TYPE);
            response.getWriter().print(map);

        }catch (Exception e){
            System.out.println("error");
            e.printStackTrace();
            throw new RuntimeException(e);
        }


    }




}
