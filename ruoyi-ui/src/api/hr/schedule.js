import request from '@/utils/request'

// 查询排班矩阵
export function getScheduleMatrix(query) {
  return request({
    url: '/hr/schedule/matrix',
    method: 'get',
    params: query
  })
}

// 批量保存排班
export function batchSaveSchedule(data) {
  return request({
    url: '/hr/schedule/batchSave',
    method: 'post',
    data: data
  })
}

// 查询班次列表 (用于下拉框)
export function listShift() {
  return request({
    url: '/hr/shift/list',
    method: 'get'
  })
}
