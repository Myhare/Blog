import request from '@/utils/request'

// 查询日志列表
export function logList(queryInfo) {
  return request({
    url: '/api/admin/logs',
    method: 'get',
    params: queryInfo
  })
}

// 删除日志列表
export function delLogList(logIdList){
  return request({
    url: '/api/admin/logs',
    method: 'delete',
    data: logIdList
  })
}
