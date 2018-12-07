package java_all.mybatis_domin.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by lizhijing on 2018/9/10.
 */
public class MybatisFactory {

    public static SqlSessionFactory factory(String mapperUrl){

        try {

            InputStream inputStream = Resources.getResourceAsStream(mapperUrl);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            return factory;


        }catch (Exception e){

            throw new RuntimeException(e);

        }



    }

}
