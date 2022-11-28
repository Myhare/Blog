<template>

  <div class="tag-container">
    <el-card class="main-card">
      <div class="title">标签管理</div>
      <div class="operation-container">
        <!--标签修改区域-->
        <el-row class="tag-change" style="width: 100%;">
          <el-col :span="12">
            <div class="tag-change-left">
              <el-button type="primary" size="small" plain @click="addTagShow=true">+ 新增</el-button>
              <el-button type="danger" size="small" plain @click="batchDelete(null)">批量删除</el-button>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="tag-change-right">
              <el-input placeholder="搜索内容"></el-input>
              <el-button type="primary" size="small" plain>搜索</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--标签列表-->
      <div>
        <el-table
          :data="tagList"
          border
          @selection-change="handleSelectionChange"
          style="width: 100%">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            prop="tagName"
            label="标签名称"
            width="180">
          </el-table-column>
          <el-table-column
            prop="articleCount"
            label="文章数量"
            width="180">
          </el-table-column>
          <el-table-column
            prop="updateTime"
            label="更新时间"
            :formatter="dateFormat">
          </el-table-column>
          <el-table-column
            prop="operation"
            label="操作"
            width="160">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <el-button type="primary" size="small" plain>编辑</el-button>
              <el-button type="danger" size="small" plain @click="batchDelete(scope.row.tagId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>

    <!--新增标签对话框-->
    <el-dialog
      title="提示"
      :visible.sync="addTagShow"
      width="30%"
      :before-close="addDialogClose">
      <el-input placeholder="标签名称" v-model="addValue"/>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogClose">取 消</el-button>
    <el-button type="primary" @click="addTag(addValue)">确 定</el-button>
  </span>
    </el-dialog>

    <!--编辑标签对话框-->
    <el-dialog
      title="提示"
      :visible.sync="addTagShow"
      width="30%"
      :before-close="addDialogClose">
      <el-input placeholder="标签名称" v-model="addValue"/>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogClose">取 消</el-button>
    <el-button type="primary" @click="addTag(addValue)">确 定</el-button>
  </span>
    </el-dialog>

  </div>

</template>

<script>
import moment from "moment";

export default {
  name: "Tag",
  data(){
    return{
      tagCount: 0,   // 一共有多少标签数量
      tagList: [],   // 标签列表
      tagIdList:[],  // 标签id列表
      queryInfo: {
        pageNum: 1,         // 当前的页数
        pageSize: 5,        // 一页显示的大小
        keywords: ''        // 查询条件
      },
      addTagShow: false,
      addValue: '',   // 添加标签输入框
      updateCateShow: false,  // 标记标签
      updateValue:''
    }
  },
  methods:{
    // 管理 添加标签对话框之前执行
    addDialogClose(){
      this.addTagShow = false;
      setTimeout(()=>{
        this.addValue = '';
      },500)
    },
    // 分页查询标签
    getCateList(){
      this.$store.dispatch("tag/getTagList",this.queryInfo)
        .then(res => {
          console.log("查询标签成功回调函数,res:");
          console.log(res);
          const {data} = res;
          this.tagList = data.recordList;
          this.tagCount = data.count;
          console.log();
        })
        .catch(err =>{
          console.log("查询标签失败");
          console.log(err);
        })
    },
    // 向后端发送请求添加标签
    addTag(cateName){
      if (cateName===null || cateName === ''){
        return this.$message.warning("标签名不能为空")
      }
      // 向后端发送请求
      this.$store.dispatch("tag/addTag",cateName)
        .then(res => {
          console.log("添加标签成功回调函数,res:");
          console.log(res);
          // 重新获取标签数据
          this.getCateList();
          this.addValue = '';
          this.$message.success(res.message);
        })
        .catch(err =>{
          console.log("添加标签失败");
          console.log(err);
        })
      this.addTagShow = false
    },

    // 修改表格前面多选框时调用的函数，参数为当前表格的数据
    handleSelectionChange(tagList){
      // console.log(tagList);
      // 重置标签id列表
      this.tagIdList = [];
      tagList.forEach(item => {
        this.tagIdList.push(item.tagId);
      })
      console.log(this.tagIdList);
    },
    // 删除标签
    batchDelete(id){
      let params = [];
      if (id == null){
        params = this.tagIdList;
      }else {
        params = [id];
      }
      console.log(this.tagIdList);
      console.log(params);
      this.$confirm('确定删除这些标签吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 执行批量删除标签操作
        this.$store.dispatch("tag/delTag",params)
          .then(res => {
            console.log(res);
            this.$message.success(res.message)
            this.getCateList();
          }).catch(err => {
          this.$message.error(err);
          this.getCateList();
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
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
    this.getCateList();
  }
}
</script>

<style scoped lang="scss">

.tag-container{
  padding: 20px;
  .tag-change{
    .tag-change-left{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
    }
    .tag-change-right{
      display: flex;
      flex-direction: row;
      justify-content: flex-end;
      align-items: center;
      .el-input{
        width: 45%;
      }
      .el-button{
        margin-left: 20px;
      }
    }
  }
}

</style>
