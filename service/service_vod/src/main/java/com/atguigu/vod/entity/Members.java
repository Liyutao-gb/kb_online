package com.atguigu.vod.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (Members)实体类
 *
 * @author liyutao
 * @since 2021-05-17 13:29:20
 */
public class Members implements Serializable {
    private static final long serialVersionUID = -65318018623055375L;
    /**
     * 自增
     */
    private Integer id;

    private String username;

    private Integer age;

    private Integer sex;

    private Date date;

    private String desc;

    private Boolean lid;

    private Long longid;

    private Byte[] name;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Boolean getLid() {
        return lid;
    }

    public void setLid(Boolean lid) {
        this.lid = lid;
    }

    public Long getLongid() {
        return longid;
    }

    public void setLongid(Long longid) {
        this.longid = longid;
    }

    public Byte[] getName() {
        return name;
    }

    public void setName(Byte[] name) {
        this.name = name;
    }

}
