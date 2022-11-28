import request from '@/utils/request'

export function getRoutes() {
  return request({
    url: '/vue-element-admin/routes',
    method: 'get'
  })
}

// 分页获取标签
export function getTagList(queryInfo){
  return request({
    url: '/api/getTabList',
    method: 'get',
    params: {
      pageNum: queryInfo.pageNum,
      pageSize: queryInfo.pageSize,
      keywords: queryInfo.keywords
    }
  })
}

// 添加标签
export function addTag(tagName){
  return request({
    url: `/api/addTag/${tagName}`,
    method: 'post'
  })
}

// 删除标签
export function delTag(tagIdList){
  return request({
    url: '/api/deleteTag',
    method: 'post',
    data: tagIdList
  })
}

// 查询所有标签
export function selectAllTag(){
  return request({
    url: '/api/getAllTagName',
    method: 'get'
  })
}

// 模糊查询分类
export function searchTag(keywords){
  return request({
    url: '/api/searchTagList',
    method: 'get',
    params: {
      keywords: keywords
    }
  })
}
















