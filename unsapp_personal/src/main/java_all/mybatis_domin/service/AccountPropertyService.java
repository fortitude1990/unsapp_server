package java_all.mybatis_domin.service;

import java_all.mybatis_domin.mapper_class.AccountProperty;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by lizhijing on 2018/10/8.
 */
public class AccountPropertyService {

    static public String statement = "mapper.accountProperty.";
    static public SqlSessionFactory factory = MybatisFactory.factory(MapperUrl.mybatis_conf);

    public AccountProperty gainAccountPropertyMessageBy(String accountId){

        try {
            SqlSession sqlSession = factory.openSession();
            List<AccountProperty> list = sqlSession.selectList(statement+"allMessage",accountId);
            sqlSession.commit();
            sqlSession.close();
            if (list == null || list.size() == 0){
                return null;
            }else {
                return list.get(0);
            }
        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public boolean insertPayPwd(AccountProperty accountProperty){

        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.insert(statement + "insertPassword",accountProperty);
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

    public boolean updatePayPwd(AccountProperty accountProperty){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updatePassword", accountProperty);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }

            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }


    public boolean insertAccountProperty(AccountProperty accountProperty){
        try {

            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.insert(statement + "insertProperty", accountProperty);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }

            return false;
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    public boolean updateAccountProperty(AccountProperty accountProperty){
        try {

            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateProperty", accountProperty);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }
            return false;
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    public boolean updateAccountPropertyMonthlySpendingOrMonthlyIncome(AccountProperty accountProperty){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.update(statement + "updateMonthlySpendingOrMonthlyIncome", accountProperty);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }
            return false;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

}
