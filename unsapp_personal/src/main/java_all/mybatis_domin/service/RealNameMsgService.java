package java_all.mybatis_domin.service;

import java_all.mybatis_domin.mapper_class.RealNameMsg;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by lizhijing on 2018/9/21.
 */
public class RealNameMsgService {

    public static String statement = "mapper.realNameMsgMapper.";
    public static SqlSessionFactory factory = MybatisFactory.factory(MapperUrl.mybatis_conf);

    public Boolean insertRealNameMsg(RealNameMsg msg){

        try {

            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.insert(statement + "insertRealNameMsg",msg);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
           e.printStackTrace();
        }

        return false;
    }

    public List<RealNameMsg> selectByAccountId(String accountId){
        try {

            SqlSession sqlSession = factory.openSession();
            List<RealNameMsg> list = sqlSession.selectList(statement + "getRealNameMsgByAccountId",accountId);
            return list;

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


}
