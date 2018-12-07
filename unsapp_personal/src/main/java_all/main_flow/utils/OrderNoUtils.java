package java_all.main_flow.utils;

/**
 * Created by lizhijing on 2018/10/18.
 */
public class OrderNoUtils {

    public static String orderNo(){
        int a = (int)(Math.random()*(9999-1000+1))+1000;//产生1000-9999的随机数
        return  DateUtils.nowDate() + String.valueOf(a);
    }


}
