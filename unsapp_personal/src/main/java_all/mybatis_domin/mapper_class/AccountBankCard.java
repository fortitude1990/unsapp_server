package java_all.mybatis_domin.mapper_class;

/**
 * Created by lizhijing on 2018/10/11.
 */
public class AccountBankCard {

    private int id;
    private String accountId;
    private String bankNo;
    private String bankName;
    private String bankCode;
    private String bankAboutMobile;
    private String name;
    private String cardType;

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

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBankAboutMobile() {
        return bankAboutMobile;
    }

    public void setBankAboutMobile(String bankAboutMobile) {
        this.bankAboutMobile = bankAboutMobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

}
