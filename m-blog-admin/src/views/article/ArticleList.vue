<template>
  <div class="articles-container">
    <el-card class="main-card">
      <div class="title">文章管理</div>
      <div class="operation-container">
        <!--状态选择-->
        <div class="article-status-menu">
          <span :class="isActiveStatus('all')" @click="changeStatus('all')">全部</span>
          <span :class="isActiveStatus('public')" @click="changeStatus('public')">公开</span>
          <span :class="isActiveStatus('secret')" @click="changeStatus('secret')">私密</span>
          <span :class="isActiveStatus('draft')" @click="changeStatus('draft')">草稿箱</span>
          <span :class="isActiveStatus('recycle')" @click="changeStatus('recycle')">回收站</span>
        </div>
      </div>
      <!--文章修改、搜索区域-->
      <el-row class="articles-search" style="width: 100%;">
        <el-col :span="6">
          <div class="articles-search-left">
            <el-button @click="deleteArticle" type="danger" size="small" plain :disabled="articleIdList.length===0">批量删除</el-button>
          </div>
        </el-col>
        <el-col :span="18">
          <div class="articles-search-right">
            <!--文章类别-->
            <el-select v-model="articleQueryInfo.type"
                       size="small"
                       clearable
                       @change="getArticleList"
                       @clear="getArticleList"
                       placeholder="请选择文章类型">
              <el-option
                v-for="item in articleTypeList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-select>
            <!--文章分类-->
            <el-select v-model="articleQueryInfo.categoryId"
                       size="small"
                       clearable
                       @change="getArticleList"
                       @clear="getArticleList"
                       placeholder="请选择文章分类">
              <el-option
                v-for="item in categoryList"
                :key="item.id"
                :label="item.categoryName"
                :value="item.id">
              </el-option>
            </el-select>
            <!--文章标签-->
            <el-select v-model="articleQueryInfo.tagId"
                       size="small"
                       clearable
                       @change="getArticleList"
                       @clear="getArticleList"
                       placeholder="请选择文章标签">
              <el-option
                v-for="item in tagList"
                :key="item.id"
                :label="item.tagName"
                :value="item.id">
              </el-option>
            </el-select>
            <el-input placeholder="搜索内容"
                      size="small"
                      @keyup.enter="getArticleList"
                      v-model="articleQueryInfo.keywords"></el-input>
            <el-button type="primary" size="small" @click="getArticleList" plain>搜索</el-button>
          </div>
        </el-col>
      </el-row>
      <!--文章列表-->
      <div class="articleList">
        <el-table
          :data="articleList"
          @selection-change="artHandleSelectionChange"
          border
          style="width: 100%">
          <!--多选框-->
          <el-table-column
            type="selection"
            width="45">
          </el-table-column>
          <!--文章封面-->
          <el-table-column
            label="封面"
            width="160">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <div class="articleCover">
                <img :src="scope.row.cover" alt="博客封面"/>
              </div>
            </template>
          </el-table-column>
          <!--文章标题-->
          <el-table-column
            prop="title"
            label="标题">
          </el-table-column>
          <!--文章分类-->
          <el-table-column
            prop="categoryName"
            label="分类">
          </el-table-column>
          <!--文章标签-->
          <el-table-column
            label="标签"
            width="170">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <el-tag v-for="tag in scope.row.tagNameList" style="margin: 0.2rem 0.2rem">{{tag}}</el-tag>
            </template>
          </el-table-column>
          <!--浏览量-->
          <el-table-column
            prop="pageViews"
            label="浏览量">
          </el-table-column>
          <!--点赞量-->
          <el-table-column
            prop="pageLikes"
            label="点赞量">
          </el-table-column>
          <!--文章类型-->
          <el-table-column
            prop="type"
            label="类型">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <el-tag>{{scope.row.type===1?'原创':'转载'}}</el-tag>
            </template>
          </el-table-column>
          <!--置顶-->
          <el-table-column
            prop="isTop"
            label="置顶">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <el-switch
                v-model="scope.row.isTop"
                @change="changeIsTop(scope.row.id,scope.row.isTop)"
                :active-value = 1
                :inactive-value = 0>
              </el-switch>
            </template>
          </el-table-column>
          <!--操作-->
          <el-table-column
            prop="address"
            label="操作"
            width="160">
            <template slot-scope="scope">  <!--scope.row就相当于这一行的所有数据-->
              <el-button type="primary" @click="editArticle(scope.row.id)" size="small" plain>编辑</el-button>
              <el-button type="danger" size="small" plain @click="deleteArticle($event,scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
      <!--分页组件-->
      <el-pagination
        class="pagination-container"
        @size-change="pageSizeChange"
        @current-change="pageCurrentChange"
        :current-page="articleQueryInfo.pageNum"
        :page-sizes="[2,5,10]"
        :page-size="articleQueryInfo.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="articleCount">
      </el-pagination>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "ArticleList",
  data(){
    return{
      nowStatus: 'all',   // 当前选择的状态
      articleTypeList:[ // 文章类型列表
        { value: 1, label: '原创' },
        { value: 2, label: '转载' }
      ],
      categoryList:[],
      tagList:[],
      articleList:[],   // 查询到的文章列表
      articleCount: 0,  // 文章数量
      articleIdList: [],   // 选择的文章id列表
      articleQueryInfo:{
        type: null,     // 文章类型，1公开2转载
        status: null,
        categoryId: null,
        tagId: null,        // 标签id，这里只支持查询单个标签
        pageNum: 1,         // 当前的页数
        pageSize: 5,        // 一页显示的大小
        keywords: '',        // 查询条件
        isDelete: 0         // 是否删除
      }

    }
  },
  methods:{
    // 修改查询文章状态
    changeStatus(status){
      this.nowStatus = status;
      switch (status){
        case 'all':
          this.articleQueryInfo.status = null;
          this.articleQueryInfo.isDelete = 0;
          break;
        case 'public':
          this.articleQueryInfo.status = 1;
          this.articleQueryInfo.isDelete = 0;
          break;
        case 'secret':
          this.articleQueryInfo.status = 2;
          this.articleQueryInfo.isDelete = 0;
          break;
        case 'draft':
          this.articleQueryInfo.status = 0;
          this.articleQueryInfo.isDelete = 0;
          break;
        case 'recycle':
          this.articleQueryInfo.status = null;
          this.articleQueryInfo.isDelete = 1;
          break;
      }
    },
    // 查询所有的分类数据
    searchCategory(){
      this.$store.dispatch('category/selectAllCate')
        .then(res => {
          const {data} = res;
          /**
           * data{
           *   categoryName,
           *   id
           * }
           */
          this.categoryList = data;
        })
    },
    // 查询所有标签数据
    searchTag(){
      this.$store.dispatch("tag/selectAllTag")
        .then(res => {
          const { data } = res;
          /**
           * data{
           *   id,
           *   tagName
           * }
           */
          this.tagList = data;
        })
    },
    // 分页获取文章列表
    getArticleList(){
      this.$store.dispatch('article/getArticleList',this.articleQueryInfo)
        .then(res => {
          console.log('查询文章列表成功,res:');
          console.log(res);
          const { data } = res;
          this.articleCount = data.count;
          this.articleList = data.recordList;
        }).catch(()=>{
        console.log('查询文章列表失败');
      })
    },
    // 修改一页查询的大小
    pageSizeChange(newSize){
      this.articleQueryInfo.pageSize = newSize;
      // 重新获取数据
      this.getArticleList();
    },
    // 修改从第几页查询
    pageCurrentChange(newPage) {
      this.articleQueryInfo.pageNum = newPage;
      // 重新获取数据
      this.getArticleList();
    },
    // 选择表格前面的多选框
    artHandleSelectionChange(articleList){
      // articleList为当前选择的表格的对象
      console.log(articleList);
      // 重置选择的文章id列表
      this.articleIdList = [];
      articleList.map(item => {
        this.articleIdList.push(item.id);
      })
      // console.log(this.articleIdList);
    },
    // 删除文章
    deleteArticle(event,articleId){
      this.$confirm('确定删除文章吗', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        console.log('articleId=>');
        console.log(articleId);
        let articleIdList = [];
        if (articleId){
          articleIdList = [articleId];
        }else {
          // 遍历选择的所有文章对象，将选择了的文章id添加进来
          articleIdList = this.articleIdList;
        }
        // 批量删除文章
        this.$store.dispatch('article/deleteArticle',articleIdList)
          .then(res => {
            console.log('删除文章成功,res:');
            console.log(res);
            this.getArticleList();
          }).catch(err => {
          console.log('删除文章失败,err:');
          console.log(err);
        })
      })
    },
    // 修改文章是否置顶
    changeIsTop(articleId,isTop){
      let data = {
        articleId: articleId,
        isTop: isTop
      }
      this.$store.dispatch('article/changeArticleTop',data)
        .then(res => {
          console.log("修改文章置顶情况成功");
        }).catch(err => {
        // 将状态变回原来的样子
        isTop = isTop === 1 ? 0 : 1;
        console.log("修改文章置顶失败");
        console.log('将isTop修改回来-->'+isTop);
        console.log(err);
      })
    },
    // 编辑文章
    editArticle(articleId){
      this.$router.push({
        path: '/article/addArticle',
        query:{
          id: articleId
        }
      })
    }
  },
  computed:{
    isActiveStatus(){
      // 闭包函数，通过闭包对计算属性进行传参
      return function (status){
        return this.nowStatus === status?'nowStatus':'otherStatus';
      }
    }
  },
  watch:{
    // 深度监听属性中的值
    'articleQueryInfo.status': {
      handler(){
        this.articleQueryInfo.pageNum = 1;
        this.getArticleList();
      }
    },
    'articleQueryInfo.isDelete':{
      handler(){
        this.articleQueryInfo.pageNum = 1;
        this.getArticleList();
      }
    }
  },
  created() {
    this.getArticleList();  // 获取文章列表
    this.searchCategory();  // 获取文章分类
    this.searchTag()        // 获取文章标签
  }
}
</script>

<style scoped lang="scss">

.articles-container{
  padding: 20px;

  .article-status-menu{
    font-size: 14px;
    color: #999;
    span{
      margin-right: 24px;
    }
  }

  // 当前选择的状态样式
  .nowStatus{
    color: #333;
    font-weight: bold;
    cursor: pointer;
  }
  // 其他样式
  .otherStatus{
    cursor: pointer;  // 鼠标移动上去改变样式
  }

  // 删除、修改样式区域
  .articles-search{
    // 左侧区域
    .articles-search-left{
      display: flex;
      flex-direction: row;
      justify-content: flex-start;
      align-items: center;
    }
    // 右侧区域
    .articles-search-right{
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

  // 文章列表
  .articleList{
    margin-top: 1rem;
    // 博客封面
    .articleCover{
      img{
        vertical-align:text-top;  // 图片垂直对齐
        width: 100%;
        height: auto;
        border-radius: 4px;
      }
    }
  }
  // 分页组件



}

</style>
