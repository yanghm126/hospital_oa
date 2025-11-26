import request from '@/utils/request'

// 查询班次列表
export function listShift(query) {
  return request({
    url: '/hr/shift/list',
    method: 'get',
    params: query
  })
}

// 查询班次详细
export function getShift(shiftId) {
  return request({
    url: '/hr/shift/' + shiftId,
    method: 'get'
  })
}

// 新增班次
export function addShift(data) {
  return request({
    url: '/hr/shift',
    method: 'post',
    data: data
  })
}

// 修改班次
export function updateShift(data) {
  return request({
    url: '/hr/shift',
    method: 'put',
    data: data
  })
}

// 删除班次
export function delShift(shiftId) {
  return request({
    url: '/hr/shift/' + shiftId,
    method: 'delete'
  })
}
