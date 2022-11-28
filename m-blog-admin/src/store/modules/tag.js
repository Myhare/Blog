import {
  addTag,
  delTag,
  getTagList,
  selectAllTag,
  searchTag
} from '@/api/tag'

const actions = {

  // 分页查询标签信息
  getTagList(context,tagIdList){
    return new Promise((resolve, reject) => {
      getTagList(tagIdList)
        .then(res=>{
          resolve(res);
        })
        .catch(error=>{
          reject(error);
        })
    })
  },

  // 添加标签
  addTag(context,tagName){
    return new Promise((resolve, reject) => {
      addTag(tagName)
        .then(res => {
          resolve(res);
        }).catch(err=>{
          reject(err);
      })
    })
  },

  // 删除标签
  delTag(context,tagIdList){
    return new Promise((resolve, reject) => {
      delTag(tagIdList)
        .then(res=>{
          resolve(res);
        }).catch(err=>{
          reject(err);
      })
    })
  },

  // 查询所有标签
  selectAllTag(context){
    return new Promise((resolve, reject) => {
      selectAllTag()
        .then(res => {
          resolve(res);
        }).catch(err => {
          reject(err);
      })
    })
  },

  // 分页模糊查询
  searchTag(context,keywords){
    return new Promise((resolve, reject) => {
      searchTag(keywords)
        .then(res => {
          resolve(res)
        }).catch(err => {
          reject(err);
      })
    })
  }
}

export default {
  namespaced: true,
  actions
}
