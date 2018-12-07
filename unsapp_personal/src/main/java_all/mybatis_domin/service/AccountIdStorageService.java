package java_all.mybatis_domin.service;

import java_all.mybatis_domin.mapper_class.AccountIdStorage;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by lizhijing on 2018/9/10.
 */
public class AccountIdStorageService {

    public static String statement = "mapper.accountIdStorageMapper.";
    public static SqlSessionFactory factory = MybatisFactory.factory(MapperUrl.mybatis_conf);

    public AccountIdStorage selectOneValid(){

        try {
            SqlSession sqlSession = factory.openSession();
            AccountIdStorage accountIdStorage = sqlSession.selectOne(statement + "getValid",1);
            sqlSession.commit();
            sqlSession.close();
            if (accountIdStorage.getValid() == true){

                return accountIdStorage;

            }else {
                return null;
            }
        }catch (Exception e){

            throw new RuntimeException(e);
        }



    }

    public boolean updateAccountIdStorage(AccountIdStorage accountIdStorage){

        if (accountIdStorage != null){

            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateValid",accountIdStorage);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }else {
                return false;
            }

        }else {
            return false;
        }


    }

    public Boolean updateAccountIdStorageToInvalid(AccountIdStorage accountIdStorage){

        if (accountIdStorage != null){
            accountIdStorage.setValid(false);
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateValid",accountIdStorage);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }

    public Boolean updateAccountIdStorageToValid(AccountIdStorage accountIdStorage){

        if (accountIdStorage != null){
            accountIdStorage.setValid(true);
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateValid",accountIdStorage);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }else {
                return false;
            }
        }else {
            return false;
        }

    }


}
