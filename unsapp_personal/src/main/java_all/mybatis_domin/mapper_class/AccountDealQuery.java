package java_all.mybatis_domin.mapper_class;

/**
 * Created by lizhijing on 2018/10/12.
 */
public class AccountDealQuery {

    private int startIndex;
    private int queryLength;
    private String accountId;
    private String orderNo;
    private String dealType;
    private String startTime;
    private String endTime;


    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setQueryLength(int queryLength) {
        this.queryLength = queryLength;
    }

    public int getQueryLength() {
        return queryLength;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setDealType(String dealType) {
        this.dealType = dealType;
    }

    public String getDealType() {
        return dealType;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
