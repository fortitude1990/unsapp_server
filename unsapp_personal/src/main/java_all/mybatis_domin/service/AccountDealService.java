package java_all.mybatis_domin.service;

import java_all.mybatis_domin.mapper_class.AccountDeal;
import java_all.mybatis_domin.mapper_class.AccountDealQuery;
import java_all.mybatis_domin.mapper_class.AccountProperty;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

/**
 * Created by lizhijing on 2018/10/11.
 */
public class AccountDealService {

    public static String statement = "mapper.accountDealMapper.";

    public static String SELECT_ALL_RECHARGE = "selectAllRecharge";
    public static String SELECT_ALL_WITHDRAW = "selectAllWithdraw";
    public static String SELECT_ALL_TRANSFER = "selectAllTransfer";
    public static String SELECT_ONE_RECHARGE = "selectOneRcharge";
    public static String SELECT_ONE_WITHDRAW = "selectOneWithdraw";
    public static String SELECT_ONE_TRANSFER = "selectOneTransfer";
    public static String SELECT_BY_SIZE = "selectBySize";
    public static String INSERT_ACCOUNT_DEAL = "insertAccountDeal";
    public static String SELECTBYTIME = "selectByTime";
    public static String SELECT_ALL_BY_SIZE = "selectAllBySize";


    public enum  OperateTypeEnum{
        INSERT,
        UADATE,
        SELECTLIST,
        SELECTONE
    }

    public static SqlSessionFactory factory = MybatisFactory.factory(MapperUrl.mybatis_conf);


    public boolean insertAccountDeal(AccountDeal accountDeal){
        boolean result = sqlSessionDealInsertOrUpdate(accountDeal,
                OperateTypeEnum.INSERT, INSERT_ACCOUNT_DEAL);
        return result;
    }


    public AccountDeal selectOneTransfer(String orderNo){
        List<AccountDeal> list = (List)sqlSessionDealSelect(orderNo,
                OperateTypeEnum.SELECTLIST,SELECT_ONE_TRANSFER);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public AccountDeal selectOneWithdraw(String orderNo){
        List<AccountDeal> list = (List)sqlSessionDealSelect(orderNo,
                OperateTypeEnum.SELECTLIST,SELECT_ONE_WITHDRAW);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }


    public AccountDeal selectOneRecharge(String orderNo){
        List<AccountDeal> list = (List)sqlSessionDealSelect(orderNo,
                OperateTypeEnum.SELECTLIST,SELECT_ONE_RECHARGE);
        if (list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }

    public List<AccountDeal> selectBySize(AccountDealQuery accountDealQuery){

        List<AccountDeal> list = (List)sqlSessionDealSelect(accountDealQuery,
                OperateTypeEnum.SELECTLIST, SELECT_BY_SIZE);
        return list;

    }

    public List<AccountDeal> selectAllRecharge(String accountId){
       List<AccountDeal> list  = (List) sqlSessionDealSelect(accountId,OperateTypeEnum.SELECTLIST,SELECT_ALL_RECHARGE);
       return list;
    }

    public List<AccountDeal> selectAllWithdraw(String accountId){
        List<AccountDeal> list  = (List) sqlSessionDealSelect(accountId,OperateTypeEnum.SELECTLIST,SELECT_ALL_WITHDRAW);
        return list;
    }

    public List<AccountDeal> selectAllTransfer(String accountId){
        List<AccountDeal> list  = (List) sqlSessionDealSelect(accountId,OperateTypeEnum.SELECTLIST,SELECT_ALL_TRANSFER);
        return list;
    }

    public List<AccountDeal> selectByTime(AccountDealQuery accountDealQuery){
        List<AccountDeal> list = (List<AccountDeal>) sqlSessionDealSelect(accountDealQuery, OperateTypeEnum.SELECTLIST, SELECTBYTIME);
        return list;
    }

    public List<AccountDeal> selectAllBySize(AccountDealQuery accountDealQuery){
        List<AccountDeal> list = (List<AccountDeal>) sqlSessionDealSelect(accountDealQuery, OperateTypeEnum.SELECTLIST, SELECT_ALL_BY_SIZE);
        return list;
    }


    public Object sqlSessionDealSelect(Object object, OperateTypeEnum operateTypeEnum,String url){

        try {
            SqlSession sqlSession = factory.openSession();
            List result;
            switch (operateTypeEnum){
                case SELECTLIST:
                    result = sqlSession.selectList(statement + url,object);
                    break;
                case SELECTONE:
                    result = sqlSession.selectOne(statement + url,object);
                    break;
                default:
                    result = null;
                    break;
            }
            sqlSession.commit();
            sqlSession.close();
           return result;

        }catch (Exception e){
            throw new RuntimeException(e);
        }

    }


    public boolean sqlSessionDealInsertOrUpdate(Object object, OperateTypeEnum operateTypeEnum,String url){

        try {
            SqlSession sqlSession = factory.openSession();
            int result = 0;
            switch (operateTypeEnum){
                case INSERT:
                    result = sqlSession.insert(statement + url,object);
                    break;
                case UADATE:
                    result = sqlSession.update(statement + url,object);
                    break;
            }

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

}
