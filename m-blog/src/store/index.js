import Vue from 'vue'
import Vuex from 'vuex'
import createPersistedState from 'vuex-persistedstate';

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    searchFlag: false,
    registerFlag: false,
    forgetFlag: false,
    emailFlag: false,
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
    talkLikeSet: [],
    loginFlag: false, // 是否打开登录对话框
    blogInfo:{}, // 博客信息
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
      state.talkLikeSet = user.talkLikeSet ? user.talkLikeSet : [];           // 未完成
      state.email = user.email;
      state.loginType = user.loginType; // 未完成
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
      state.talkLikeSet = [];       // 未完成
      state.email = null;
      state.loginType = null; // 未完成
    },
    // 关闭所有对话框
    CLOSE_MODEL(state){
      state.loginFlag = false;
      state.registerFlag = false;
    },
    // 评论点赞
    COMMENT_LIKE(state,commentId){
      var commentLikeSet = state.commentLikeSet;
      if (commentLikeSet.indexOf(commentId) != -1) {
        commentLikeSet.splice(commentLikeSet.indexOf(commentId), 1);
      } else {
        commentLikeSet.push(commentId);
      }
    },
    // 修改登录用户信息
    UPDATE_USER_INFO(state, user) {
      state.nickname = user.nickname;
      state.intro = user.intro;
      state.webSite = user.webSite;
    },
  },
  actions: {

  },
  modules: {
  },
  plugins: [
    createPersistedState({
      storage: window.sessionStorage
    })
  ]
})
