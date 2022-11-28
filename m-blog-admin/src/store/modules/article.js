import {
  uploadArticleFile,
  addArticle,
  getArticleList,
  deleteArticle,
  changeArticleTop,
  getArticleById
} from '@/api/article'

const state = {

}
const actions = {

  // 上传博客图片文件
  uploadArticleFile(context,fileForm){
    return new Promise((resolve, reject) => {
      uploadArticleFile(fileForm)
        .then(res => {
          // console.log('上传文件回调函数');
          resolve(res);
        }).catch(error => {
        reject(error);
      })
    })
  },

  // 上传文章
  addArticle(context,article){
    return new Promise((resolve, reject) => {
      addArticle(article)
        .then(res => {
          resolve(res);
        }).catch(err => {
        reject(err);
      })
    })
  },

  // 分页获取文章列表
  getArticleList(context,articleQueryInfo){
    return new Promise(resolve => {
      getArticleList(articleQueryInfo)
        .then(res => {
          resolve(res);
        })
    })
  },

  // 删除指定文章
  deleteArticle(context,articleIdList){
    // console.log('vuex中接收到的articleIdList：');
    // console.log(articleIdList);
    return new Promise((resolve, reject) => {
      deleteArticle(articleIdList)
        .then(res => {
          resolve(res);
        }).catch(err => {
        reject(err);
      })
    })
  },

  // 修改文章置顶情况
  changeArticleTop(context,data){
    return new Promise((resolve, reject) => {
      changeArticleTop(data)
        .then(res => {
          resolve(res);
        }).catch(err => {
        reject(err);
      })
    })
  },

  // 通过id获取文章
  getArticleById(state,id){
    return new Promise((resolve, reject) => {
      getArticleById(id)
        .then(res => {
          resolve(res);
        }).catch(err => {
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
