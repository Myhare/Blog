import request from "@/utils/request";

// 获取网站基本信息
export function getWebsiteConfig(){
  return request({
    url: '/api/admin/website/config',
    method: 'get'
  })
}

// 更新网站基本信息
export function updateWebsiteConfig(websiteConfig){
  return request({
    url: '/api/admin/website/config',
    method: 'post',
    data: websiteConfig
  })
}

// 获取网站页面信息
export function getPageList(){
  return request({
    url: '/api/admin/page/list',
    method: 'get'
  })
}

// 删除网站页面
export function delPage(pageId){
  return request({
    url: `/api/admin/pages/${pageId}`,
    method: 'delete'
  })
}

// 更新页面信息
export function updatePageInfo(page){
  return request({
    url: '/api/admin/pages',
    method: 'post',
    data: page
  })
}

// 获取网站统计信息
export function getBackStatistics(){
  return request({
    url: '/api/admin',
    method: 'get'
  })
}

// 获取用户地区统计信息
export function getUserAreaStatistics(type){
  return request({
    url: '/api/admin/user/area',
    method: 'get',
    params: {
      type: type
    }
  })
}


