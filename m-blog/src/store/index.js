import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchFlag: false,   // 是否打开搜索对话框
    registerFlag: false,
    forgetFlag: false,
    emailFlag: false,
    gptFlag : false,  // 是否打开GPT
    drawer: false,
    loginUrl: "",
    userId: null,
    avatar: null,
    nickname: null,
    intro: null,
    webSite: null,
    loginType: null,
    email: null,
    articleLikeSet: [],   // 文章点赞集合
    commentLikeSet: [],   // 评论点赞集合
    talkLikeSet: [],      // 说说点赞集合
    loginFlag: false, // 是否打开登录对话框
    blogInfo:{}, // 博客信息
    // metaInfo: {}, // meta信息
  },
  getters: {
  },
  mutations: {
    // 设置博客基本信息
    checkBlogInfo(state,blogInfo){
      // console.log("成功进入到vuex中的checkBlogInfo方法，blogInfo：");
      // console.log(blogInfo);
      state.blogInfo = blogInfo;
    },
    // 登录后设置用户基本信息
    LOGIN(state, user) {
      state.userId = user.userInfoId;
      state.avatar = user.avatar;
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;   // 未完成
      state.articleLikeSet = user.articleLikeSet ? user.articleLikeSet : [];  // 用户文章点赞列表(未完成)
      state.commentLikeSet = user.commentLikeSet ? user.commentLikeSet : [];  // 用户评论点赞列表
      state.talkLikeSet = user.talkLikeSet ? user.talkLikeSet : [];
      state.email = user.email;
      state.loginType = user.loginType; // 登录类型
    },
    // 退出登录
    LOGOUT(state){
      state.userId = null;
      state.avatar = null;
      state.nickname = null;
      state.intro = null;
      state.webSite = null;   // 未完成
      state.articleLikeSet = [];    // 未完成
      state.commentLikeSet = [];    // 评论点赞集合
      state.talkLikeSet = [];       // 说说点赞集合
      state.email = null;
      state.loginType = null; // 登录类型
    },
    // 关闭所有对话框
    CLOSE_MODEL(state){
      state.loginFlag = false;
      state.registerFlag = false;
      state.emailFlag = false;
      state.gptFlag = false;
    },
    // 评论点赞
    COMMENT_LIKE(state,commentId){
      var commentLikeSet = state.commentLikeSet;
      if (commentLikeSet.indexOf(commentId) !== -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1); // splice(要修改的数组下标,要删除的元素数量)
      } else {
        commentLikeSet.push(commentId);
      }
    },
    // 说说点赞
    TALK_LIKE(state, talkId){
      var talkLikeSet = state.talkLikeSet;
      if (talkLikeSet.indexOf(talkId) !== -1){
        // 取消点赞
        talkLikeSet.splice(talkLikeSet.indexOf(talkId), 1);
      }else {
        talkLikeSet.push(talkId)
      }
    },
    // 修改登录用户信息
    UPDATE_USER_INFO(state, user) {
      state.nickname = user.nickName;
      state.intro = user.intro;
      state.webSite = user.webSite;
    },
    // 修改用户绑定邮箱
    CHANGE_EMAIL(state, email){
      state.email = email
    },
    // 保存登录路径
    SAVE_LOGIN_URL(state, url){
      state.loginUrl = url;
    },
    // 修改页面META信息
    // CHANGE_META_INFO(state, metaInfo) {
    //   console.log(metaInfo,"metaInfo")
    //   state.metaInfo = metaInfo;
    // }
  },
  actions: {

  },
  modules: {
  },
  // 解决刷新后vuex会清除的问题
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
