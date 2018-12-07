package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.main_flow.utils.PictureUtils;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.service.BaseMsgService;
import org.springframework.stereotype.Controller;
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
 * Created by lizhijing on 2018/10/23.
 */
@Controller
@RequestMapping(value = "/basemsg", method = RequestMethod.POST)
public class BaseMsgController {

    @RequestMapping(value = "/gain", method = RequestMethod.POST)
    @ResponseBody

    public void baseMsg(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String accountId = jsonObject.getString("accountId");

        Map<String, Object> map = new HashMap<String, Object>();

        if (accountId == null){
            map.put(HttpResponseConstant.RSPCODE, "1001");
            map.put(HttpResponseConstant.RSPMSG, "缺少参数");
            response.getWriter().print(JSON.toJSONString(map));
            return;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){
            map.put(HttpResponseConstant.RSPCODE, "1002");
            map.put(HttpResponseConstant.RSPMSG, "账号不存在");
            response.getWriter().print(JSON.toJSONString(map));
            return;
        }

        BaseMsgService baseMsgService = new BaseMsgService();
        List<BaseMsg> list = baseMsgService.selectBaseMsgByAccountId(accountId);
        BaseMsg baseMsg = list.get(0);
        map.put("sex", baseMsg.getSex());
        map.put("birthday", baseMsg.getBirthday());
        map.put("nickname", baseMsg.getNickname());
        map.put("email", baseMsg.getEmail());
        map.put("name", baseMsg.getName());

        if (baseMsg.getHeadPortraitImage() != null){
            String imageName = baseMsg.getHeadPortraitImage();
            String image =  PictureUtils.read(imageName, PictureUtils.PictureSavePath.BASE_MSG);
            map.put("headPortraitImage", image);
        }

        if (baseMsg.getGesturesPwd() == null){
            map.put("isSettingGesture", "0");
        }else {
            map.put("isSettingGesture", "1");
        }
        if (baseMsg.getPwd() == null){
            map.put("isSettingPwd", "0");
        }else {
            map.put("isSettingPwd", "1");
        }

        if (AccountMessageUtil.whetherTheAccountIsRealNameOrNotByAccountId(accountId)){
            map.put("isRealName", "1");
        }else {
            map.put("isRealName", "0");
        }

        if (AccountMessageUtil.whetherThePayPasswordIsSetByAccountId(accountId)){
            map.put("isSetPayPwd", "1");
        }else {
            map.put("isSetPayPwd", "0");
        }

        map.put(HttpResponseConstant.RSPCODE, "0000");
        map.put(HttpResponseConstant.RSPMSG, "成功");

        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.getWriter().print(JSON.toJSONString(map));

    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody

    public void updateBaseMsg(HttpServletRequest request, HttpServletResponse response) throws IOException{

       String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
       JSONObject jsonObject = JSON.parseObject(jsonString);
       BaseMsg baseMsg = JSONObject.toJavaObject(jsonObject, BaseMsg.class);

       Map<String, Object> map = new HashMap<String, Object>();

       if (baseMsg.getAccountId() == null){
           map.put(HttpResponseConstant.RSPCODE, "1001");
           map.put(HttpResponseConstant.RSPMSG, "缺少参数");
           toResponse(response, map);
           return;
       }

       if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(baseMsg.getAccountId())){
           map.put(HttpResponseConstant.RSPCODE, "1002");
           map.put(HttpResponseConstant.RSPMSG, "账号不存在");
           toResponse(response,map);
           return;
       }

       BaseMsgService baseMsgService = new BaseMsgService();
       List<BaseMsg> list = baseMsgService.selectBaseMsgByAccountId(baseMsg.getAccountId());
       BaseMsg baseMsg1 = list.get(0);

       if (baseMsg.getSex() != null){
           baseMsg1.setSex(baseMsg.getSex());
       }

       if (baseMsg.getBirthday() != null){
           baseMsg1.setBirthday(baseMsg.getBirthday());
       }

       if (baseMsg.getPwd() != null){
           baseMsg1.setPwd(baseMsg.getPwd());
       }

       if (baseMsg.getNickname() != null){
           baseMsg1.setNickname(baseMsg.getNickname());
       }

       if (baseMsg.getEmail() != null){
           baseMsg1.setEmail(baseMsg.getEmail());
       }

       if (baseMsg.getGesturesPwd() != null){
           baseMsg1.setGesturesPwd(baseMsg.getGesturesPwd());
       }



       if (baseMsg.getConstellation() != null){
           baseMsg1.setConstellation(baseMsg.getConstellation());
       }

       if (baseMsg.getHeight() != null){
           baseMsg1.setHeight(baseMsg.getHeight());
       }

       if (baseMsg.getWeight() != null){
           baseMsg1.setWeight(baseMsg.getWeight());
       }

       if (baseMsg.getRegion() != null){
           baseMsg1.setRegion(baseMsg.getRegion());
       }

       if (baseMsg.getProfessional() != null){
           baseMsg1.setProfessional(baseMsg.getProfessional());
       }

       if (baseMsg.getIncome() != null){
           baseMsg1.setIncome(baseMsg.getIncome());
       }


        boolean result = false;

        if (baseMsg.getHeadPortraitImage() != null){

            String imageName = "headPortraitImage" + baseMsg.getAccountId();
            result  = PictureUtils.save(baseMsg.getHeadPortraitImage(), imageName, PictureUtils.PictureSavePath.BASE_MSG);
            baseMsg1.setHeadPortraitImage(imageName);
            if (result){
                result = baseMsgService.updateBaseMsg(baseMsg1);
            }

        }else {
             result = baseMsgService.updateBaseMsg(baseMsg1);
        }


       if (result){
           map.put(HttpResponseConstant.RSPCODE, "0000");
           map.put(HttpResponseConstant.RSPMSG, "成功");
       }else {
           map.put(HttpResponseConstant.RSPCODE, "2222");
           map.put(HttpResponseConstant.RSPMSG, "出现异常");
       }

        toResponse(response,map);
        return;
    }

    @RequestMapping(value = "/verifyTel")
    @ResponseBody
    public void verifyTel(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String tel = jsonObject.getString("tel");

        Map<String, Object> map = new HashMap<String, Object>();
        if (tel == null){
            map.put(HttpResponseConstant.RSPCODE, "1001");
            map.put(HttpResponseConstant.RSPMSG, "缺少参数");
            toResponse(response, map);
            return;
        }

        BaseMsgService baseMsgService = new BaseMsgService();
        List<BaseMsg> list =  baseMsgService.selectBaseMsgByTel(tel);
        if (list == null || list.size() == 0){
            map.put(HttpResponseConstant.RSPCODE, "2001");
            map.put(HttpResponseConstant.RSPMSG, "非银生宝账户");
            toResponse(response, map);
            return;
        }

        BaseMsg baseMsg = list.get(0);
        map.put(HttpResponseConstant.RSPCODE, "0000");
        map.put(HttpResponseConstant.RSPMSG, "是银生宝账户");
        map.put("name", baseMsg.getName());
        map.put("accountId", baseMsg.getAccountId());
        map.put("tel", baseMsg.getTel());
        toResponse(response, map);

    }



    public void  toResponse(HttpServletResponse response, Map map){
        try {
            response.setContentType(HttpResponseConstant.CONTENT_TYPE);
            response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
            response.getWriter().print(JSON.toJSONString(map));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
