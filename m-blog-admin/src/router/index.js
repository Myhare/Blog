import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/* Router Modules */
// import componentsRouter from './modules/components'
// import chartsRouter from './modules/charts'
// import tableRouter from './modules/table'
// import nestedRouter from './modules/nested'
import usersRouter from './modules/users'     // 用户列表
import articleRouter from './modules/article' // 文章管理
import messageRouter from "@/router/modules/message";  // 消息管理
import websiteRouter from "@/router/modules/website";   // 网站管理
import logRouter from "@/router/modules/log";   // 日志管理
import talkRouter from "@/router/modules/talk";  // 说说管理

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   如果设置为true，项目将不会显示在侧边栏中（默认值为false）
 * alwaysShow: true               如果设置为true，将始终显示根菜单
 *                                如果未设置alwaysShow，则当项目具有多个子路由时，
 *                                它将变为嵌套模式，否则不显示根菜单
 * redirect: noRedirect           如果设置为noRedirect，则面包屑中不会重定向
 * name:'router-name'             路由名称 <keep-alive> (必须设置！！！)
 * meta : {
    roles: ['admin','editor']    控制页面角色（可以设置多个角色）
    title: 'title'               边栏和面包屑中显示的名称（推荐集）
    icon: 'svg-name'/'el-icon-x' 图标显示在侧边栏中
    noCache: true                如果设置为true，则不会缓存页面（默认值为false）
    affix: true                  如果设置为true，标签将粘贴在标签视图中
    breadcrumb: false            如果设置为false，则项目将隐藏在面包屑中（默认值为true）
    activeMenu: '/example/list'  如果设置路径，边栏将突出显示您设置的路径
  }
 */

/**
 * 恒定路线
 * 没有权限要求的基页
 * 可以访问所有角色
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/auth-redirect',
    component: () => import('@/views/login/auth-redirect'),
    hidden: true
  },
  {
    path: '/404',
    component: () => import('@/views/error-page/404'),
    hidden: true
  },
  {
    path: '/401',
    component: () => import('@/views/error-page/401'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/home',
    children:[
      {
        path: 'home',
        component: ()=>import('@/views/home/Home'),
        name:'Home',
        meta: { title: '首页', icon: 'el-icon-picture-outline-round', affix: true }
      }
    ]
  }
]

/**
 * asyncRoutes 异步路由
 * 需要根据用户角色动态加载的路由
 */
export const asyncRoutes = [
  /** 当路由图太长时，可以将其拆分为小模块 **/
  // componentsRouter,
  // chartsRouter,
  // nestedRouter,
  // tableRouter,
  usersRouter,
  articleRouter,
  messageRouter,
  websiteRouter,
  logRouter,
  talkRouter,

  // 个人中心
  {
    path: '/personCenter',
    component: Layout,
    name: 'personCenter',
    meta: {
      title: '个人中心',
      icon: 'user'
    },
    children: [
      {
        path: '',
        component: () => import('@/views/users/personalCenter'),
        name: 'personCenter',
        meta: {
          title: '个人中心'
        }
      }
    ]
  },

  // 404页必须放在末尾！！！
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  mode: 'history',
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
