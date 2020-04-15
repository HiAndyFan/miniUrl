package com.miniurl.entity;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

public class Clipboard {
    /**
     * 剪贴板条目ID
     */
    @Id
    @Column(name = "RESOURSE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer resourseId;

    /**
     * 创建人UID
     */
    @Column(name = "CREATED_BY_UID")
    private Integer createdByUid;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 生存周期
     */
    @Column(name = "ID_TTL")
    private Integer idTtl;

    /**
     * 内容
     */
    private String content;

    /**
     * 获取剪贴板条目ID
     *
     * @return RESOURSE_ID - 剪贴板条目ID
     */
    public Integer getResourseId() {
        return resourseId;
    }

    /**
     * 设置剪贴板条目ID
     *
     * @param resourseId 剪贴板条目ID
     */
    public void setResourseId(Integer resourseId) {
        this.resourseId = resourseId;
    }

    /**
     * 获取创建人UID
     *
     * @return CREATED_BY_UID - 创建人UID
     */
    public Integer getCreatedByUid() {
        return createdByUid;
    }

    /**
     * 设置创建人UID
     *
     * @param createdByUid 创建人UID
     */
    public void setCreatedByUid(Integer createdByUid) {
        this.createdByUid = createdByUid;
    }

    /**
     * 获取创建时间
     *
     * @return CREATED_TIME - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取生存周期
     *
     * @return ID_TTL - 生存周期
     */
    public Integer getIdTtl() {
        return idTtl;
    }

    /**
     * 设置生存周期
     *
     * @param idTtl 生存周期
     */
    public void setIdTtl(Integer idTtl) {
        this.idTtl = idTtl;
    }

    /**
     * 获取内容
     *
     * @return content - 内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置内容
     *
     * @param content 内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}