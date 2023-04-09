<template>

  <div class="category-container">
    <el-card class="main-card">
      <div class="title">分类管理</div>
      <div class="operation-container">
        <!--分类修改区域-->
        <el-row class="category-change" style="width: 100%;">
          <el-col :span="12">
            <div class="category-change-left">
              <el-button type="primary" size="small" plain @click="addCategoryShow=true">+ 新增</el-button>
              <el-button type="danger" size="small" plain @click="batchDelete(null)">批量删除</el-button>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="category-change-right">
              <el-input placeholder="搜索内容"></el-input>
              <el-button type="primary" size="small" plain>搜索</el-button>
            </div>
          </el-col>
        </el-row>
      </div>
      <!--分类列表-->
      <div>
        <el-table
          :data="categoryList"
          border
          @selection-change="handleSelectionChange"
          style="width: 100%">
          <el-table-column
            type="selection"
            width="55">
          </el-table-column>
          <el-table-column
            prop="categoryName"
            label="分类名称"
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
              <el-button type="danger" size="small" plain @click="batchDelete(scope.row.categoryId)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!-- 分页 -->
      <el-pagination
        class="pagination-container"
        background
        @size-change="sizeChange"
        @current-change="currentChange"
        :current-page="queryInfo.pageNum"
        :page-size="queryInfo.pageSize"
        :total="categoryCount"
        :page-sizes="[10, 20]"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </el-card>

    <!--新增分类对话框-->
    <el-dialog
      title="提示"
      :visible.sync="addCategoryShow"
      width="30%"
      :before-close="addDialogClose">
      <el-input placeholder="分类名称" v-model="addValue"/>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogClose">取 消</el-button>
    <el-button type="primary" @click="addCategory(addValue)">确 定</el-button>
  </span>
    </el-dialog>

    <!--编辑分类对话框-->
    <el-dialog
      title="提示"
      :visible.sync="addCategoryShow"
      width="30%"
      :before-close="addDialogClose">
      <el-input placeholder="分类名称" v-model="addValue"/>
      <span slot="footer" class="dialog-footer">
    <el-button @click="addDialogClose">取 消</el-button>
    <el-button type="primary" @click="addCategory(addValue)">确 定</el-button>
  </span>
    </el-dialog>

  </div>

</template>

<script>
import moment from "moment";
export default {
  name: "CateGory",
  data(){
    return{
      categoryCount: 0,   // 一共有多少分类数量
      categoryList: [],   // 分类列表
      categoryIdList:[],  // 分类id列表
      queryInfo: {
        pageNum: 1,         // 当前的页数
        pageSize: 10,        // 一页显示的大小
        keywords: ''        // 查询条件
      },
      addCategoryShow: false,
      addValue: '',   // 添加分类输入框
      updateCateShow: false,  // 标记分类
      updateValue:''
    }
  },
  methods:{
    // 管理 添加分类对话框之前执行
    addDialogClose(){
      this.addCategoryShow = false;
      setTimeout(()=>{
        this.addValue = '';
      },500)
    },
    // 分页查询所有分类
    getCateList(){
      this.$store.dispatch("category/selectCateList",this.queryInfo)
        .then(res => {
          console.log("查询分类成功回调函数,res:");
          console.log(res);
          const {data} = res;
          this.categoryList = data.recordList;
          this.categoryCount = data.count;
          console.log();
        })
        .catch(err =>{
          console.log("查询分类失败");
          console.log(err);
        })
    },
    sizeChange(size) {
      this.queryInfo.pageSize = size;
      this.getCateList();
    },
    currentChange(current) {
      this.queryInfo.pageNum = current;
      this.getCateList();
    },
    // 向后端发送请求添加分类
    addCategory(cateName){
      if (cateName===null || cateName === ''){
        return this.$message.warning("分类名不能为空")
      }
      // 向后端发送请求
      this.$store.dispatch("category/addCategory",cateName)
        .then(res => {
          console.log("添加分类成功回调函数,res:");
          console.log(res);
          // 重新获取分类数据
          this.getCateList();
          this.addValue = '';
          this.$message.success(res.message);
        })
        .catch(err =>{
          console.log("添加分类失败");
          console.log(err);
        })
      this.addCategoryShow = false
    },

    // 修改表格前面多选框时调用的函数，参数为当前表格的数据
    handleSelectionChange(categoryList){
      console.log(categoryList);
      // 重置分类id列表
      this.categoryIdList = [];
      categoryList.forEach(item => {
        this.categoryIdList.push(item.categoryId);
      })
      console.log(this.categoryIdList);
    },
    // 删除分类
    batchDelete(id){
      let params = [];
      if (id == null){
        params = this.categoryIdList;
      }else {
        params = [id];
      }
      // console.log(this.categoryIdList);
      console.log(params);
      this.$confirm('确定删除这些分类吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        // 执行批量删除分类操作
        this.$store.dispatch("category/deleteCate",params)
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

.category-container{
  padding: 20px;
  .category-change{
    .category-change-left{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
    }
    .category-change-right{
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
