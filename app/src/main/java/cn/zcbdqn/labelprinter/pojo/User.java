package cn.zcbdqn.labelprinter.pojo;

import org.litepal.crud.DataSupport;

/**
 * Created by gumuyun on 2019/1/3.
 */

public class User extends DataSupport {

    private Integer id;
    private String userName;
    private String password;
    private Integer companyId;
    private Integer createBy;
    private String createDate;
    private Integer updateBy;
    private String updateDate;

    private String remarks;


    public User() {
    }

    public User(Integer id, String userName, String password, Integer companyId, Integer createBy, String createDate, Integer updateBy, String updateDate, String remarks) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.companyId = companyId;
        this.createBy = createBy;
        this.createDate = createDate;
        this.updateBy = updateBy;
        this.updateDate = updateDate;
        this.remarks = remarks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public Integer getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Integer getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Integer updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}


