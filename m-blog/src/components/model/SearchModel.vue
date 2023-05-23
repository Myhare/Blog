<template>
  <!-- 搜索框 -->
  <v-dialog v-model="searchFlag" max-width="800" :fullscreen="isMobile">
    <v-card class="search-wrapper" style="border-radius:4px">
      <div class="mb-3">
        <span class="search-title">本地搜索</span>
        <!-- 关闭按钮 -->
        <v-icon class="float-right" @click="searchFlag = false">
          mdi-close
        </v-icon>
      </div>
      <!-- 输入框 -->
      <div class="search-input-wrapper">
        <v-icon>mdi-magnify</v-icon>
        <input v-model="keywords" @keyup.enter="search" placeholder="输入想要搜索的内容..." />
      </div>
      <hr class="divider" />
      <!--搜索标签页-->
      <div class="search-result-wrapper">
        <v-tabs vertical
                @change="onTabChange">
          <v-tab>
            <v-icon left>
              mdi-account
            </v-icon>
            文章
          </v-tab>
          <v-tab>
            <v-icon left>
              mdi-lock
            </v-icon>
            分类
          </v-tab>
          <v-tab>
            <v-icon left>
              mdi-tag-multiple
            </v-icon>
            标签
          </v-tab>
          <v-tab>
            <v-icon left>
              mdi-image-size-select-actual
            </v-icon>
            图片
          </v-tab>

          <!--文章搜索结果-->
          <v-tab-item>
            <v-card flat>
              <!-- 搜索结果 -->
              <div>
                <ul>
                  <li class="search-reslut" v-for="item of articleList" :key="item.id">
                    <!-- 文章标题 -->
                    <a @click="goToArticle(item.id)" v-html="item.title" />
                    <!-- 文章内容 -->
                    <p
                        class="search-reslut-content text-justify"
                        v-html="item.content"
                    />
                  </li>
                </ul>
                <div v-if="isSearchIng && articleList.length === 0"
                     style="text-align: center">
                  搜索中。。。
                </div>
                <!-- 搜索结果不存在提示 -->
                <div
                    v-show="flag && !isSearchIng && this.searchType === 1 && articleList.length === 0"
                    style="font-size:0.875rem"
                >
                  找不到您查询的内容：{{ keywords }}
                </div>
              </div>
            </v-card>
          </v-tab-item>
          <!--分类搜索结果-->
          <v-tab-item>
            <v-card flat>
              <v-btn v-for="category in categoryList"
                     rounded
                     v-html="category.categoryName"
                     @click="goToCategory(category.id)"
                     class="searchReBtn"
              />
              <div v-show="isSearchIng && categoryList.length === 0"
                   style="text-align: center">
                搜索中。。。
              </div>
              <!-- 搜索结果不存在提示 -->
              <div
                  v-show="flag && !isSearchIng && this.searchType === 2 && categoryList.length === 0"
                  style="font-size:0.875rem"
              >
                找不到您查询的内容：{{ keywords }}
              </div>
            </v-card>
          </v-tab-item>
          <!--标签搜索结果-->
          <v-tab-item>
            <v-card flat>
              <v-btn v-for="tag in tagList"
                     rounded
                     v-html="tag.tagName"
                     @click="goToTag(tag.id)"
                     class="searchReBtn"
              />
              <div v-show="isSearchIng && tagList.length === 0"
                   style="text-align: center">
                搜索中。。。
              </div>
              <!-- 搜索结果不存在提示 -->
              <div
                  v-show="flag && !isSearchIng && this.searchType === 3 && tagList.length === 0"
                  style="font-size:0.875rem"
              >
                找不到您查询的内容：{{ keywords }}
              </div>
            </v-card>
          </v-tab-item>
          <!--图片搜索结果-->
          <v-tab-item >
            <v-card
                flat
                class="animated zoomIn picture-card">
              <v-row>
                <v-col md="4" cols="12" v-for="picture in pictureList">
                  <div class="picture-con">
                    <v-img
                        :src="picture.url"
                        width="100%"
                        height="100%"
                        @click="goToPicture(picture.url)"
                        style="cursor:pointer"
                    ></v-img>
                  </div>
                  <span>{{picture.title}}</span>
                </v-col>
                <v-col cols = "12"
                       v-if="isSearchIng && pictureList.length === 0"
                       style="text-align: center; margin-top: 25px">
                  搜索中。。。
                </v-col>
                <v-col>
                  <div
                      v-show="flag && !isSearchIng && this.searchType === 3 && pictureList.length === 0"
                      style="text-align: center"
                  >
                    找不到您查询的内容：{{ keywords }}
                  </div>
                </v-col>
              </v-row>
            </v-card>
          </v-tab-item>
        </v-tabs>
      </div>
    </v-card>
  </v-dialog>
</template>

<script>
export default {
  data: function() {
    return {
      keywords: "",
      articleList: [],  // 文章列表
      categoryList: [], // 分类列表
      tagList: [],  // 标签列表
      pictureList: [], // 图片列表
      flag: false,     // 搜索框是否输入了有效值
      searchType : 1,  // 当前搜索的类型
      searchTypeList: [
        {value: 1, label: '文章', icon: 'mdi-account'},
        {value: 2, label: '分类', icon: 'mdi-lock'},
        {value: 3, label: '标签', icon: 'mdi-lock'},
        {value: 4, label: '图片', icon: 'mdi-access-point'}
      ],
      isSearchIng: false,  // 是否正在搜索
      searchTimer: null  // 搜索防抖计时器
    };
  },
  methods: {
    onTabChange(searchIndex){
      // alert(searchIndex)
      // 后端规定的搜索类型是从1开始
      this.searchType = searchIndex +1;
      // 重新搜索
      this.search()
    },
    search(){
      // 进行防抖
      // 清除之前的计时器，重新开始计时器
      clearTimeout(this.searchTimer)
      this.searchTimer = setTimeout(() => {
        this.doSearch()
      },500)

    },
    doSearch(){
      this.flag = this.keywords.trim() !== "";
      if (!this.flag){
        // 初始化搜索结果
        this.articleList = [];
        this.categoryList = [];
        this.tagList = [];
        this.pictureList = [];
        return
      }
      this.isSearchIng = true;
      this.$axios
          .get("/api/blog/search", {
            params: {
              current: 1,
              keywords: this.keywords,
              searchType: this.searchType
            }
          })
          .then(({ data }) => {
            this.articleList = data.data.articleList == null ? [] : data.data.articleList;
            this.categoryList = data.data.categoryList == null ? [] : data.data.categoryList;
            this.tagList = data.data.tagList == null ? [] : data.data.tagList;
            this.pictureList = data.data.pictureList == null ? [] : data.data.pictureList;
            this.isSearchIng = false;
          }).catch(err => {
            this.isSearchIng = false;
      });
    },
    goToArticle(articleId) {
      this.$store.state.searchFlag = false;
      this.$router.push({ path: "/articles/" + articleId });
    },
    goToCategory(categotyId) {
      this.$store.state.searchFlag = false;
      this.$router.push({ path: "/categories/" + categotyId });
    },
    goToTag(tagId){
      this.$store.state.searchFlag = false;
      this.$router.push({ path: "/tags/" + tagId });
    },
    goToPicture(url){
      window.open(url);
    }
  },
  computed: {
    searchFlag: {
      set(value) {
        this.$store.state.searchFlag = value;
      },
      get() {
        return this.$store.state.searchFlag;
      }
    },
    isMobile() {
      const clientWidth = document.documentElement.clientWidth;
      if (clientWidth > 960) {
        return false;
      }
      return true;
    }
  },
  watch: {
    // 监听搜索栏进行搜索
    keywords(value) {
      this.search(value, this.searchType);
    }
  }
};
</script>

<style scoped>
.search-wrapper {
  padding: 1.25rem;
  height: 100%;
  background: #fff !important;
}
.search-title {
  color: #49b1f5;
  font-size: 1.25rem;
  line-height: 1;
}

.searchReBtn{
  margin: 0.5rem;
}

.search-input-wrapper {
  display: flex;
  padding: 5px;
  height: 35px;
  width: 100%;
  border: 2px solid #8e8cd8;
  border-radius: 2rem;
}
.search-input-wrapper input {
  width: 100%;
  margin-left: 5px;
  outline: none;
}
@media (min-width: 960px) {
  .search-result-wrapper {
    margin-left: -15px;
    padding-right: 5px;
    height: 450px;
    overflow: auto;
  }
}
@media (max-width: 959px) {
  .search-result-wrapper {
    margin-left: -15px;
    height: calc(100vh - 110px);
    overflow: auto;
  }
}
.search-reslut a {
  color: #555;
  font-weight: bold;
  border-bottom: 1px solid #999;
  text-decoration: none;
}
.search-reslut-content {
  color: #555;
  cursor: pointer;
  border-bottom: 1px dashed #ccc;
  padding: 5px 0;
  line-height: 2;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
}
.divider {
  margin: 20px 0;
  border: 2px dashed #d2ebfd;
}

.picture-con{
  height: 220px;
  overflow: hidden;
}

.picture-card:hover {
  transition: all 0.3s;
  box-shadow: 0 4px 12px 12px rgba(7, 17, 27, 0.15);
}
.picture-card:not(:hover) {
  transition: all 0.3s;
}
.picture-card:hover .on-hover {
  transition: all 0.6s;
  transform: scale(1.1);
}
.picture-card:not(:hover) .on-hover {
  transition: all 0.6s;
}

</style>
