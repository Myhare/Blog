import Vue from 'vue'

import Cookies from 'js-cookie'

import 'normalize.css/normalize.css' // a modern alternative to CSS resets

import Element from 'element-ui'
import './styles/element-variables.scss'
import enLang from 'element-ui/lib/locale/lang/zh-CN'// 如果使用中文语言包请默认支持，无需额外引入，请删除该依赖
import '@/styles/index.scss' // global css
import App from './App'
import store from './store'
import router from './router'
import "./assets/css/iconfont.css";
import "echarts/lib/chart/line";
import "echarts/lib/chart/pie";
import "echarts/lib/chart/bar";
import "echarts/lib/chart/map";
import "echarts/lib/component/tooltip";
import "echarts/lib/component/legend";
import "echarts/lib/component/title";
import './icons' // 图标
import './permission' // 权限控制
import './utils/error-log' // 错误日志
import "./assets/css/index.css";    // 公共css
import VueCalendarHeatmap from "vue-calendar-heatmap";  // 日历组件
import ECharts from "vue-echarts";
import vuetify from "@/plugins/vuetify";
import * as filters from './filters'; // 通用过滤器
import tagCloud from './assets/js/tag-cloud'
// markdown编辑器
import mavonEditor from 'mavon-editor';
import 'mavon-editor/dist/css/index.css';
// 全局变量
import global from "@/assets/js/global";

// 引入全局时间格式化组件库
import moment from "moment";
import tag from "@/store/modules/tag";
// // 定义时间格式化全局过滤器
// Vue.filter('dateFormat', (dateStr, pattern = 'YYYY-DD-MM') => {
//   return moment(dateStr).format(pattern)
// })
Vue.prototype.$moment = moment


Vue.use(mavonEditor); // markdown
Vue.use(tagCloud);    // 标签云
Vue.use(VueCalendarHeatmap) // 日历组件
Vue.prototype.$global = global;  // 设置全局变量

Vue.component("v-chart", ECharts);

Vue.use(Element, {
  size: Cookies.get('size') || 'medium', // set element-ui default size
  locale: enLang // 如果使用中文，无需设置，请删除
})

// register global utility filters
Object.keys(filters).forEach(key => {
  Vue.filter(key, filters[key])
})

Vue.config.productionTip = false

new Vue({
  el: '#app',
  router,
  store,
  vuetify,
  render: h => h(App)
})
