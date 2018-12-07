package java_all.main_flow.controller;

import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.service.BaseMsgService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lizhijing on 2018/9/21.
 */


@Controller
public class GestureVerifyController {

    @RequestMapping(value = "/gesture_verify",method = RequestMethod.POST)
    @ResponseBody

    public void gesture_verify(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String tel = request.getParameter("tel");
        String gesture = request.getParameter("gesture");

        String rspCode = "rspCode";
        String rspMsg = "rspMsg";

        Map map = new HashMap();


        BaseMsg msg = new BaseMsg();
        msg.setTel(tel);
        msg.setGesturesPwd(gesture);

        BaseMsgService service = new BaseMsgService();
        List<BaseMsg> list = service.selectBaseMsgByTel(msg.getTel());

        if (list.size() > 0){

            if (list.size() == 1){

                BaseMsg baseMsg = list.get(0);

                long currentTime = new Date().getTime();
                int pwdErrorNum = baseMsg.getPwdErrorNum();

                if (currentTime / 1000 - Long.parseLong(baseMsg.getLoginTime())  > 10 * 60){
                    pwdErrorNum = 0;
                    if (baseMsg.getGesturesPwd() == msg.getGesturesPwd()){

                        map.put(rspCode,"0000");
                        map.put(rspMsg,"成功");

                    }else {

                        pwdErrorNum ++;
                        map.put(rspCode,"1001");
                        map.put(rspMsg,"手势错误");

                    }

                }else {

                    if (pwdErrorNum < 5){

                        if (baseMsg.getGesturesPwd() == msg.getGesturesPwd()){

                            map.put(rspCode,"0000");
                            map.put(rspMsg,"成功");

                        }else {

                            pwdErrorNum ++;
                            map.put(rspCode,"1001");
                            map.put(rspMsg,"手势错误");

                        }

                    }else {

                        map.put(rspCode,"1002");
                        map.put(rspMsg,"您已经第" + pwdErrorNum + "次手势输入错误，请十分钟后再做操作");

                    }

                }

                baseMsg.setLoginTime( String.valueOf(currentTime / 1000) );
                baseMsg.setPwdErrorNum(pwdErrorNum);

                Boolean result = service.updateBaseMsgLoginTime(baseMsg);
                Boolean result1 = service.updateBaseMsgPwdInputErrorNum(baseMsg);

                if (result && result1){

                }else {
                    map.put(rspCode,"2222");
                    map.put(rspMsg,"账号异常");
                }

            }else {
                map.put(rspCode,"2222");
                map.put(rspMsg,"账号异常");
            }

        }else {
            map.put(rspCode,"1111");
            map.put(rspMsg,"账号不存在");
        }

        response.setContentType("text/html;charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(map);


    }



}
