package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Company_User")
/*@NamedStoredProcedureQuery(
        name = "proc_page", //该存储过程在JPA中的命名
        procedureName = "proc_page", //Mysql中存储过程的名字
        parameters = {
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "tab",type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "startItem",type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "size",type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.INOUT, name = "sortRule",type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.IN, name = "order_field",type = String.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT,name = "totalElements",type = Integer.class),
                @StoredProcedureParameter(mode = ParameterMode.OUT,name = "pageCount",type = Integer.class ),
                @StoredProcedureParameter(mode = ParameterMode.OUT,name = "isFirst",type = String.class ),
                @StoredProcedureParameter(mode = ParameterMode.OUT,name = "isLast",type = String.class )})*/
public class CompanyUser {

    private Integer id;
    private String address;
    private String code;
    private String password;
    private String name;

    public CompanyUser() {
    }

    public CompanyUser(Integer id, String address, String code, String password, String name) {
        this.id = id;
        this.address = address;
        this.code = code;
        this.password = password;
        this.name = name;
    }

    @Id
    @GeneratedValue
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
