package java_all.main_flow.utils;

import java.text.DecimalFormat;

/**
 * Created by lizhijing on 2018/10/18.
 */
public class DataFormatUtils {

    public static String twoDecimalPlacesFormat(String value){
        Double amount = Double.parseDouble(value);
        DecimalFormat format = new DecimalFormat("0.00");
        return format.format(amount);
    }

    public static double getTwoDecimalPlacesDoubleFormat(String value){
       String doubleString = twoDecimalPlacesFormat(value);
       return Double.parseDouble(doubleString);
    }


}
