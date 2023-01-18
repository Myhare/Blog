<template>
  <v-app id="app">
    <!--导航栏-->
    <TopNavBar/>
    <!-- 侧边导航栏 -->
    <SideNavBar></SideNavBar>
    <!--内容-->
    <v-main>
      <router-view :key="$route.fullPath"></router-view>
    </v-main>
    <!--页脚-->
    <Footer/>
    <!-- 返回顶部 -->
    <BackTop></BackTop>
    <!-- 搜索模态框 -->
<!--    <searchModel></searchModel>-->
    <!-- 登录模态框 -->
    <LoginModel></LoginModel>
    <!-- 注册模态框 -->
    <RegisterModel></RegisterModel>
    <!-- 忘记密码模态框 -->
    <ForgetModel></ForgetModel>
    <!-- 绑定邮箱模态框 -->
<!--    <EmailModel></EmailModel>-->
    <!--聊天室-->
    <ChatRoom></ChatRoom>
  </v-app>
</template>

<script>
import TopNavBar from "@/components/layout/TopNavBar";
import Footer from "@/components/layout/Footer";
import searchModel from "./components/model/SearchModel";
import LoginModel from "./components/model/LoginModel";
import RegisterModel from "./components/model/RegisterModel";
import ForgetModel from "./components/model/ForgetModel";
import EmailModel from "./components/model/EmailModel";
import BackTop from "@/components/BackTop";
import ChatRoom from "@/components/ChatRoom"; // 聊天室
import SideNavBar from "@/components/layout/SideNavBar"; // 侧边导航栏
export default {
  name: 'App',
  components:{
    TopNavBar,
    Footer,
    searchModel,
    LoginModel,
    RegisterModel,
    ForgetModel,
    EmailModel,
    BackTop,
    ChatRoom,
    SideNavBar
  },
  data: () => ({
    //
  }),
  methods:{
    getBlogInfo(){
      this.$axios.get('/api/')
          .then(({ data }) => {
            this.$store.commit("checkBlogInfo",data.data);
          })
    }
  },
  created() {
    // 获取博客信息
    this.getBlogInfo();
    // 上传访客信息
    this.$axios.post('/api/report')
  },
  computed: {
    blogInfo() {
      return this.$store.state.blogInfo;
    }
  }
};
</script>
