package java_all.main_flow.utils;

import java_all.mybatis_domin.mapper_class.AccountBankCard;
import java_all.mybatis_domin.mapper_class.AccountProperty;
import java_all.mybatis_domin.mapper_class.BaseMsg;
import java_all.mybatis_domin.mapper_class.RealNameMsg;
import java_all.mybatis_domin.service.AccountBankCardService;
import java_all.mybatis_domin.service.AccountPropertyService;
import java_all.mybatis_domin.service.BaseMsgService;
import java_all.mybatis_domin.service.RealNameMsgService;

import java.util.List;

/**
 * Created by lizhijing on 2018/10/9.
 */
public class AccountMessageUtil {

    public static boolean whetherTheAccountPropertyCanClearAllMonthlyIncomeOrSpending(AccountProperty accountProperty){

        String updateTime = accountProperty.getUpdateTime();
        if (updateTime != null){

            String nowDateString = DateUtils.nowDate("yyyyMM");
            String updateDateString = DateUtils.transferFormat("yyyyMMddHHmmss", "yyyyMM", updateTime);

            if (Integer.parseInt(nowDateString) > Integer.parseInt(updateDateString)){
                return true;
            }
        }

        return false;
    }


    public static boolean whetherTheAccountIsRealNameOrNotByAccountId(String accountId){
        RealNameMsgService service = new RealNameMsgService();
        List<RealNameMsg> list = service.selectByAccountId(accountId);
        if (list != null && list.size() > 0){
            return true;
        }
        return false;
    }


   public static boolean whetherTheAccountExistsOrNotByAccountId(String accountId){

        BaseMsgService baseMsgService = new BaseMsgService();
        List<BaseMsg> list = baseMsgService.selectBaseMsgByAccountId(accountId);
        if (list != null && list.size() > 0){
            return true;
        }
        return false;

    }

   public static boolean whetherThePayPasswordIsSetByAccountId(String accountId){

       if (whetherTheAccountExistsOrNotByAccountId(accountId)){

           AccountPropertyService accountPropertyService = new AccountPropertyService();
           AccountProperty accountProperty = accountPropertyService.gainAccountPropertyMessageBy(accountId);
           if (accountProperty != null && accountProperty.getPayPwd() != null && accountProperty.getPayPwd().length() > 0){
               return true;
           }else {
               return false;
           }
       }
        return false;
    }

    public static boolean whetherTheAccountPropertyIsExistsOrNotInDatabaseByAccountId(String accountId){

        AccountPropertyService accountPropertyService = new AccountPropertyService();
        AccountProperty accountProperty = accountPropertyService.gainAccountPropertyMessageBy(accountId);
        if (accountProperty != null){
            return true;
        }
        return false;
    }

    public static boolean whetherTheBankCardIsExistsOrNotByBankNo(String bankNo){
        AccountBankCardService accountBankCardService = new AccountBankCardService();
        AccountBankCard accountBankCard = accountBankCardService.selectOneBankCardByBankNo(bankNo);
        if (accountBankCard != null){
            return true;
        }
        return false;
    }

}
