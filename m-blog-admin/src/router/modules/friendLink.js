import Layout from '@/layout'

const articleRouter = {
  path: '/friendLink',
  component: Layout,
  name: 'friendLink',
  meta:{
    title:'友链管理',
    icon:'friend-link'
  },
  children:[
    // 友链管理
    {
      path: '',
      component: () => import('@/views/friendLink/FriendLink'),
      name: 'friendLink',
      meta: {
        title: '友链管理'
      }
    }
  ]
}

export default articleRouter
