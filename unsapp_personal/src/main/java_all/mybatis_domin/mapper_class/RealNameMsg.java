package java_all.mybatis_domin.mapper_class;

/**
 * Created by lizhijing on 2018/9/21.
 */
public class RealNameMsg {

    private int id;
    private String accountId;
    private String name;
    private String identityId;
    private String identityIdValidDate;
    private String frontFaceOfIdCardPhoto;
    private String reverseSideOfIdCardPhoto;
    private String status;

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getIdentityId() {
        return identityId;
    }

    public void setIdentityId(String identityId) {
        this.identityId = identityId;
    }

    public String getIdentityIdValidDate() {
        return identityIdValidDate;
    }

    public void setIdentityIdValidDate(String identityIdValidDate) {
        this.identityIdValidDate = identityIdValidDate;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFrontFaceOfIdCardPhoto() {
        return frontFaceOfIdCardPhoto;
    }

    public void setFrontFaceOfIdCardPhoto(String frontFaceOfIdCardPhoto) {
        this.frontFaceOfIdCardPhoto = frontFaceOfIdCardPhoto;
    }

    public String getReverseSideOfIdCardPhoto() {
        return reverseSideOfIdCardPhoto;
    }

    public void setReverseSideOfIdCardPhoto(String reverseSideOfIdCardPhoto) {
        this.reverseSideOfIdCardPhoto = reverseSideOfIdCardPhoto;
    }
}
