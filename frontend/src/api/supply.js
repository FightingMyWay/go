import request from './request'

// 获取用品分页列表
export function getSupplyItems(params) {
  return request({
    url: '/supply/page',
    method: 'get',
    params
  })
}

// 根据ID获取用品详情
export function getSupplyById(id) {
  return request({
    url: `/supply/${id}`,
    method: 'get'
  })
}

// 根据分类获取用品列表
export function getSupplyByCategory(categoryId) {
  return request({
    url: `/supply/category/${categoryId}`,
    method: 'get'
  })
}

// 获取所有用品列表
export function getSupplyList() {
  return request({
    url: '/supply/list',
    method: 'get'
  })
}

// 获取分类列表
export function getCategories() {
  return request({
    url: '/supply/categories',
    method: 'get'
  })
}

// 创建用品
export function createSupplyItem(data) {
  return request({
    url: '/supply',
    method: 'post',
    data
  })
}

// 更新用品
export function updateSupplyItem(data) {
  return request({
    url: '/supply',
    method: 'put',
    data
  })
}

// 删除用品
export function deleteSupplyItem(id) {
  return request({
    url: `/supply/${id}`,
    method: 'delete'
  })
}

// 更新库存
export function updateStock(id, quantity) {
  return request({
    url: `/supply/stock/${id}`,
    method: 'put',
    params: { quantity }
  })
}
