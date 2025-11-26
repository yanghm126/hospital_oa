<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="考勤组名" prop="groupName">
        <el-input
          v-model="queryParams.groupName"
          placeholder="请输入考勤组名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['hr:attendanceGroup:add']"
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
          v-hasPermi="['hr:attendanceGroup:edit']"
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
          v-hasPermi="['hr:attendanceGroup:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['hr:attendanceGroup:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="groupList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="考勤组名称" align="center" prop="groupName" />
      <el-table-column label="考勤地址" align="center" prop="address" show-overflow-tooltip />
      <el-table-column label="允许误差(米)" align="center" prop="allowedRadius" />
      <el-table-column label="工作日" align="center" prop="workDays" :formatter="formatWorkDays" show-overflow-tooltip />
      <el-table-column label="适用部门" align="center" prop="deptIds" :formatter="formatDept" show-overflow-tooltip />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hr:attendanceGroup:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hr:attendanceGroup:remove']"
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

    <!-- 添加或修改考勤组对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="600px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="考勤组名称" prop="groupName">
          <el-input v-model="form.groupName" placeholder="请输入考勤组名称" />
        </el-form-item>
        <el-form-item label="打卡范围" prop="allowedRadius">
          <el-input-number v-model="form.allowedRadius" :min="0" :step="50" placeholder="单位：米" />
          <span class="ml10">米</span>
        </el-form-item>
        <el-form-item label="适用部门" prop="deptIds">
          <treeselect v-model="form.deptIds" :options="deptOptions" :show-count="true" multiple placeholder="请选择适用部门" />
        </el-form-item>
        <el-form-item label="工作日" prop="workDays">
          <el-checkbox-group v-model="form.workDays">
            <el-checkbox
              v-for="item in weekOptions"
              :key="item.value"
              :label="item.value"
            >{{item.label}}</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="考勤地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入考勤地址" />
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度" />
            </el-form-item>
          </el-col>
        </el-row>
        <!-- 预留地图选点插槽 -->
        <!-- <div class="map-select-btn">
             <el-button type="primary" plain @click="handleOpenMap">地图选点</el-button>
        </div> -->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listAttendanceGroup, getAttendanceGroup, delAttendanceGroup, addAttendanceGroup, updateAttendanceGroup } from "@/api/hr/attendanceGroup";
import { listDept } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "AttendanceGroup",
  components: { Treeselect },
  data() {
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
      // 考勤组表格数据
      groupList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 部门树选项
      deptOptions: [],
      // 部门名称映射 { id: name }
      deptNameMap: {},
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        groupName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        groupName: [
          { required: true, message: "考勤组名称不能为空", trigger: "blur" }
        ],
        allowedRadius: [
          { required: true, message: "允许打卡范围不能为空", trigger: "blur" }
        ],
        deptIds: [
          { required: true, message: "适用部门不能为空", trigger: "change" }
        ],
        workDays: [
          { required: true, message: "工作日不能为空", trigger: "change" }
        ],
        address: [
          { required: true, message: "考勤地址不能为空", trigger: "blur" }
        ],
        longitude: [
          { required: true, message: "经度不能为空", trigger: "blur" },
          { pattern: /^-?\d+(\.\d+)?$/, message: "请输入正确的经度格式", trigger: "blur" }
        ],
        latitude: [
          { required: true, message: "纬度不能为空", trigger: "blur" },
          { pattern: /^-?\d+(\.\d+)?$/, message: "请输入正确的纬度格式", trigger: "blur" }
        ]
      },
      // 工作日字典
      weekOptions: [
        { value: '1', label: '周一' },
        { value: '2', label: '周二' },
        { value: '3', label: '周三' },
        { value: '4', label: '周四' },
        { value: '5', label: '周五' },
        { value: '6', label: '周六' },
        { value: '7', label: '周日' }
      ]
    };
  },
  created() {
    this.getList();
    this.getDeptTree();
  },
  methods: {
    /** 查询考勤组列表 */
    getList() {
      this.loading = true;
      listAttendanceGroup(this.queryParams).then(response => {
        this.groupList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
        this.flattenDepts(this.deptOptions);
      });
    },
    /** 扁平化部门树用于回显名称 */
    flattenDepts(options) {
      options.forEach(item => {
        this.deptNameMap[item.id] = item.label;
        if (item.children) {
          this.flattenDepts(item.children);
        }
      });
    },
    /** 取消按钮 */
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 表单重置 */
    reset() {
      this.form = {
        id: null,
        groupName: null,
        address: null,
        allowedRadius: 200,
        workDays: [], // 默认为空数组
        deptIds: [],  // 默认为空数组
        longitude: null,
        latitude: null
      };
      this.resetForm("form");
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
    /** 多选框选中数据 */
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加考勤组";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAttendanceGroup(id).then(response => {
        this.form = response.data;
        // 数据转换：字符串转数组
        if (this.form.deptIds) {
           // 注意：treeselect id 可能是数字，如果是字符串分隔，需确认是ID字符串
           this.form.deptIds = this.form.deptIds.split(',').map(Number);
        } else {
           this.form.deptIds = [];
        }
        if (this.form.workDays) {
           this.form.workDays = this.form.workDays.split(',');
        } else {
           this.form.workDays = [];
        }
        this.open = true;
        this.title = "修改考勤组";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 拷贝一份 form 用于提交，避免修改页面显示数据格式
          let submitData = { ...this.form };
          
          // 数据转换：数组转字符串
          if (Array.isArray(submitData.deptIds)) {
            submitData.deptIds = submitData.deptIds.join(',');
          }
          if (Array.isArray(submitData.workDays)) {
             // 排序一下，保证顺序
            submitData.workDays = submitData.workDays.sort().join(',');
          }

          if (this.form.id != null) {
            updateAttendanceGroup(submitData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addAttendanceGroup(submitData).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除考勤组编号为"' + ids + '"的数据项？').then(function() {
        return delAttendanceGroup(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('hr/attendanceGroup/export', {
        ...this.queryParams
      }, `attendanceGroup_${new Date().getTime()}.xlsx`)
    },
    /** 格式化工作日 */
    formatWorkDays(row, column) {
      if (!row.workDays) return '';
      
      // 排序确保顺序
      const days = row.workDays.split(',').sort();
      const daysStr = days.join(',');

      if (daysStr === '1,2,3,4,5') {
        return '周一至周五';
      }
      if (daysStr === '1,2,3,4,5,6') {
        return '周一至周六';
      }
      if (daysStr === '1,2,3,4,5,6,7') {
        return '全周';
      }

      const labels = days.map(val => {
        const option = this.weekOptions.find(o => o.value === val);
        return option ? option.label : val;
      });
      return labels.join(', ');
    },
    /** 格式化部门 */
    formatDept(row, column) {
      if (!row.deptIds) return '';
      return row.deptIds.split(',')
        .map(id => this.deptNameMap[Number(id)] || id)
        .join(', ');
    }
  }
};
</script>

<style scoped>
.ml10 {
  margin-left: 10px;
}
</style>
