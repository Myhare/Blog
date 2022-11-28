import {
  addCategory,
  selectCateSearch,
  selectCateList,
  deleteCate,
  selectAllCate
} from '@/api/category'

const state = {

}
const actions = {

  // 分页查询分类数据
  selectCateList(context,queryInfo){
    return new Promise(((resolve, reject) => {
      selectCateList(queryInfo)
        .then(res => {
          resolve(res);
        }).catch(err=>{
          reject(err);
      })
    }))
  },

  // 搜索查询分类数据
  selectCateSearch(context,keywords){
    return new Promise((resolve, reject) => {
      selectCateSearch(keywords)
        .then(res => {
          resolve(res);
        }).catch(err => {
          reject(err);
      })
    })
  },

  // 查询所有分类数据
  selectAllCate(context){
    return new Promise((resolve, reject) => {
      selectAllCate()
        .then(res => {
          resolve(res);
        }).catch(err => {
          reject(err);
      })
    })
  },

  // 添加一个分类
  addCategory(context,cateName){
    console.log('传递进vuex中搞得cateName参数是：' + cateName);
    return new Promise((resolve, reject) => {
      addCategory(cateName)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        })
    })
  },

  // 删除分类
  async deleteCate(context,cateIdList){
    console.log('传递进vuex中搞得cateIdList参数是:');
    console.log(cateIdList);
    return new Promise((resolve, reject) => {
      deleteCate(cateIdList)
        .then(res => {
          resolve(res);
        })
        .catch(err => {
          reject(err);
        })
    })
  }

}

export default {
  namespaced: true,
  state,
  actions
}
