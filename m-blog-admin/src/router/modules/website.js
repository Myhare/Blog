import Layout from '@/layout'

const userRouter = {
  path: '/website',
  component: Layout,
  name: 'Website',
  meta:{
    title:'系统管理',
    icon:'setup'
  },
  children:[
    {
      path: '/websiteManage',
      component: ()=> import('@/views/website/Website'),
      name: 'website',
      meta:{
        title:'网站管理',
        icon: 'website'
      }
    },
    {
      path: '/page',
      component: ()=> import('@/views/website/Page'),
      name: 'page',
      meta: {
        title: '页面管理',
        icon: 'website'
      }
    }
  ]
}

export default userRouter
