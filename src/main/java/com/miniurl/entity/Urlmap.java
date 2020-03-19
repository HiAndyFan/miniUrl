package com.miniurl.entity;

import java.util.Date;
import javax.persistence.*;

public class Urlmap {
    /**
     * 短链接
     */
    @Id
    @Column(name = "RESOURSE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String resourseId;

    /**
     * 乐观锁
     */
    @Column(name = "REVISION")
    private String revision;

    /**
     * 创建人UID
     */
    @Column(name = "CREATED_BYUID")
    private String createdByuid;

    /**
     * 创建时间
     */
    @Column(name = "CREATED_TIME")
    private Date createdTime;

    /**
     * 更新时间
     */
    @Column(name = "UPDATED_TIME")
    private Date updatedTime;

    /**
     * 源链接
     */
    @Column(name = "ORIGINAL_URL")
    private String originalUrl;

    /**
     * 生存周期
     */
    @Column(name = "ID_TTL")
    private Integer idTtl;

    /**
     * 获取短链接
     *
     * @return RESOURSE_ID - 短链接
     */
    public String getResourseId() {
        return resourseId;
    }

    /**
     * 设置短链接
     *
     * @param resourseId 短链接
     */
    public void setResourseId(String resourseId) {
        this.resourseId = resourseId;
    }

    /**
     * 获取乐观锁
     *
     * @return REVISION - 乐观锁
     */
    public String getRevision() {
        return revision;
    }

    /**
     * 设置乐观锁
     *
     * @param revision 乐观锁
     */
    public void setRevision(String revision) {
        this.revision = revision;
    }

    /**
     * 获取创建人UID
     *
     * @return CREATED_BYUID - 创建人UID
     */
    public String getCreatedByuid() {
        return createdByuid;
    }

    /**
     * 设置创建人UID
     *
     * @param createdByuid 创建人UID
     */
    public void setCreatedByuid(String createdByuid) {
        this.createdByuid = createdByuid;
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
     * 获取更新时间
     *
     * @return UPDATED_TIME - 更新时间
     */
    public Date getUpdatedTime() {
        return updatedTime;
    }

    /**
     * 设置更新时间
     *
     * @param updatedTime 更新时间
     */
    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    /**
     * 获取源链接
     *
     * @return ORIGINAL_URL - 源链接
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    /**
     * 设置源链接
     *
     * @param originalUrl 源链接
     */
    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
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
}