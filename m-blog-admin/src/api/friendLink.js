import request from '@/utils/request'

// 查询友链列表
export function getLinkList(queryInfo){
  return request({
    url: '/api/admin/links',
    method: 'get',
    params: queryInfo
  })
}

// 添加或修改友链
export function saveOrUpdateLink(link){
  return request({
    url: '/api/admin/links',
    method: 'post',
    data: link
  })
}

// 删除友链
export function deleteLink(linkIdList){
  console.log(linkIdList);
  return request({
    url: '/api/admin/links',
    method: 'delete',
    data: linkIdList
  })
}

