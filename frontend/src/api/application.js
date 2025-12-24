import request from './request'

// 获取申请分页列表
export function getApplicationPage(params) {
  return request({
    url: '/application/page',
    method: 'get',
    params
  })
}

// 根据ID获取申请详情
export function getApplicationById(id) {
  return request({
    url: `/application/${id}`,
    method: 'get'
  })
}

// 创建申请
export function createApplication(data) {
  return request({
    url: '/application',
    method: 'post',
    data
  })
}

// 审批申请
export function approveApplication(id, data) {
  return request({
    url: `/application/approve/${id}`,
    method: 'put',
    data
  })
}

// 获取用户的申请列表
export function getUserApplications(userId, params) {
  return request({
    url: `/application/user/${userId}`,
    method: 'get',
    params
  })
}

// 获取我的申请列表
export function getMyApplications(params) {
  return request({
    url: '/application/my',
    method: 'get',
    params
  })
}

// 取消申请
export function cancelApplication(id) {
  return request({
    url: `/application/${id}/cancel`,
    method: 'put'
  })
}
