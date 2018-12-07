package java_all.main_flow.utils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;

/**
 * Created by lizhijing on 2018/10/26.
 */






public class PictureUtils {

    private static String realNamePath = "/Users/lizhijing/Desktop/JavaPicture/unsapp_personal/RealName/";
    private static String baseMsgPath = "/Users/lizhijing/Desktop/JavaPicture/unsapp_personal/BaseMsg/";
    private static String bankImagePath = "/Users/lizhijing/Desktop/JavaPicture/unsapp_personal/BankImage/";

    public enum PictureSavePath{

        REAL_NAME,
        BASE_MSG,
        BANK_IMAGE

    }
    public static boolean save(String imgStr, String imgName, PictureSavePath path) {

        String savePath = "";
        switch (path){
            case REAL_NAME:
                savePath = realNamePath;
                break;
            case BASE_MSG:
                savePath = baseMsgPath;
                break;
            case BANK_IMAGE:
                savePath = bankImagePath;

        }

        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = savePath + imgName + ".jpg";//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static String read(String imageName, PictureSavePath path){

        String savePath = "";
        switch (path){
            case REAL_NAME:
                savePath = realNamePath;
                break;
            case BASE_MSG:
                savePath = baseMsgPath;
                break;
            case BANK_IMAGE:
                savePath = bankImagePath;

        }

        //对字节数组字符串进行Base64解码并生成图片
        if (imageName == null) //图像数据为空
            return null;

        try {
            String imgFilePath = savePath + imageName + ".jpg";//新生成的图片
//            OutputStream out = new FileOutputStream(imgFilePath);
//            out.write(b);
//            out.flush();
//            out.close();
            File file = new File(imgFilePath);
            InputStream inputStream;
            try {
                inputStream = new FileInputStream(file);
                byte[] tempbytes = new byte[1000000];
                inputStream.read(tempbytes);
                BASE64Encoder encoder = new BASE64Encoder();
                String content = encoder.encode(tempbytes);
                return content;

            }catch (Exception e){
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }


}
