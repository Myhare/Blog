<template>
  <div class="message-container">
    <el-card class="main-card">
      <div class="title">留言管理</div>
      <div class="review-menu">
        <span>状态</span>
        <span
          @click="changeReview(null)"
          :class="isReview == null ? 'active-review' : 'review'"
        >
        全部
      </span>
        <span
          @click="changeReview(1)"
          :class="isReview === 1 ? 'active-review' : 'review'"
        >
        正常
      </span>
        <span
          @click="changeReview(0)"
          :class="isReview === 0 ? 'active-review' : 'review'"
        >
        审核中
      </span>
      </div>
      <!-- 表格操作 -->
      <div class="operation-container">
        <el-button
          type="danger"
          size="small"
          icon="el-icon-delete"
          :disabled="messageIdList.length == 0"
          @click="deleteFlag = true"
        >
          批量删除
        </el-button>
        <el-button
          type="success"
          size="small"
          icon="el-icon-success"
          :disabled="messageIdList.length == 0"
          @click="updateMessageReview(null)"
        >
          批量通过
        </el-button>
        <!-- 数据筛选 -->
        <div style="margin-left:auto">
          <el-input
            v-model="keywords"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入用户昵称"
            style="width:200px"
            @keyup.enter.native="searchMessages"
          />
          <el-button
            type="primary"
            size="small"
            icon="el-icon-search"
            style="margin-left:1rem"
            @click="searchMessages"
          >
            搜索
          </el-button>
        </div>
      </div>
      <!-- 表格展示 -->
      <el-table
        border
        v-loading="loading"
        :data="messageList"
        @selection-change="selectionChange"
      >
        <!-- 表格列 -->
        <el-table-column type="selection" width="55" />
        <el-table-column prop="avatar" label="头像" align="center" width="150">
          <template slot-scope="scope">
            <img :src="scope.row.avatar" width="40" height="40" />
          </template>
        </el-table-column>
        <el-table-column
          prop="nickname"
          label="留言人"
          align="center"
          width="150"
        />
        <el-table-column prop="messageContent" label="留言内容" align="center" />
        <el-table-column
          prop="ipAddress"
          label="ip地址"
          align="center"
          width="150"
        />
        <el-table-column
          prop="ipSource"
          label="ip来源"
          align="center"
          width="170"
        />
        <!-- 状态 -->
        <el-table-column prop="isReview" label="状态" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isReview == 0" type="warning">审核中</el-tag>
            <el-tag v-if="scope.row.isReview == 1" type="success">正常</el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createTime"
          label="留言时间"
          width="140"
          align="center"
        >
          <template slot-scope="scope">
            <i class="el-icon-time" style="margin-right:5px" />
            {{ scope.row.createTime | date }}
          </template>
        </el-table-column>
        <!-- 列操作 -->
        <el-table-column label="操作" width="160" align="center">
          <template slot-scope="scope">
            <el-button
              v-if="scope.row.isReview === 0"
              size="mini"
              type="success"
              slot="reference"
              @click="updateMessageReview(scope.row.id)"
            >
              通过
            </el-button>
            <el-popconfirm
              style="margin-left:10px"
              title="确定删除吗？"
              @onConfirm="deleteMessage(scope.row.id)"
            >
              <el-button size="mini" type="danger" slot="reference">
                删除
              </el-button>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <el-pagination
        class="pagination-container"
        background
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page="current"
        :page-size="size"
        :total="count"
        :page-sizes="[10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
      />
      <!-- 批量删除对话框 -->
      <el-dialog :visible.sync="deleteFlag" width="30%">
        <div class="dialog-title-container" slot="title">
          <i class="el-icon-warning" style="color:#ff9900" />提示
        </div>
        <div style="font-size:1rem">是否删除选中项？</div>
        <div slot="footer">
          <el-button @click="deleteFlag = false">取 消</el-button>
          <el-button type="primary" @click="deleteMessage(null)">
            确 定
          </el-button>
        </div>
      </el-dialog>
    </el-card>
  </div>
</template>

<script>
import qs from 'qs'
export default {
  created() {
    this.listMessages();
  },
  data: function() {
    return {
      loading: true,
      deleteFlag: false,
      messageIdList: [],
      messageList: [],
      keywords: null,
      isReview: null,
      current: 1,
      size: 10,
      count: 0
    };
  },
  methods: {
    selectionChange(messageList) {
      this.messageIdList = [];
      messageList.forEach(item => {
        this.messageIdList.push(item.id);
      });
    },
    searchMessages() {
      this.current = 1;
      this.listMessages();
    },
    sizeChange(size) {
      this.size = size;
      this.listMessages();
    },
    currentChange(current) {
      this.current = current;
      this.listMessages();
    },
    // 删除留言
    deleteMessage(id) {
      var param = {};
      if (id != null) {
        param = [id];
      } else {
        param = this.messageIdList;
      }
      console.log(param);
      // 批量删除
      this.$store.dispatch('message/delMessageList',param)
      .then(res => {
        if (res.flag) {
          this.$notify.success({
            title: "成功",
            message: res.message
          });
          this.listMessages();
        } else {
          this.$notify.error({
            title: "失败",
            message: res.message
          });
        }
        this.deleteFlag = false;
      })
    },
    // 更新留言审核
    updateMessageReview(id) {
      let param = {};
      if (id != null) {
        param.idList = [id];
      } else {
        param.idList = this.messageIdList;
      }
      // 这里暂时只将留言改成已审核，审核后可以删除
      param.isReview = 1;
      this.$store.dispatch('message/updateMessageReview',param)
      .then(res => {
        if (res.flag) {
          this.$notify.success({
            title: "成功",
            message: res.message
          });
          this.listMessages();
        } else {
          this.$notify.error({
            title: "失败",
            message: res.message
          });
        }
      })
    },
    changeReview(review) {
      this.isReview = review;
    },
    // 查询留言列表
    listMessages() {
      const params = {
        current: this.current,
        size: this.size,
        keywords: this.keywords,
        isReview: this.isReview
      }
      console.log(params);
      this.$store.dispatch('message/messageList',params)
        .then(data => {
          // console.log('查询后台留言列表成功,res-->');
          // console.log(data);
          this.messageList = data.data.recordList;
          this.count = data.data.count;
          this.loading = false;
      })
    }
  },
  watch: {
    isReview() {
      this.current = 1;
      this.listMessages();
    }
  }
};
</script>

<style scoped>
.message-container{
  padding: 20px;
}
.operation-container {
  margin-top: 1.5rem;
}
.review-menu {
  font-size: 14px;
  margin-top: 40px;
  color: #999;
}
.review-menu span {
  margin-right: 24px;
}
.review {
  cursor: pointer;
}
.active-review {
  cursor: pointer;
  color: #333;
  font-weight: bold;
}
</style>
