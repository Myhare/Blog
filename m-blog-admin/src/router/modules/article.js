import Layout from '@/layout'

const articleRouter = {
  path: '/article',
  component: Layout,
  name: 'article',
  meta:{
    title:'文章管理',
    icon:'form'
  },
  children:[
    // 发布文章
    {
      path: 'addArticle',
      component: () => import('@/views/article/AddArticle'),
      name: 'addArticle',
      meta: {
        title: '发布文章'
      }
    },
    // 文章管理
    {
      path: 'articles',
      component: () => import('@/views/article/ArticleList'),
      name: 'articles',
      meta: {
        title: '文章管理'
      }
    },
    // 分类管理
    {
      path: 'category',
      component: () => import('@/views/article/CateGory'),
      name: 'category',
      meta: {
        title: '分类管理'
      }
    },
    // 标签管理
    {
      path: 'tag',
      component: () => import('@/views/article/Tag'),
      name: 'tag',
      meta: {
        title: '标签管理'
      }
    }
  ]
}

export default articleRouter
