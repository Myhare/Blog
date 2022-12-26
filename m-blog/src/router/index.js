import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  // 主页
  {
    path: '/',
    component: () => import('@/views/home/Home')
  },
  // 分类列表
  {
    path: '/categories',
    component: () => import('@/views/category/Category'),
    meta:{
      title: '分类'
    }
  },
  // 文章预览-分类
  {
    path: '/categories/:categoryId',
    component: () => import('@/views/article/ArticleList')
  },
  // 标签列表
  {
    path: '/tags',
    component: () => import('@/views/tag/Tag'),
    meta:{
      title: '标签'
    }
  },
  // 文章预览-标签
  {
    path: '/tags/:tagId',
    component: () => import('@/views/article/ArticleList')
  },
  // 查看文章
  {
    path: '/articles/:articleId',
    component: () => import('@/views/article/Article')
  },
  // 文章归档
  {
    path: '/archives',
    component: () => import('@/views/archive/Archive'),
    meta: {
      title: '归档'
    }
  },
   // 个人信息
  {
    path: '/user',
    component: () => import('@/views/user/User'),
    meta: {
      title: '个人中心'
    }
  }
]

const router = new VueRouter({
  routes,
  // mode: 'hash'
  mode: 'history'
})

export default router