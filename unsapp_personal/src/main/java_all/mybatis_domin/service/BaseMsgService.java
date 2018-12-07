package java_all.mybatis_domin.service;

import java_all.mybatis_domin.mapper_class.BaseMsg;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by lizhijing on 2018/9/10.
 */

public class BaseMsgService {


   public static String statement = "mapper.baseMsgMapper.";
   public static SqlSessionFactory factory = MybatisFactory.factory(MapperUrl.mybatis_conf);

    public void registerBaseMsg(BaseMsg msg){

        System.out.print("register进来了");

        try {
            SqlSession sqlSession = factory.openSession();
            sqlSession.insert(statement + "insertRegisterMsg",msg);
            sqlSession.commit();
            sqlSession.close();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public List<BaseMsg> selectBaseMsgByTel(String tel){

           try {
                SqlSession sqlSession = factory.openSession();
                List<BaseMsg> list = sqlSession.selectList(statement + "getBaseMsgByTel",tel);
                sqlSession.commit();
                sqlSession.close();
                return list;
            }catch (Exception e){
                throw new RuntimeException(e);
            }

    }


    public Boolean updateBaseMsgLoginTime(BaseMsg baseMsg){
        try {

            SqlSession sqlSession = factory.openSession();
           int rsulut = sqlSession.update(statement + "updateLoginTime",baseMsg);
            sqlSession.commit();
            sqlSession.close();
           if (rsulut == 1){
               return true;
           }else {
               return false;
           }


        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    public Boolean updateBaseMsgPwdInputErrorNum(BaseMsg baseMsg){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updatePwdErrorNum",baseMsg);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }else {
                return false;
            }

        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }


    public List<BaseMsg> selectBaseMsgByAccountId(String accountId){
        try {
            SqlSession sqlSession = factory.openSession();
            List result = sqlSession.selectList(statement + "getBaseMsgByAccountId",accountId);
            sqlSession.commit();
            sqlSession.close();
            return result;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public boolean updateBaseMsg(BaseMsg baseMsg){
        try {

            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateBaseMsg", baseMsg);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateLoginTime(BaseMsg baseMsg){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateLoginTime", baseMsg);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    public boolean updatePwdErrorNum(BaseMsg baseMsg){
        return updateCommon("updatePwdErrorNum", baseMsg);
    }

    private boolean updateCommon(String url, BaseMsg baseMsg){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + url, baseMsg);
            if (result == 1){
                return true;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
