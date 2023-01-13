package com.ming.m_blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 日志实体
 */
@TableName(value ="operation_log")
@Data
public class OperationLog implements Serializable {
    /**
     * 主键id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 操作模块
     */
    private String optModule;

    /**
     * 操作类型
     */
    private String optType;

    /**
     * 操作url
     */
    private String optUrl;

    /**
     * 操作方法
     */
    private String optMethod;

    /**
     * 操作描述
     */
    private String optDesc;

    /**
     * 请求参数
     */
    private String requestParam;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 返回数据
     */
    private String responseData;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 操作ip
     */
    private String ipAddress;

    /**
     * 操作地址
     */
    private String ipSource;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        OperationLog other = (OperationLog) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getOptModule() == null ? other.getOptModule() == null : this.getOptModule().equals(other.getOptModule()))
            && (this.getOptType() == null ? other.getOptType() == null : this.getOptType().equals(other.getOptType()))
            && (this.getOptUrl() == null ? other.getOptUrl() == null : this.getOptUrl().equals(other.getOptUrl()))
            && (this.getOptMethod() == null ? other.getOptMethod() == null : this.getOptMethod().equals(other.getOptMethod()))
            && (this.getOptDesc() == null ? other.getOptDesc() == null : this.getOptDesc().equals(other.getOptDesc()))
            && (this.getRequestParam() == null ? other.getRequestParam() == null : this.getRequestParam().equals(other.getRequestParam()))
            && (this.getRequestMethod() == null ? other.getRequestMethod() == null : this.getRequestMethod().equals(other.getRequestMethod()))
            && (this.getResponseData() == null ? other.getResponseData() == null : this.getResponseData().equals(other.getResponseData()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getNickname() == null ? other.getNickname() == null : this.getNickname().equals(other.getNickname()))
            && (this.getIpAddress() == null ? other.getIpAddress() == null : this.getIpAddress().equals(other.getIpAddress()))
            && (this.getIpSource() == null ? other.getIpSource() == null : this.getIpSource().equals(other.getIpSource()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getOptModule() == null) ? 0 : getOptModule().hashCode());
        result = prime * result + ((getOptType() == null) ? 0 : getOptType().hashCode());
        result = prime * result + ((getOptUrl() == null) ? 0 : getOptUrl().hashCode());
        result = prime * result + ((getOptMethod() == null) ? 0 : getOptMethod().hashCode());
        result = prime * result + ((getOptDesc() == null) ? 0 : getOptDesc().hashCode());
        result = prime * result + ((getRequestParam() == null) ? 0 : getRequestParam().hashCode());
        result = prime * result + ((getRequestMethod() == null) ? 0 : getRequestMethod().hashCode());
        result = prime * result + ((getResponseData() == null) ? 0 : getResponseData().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getNickname() == null) ? 0 : getNickname().hashCode());
        result = prime * result + ((getIpAddress() == null) ? 0 : getIpAddress().hashCode());
        result = prime * result + ((getIpSource() == null) ? 0 : getIpSource().hashCode());
        result = prime * result + ((getCreateTime() == null) ? 0 : getCreateTime().hashCode());
        result = prime * result + ((getUpdateTime() == null) ? 0 : getUpdateTime().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", optModule=").append(optModule);
        sb.append(", optType=").append(optType);
        sb.append(", optUrl=").append(optUrl);
        sb.append(", optMethod=").append(optMethod);
        sb.append(", optDesc=").append(optDesc);
        sb.append(", requestParam=").append(requestParam);
        sb.append(", requestMethod=").append(requestMethod);
        sb.append(", responseData=").append(responseData);
        sb.append(", userId=").append(userId);
        sb.append(", nickname=").append(nickname);
        sb.append(", ipAddress=").append(ipAddress);
        sb.append(", ipSource=").append(ipSource);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
