import {
  saveOrUpdateTalk,
  talkList,
  getTalkById,
  delTalks
} from '@/api/talk'

const state = {

}
const actions = {
  // 添加或修改说说
  saveOrUpdateTalk(context, talk) {
    return new Promise(resolve => {
      saveOrUpdateTalk(talk).then(res => {
        resolve(res);
      })
    })
  },

  // 查询说说列表
  talkList(context, queryInfo){
    return new Promise(resolve => {
      talkList(queryInfo).then(res => {
        resolve(res);
      })
    })
  },

  // 通过id查询说说
  getTalkById(context, talkId){
    return new Promise(resolve => {
      getTalkById(talkId).then(res => {
        resolve(res);
      })
    })
  },

  // 删除说说
  // 通过id查询说说
  delTalks(context, talkIdList){
    return new Promise(resolve => {
      delTalks(talkIdList).then(res => {
        resolve(res);
      })
    })
  },

}

export default {
  namespaced: true,
  state,
  actions
}
