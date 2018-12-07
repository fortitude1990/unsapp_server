package java_all.mybatis_domin.mapper_class;

/**
 * Created by lizhijing on 2018/10/8.
 */
public class AccountProperty implements Cloneable {

    private int id;
    private String accountId;
    private String totalProperty;
    private String availableProperty;
    private String monthlySpending;
    private String monthlyIncome;
    private String payPwd;
    private String updateTime;
    private int payPwdErrorNo;
    private String yesterdayIncome;
    private String yesterdaySpending;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getTotalProperty() {
        return totalProperty;
    }

    public void setTotalProperty(String totalProperty) {
        this.totalProperty = totalProperty;
    }

    public String getAvailableProperty() {
        return availableProperty;
    }

    public void setAvailableProperty(String availableProperty) {
        this.availableProperty = availableProperty;
    }

    public String getMonthlySpending() {
        return monthlySpending;
    }

    public void setMonthlySpending(String monthlySpending) {
        this.monthlySpending = monthlySpending;
    }

    public String getMonthlyIncome() {
        return monthlyIncome;
    }

    public void setMonthlyIncome(String monthlyIncome) {
        this.monthlyIncome = monthlyIncome;
    }

    public String getPayPwd() {
        return payPwd;
    }

    public void setPayPwd(String payPwd) {
        this.payPwd = payPwd;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public int getPayPwdErrorNo() {
        return payPwdErrorNo;
    }

    public void setPayPwdErrorNo(int payPwdErrorNo) {
        this.payPwdErrorNo = payPwdErrorNo;
    }

    public String getYesterdayIncome() {
        return yesterdayIncome;
    }

    public void setYesterdayIncome(String yesterdayIncome) {
        this.yesterdayIncome = yesterdayIncome;
    }

    public String getYesterdaySpending() {
        return yesterdaySpending;
    }

    public void setYesterdaySpending(String yesterdaySpending) {
        this.yesterdaySpending = yesterdaySpending;
    }

    @Override
    public AccountProperty clone(){

        AccountProperty accountProperty = null;
        try {
           accountProperty = (AccountProperty) super.clone();
        }catch (CloneNotSupportedException e){
            throw new RuntimeException(e);
        }
        return accountProperty;
    }
}
