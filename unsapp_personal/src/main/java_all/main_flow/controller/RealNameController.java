package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.GetRequestJsonUtils;
import java_all.main_flow.utils.PictureUtils;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.mapper_class.RealNameMsg;
import java_all.mybatis_domin.service.BaseMsgService;
import java_all.mybatis_domin.service.RealNameMsgService;
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
 * Created by lizhijing on 2018/9/21.
 */


@Controller
public class RealNameController {

    @RequestMapping(value = "/realname",method = RequestMethod.POST)
    @ResponseBody

    public void realname(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String rspCode = "rspCode";
        String rspMsg = "rspMsg";
        Map responseMap = new HashMap();

        RealNameMsg realNameMsg;
        realNameMsg = checkRequestParams(request);

        if (realNameMsg == null){
            responseMap.put(rspCode,"1001");
            responseMap.put(rspMsg,"缺少参数");
        }else {

            BaseMsgService baseMsgService = new BaseMsgService();

            RealNameMsgService realNameMsgService = new RealNameMsgService();


           List<BaseMsg> list = baseMsgService.selectBaseMsgByAccountId(realNameMsg.getAccountId());
            if (list.size() == 0){
                responseMap.put(rspCode,"1002");
                responseMap.put(rspMsg,"accountId不存在");
            }else if(list.size() > 1) {
                responseMap.put(rspCode,"1003");
                responseMap.put(rspMsg,"accountId账户异常");
            }else {

                if (AccountMessageUtil.whetherTheAccountIsRealNameOrNotByAccountId(realNameMsg.getAccountId())){
                    responseMap.put(rspCode,"1004");
                    responseMap.put(rspMsg,"账户已实名认证");
                }else {

                    String frontFaceOfIdCardPhoto = "frontFaceOfIdCardPhoto";
                    String reverseSideOfIdCardPhoto = "reverseSideOfIdCardPhoto";

                    boolean savePositiveResult = false;
                    boolean saveReverseResult = false;
                    savePositiveResult = PictureUtils.save(realNameMsg.getFrontFaceOfIdCardPhoto(),
                            frontFaceOfIdCardPhoto + realNameMsg.getAccountId(), PictureUtils.PictureSavePath.REAL_NAME);
                    saveReverseResult = PictureUtils.save(realNameMsg.getReverseSideOfIdCardPhoto(),
                            reverseSideOfIdCardPhoto + realNameMsg.getAccountId(), PictureUtils.PictureSavePath.REAL_NAME);
                    if (savePositiveResult && saveReverseResult){

                        realNameMsg.setFrontFaceOfIdCardPhoto(frontFaceOfIdCardPhoto + realNameMsg.getAccountId());
                        realNameMsg.setReverseSideOfIdCardPhoto(reverseSideOfIdCardPhoto + realNameMsg.getAccountId());


                        Boolean result = realNameMsgService.insertRealNameMsg(realNameMsg);
                        if (result == true){

                            BaseMsg baseMsg = list.get(0);
                            baseMsg.setName(realNameMsg.getName());
                            baseMsg.setIdentityId(realNameMsg.getIdentityId());
                            boolean result1 =  baseMsgService.updateBaseMsg(baseMsg);
                            if (!result1){
                                System.out.print("实名认证数据导入基本数据列表时出错");
                            }
                            responseMap.put(rspCode,"0000");
                            responseMap.put(rspMsg,"实名认证成功");
                        }else {
                            responseMap.put(rspCode,"2222");
                            responseMap.put(rspMsg,"数据库异常");
                        }

                    }else {
                        responseMap.put(rspCode,"2001");
                        responseMap.put(rspMsg,"图片保存失败");
                    }

                }



            }

        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().print(JSON.toJSONString(responseMap));


    }


    public RealNameMsg checkRequestParams(HttpServletRequest request) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);

        String name = jsonObject.getString("name");
        String identityId = jsonObject.getString("identityId");
        String identityIdValidDate = jsonObject.getString("identityIdValidDate");
        String frontFaceOfIdCardPhoto = jsonObject.getString("frontFaceOfIdCardPhoto");
        String reverseSideOfIdCardPhoto = jsonObject.getString("reverseSideOfIdCardPhoto");
        String accountId = jsonObject.getString("accountId");


        if (name == null ||
                identityId == null ||
                identityIdValidDate == null ||
                frontFaceOfIdCardPhoto == null ||
                reverseSideOfIdCardPhoto == null ||
                accountId == null){
            return null;
        }

        RealNameMsg msg = new RealNameMsg();
        msg.setAccountId(accountId);
        msg.setName(name);
        msg.setIdentityId(identityId);
        msg.setIdentityIdValidDate(identityIdValidDate);
        msg.setFrontFaceOfIdCardPhoto(frontFaceOfIdCardPhoto);
        msg.setReverseSideOfIdCardPhoto(reverseSideOfIdCardPhoto);
        return msg;

    }


}

