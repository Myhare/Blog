import {
  getLinkList,
  saveOrUpdateLink,
  deleteLink
} from '@/api/friendLink'

const state = {

}
const actions = {

  // 分页查询友链列表
  getLinkList(context,queryInfo){
    return new Promise(resolve => {
      getLinkList(queryInfo)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 添加或删除友链
  saveOrUpdateLink(context, link){
    return new Promise(resolve => {
      saveOrUpdateLink(link)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 删除友链
  deleteLink(context, linkIdList){
    return new Promise(resolve => {
      deleteLink(linkIdList)
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
