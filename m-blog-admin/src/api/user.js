import request from '@/utils/request'

export function login(data) {
  return request({
    url: '/api/login',
    method: 'post',
    data
  })
}

export function getInfo(token) {
  return request({
    url: '/api/getUserSimpInfo',
    method: 'get',
    params: { token }
  })
}

export function logout() {
  return request({
    url: '/api/logout',
    method: 'post'
  })
}

/**
 * 获取用户信息
 * @param queryInfo 分页查询参数
 */
export function getUserList(queryInfo){
  return request({
    url: '/api/admin/getUserList',
    method:'get',
    params: {
      pageNum: queryInfo.pageNum,
      pageSize: queryInfo.pageSize,
      keywords: queryInfo.keywords
    }
  })
}

/**
 * 更新用户删除状态
 * @param userId 想要修改的用户id
 * @param isDelete 删除之前的删除状态
 */
export function updateUserDelete(userId,isDelete){
  const param = {
    userId: userId,
    isDelete: isDelete
  }
  return request({
    url: '/api/admin/updateUserDelete',
    method: 'post',
    data: param
  })
}

/**
 * 更新用户简单信息
 * @param nickName 用户昵称
 * @param intro    用户简介
 */
export function changeUserInfo(nickName, intro){
  let params = {
    nickName,
    intro
  }

  return request({
    url: '/api/admin/userInfoChange',
    method: 'post',
    data: params
  })

}

/**
 * 更新用户密码
 * @param data 数据
 * @returns 结果
 */
export function updatePassword(data){
  return request({
    url: '/api/admin/updatePassword',
    method: 'post',
    data
  })
}

/**
 * 获取在线用户信息
 */
export function getOnline(params){
  // console.log(params);
  return request({
    url: '/api/admin/userInfo/online',
    method: 'get',
    params: {
      keywords: params.keywords,
      current: params.current,
      size: params.size
    }
  })
}

/**
 * 下线用户
 */
export function removeOnlineUser(userId){
  return request({
    url: `/api/admin/online/${userId}/remove`,
    method: 'post'
  })
}
