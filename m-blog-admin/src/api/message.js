import request from "@/utils/request";

// 查询后台评论列表
export function getCommentList(commentQueryInfo){
  return request({
    url: '/api/admin/comments',
    method: 'get',
    params: commentQueryInfo
  })
}

// 逻辑删除后端指定评论
export function delCommentList(commentIdList){
  return request({
    url: '/api/admin/delComment',
    method: 'post',
    data: commentIdList
  })
}

// 批量通过评论审核
export function reviewCommentList(commentIdList){
  return request({
    url: '/api/admin/reviewCom',
    method: 'post',
    data: commentIdList
  })
}

// 彻底删除评论
export function reallyDelCommentList(commentIdList){
  return request({
    url: '/api/admin/reallyDelComment',
    method: 'post',
    data: commentIdList
  })
}

// 恢复评论
export function restoreCommentList(commentIdList){
  return request({
    url: '/api/admin/restoreComment',
    method: 'post',
    data: commentIdList
  })
}

// 后台查询留言列表
export function messageList(queryInfo){
  return request({
    url: '/api/admin/messages',
    method: 'get',
    params: queryInfo
  })
}

// 删除留言列表
export function delMessageList(messageIdList){
  return request({
    url: '/api/admin/delMessage',
    method: 'post',
    data: messageIdList
  })
}

// 审核留言
export function updateMessageReview(param){
  return request({
    url: '/api/admin/updateMessage',
    method: 'post',
    data: param
  })
}








