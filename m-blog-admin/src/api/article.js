import request from '@/utils/request'

export function fetchList(query) {
  return request({
    url: '/vue-element-admin/article/list',
    method: 'get',
    params: query
  })
}

export function fetchArticle(id) {
  return request({
    url: '/vue-element-admin/article/detail',
    method: 'get',
    params: { id }
  })
}

export function fetchPv(pv) {
  return request({
    url: '/vue-element-admin/article/pv',
    method: 'get',
    params: { pv }
  })
}

export function createArticle(data) {
  return request({
    url: '/vue-element-admin/article/create',
    method: 'post',
    data
  })
}

export function updateArticle(data) {
  return request({
    url: '/vue-element-admin/article/update',
    method: 'post',
    data
  })
}

// -----------------博客-------------------
// 上传博客图片
export function uploadArticleFile(fileForm){
  return request({
    url: '/api/articleFile',
    method: 'post',
    data: fileForm
  })
}

// 上传文章
export function addArticle(article){
  return request({
    url: '/api/addArticle',
    method: 'post',
    data: article
  })
}

// 分页查询文章列表
export function getArticleList(articleQueryInfo){
  return request({
    url: '/api/admin/articles',
    method: 'get',
    params: articleQueryInfo
  })
}

// 删除文章
export function deleteArticle(articleIdList){
  return request({
    url: '/api/admin/deleteArticle',
    method: 'post',
    data: articleIdList
  })
}

// 修改文章置顶情况
export function changeArticleTop(data){
  return request({
    url: '/api/admin/changeTop',
    method: 'post',
    data: data
  })
}

// 通过id查询文章
export function getArticleById(id){
  return request({
    url: '/api/selectArticle',
    method: 'get',
    params: {articleId: id}
  })
}
