import Layout from '@/layout'

const messageRouter = {
  path: '/message',
  name: 'Message',
  component: Layout,
  meta:{
    title: '消息管理',
    icon: 'message'
  },
  children:[
    {
      path: "comment",
      component: () => import('@/views/message/Comment'),
      name: 'comment',
      meta: {
        title: '评论管理'
      }
    },
    {
      path: "message",
      component:() => import('@/views/message/Message'),
      name: 'message',
      meta: {
        title: '留言管理'
      }
    }
  ]

}

export default messageRouter
