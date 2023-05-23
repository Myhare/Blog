<template>
  <div class="comment-container">
    <el-card class="main-card">
      <div class="title">评论管理</div>
      <div class="operation-container">
        <!--状态选择-->
        <div class="article-status-menu">
          <span :class="isActiveStatus('all')" @click="changeStatus('all')">全部</span>
          <span :class="isActiveStatus('normal')" @click="changeStatus('normal')">正常</span>
          <span :class="isActiveStatus('review')" @click="changeStatus('review')">待审核</span>
          <span :class="isActiveStatus('deleted')" @click="changeStatus('deleted')">已删除</span>
        </div>
      </div>
      <!--评论条件查询、搜索区域-->
      <el-row class="comment-search" style="width: 100%;">
        <el-col :span="6">
          <div v-if="commentQueryInfo.isDelete === 0" class="comment-search-left">
            <el-button type="danger" @click="delComment" size="small" plain>批量删除</el-button>
            <el-button type="success" @click="reviewCommentList(null)" size="small" plain>批量通过</el-button>
          </div>
          <div v-else class="comment-search-left">
            <el-button type="danger" @click="reallyDelComment" size="small" plain>批量彻底删除</el-button>
            <el-button type="success" @click="restoreDelComment" size="small" plain>批量恢复</el-button>
          </div>
        </el-col>
        <el-col :span="18">
          <div class="comment-search-right">
            <!--文章类别-->
            <el-select v-model="commentQueryInfo.type"
                       size="small"
                       clearable
                       @change="getCommentList"
                       @clear="getCommentList"
                       placeholder="评论类型">
              <el-option
                v-for="item in commentTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <el-input placeholder="请输入评论用户昵称"
                      size="small"
                      @keyup.enter="getCommentList"
                      v-model="commentQueryInfo.keywords"></el-input>
            <el-button type="primary" size="small" @click="getCommentList" plain>搜索</el-button>
          </div>
        </el-col>
      </el-row>
      <!--评论列表-->
      <div class="commentList">
        <el-table
          :data="commentList"
          @selection-change="artHandleSelectionChange"
          border
          style="width: 100%">
          <!--多选框-->
          <el-table-column
            type="selection"
            width="45">
          </el-table-column>
          <!--评论用户头像-->
          <el-table-column
            label="头像"
            width="100">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <div class="avatar">
                <img :src="scope.row.avatar" alt="用户头像"/>
              </div>
            </template>
          </el-table-column>
          <!--评论用户昵称-->
          <el-table-column
            prop="nickname"
            label="昵称">
          </el-table-column>
          <!--被评论用户昵称-->
          <el-table-column
            label="评论对象">
            <template slot-scope="scope">
              {{scope.row.replyNickname ? scope.row.replyNickname : '无'}}
            </template>
          </el-table-column>
          <!--文章标签-->
          <el-table-column
            label="文章标题"
            align="center"
          >
            <template slot-scope="scope">
              {{scope.row.title ? scope.row.title : '无'}}
            </template>
          </el-table-column>
          <!--评论内容-->
          <el-table-column
            label="评论内容">
            <template slot-scope="scope">
              <span v-html="scope.row.content" class="comment-content" />
            </template>
          </el-table-column>
          <!--评论时间-->
          <el-table-column
            prop="commentTime"
            label="评论时间"
            :formatter="this.$global.dateFormat">
          </el-table-column>
          <!--评论状态-->
          <el-table-column
            label="评论状态">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.idDelete===1" style="margin: 0.2rem 0.2rem">
                已删除
              </el-tag>
              <el-tag v-else-if="scope.row.isReview===0" style="margin: 0.2rem 0.2rem">
                未审核
              </el-tag>
              <el-tag v-else-if="scope.row.isReview===1" style="margin: 0.2rem 0.2rem">
                已审核
              </el-tag>
            </template>
          </el-table-column>
          <!--评论来源-->
          <el-table-column
            label="来源">
            <template slot-scope="scope">
              <el-tag v-if="scope.row.type===1" style="margin: 0.2rem 0.2rem">
                文章
              </el-tag>
              <el-tag v-if="scope.row.type===2" style="margin: 0.2rem 0.2rem">
                友链
              </el-tag>
              <el-tag v-if="scope.row.type===3" style="margin: 0.2rem 0.2rem">
                说说
              </el-tag>
            </template>
          </el-table-column>
          <!--操作-->
          <el-table-column
            label="操作"
            width="170">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <div v-if="commentQueryInfo.isDelete === 0">
                <el-button
                  size="mini"
                  type="success"
                  slot="reference"
                  plain
                  @click = "reviewCommentList(scope.row.id)"
                  v-if="scope.row.isReview===0"
                >
                  通过
                </el-button>
                <el-button type="danger"
                           size="mini"
                           plain
                           @click="delComment($event,scope.row.id)">删除
                </el-button>
              </div>
              <div v-else>
                <el-button
                  size="mini"
                  type="success"
                  slot="reference"
                  @click="restoreDelComment($event,scope.row.id)"
                  plain
                >
                  恢复
                </el-button>
                <el-button type="danger"
                           size="mini"
                           plain
                           @click="reallyDelComment($event,scope.row.id)">彻底删除
                </el-button>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--分页组件-->
      <el-pagination
        class="pagination-container"
        @size-change="pageSizeChange"
        @current-change="pageCurrentChange"
        :current-page="commentQueryInfo.pageNum"
        :page-sizes="[2,5,10]"
        :page-size="commentQueryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="commentCount">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "Comment",
  created() {
    this.getCommentList();
  },
  data(){
    return{
      nowStatus: 'normal', // 当前选择的状态
      commentTypeList: [
        { value: 1, label: '文章' },
        { value: 2, label: '友链' },
        { value: 3, label: '说说' }
      ],
      commentQueryInfo:{
        type: null,     // 评论类型，1文章 2友链 3说说
        review: 1,   // 是否审核，0否 1是
        keywords: '',  // 查询条件
        pageNum: 1,
        pageSize: 10,
        isDelete: 0     // 是否删除
      },
      commentIdList: [], // 评论id列表
      commentList:[],   // 评论列表
      commentCount: 0,  // 评论数量
    }
  },
  computed:{
    // 活动状态
    isActiveStatus() {
      return function (status){
        return this.nowStatus === status ? 'nowStatus':'otherStatus';
      };
    }
  },
  methods:{
    // 查询文章列表
    getCommentList(){
      console.log('即将查询后台评论列表：');
      console.log(this.commentQueryInfo);
      this.$store.dispatch('message/getCommentList',this.commentQueryInfo)
        .then(( { data } ) => {
          console.log('查询后台列表成功，data：');
          console.log(data);
          this.commentList = data.recordList;
          this.commentCount = data.count;
        })
    },
    // 修改查询文章状态
    changeStatus(status){
      console.log('想要修改的状态：');
      console.log(status);
      this.nowStatus = status;
      switch (status){
        case 'all':     // 所有
          this.commentQueryInfo.review = null;
          this.commentQueryInfo.isDelete = 0;
          break;
        case 'normal':  // 正常
          this.commentQueryInfo.review = 1;
          this.commentQueryInfo.isDelete = 0;
          break;
        case 'review':   // 待审核
          console.log('1111');
          this.commentQueryInfo.review = 0;
          this.commentQueryInfo.isDelete = 0;
          break;
        case 'deleted':   // 已删除
          this.commentQueryInfo.review = null;
          this.commentQueryInfo.isDelete = 1;
          break;
      }
      this.getCommentList();
    },
    // 批量通过评论审核
    reviewCommentList(id){
      let commentIdList = [];
      if (id){
        commentIdList = [id];
      }else {
        commentIdList = this.commentIdList;
      }
      console.log("当前id："+id);
      console.log(commentIdList);
      console.log(this.commentIdList);
      this.$store.dispatch('message/reviewCommentList',commentIdList)
        .then(({data}) => {
          console.log('通过审核成功，data：');
          console.log(data);
          this.$message.success('通过审核');
          // 重新获取评论列表
          this.getCommentList();
        })
    },
    // 删除评论列表
    delComment(event,commentId){
      this.$confirm('确定删除评论吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log('commentId=>');
        console.log(commentId);
        let commentIdList = [];
        if (commentId){
          commentIdList = [commentId];
        }else {
          // 遍历选择的所有文章对象，将选择了的文章id添加进来
          commentIdList = this.commentIdList;
        }
        console.log('this.commentIdList：');
        console.log(this.commentIdList);
        console.log('commentIdList=>');
        console.log(commentIdList);
        // 批量删除文章
        this.$store.dispatch('message/delCommentList',commentIdList)
          .then(res => {
            console.log('删除文章成功,res:');
            console.log(res);
            this.getCommentList();
          }).catch(err => {
          console.log('删除文章失败,err:');
          console.log(err);
        })
      })
    },
    // 彻底删除评论
    reallyDelComment(event,commentId){
      this.$confirm('真的要彻底删除吗，删除后无法找回', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let commentIdList = [];
        if (commentId){
          commentIdList = [commentId];
        }else {
          // 遍历选择的所有文章对象，将选择了的文章id添加进来
          commentIdList = this.commentIdList;
        }
        // 批量删除文章
        this.$store.dispatch('message/reallyDelCommentList',commentIdList)
          .then(res => {
            console.log('删除文章成功,res:');
            console.log(res);
            this.getCommentList();
          }).catch(err => {
          console.log('删除文章失败,err:');
          console.log(err);
        })
      })
    },
    // 恢复评论
    restoreDelComment(event,commentId){
      this.$confirm('确定恢复评论吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        let commentIdList = [];
        if (commentId){
          commentIdList = [commentId];
        }else {
          // 遍历选择的所有文章对象，将选择了的文章id添加进来
          commentIdList = this.commentIdList;
        }
        // 批量删除文章
        this.$store.dispatch('message/restoreCommentList',commentIdList)
          .then(res => {
            console.log('恢复文章成功,res:');
            console.log(res);
            this.getCommentList();
          }).catch(err => {
          console.log('恢复文章失败,err:');
          console.log(err);
        })
      })
    },
    // 选择表格前面的多选框
    artHandleSelectionChange(commentList){
      // articleList为当前选择的表格的对象
      // 重置选择的文章id列表
      this.commentIdList = [];
      commentList.map(item => {
        this.commentIdList.push(item.id);
      })
      console.log('commentList:');
      console.log(commentList);
      console.log('this.commentIdList：');
      console.log(this.commentIdList);
      // console.log(this.articleIdList);
    },
    // 修改一页查询的大小
    pageSizeChange(newSize){
      this.commentQueryInfo.pageSize = newSize;
      // 重新获取数据
      this.getCommentList();
    },
    // 修改从第几页查询
    pageCurrentChange(newPage) {
      this.commentQueryInfo.pageNum = newPage;
      // 重新获取数据
      this.getCommentList();
    },
  },
  watch:{
    // 深度监听属性中的值
    'commentQueryInfo.review':{
      handler(){
        this.commentQueryInfo.pageNum = 1;
      }
    }
  }
}
</script>

<style scoped lang="scss">

.comment-container{
  padding: 20px;

  // 选择状态栏
  .article-status-menu{
    font-size: 14px;
    color: #999;
    span{
      margin-right: 24px;
    }
    // 当前选择的状态样式
    .nowStatus{
      color: #333;
      font-weight: bold;
      cursor: pointer;
    }
    // 其他状态样式
    .otherStatus{
      cursor: pointer;  // 鼠标移动上去改变样式
    }
  }

  // 删除、修改样式区域
  .comment-search{
    // 左侧区域
    .comment-search-left{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
    }
    // 右侧区域
    .comment-search-right{
      display: flex;
      flex-direction: row;
      justify-content: flex-end;
      align-items: center;
      .el-select{
        margin-right: 1rem;
      }
      .el-input{
        width: 25%;
      }
      .el-button{
        margin-left: 15px;
      }
    }
  }
  // 评论列表
  .commentList{
    margin-top: 1rem;
    // 评论用户头像
    .avatar{
      img{
        vertical-align:text-top;  // 图片垂直对齐
        width: 80%;
        height: auto;
        border-radius: 4px;
      }
    }
  }
}

.comment-content{
  display: inline-block;
}

</style>
