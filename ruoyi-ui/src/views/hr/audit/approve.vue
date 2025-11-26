<template>
  <div class="app-container">
    <div class="mb8">
        <h3>待办审批</h3>
    </div>
    <el-table v-loading="loading" :data="list">
        <el-table-column label="申请人" prop="userName" width="120" />
        <el-table-column label="部门" prop="deptName" width="120" />
        <el-table-column label="类型" prop="applyType" width="100">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.applyType === '1'">请假</el-tag>
                <el-tag v-else type="warning">补卡</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="时间范围" width="300">
            <template slot-scope="scope">
                {{ scope.row.startTime }} 至 {{ scope.row.endTime }}
            </template>
        </el-table-column>
        <el-table-column label="事由" prop="reason" />
        <el-table-column label="申请时间" prop="createTime" width="160" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-check"
                  @click="handleAudit(scope.row, '1')"
                >通过</el-button>
                <el-button
                  size="mini"
                  type="text"
                  icon="el-icon-close"
                  style="color: #F56C6C"
                  @click="handleAudit(scope.row, '2')"
                >驳回</el-button>
            </template>
        </el-table-column>
    </el-table>
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
import { listTodoAudit, auditApply } from "@/api/hr/audit";

export default {
  data() {
    return {
      loading: true,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listTodoAudit(this.queryParams).then(res => {
        this.list = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    handleAudit(row, status) {
        const action = status === '1' ? '通过' : '驳回';
        this.$modal.prompt('请输入审批意见（可选）', '确认' + action, {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
        }).then(({ value }) => {
            auditApply({
                applyId: row.applyId,
                status: status,
                auditMsg: value
            }).then(res => {
                this.$modal.msgSuccess("操作成功");
                this.getList();
            });
        }).catch(() => {});
    }
  }
};
</script>

