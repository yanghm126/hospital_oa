<template>
  <div class="app-container">
    <el-form :inline="true" size="small">
        <el-form-item label="状态">
            <el-select v-model="queryParams.status" placeholder="全部" clearable>
                <el-option label="待审" value="0" />
                <el-option label="通过" value="1" />
                <el-option label="驳回" value="2" />
            </el-select>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="getList">查询</el-button>
            <el-button type="success" icon="el-icon-plus" @click="handleAdd">发起申请</el-button>
        </el-form-item>
    </el-form>

    <el-table v-loading="loading" :data="list">
        <el-table-column label="申请ID" prop="applyId" width="80" />
        <el-table-column label="类型" prop="applyType" width="100">
            <template slot-scope="scope">
                <el-tag v-if="scope.row.applyType === '1'">请假</el-tag>
                <el-tag v-else type="warning">补卡</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="开始时间" prop="startTime" width="160" />
        <el-table-column label="结束时间" prop="endTime" width="160" />
        <el-table-column label="事由" prop="reason" />
        <el-table-column label="状态" prop="status" width="100">
             <template slot-scope="scope">
                <el-tag v-if="scope.row.status === '0'" type="info">待审</el-tag>
                <el-tag v-else-if="scope.row.status === '1'" type="success">通过</el-tag>
                <el-tag v-else type="danger">驳回</el-tag>
            </template>
        </el-table-column>
        <el-table-column label="审批意见" prop="auditMsg" />
        <el-table-column label="申请时间" prop="createTime" width="160" />
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 申请弹窗 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="类型" prop="applyType">
                <el-radio-group v-model="form.applyType">
                    <el-radio label="1">请假</el-radio>
                    <el-radio label="2">补卡</el-radio>
                </el-radio-group>
            </el-form-item>
            <el-form-item label="开始时间" prop="startTime">
                <el-date-picker v-model="form.startTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择开始时间" style="width: 100%" />
            </el-form-item>
            <el-form-item label="结束时间" prop="endTime" v-if="form.applyType === '1'">
                <el-date-picker v-model="form.endTime" type="datetime" value-format="yyyy-MM-dd HH:mm:ss" placeholder="选择结束时间" style="width: 100%" />
            </el-form-item>
             <el-form-item label="事由" prop="reason">
                <el-input v-model="form.reason" type="textarea" placeholder="请输入申请原因" />
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="open = false">取 消</el-button>
        </div>
    </el-dialog>
  </div>
</template>

<script>
import { listMyApply, submitApply } from "@/api/hr/audit";

export default {
  data() {
    return {
      loading: true,
      list: [],
      total: 0,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null
      },
      open: false,
      title: "",
      form: {},
      rules: {
          applyType: [{ required: true, message: "请选择类型", trigger: "change" }],
          startTime: [{ required: true, message: "请选择时间", trigger: "blur" }],
          reason: [{ required: true, message: "请输入事由", trigger: "blur" }]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      listMyApply(this.queryParams).then(res => {
        this.list = res.rows;
        this.total = res.total;
        this.loading = false;
      });
    },
    handleAdd() {
        this.reset();
        this.open = true;
        this.title = "发起申请";
    },
    reset() {
        this.form = {
            applyType: "1",
            startTime: null,
            endTime: null,
            reason: null
        };
    },
    submitForm() {
        this.$refs["form"].validate(valid => {
            if (valid) {
                if (this.form.applyType === '2') {
                    // 补卡结束时间等于开始时间
                    this.form.endTime = this.form.startTime;
                }
                submitApply(this.form).then(res => {
                    this.$modal.msgSuccess("提交成功，请等待审批");
                    this.open = false;
                    this.getList();
                });
            }
        });
    }
  }
};
</script>

