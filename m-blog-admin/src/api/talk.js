import request from '@/utils/request'

/**
 * 保存或者修改说说
 */
export function saveOrUpdateTalk(talk) {
  return request({
    url: '/api/saveOrUpdate',
    method: 'post',
    data: talk
  })
}

/**
 * 查询后台说说列表
 */
export function talkList(queryInfo){
  return request({
    url: '/api/admin/talks',
    method: 'get',
    params: queryInfo
  })
}

/**
 * 通过id查询后台说说
 */
export function getTalkById(talkId){
  return request({
    url: `/api/admin/talks/${talkId}`,
    method: 'get'
  })
}

/**
 * 删除说说
 */
export function delTalks(talkIdList){
  return request({
    url: '/api/admin/talks',
    method: 'delete',
    data: talkIdList
  })
}











