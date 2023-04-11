import Layout from '@/layout'

const logRouter = {
  path: '/log',
  name: 'Log',
  component: Layout,
  meta:{
    title: '日志管理',
    icon: 'message'
  },
  children:[
    {
      path: "log",
      component: () => import('@/views/log/Operation'),
      name: '日志管理',
      meta: {
        title: '日志管理'
      }
    }
  ]

}

export default logRouter
