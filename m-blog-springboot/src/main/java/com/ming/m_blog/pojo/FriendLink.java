package com.ming.m_blog.pojo;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 友链
 * @TableName friend_link
 */
@TableName(value ="friend_link")
@Data
public class FriendLink implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 链接名
     */
    private String linkName;

    /**
     * 链接头像
     */
    private String linkAvatar;

    /**
     * 链接地址
     */
    private String linkAddress;

    /**
     * 链接介绍
     */
    private String linkIntro;

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
        FriendLink other = (FriendLink) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getLinkName() == null ? other.getLinkName() == null : this.getLinkName().equals(other.getLinkName()))
            && (this.getLinkAvatar() == null ? other.getLinkAvatar() == null : this.getLinkAvatar().equals(other.getLinkAvatar()))
            && (this.getLinkAddress() == null ? other.getLinkAddress() == null : this.getLinkAddress().equals(other.getLinkAddress()))
            && (this.getLinkIntro() == null ? other.getLinkIntro() == null : this.getLinkIntro().equals(other.getLinkIntro()))
            && (this.getCreateTime() == null ? other.getCreateTime() == null : this.getCreateTime().equals(other.getCreateTime()))
            && (this.getUpdateTime() == null ? other.getUpdateTime() == null : this.getUpdateTime().equals(other.getUpdateTime()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getLinkName() == null) ? 0 : getLinkName().hashCode());
        result = prime * result + ((getLinkAvatar() == null) ? 0 : getLinkAvatar().hashCode());
        result = prime * result + ((getLinkAddress() == null) ? 0 : getLinkAddress().hashCode());
        result = prime * result + ((getLinkIntro() == null) ? 0 : getLinkIntro().hashCode());
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
        sb.append(", linkName=").append(linkName);
        sb.append(", linkAvatar=").append(linkAvatar);
        sb.append(", linkAddress=").append(linkAddress);
        sb.append(", linkIntro=").append(linkIntro);
        sb.append(", createTime=").append(createTime);
        sb.append(", updateTime=").append(updateTime);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}
