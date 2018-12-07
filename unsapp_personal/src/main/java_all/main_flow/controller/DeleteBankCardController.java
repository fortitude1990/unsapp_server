package java_all.main_flow.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java_all.main_flow.constant.HttpResponseConstant;
import java_all.main_flow.utils.AccountMessageUtil;
import java_all.main_flow.utils.GetRequestJsonUtils;
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
public class DeleteBankCardController {

    @RequestMapping(value = "/delete/bankcard", method = RequestMethod.POST)
    @ResponseBody

    public void deleteBankCard(HttpServletRequest request, HttpServletResponse response) throws IOException{

        String jsonString = GetRequestJsonUtils.getRequestJsonString(request);
        JSONObject jsonObject = JSON.parseObject(jsonString);
        String accountId = jsonObject.getString("accountId");
        String bankNo = jsonObject.getString("bankNo");

        Map<String,Object> map = new HashMap<String, Object>();

        if (accountId == null || bankNo == null){
            map.put(HttpResponseConstant.RSPCODE,"1001");
            map.put(HttpResponseConstant.RSPMSG,"缺少参数");
            toResponse(response,map);
            return;
        }

        if (!AccountMessageUtil.whetherTheAccountExistsOrNotByAccountId(accountId)){
            map.put(HttpResponseConstant.RSPCODE,"1002");
            map.put(HttpResponseConstant.RSPMSG,"账号不存在");
            toResponse(response,map);
            return;
        }

        if (!AccountMessageUtil.whetherTheBankCardIsExistsOrNotByBankNo(bankNo)){
            map.put(HttpResponseConstant.RSPCODE,"1003");
            map.put(HttpResponseConstant.RSPMSG,"银行卡不存在");
            toResponse(response,map);
            return;
        }

        AccountBankCardService service = new AccountBankCardService();
        boolean result = service.deleteBankCard(bankNo);
        if (result){
            map.put(HttpResponseConstant.RSPCODE,"0000");
            map.put(HttpResponseConstant.RSPMSG,"删除成功");
            toResponse(response,map);
        }else {
            map.put(HttpResponseConstant.RSPCODE,"1004");
            map.put(HttpResponseConstant.RSPMSG,"删除失败");
            toResponse(response,map);
        }

    }

    void toResponse(HttpServletResponse response, Map map) throws IOException{
        response.setContentType(HttpResponseConstant.CONTENT_TYPE);
        response.setCharacterEncoding(HttpResponseConstant.CHARACTER_ENCODING);
        response.getWriter().print(map);
    }
}
