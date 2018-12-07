package java_all.mybatis_domin.mapper_class;

/**
 * Created by lizhijing on 2018/9/10.
 */
public class AccountIdStorage {

    private int accountId;
    private Boolean valid;

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setValid(Boolean valid) {
        this.valid = valid;
    }

    public Boolean getValid() {
        return valid;
    }
}
