import Layout from '@/layout'

const userRouter = {
  path: '/users',
  component: Layout,
  name: 'Users',
  meta:{
    title:'用户管理',
    icon:'user'
  },
  children:[
    {
      path: 'userList',
      component: ()=> import('@/views/users/UserList'),
      name: 'userList',
      meta:{
        title:'用户列表'
      }
    },
    {
      path: 'onLine',
      component: ()=> import('@/views/users/Online'),
      name: 'onLine',
      meta: {
        title: '在线用户'
      }
    }
  ]
}

export default userRouter
