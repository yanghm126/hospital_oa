import request from '@/utils/request'

// 我的申请列表
export function listMyApply(query) {
  return request({
    url: '/hr/audit/my',
    method: 'get',
    params: query
  })
}

// 待办审批列表
export function listTodoAudit(query) {
  return request({
    url: '/hr/audit/todo',
    method: 'get',
    params: query
  })
}

// 提交申请
export function submitApply(data) {
  return request({
    url: '/hr/audit/submit',
    method: 'post',
    data: data
  })
}

// 审批操作
export function auditApply(data) {
  return request({
    url: '/hr/audit/audit',
    method: 'put',
    data: data
  })
}

