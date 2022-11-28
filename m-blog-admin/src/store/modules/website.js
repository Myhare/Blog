import {
  getWebsiteConfig,
  updateWebsiteConfig,
  getPageList,
  updatePageInfo,
  getBackStatistics,
  getUserAreaStatistics
} from '@/api/website'

const state = {

}
const actions = {

  // 获取网站基本信息
  getWebsiteConfig(){
    return new Promise(resolve => {
      getWebsiteConfig()
        .then(res => {
          resolve(res)
        })
    })
  },

  // 更新网站基本信息
  updateWebsiteConfig(context,websiteConfig){
    return new Promise(resolve => {
      updateWebsiteConfig(websiteConfig)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 获取网站页面信息
  getPageList(context){
    return new Promise(resolve => {
      getPageList()
        .then(res => {
          resolve(res);
        })
    })
  },

  // 修改页面信息
  updatePageInfo(context,page){
    return new Promise(resolve => {
      updatePageInfo(page)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 获取网站统计信息
  getBackStatistics(context){
    return new Promise(resolve => {
      getBackStatistics()
        .then(res => {
          resolve(res);
        })
    })
  },

  // 获取用户地区统计信息
  getUserAreaStatistics(context,type){
    return new Promise(resolve => {
      getUserAreaStatistics(type)
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
