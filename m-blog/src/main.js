import Vue from 'vue'
import App from './App.vue'
import router from './router'
import store from './store'
import vuetify from './plugins/vuetify'
// import animated from 'animate.css'
import "./assets/css/index.css";
import "./assets/css/iconfont.css";
import "./assets/css/markdown.css";
import "./assets/css/vue-social-share/client.css";
import config from "./assets/js/config";
import axios from "axios";
import InfiniteLoading from "vue-infinite-loading"; // 引用无限加载组件
import dayjs from "dayjs";               // 时间处理插件
import "highlight.js/styles/atom-one-dark.css";    // 代码高亮组件
import VueImageSwipe from "vue-image-swipe";        // 图片预览组件
import { vueBaberrage } from 'vue-baberrage'    // 弹幕组件
import NProgress from "nprogress";
import "vue-image-swipe/dist/vue-image-swipe.css";
import Toast from "./components/toast/index";       // 全局消息提示
import global from "@/assets/js/global";        // 全局变量
// import Meta from 'vue-meta';   // meta相关

// Vue.use(animated);
Vue.use(InfiniteLoading);
Vue.use(VueImageSwipe);
Vue.use(Toast);
Vue.use(vueBaberrage)
// Vue.use(Meta)

Vue.prototype.$global = global; // 设置全局变量
Vue.prototype.config = config;
Vue.prototype.$axios = axios;

Vue.filter("date", function(value) {
    return dayjs(value).format("YYYY-MM-DD");
});

Vue.filter("year", function(value) {
    return dayjs(value).format("YYYY");
});

Vue.filter("hour", function(value) {
    return dayjs(value).format("HH:mm:ss");
});

Vue.filter("time", function(value) {
    return dayjs(value).format("YYYY-MM-DD HH:mm:ss");
});

Vue.filter("num", function(value) {
    if (value >= 1000) {
        return (value / 1000).toFixed(1) + "k";
    }
    return value;
});

axios.interceptors.response.use(
    function(response) {
        // console.log("返回response数据：");
        // console.log(response);
        switch (response.data.code) {
            case 50000:
                Vue.prototype.$toast({ type: "error", message: "系统异常" });
                break;
            case 51000:
                Vue.prototype.$toast({type:"error", message: response.data.message});
                break;
        }
        return response;
    },
    function(error) {
        return Promise.reject(error);
    }
)
router.beforeEach((to, from, next) => {
    NProgress.start();
    if (to.meta.title) {
        document.title = to.meta.title;
    }
    next();
});

router.afterEach(() => {
    window.scrollTo({
        top: 0,
        behavior: "instant"
    });
    NProgress.done();
});

// 路由前置守卫用来修改meta数据
// router.beforeEach((to, from, next) => {
//     if (to.meta.metaInfo){
//         store.commit("CHANGE_META_INFO", to.meta.metaInfo)
//     }
//     next()
// });

Vue.config.productionTip = false

new Vue({
    router,
    store,
    vuetify,
    // metaInfo(){
    //   return{
    //       title: this.$store.state.metaInfo.title,
    //       meta: [
    //           {
    //               name: "keywords",
    //               content: "11111"
    //               // content: this.$store.state.metaInfo.keywords
    //           }, {
    //               name: "description",
    //               content: this.$store.state.metaInfo.description
    //           }
    //       ]
    //   }
    // },
    render: h => h(App)
}).$mount('#app')
