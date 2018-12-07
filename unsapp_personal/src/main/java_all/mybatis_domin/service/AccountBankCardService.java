package java_all.mybatis_domin.service;

import java_all.mybatis_domin.mapper_class.AccountBankCard;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by lizhijing on 2018/10/11.
 */
public class AccountBankCardService {

    public static String statement = "mapper.accountBankCardMapper.";
    public static SqlSessionFactory factory = MybatisFactory.factory(MapperUrl.mybatis_conf);

    public List<AccountBankCard> selectAllBankCardByAccountId(String accountId){
        try {
            SqlSession sqlSession = factory.openSession();
            List<AccountBankCard> list = sqlSession.selectList(statement + "allBankCard",accountId);
            sqlSession.commit();
            sqlSession.close();
            if (list != null && list.size() > 0){
                return list;
            }
            return null;
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    public AccountBankCard selectOneBankCardByBankNo(String bankNo){
        try {
            SqlSession sqlSession = factory.openSession();
            List<AccountBankCard> list = sqlSession.selectList(statement + "selectOneBankCard",bankNo);
            if (list != null && list.size() > 0){
                return list.get(0);
            }
            return null;
        }catch (Exception e){
            throw  new RuntimeException(e);
        }
    }

    public boolean insertBankCard(AccountBankCard accountBankCard){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.insert(statement + "insertBankCard", accountBankCard);
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

    public boolean deleteBankCard(String bankNo){
        try {
            SqlSession sqlSession = factory.openSession();
            int result = sqlSession.delete(statement + "deleteBankCard", bankNo);
            sqlSession.commit();
            sqlSession.close();
            if (result == 1){
                return true;
            }
            return false;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
