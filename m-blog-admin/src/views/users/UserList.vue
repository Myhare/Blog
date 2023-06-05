<template>

  <div class="userList-container">
    <el-card class="main-card">
      <div class="title">用户列表</div>
      <div class="operation-container">
        <div class="selectBox">
          <el-input
            v-model="queryInfo.keywords"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入昵称"
            @keyup.enter="getUserList"
            style="width:200px"
          />
          <el-button
            type="primary"
            @click="getUserList()"
            size="small"
            icon="el-icon-search"
            style="margin-left:1rem"
          >
            搜索
          </el-button>
        </div>
      </div>
      <!--用户信息表单-->
      <el-table
        :data="userList"
        stripe
        style="width: 100%">
        <el-table-column label="头像">
          <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
            <el-image
              style="width: 50px;
              height: 50px"
              :src="scope.row.avatar">
            </el-image>
          </template>
        </el-table-column>
        <el-table-column prop="nickname" label="昵称"></el-table-column>
        <el-table-column label="角色">
          <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
            <el-tag v-for="tag in scope.row.roles" style="margin: 0.2rem 0.2rem">{{tag}}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="blogCount" label="发布博客数量"></el-table-column>
        <el-table-column prop="createTime" label="账号创建时间" :formatter="dateFormat"></el-table-column>
        <el-table-column prop="lastLoginTime" label="最后登录时间" :formatter="dateFormat"></el-table-column>
        <el-table-column label="禁用">
          <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
            &ensp;&ensp;
            <el-switch v-model="scope.row.isDelete"
                       @change="userStateChanged(scope.row.userId,scope.row.isDelete)"
                       :active-value = 1
                       :inactive-value = 0
            >
            </el-switch>  <!--可以通过slot-scope中的属性中的row获取这一行的数据-->
          </template>
        </el-table-column>
      </el-table>
      <!--分页组件-->
      <div class="pagingBox">
        <el-pagination
          @size-change="pageSizeChange"
          @current-change="pageCurrentChange"
          :current-page="queryInfo.pageNum"
          :page-sizes="[5,10,20]"
          :page-size="queryInfo.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="userCount">
        </el-pagination>
      </div>
    </el-card>
  </div>


</template>

<script>
import moment from "moment";

export default {
  name: "UserList",
  data(){
    return{
      userCount: 0,   // 一共有多少用户
      userList: [],  // 用户列表数据
      queryInfo: {
        pageNum: 1,         // 当前的页数
        pageSize: 5,        // 一页显示的大小
        keywords: ''        // 查询条件
      },
    }
  },
  methods: {
    // 分页获取用户对象
    getUserList(){
      this.$store.dispatch("user/getUserList",this.queryInfo)
        .then((data)=>{
          console.log("获取用户成功回调函数");
          console.log(data);
          const { count , recordList} = data;
          this.userCount = count;
          this.userList = recordList
        }).catch((err)=>{
        console.log("获取用户信息失败回调函数");
        console.log(err);
      })
    },
    // 修改选中的用户的状态
    userStateChanged(userId,isDelete){
      const param = {
        userId,
        isDelete
      }
      this.$store.dispatch("user/updateUserDelete",param)
        .then((data)=>{
          console.log('界面修改成功data:');
          console.log(data);
        }).catch((err)=>{
        isDelete = isDelete === 1 ? 0:1;
        console.log(isDelete);
        console.log(this.userList);
        console.log("修改用户信息失败调用函数");
        console.log(err);
      })
    },
    // 监听pageSize大小改变
    pageSizeChange (newSize) {
      // console.log(newSize)
      this.queryInfo.pageSize = newSize
      this.getUserList() // 重新获取数据
    },
    // 监听分页页码值改变事件
    pageCurrentChange (newPage) {
      // console.log(newPage)
      this.queryInfo.pageNum = newPage
      this.getUserList()
    },
    // 时间格式化
    dateFormat: function (row, column) {
      var date = row[column.property];
      if (date === undefined) {
        return "";
      }
      //修改时间格式 我要修改的是"YYYY-MM-DD"
      return moment(date).format("YYYY-MM-DD HH:mm");
    }
  },
  created() {
    // 分页获取用户信息
    this.getUserList();
  }
}
</script>

<style scoped lang="scss">

.userList-container{
  padding: 20px;
  // 搜索数据框
  .selectBox{
    margin-left: auto;
  }
  // 分页框
  .pagingBox{
    margin: 12px;
    display: flex;
    flex-direction: row;
    justify-content: right;
    align-items: center;
  }

}

</style>
