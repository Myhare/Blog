<template>

  <div class="article-container">
    <el-card class="main-card" style="position: relative">
      <div class="title">发布文章</div>
      <div class="operation-container">
        <el-input v-model="article.articleTitle"></el-input>
        <el-button style="margin-left: 30px" @click="saveArtDraft">添加草稿</el-button>
        <el-button type="primary" style="margin-left: 30px" @click="showAddArtDialog">发布博客</el-button>
      </div>
      <!--文章编辑位置-->
      <div>
        <mavon-editor
          ref="editorRef"
          v-model="article.articleContent"
          @imgAdd="imageAdd"
          style="height:calc(100vh - 260px)"
        />
      </div>
    </el-card>

    <!--发布博客对话框-->
    <el-dialog title="发布博客" width="40%" top="3vh" :visible.sync="addArticleShow">
      <el-form :model="article">
        <!--文章分类-->
        <el-form-item label="文章分类">
          <el-tag
            type="success"
            v-show="article.categoryName"
            :closable="true"
            style="margin:0 1rem 0 0"
            @close="removeCateName"
          >
            {{article.categoryName}}
          </el-tag>
          <el-popover
            placement="top-start"
            trigger="click"
            width="460"
            v-if="!article.categoryName">
            <div class="popover-title">分类</div>
            <!--带推荐的搜索框-->
            <el-autocomplete
              style="width:100%"
              class="inline-input"
              v-model="selectCateName"
              :fetch-suggestions="categorySearch"
              :trigger-on-focus="false"
              placeholder="搜索分类，按回车可添加分类"
              @keyup.enter.native="addCategorySearch(selectCateName)"
              @select="selectCategorySearch"
            >
              <!--通过插槽将返回data中的categoryName渲染上去-->
              <template slot-scope="{ item }">
                <div>{{ item.categoryName }}</div>
              </template>
            </el-autocomplete>
            <!--分类列表-->
            <div class="searchCateList">
              <div
                v-for="item in categoryNameList"
                :key="item.id"
                class="category-name"
                @click="selectCategorySearch(item)"
              >
                {{item.categoryName}}
              </div>
            </div>
            <el-button type="success" plain slot="reference" size="small">添加分类</el-button>
          </el-popover>
        </el-form-item>
        <!--文章标签-->
        <el-form-item label="文章标签">
          <el-tag
            type="success"
            :closable="true"
            v-for="(item,index) in article.tagList"
            :key="index"
            style="margin:0 1rem 0 0"
            @close="removeTag(item)"
          >
            {{item}}
          </el-tag>
          <el-popover
            placement="top-start"
            width="400"
            trigger="click"
            content="添加标签页面">
            <div>
              <div class="popover-title">添加标签</div>
              <div>
                <el-autocomplete
                  style="width:100%;margin-bottom: 1rem"
                  class="inline-input"
                  v-model="selectTagName"
                  :fetch-suggestions="tagSearch"
                  :trigger-on-focus="false"
                  placeholder="搜索标签，按回车可添加标签"
                  @keyup.enter.native="addTagSearch(selectTagName)"
                  @select="selectTagSearch"
                >
                  <!--通过插槽将返回data中的categoryName渲染上去-->
                  <template slot-scope="{ item }">
                    <div>{{ item }}</div>
                  </template>
                </el-autocomplete>
                <el-tag
                  v-for="(tag,index) in tagList"
                  :key="index"
                  @click="addTagSearch(tag)"
                  class="popover-tag-list"
                >
                  {{tag}}
                </el-tag>
              </div>
            </div>
            <el-button type="success" v-show="article.tagList.length<3" slot="reference" size="small" plain>添加标签</el-button>
          </el-popover>
        </el-form-item>
        <!--文章类型-->
        <el-form-item label="文章类型">
          <el-select v-model="article.articleType" placeholder="请选择">
            <el-option
              v-for="(item,index) in articleTypeList"
              :key="index"
              :label="item.desc"
              :value="item.type">
            </el-option>
          </el-select>
        </el-form-item>
        <!--上传封面-->
        <el-form-item label="文章封面">
          <el-upload
            drag
            :multiple="false"
            action="/api/articleFile"
            :before-upload="beforeUploadCover"
            :on-success="uploadCoverSuccess"
            :on-remove="removeCover"
            :on-exceed="exceedCover"
            :limit="1"
            multiple>
            <i class="el-icon-upload" v-if="article.coverUrl === this.$global.DEFAULT_COVER_URL"></i>
            <div class="el-upload__text" v-if="article.coverUrl === this.$global.DEFAULT_COVER_URL">
              将文件拖到此处，或<em>点击上传</em>
              <br>
              <span style="font-size: small; position: relative; top: -10px">不上传将使用默认封面</span>
            </div>
            <img v-else :src="article.coverUrl" style="width: 100%">
          </el-upload>
        </el-form-item>
        <!--是否置顶-->
        <el-form-item label="置顶">
          <el-switch
            v-model="article.isTop"
            :active-value="1"
            :inactive-value="0"
            active-color="#13ce66">
          </el-switch>
        </el-form-item>
        <!--发布方式-->
        <el-form-item label="发布方式">
          <el-radio-group v-model="article.status">
            <el-radio :label="1">公开</el-radio>
            <el-radio :label="2">私密</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="addArticleShow = false">取 消</el-button>
        <el-button type="primary" @click="addArticle">确 定</el-button>
      </div>
    </el-dialog>

  </div>

</template>

<script>
// 导入图片压缩工具
import * as imageConversion from 'image-conversion'

export default {
  name: "Article",
  data(){
    return{
      addArticleShow:false,
      selectCateName:'',  // 查询博客分类val
      selectTagName:'',    // 查询标签val
      categoryNameList:[],    // 所有分类列表
      tagList:[],         // 所有标签列表
      articleTypeList:[       // 标签属性
        {
          type: 1,
          desc: "原创"
        },
        {
          type: 2,
          desc: "转载"
        },
        {
          type: 3,
          desc: "翻译"
        }
      ],
      article: {
        id: null,
        articleTitle:'博客标题(' + this.$moment(new Date()).format("YYYY-MM-DD") + ')',
        articleContent:'',  // 博客内容
        categoryName: '',    // 博客分类
        tagList: [],  // 博客标签列表
        articleType: 1,   // 文章类型
        coverUrl: this.$global.DEFAULT_COVER_URL, // 文章封面url
        isTop: 0,         // 是否置顶
        status: 1         // 文章状态，1公开，2私密
      },
    }
  },
  methods:{
    /**
     * 文件自动上传后端
     * @param pos 写在md中的文件名
     * @param file     文件资源
     */
    imageAdd(pos,file){
      console.log("上传的文件信息file:");
      console.log(file);
      // 获取文件允许的最大的大小
      let fileMaxSize = this.$global.UPLOAD_FILE_MAX_SIZE;
      // 上传文件的实际大小
      let updateFileSize = file.size/1024/1024;

      // 通过表单的形式想后端传递图片信息
      let imgFormData = new FormData();

      // 如果上传的图片大小大于2MB，将其压缩
      if (updateFileSize < fileMaxSize){
        // 向后端上传图片
        imgFormData.append('file',file);
        console.log("现在向后端传递图片数据:");
        // 向后端发送数据
        this.$store.dispatch("article/uploadArticleFile",imgFormData)
          .then(res => {
            console.log("上传博客图片回调函数，data：");
            console.log(res);
            const {data} = res;
            // 将文章中搞得路径换成返回的路径
            this.$refs.editorRef.$img2Url(pos,data);
          }).catch(err => {
          console.log("自动存储博客文章图片失败，err：");
          console.log(err);
        })

      }else {
        console.log("文件大于2mb，将进行压缩");
        return new Promise(resolve => {
          imageConversion.compressAccurately(file,this.$global.UPLOAD_FILE_SIZE)
            .then(res => {
              console.log("压缩后的res");
              console.log(res);
              // 这里的res是图片的二进制数据，我们需要将其穿换成File文件传递给后端
              imgFormData.append(
                'file',
                new window.File([res],file.name,{type: file.fileType})
              );

              console.log("现在向后端传递图片数据:");
              // 向后端发送数据
              this.$store.dispatch("article/uploadArticleFile",imgFormData)
                .then(res => {
                  console.log("上传博客图片回调函数，res：");
                  console.log(res);
                  const {data} = res;
                  // 将文章中搞得路径换成返回的路径
                  this.$refs.editorRef.$img2Url(pos,data);
                }).catch(err => {
                console.log("自动存储博客文章图片失败，err：");
                console.log(err);
              })
            })
        })
      }
    },
    // 文章上传封面前
    beforeUploadCover(file){
      console.log('上传封面文件:');
      console.log(file);
      return new Promise((resolve, reject) => {
        // 获取文件允许的最大的大小
        let fileMaxSize = this.$global.UPLOAD_FILE_MAX_SIZE;
        // 获取上传文件实际的大小
        let updateFileSize = file.size/1024/1024;
        if (updateFileSize < fileMaxSize){
          resolve();
        }else {
          console.log('文章封面图片太大，压缩封面:');
          // 压缩文件
          imageConversion.compressAccurately(file,this.$global.UPLOAD_FILE_SIZE)
          .then(res => {
            console.log('压缩封面后返回结果');
            console.log(res);
            resolve(res);
          }).catch(err => {
            console.log('压缩文件发生错误');
            reject();
          })
        }
      })
    },
    /**
     * 文章上传封面成功
     * @param response 接口返回数据
     * @param file     上传的文件，里面包含了文件的基本信息，和response
     * @param fileList  上传的文件列表，如果上传了多个文件里面有文件
     */
    uploadCoverSuccess(response, file, fileList){
      console.log('上传封面成功,fileList');
      console.log(fileList);
      const { data } = response;    // data为返回的url
      this.article.coverUrl = data;
    },
    // 删除上传的文章封面
    removeCover(file, fileList){
      // console.log("删除上传文件,fileList:");
      // console.log(fileList);
      this.article.coverUrl = this.$global.DEFAULT_COVER_URL; // 重置为默认封面
    },
    // 超出文件数量限制钩子
    exceedCover(files, fileList){
      this.$message.warning("最多只能上传一个封面哦")
      // console.log('超出文件限制钩子');
      // console.log(files);
      // console.log(fileList);
    },
    // 打开添加博客对话框
    showAddArtDialog(){
      if (this.article.articleContent.trim()===''){
        return this.$message.error("文章内容不能为空")
      }
      if (this.article.articleTitle.trim() === ''){
        return this.$message.error("文章标题不能为空")
      }
      this.searchCateName();
      this.searchAllTag();
      this.addArticleShow = true;
    },
    // 查找所有分类
    searchCateName(){
      this.$store.dispatch("category/selectAllCate")
        .then(res => {
          this.categoryNameList = res.data;
          // console.log('查询所有分类成功,this.categoryNameList:')
          // console.log(this.categoryNameList);
        })
    },
    // 搜索提示分类
    categorySearch(querySearch,cb){
      this.$store.dispatch("category/selectCateSearch",this.selectCateName)
        .then(res => {
          // console.log(res);
          // console.log(res.data);
          cb(res.data);   // 将查询结果返回给前端element
        })
    },
    // 搜索提示标签
    tagSearch(querySearch,cb){
      console.log(this.selectTagName);
      this.$store.dispatch('tag/searchTag',this.selectTagName)
      .then(res => {
        // console.log(res);
        // console.log(res.data);
        cb(res.data);
      })
    },
    // 选择分类名称
    selectCategorySearch(item){
      this.article.categoryName = item.categoryName;
      console.log(this.article);
    },
    // 搜素框选择标签名称
    selectTagSearch(tagName){
      this.addTagSearch(tagName);
    },
    // 删除选择的搜索分类选择
    removeCateName(){
      this.article.categoryName = null;
    },
    // 删除选择的搜索标签
    removeTag(tag){
      let index = this.article.tagList.indexOf(tag);
      this.article.tagList.splice(index,1);  // 删除数组中指定的元素
    },
    // 添加分类
    addCategorySearch(cateName){
      // 这里只在前端进行存储分类，上传博客的时候一起上传到数据库
      if (cateName.trim() !== ''){
        this.article.categoryName = cateName;
      }
      this.selectCateName = '';
    },
    // 添加标签
    addTagSearch(tagName){
      console.log(this.article.tagList);
      console.log(tagName);
      if (this.article.tagList.indexOf(tagName) !== -1){
        this.$message.warning("不能添加重复标签");
      } else if (this.article.tagList.length >= 3){
        this.$message.warning("最多添加三个标签");
      }else {
        this.article.tagList.push(tagName);
      }
      this.selectTagName = '';
    },
    // 查找所有标签
    searchAllTag(){
      this.$store.dispatch('tag/selectAllTag')
        .then(res=> {
          this.tagList = res.data.map(item => item.tagName);
          // console.log('查询所有标签成功,this.tagList:');
          // console.log(this.tagList);
        })
    },
    // 添加博客
    addArticle(){
      const article = this.article;
      console.log(article);
      if (article.articleTitle === ''){
        return this.$message.error('文章标题不能为空')
      }
      if (article.articleContent === ''){
        return this.$message.error('文章内容不能为空')
      }
      if (article.categoryName === ''){
        return this.$message.error('请选择至少一个文章分类')
      }
      // 上传博客
      this.$store.dispatch('article/addArticle',article)
      .then(res => {
        console.log('上传文章成功，res：');
        console.log(res);
        this.$notify.success('上传文章成功')
        this.$router.push('/article/articles')
      }).catch(err => {
        console.log('上传文章失败');
      })
      this.addArticleShow = false
    },
    // 添加草稿
    saveArtDraft(){
      const article = this.article;
      article.status = 0;    // 将状态设置成草稿
      // console.log(article);
      if (article.articleTitle === ''){
        return this.$message.error('文章标题不能为空')
      }
      if (article.articleContent === ''){
        return this.$message.error('文章内容不能为空')
      }
      // 上传博客
      this.$store.dispatch('article/addArticle',article)
        .then(res => {
          console.log('添加草稿成功，res：');
          console.log(res);
          this.$notify.success('保存草稿成功')
          // 重新跳转到当前页面
          this.$router.push('/article/articles')
        }).catch(err => {
        console.log('保存草稿失败');
      })
    },
    // 通过id查询文章
    getArticleById(id){
      this.$store.dispatch('article/getArticleById',id)
      .then(res => {
        // console.log("查询文章成功,res=>");
        // console.log(res);
        this.article = res.data;
        // console.log(this.article);
      }).catch(err => {
        console.log('查询文章失败');
        console.log(err);
      })
    }
  },
  created() {
    const articleId = this.$route.query.id;
    if (articleId){
      console.log('当前传递过来的文章id：'+this.$route.query.id);
      this.getArticleById(articleId);
    }
  }
}
</script>

<style scoped lang="scss">

.article-container{
  padding: 20px;
}

// 弹出框标题
.popover-title {
  margin-bottom: 1rem;
  text-align: center;
}
.searchCateList{
  width: 100%;
  margin-top: 1rem;
  height: 300px;
  overflow-y: auto;
}

// 弹出框中标签列表
.popover-tag-list{
  margin: 0.2rem 0.4rem;
  cursor: pointer;  // 鼠标悬浮样式修改
}

.category-name{
  width: 100%;
  padding: 0.6rem 0.5rem;
  cursor: pointer;   // 鼠标移动上去添加样式
}
.category-name:hover{   // :hover鼠标移动在上面的样式
  background-color: #f0f9eb;
  color: #67c23a;
}

</style>
