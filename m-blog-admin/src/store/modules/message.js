import {
  getCommentList,
  delCommentList,
  reviewCommentList,
  reallyDelCommentList,
  restoreCommentList
} from '@/api/message'

const state = {

}
const actions = {

  // 分页查询后台评论列表
  getCommentList(context, commentQueryInfo){
    return new Promise(resolve => {
      getCommentList(commentQueryInfo)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 删除指定评论
  delCommentList(context,commentIdList){
    return new Promise(resolve => {
      delCommentList(commentIdList)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 批量通过评论
  reviewCommentList(context,commentIdList){
    return new Promise(resolve => {
      reviewCommentList(commentIdList)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 彻底删除评论列表
  reallyDelCommentList(context,commentIdList){
    return new Promise(resolve => {
      reallyDelCommentList(commentIdList)
        .then(res => {
          resolve(res);
        })
    })
  },
  // 恢复评论
  restoreCommentList(context,commentIdList){
    return new Promise(resolve => {
      restoreCommentList(commentIdList)
        .then(res => {
          resolve(res);
        })
    })
  }

}

export default {
  namespaced: true,
  state,
  actions
}
