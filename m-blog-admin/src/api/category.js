import request from '@/utils/request'

// 分页查询分类数据
export function selectCateList(queryInfo){
  return request({
    url: '/api/getCateList',
    methods: 'get',
    params: {
      pageNum: queryInfo.pageNum,
      pageSize: queryInfo.pageSize,
      keywords: queryInfo.keywords
    }
  })
}

// 查询分类信息
export function selectCateSearch(keywords){
  return request({
    url: '/api/searchCateList',
    method: 'get',
    params:{
      keywords: keywords
    }
  })
}

// 查询所有分类信息
export function selectAllCate(){
  return request({
    url: '/api/getAllCateList',
    method: 'get'
  })
}

// 添加一个分类信息
export function addCategory(cateName) {
  return request({
    url: `/api/addCategory/${cateName}`,
    method: 'post'
  })
}

// 批量删除分类信息
export function deleteCate(cateIdList){
  return request({
    url: '/api/deleteCate',
    method: 'post',
    data: cateIdList
  })
}
