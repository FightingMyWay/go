import request from './request'

// 获取用户分页列表
export function getUserPage(params) {
  return request({
    url: '/user/page',
    method: 'get',
    params
  })
}

// 根据ID获取用户详情
export function getUserById(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

// 创建用户
export function createUser(data) {
  return request({
    url: '/user',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return request({
    url: '/user',
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'delete'
  })
}

// 重置用户密码
export function resetUserPassword(id) {
  return request({
    url: `/user/${id}/reset-password`,
    method: 'put'
  })
}

// 切换用户状态
export function toggleUserStatus(id, status) {
  return request({
    url: `/user/${id}/status`,
    method: 'put',
    data: { status }
  })
}

// 获取部门列表
export function getDepartments() {
  return request({
    url: '/dept/list',
    method: 'get'
  })
}

// 个人资料相关API
// 获取当前用户个人资料
export function getCurrentUserProfile() {
  return request({
    url: '/user/profile',
    method: 'get'
  })
}

// 更新个人资料
export function updateProfile(data) {
  return request({
    url: '/user/profile',
    method: 'put',
    data
  })
}

// 修改密码
export function changePassword(data) {
  return request({
    url: '/user/change-password',
    method: 'put',
    data
  })
}

// 上传头像
export function uploadAvatar(formData) {
  return request({
    url: '/user/avatar',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  })
}

// 获取个人资料统计
export function getProfileStats() {
  return request({
    url: '/user/profile/stats',
    method: 'get'
  })
}

// 获取最近活动
export function getRecentActivities() {
  return request({
    url: '/user/profile/activities',
    method: 'get'
  })
}
