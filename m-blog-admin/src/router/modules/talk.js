import Layout from '@/layout'

const talkRouter = {
  path: '/talk',
  component: Layout,
  name: 'Talk',
  meta:{
    title:'说说管理',
    icon:'edit'
  },
  children:[
    {
      path: 'talkList',
      component: ()=> import('@/views/talk/TalkList'),
      name: 'talkList',
      meta:{
        title:'说说列表'
      }
    },
    {
      path: '',
      component: ()=> import('@/views/talk/Talk'),
      name: 'talk',
      meta: {
        title: '写说说'
      }
    }
  ]
}

export default talkRouter
