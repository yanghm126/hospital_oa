<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="班次名称" prop="shiftName">
        <el-input
          v-model="queryParams.shiftName"
          placeholder="请输入班次名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="班次状态" clearable>
          <el-option label="正常" value="0" />
          <el-option label="停用" value="1" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['hr:shift:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['hr:shift:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['hr:shift:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hr:shift:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="shiftList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="班次名称" align="center" prop="shiftName" min-width="100" />
      <el-table-column label="上班时间" align="center" prop="startTime" width="100">
        <template slot-scope="scope">
          {{ formatTime(scope.row.startTime) }}
        </template>
      </el-table-column>
      <el-table-column label="下班时间" align="center" prop="endTime" width="100">
        <template slot-scope="scope">
          {{ formatTime(scope.row.endTime) }}
        </template>
      </el-table-column>
      <el-table-column label="跨天" align="center" prop="crossDay" width="70">
        <template slot-scope="scope">
          <el-tag type="warning" v-if="scope.row.crossDay === 1">是</el-tag>
          <el-tag v-else type="info">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="迟到容忍" align="center" prop="lateMinutes" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.lateMinutes">{{ scope.row.lateMinutes }}分钟</span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column label="早退容忍" align="center" prop="earlyLeaveMinutes" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.earlyLeaveMinutes">{{ scope.row.earlyLeaveMinutes }}分钟</span>
          <span v-else class="text-muted">-</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status" width="80">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.status === '0'">正常</el-tag>
          <el-tag type="danger" v-else>停用</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="150">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hr:shift:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hr:shift:remove']"
          >删除</el-button>
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

    <!-- 添加或修改班次对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="550px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="班次名称" prop="shiftName">
          <el-input v-model="form.shiftName" placeholder="请输入班次名称" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="上班时间" prop="startTime">
              <el-time-picker
                v-model="form.startTime"
                value-format="HH:mm:ss"
                format="HH:mm"
                placeholder="选择上班时间"
                style="width: 100%">
              </el-time-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="下班时间" prop="endTime">
              <el-time-picker
                v-model="form.endTime"
                value-format="HH:mm:ss"
                format="HH:mm"
                placeholder="选择下班时间"
                style="width: 100%">
              </el-time-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="是否跨天" prop="crossDay">
          <el-radio-group v-model="form.crossDay">
            <el-radio :label="1">是（下班时间为次日）</el-radio>
            <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="迟到容忍" prop="lateMinutes">
              <el-input-number 
                v-model="form.lateMinutes" 
                :min="0" 
                :max="60" 
                placeholder="分钟"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="早退容忍" prop="earlyLeaveMinutes">
              <el-input-number 
                v-model="form.earlyLeaveMinutes" 
                :min="0" 
                :max="60" 
                placeholder="分钟"
                style="width: 100%"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">正常</el-radio>
            <el-radio label="1">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入备注内容" :rows="2" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
// {{RIPER-5:
//   Action: "Modified"
//   Task_ID: "9b0b86f2-b34c-474e-bd1a-0925fd534a0d"
//   Timestamp: "2025-11-26"
//   Authoring_Role: "LD"
//   Principle_Applied: "数据一致性 & 功能完善"
//   Quality_Check: "修复crossDay数据类型，添加容忍时长和状态字段"
// }}
import { listShift, getShift, delShift, addShift, updateShift } from "@/api/hr/shift";

export default {
  name: "Shift",
  data() {
    const validateEndTime = (rule, value, callback) => {
      if (!value) {
        callback(new Error("下班时间不能为空"));
      } else {
        // 如果不是跨天，结束时间必须大于开始时间
        if (this.form.crossDay === 0 && this.form.startTime && value <= this.form.startTime) {
          callback(new Error("非跨天班次，下班时间必须晚于上班时间"));
        } else {
          callback();
        }
      }
    };
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 班次表格数据
      shiftList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        shiftName: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        shiftName: [
          { required: true, message: "班次名称不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "上班时间不能为空", trigger: "change" }
        ],
        endTime: [
          { required: true, validator: validateEndTime, trigger: "change" }
        ],
        crossDay: [
          { required: true, message: "请选择是否跨天", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询班次列表 */
    getList() {
      this.loading = true;
      listShift(this.queryParams).then(response => {
        this.shiftList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        shiftId: null,
        shiftName: null,
        startTime: null,
        endTime: null,
        crossDay: 0,
        lateMinutes: 0,
        earlyLeaveMinutes: 0,
        status: '0',
        remark: null
      };
      this.resetForm("form");
    },
    /** 格式化时间显示 */
    formatTime(time) {
      if (!time) return '-';
      // 如果是 HH:mm:ss 格式，只取 HH:mm
      if (time.length > 5) {
        return time.substring(0, 5);
      }
      return time;
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.shiftId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加班次";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const shiftId = row.shiftId || this.ids
      getShift(shiftId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改班次";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.shiftId != null) {
            updateShift(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addShift(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const shiftIds = row.shiftId || this.ids;
      this.$modal.confirm('是否确认删除班次编号为"' + shiftIds + '"的数据项？').then(function() {
        return delShift(shiftIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('hr/shift/export', {
        ...this.queryParams
      }, `shift_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>

<style scoped>
.text-muted {
  color: #909399;
}
</style>
