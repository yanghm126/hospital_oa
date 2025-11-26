import request from '@/utils/request'

// 查询考勤组列表
export function listAttendanceGroup(query) {
  return request({
    url: '/hr/attendanceGroup/list',
    method: 'get',
    params: query
  })
}

// 查询考勤组详细
export function getAttendanceGroup(id) {
  return request({
    url: '/hr/attendanceGroup/' + id,
    method: 'get'
  })
}

// 新增考勤组
export function addAttendanceGroup(data) {
  return request({
    url: '/hr/attendanceGroup',
    method: 'post',
    data: data
  })
}

// 修改考勤组
export function updateAttendanceGroup(data) {
  return request({
    url: '/hr/attendanceGroup',
    method: 'put',
    data: data
  })
}

// 删除考勤组
export function delAttendanceGroup(id) {
  return request({
    url: '/hr/attendanceGroup/' + id,
    method: 'delete'
  })
}

// 导出考勤组
export function exportAttendanceGroup(query) {
  return request({
    url: '/hr/attendanceGroup/export',
    method: 'get',
    params: query
  })
}

