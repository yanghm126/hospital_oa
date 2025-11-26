<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="员工姓名" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入员工姓名"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="工号" prop="empNo">
        <el-input
          v-model="queryParams.empNo"
          placeholder="请输入工号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="手机号码" prop="phonenumber">
        <el-input
          v-model="queryParams.phonenumber"
          placeholder="请输入手机号码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="员工状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择员工状态" clearable>
          <el-option label="在编" value="0" />
          <el-option label="退休" value="1" />
          <el-option label="离职" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-button
        type="primary"
        plain
        icon="el-icon-plus"
        size="mini"
        @click="handleAdd"
        v-hasPermi="['hr:employee:add']"
      >新增</el-button>
      <el-button
        type="success"
        plain
        icon="el-icon-edit"
        size="mini"
        :disabled="single"
        @click="handleUpdate"
        v-hasPermi="['hr:employee:edit']"
      >修改</el-button>
      <el-button
        type="danger"
        plain
        icon="el-icon-delete"
        size="mini"
        :disabled="multiple"
        @click="handleDelete"
        v-hasPermi="['hr:employee:remove']"
      >删除</el-button>
      <el-button
        type="warning"
        plain
        icon="el-icon-download"
        size="mini"
        @click="handleExport"
        v-hasPermi="['hr:employee:export']"
      >导出</el-button>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="employeeList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="员工姓名" align="center" prop="sysUser.nickName" />
      <el-table-column label="工号" align="center" prop="empNo" />
      <el-table-column label="所属科室" align="center" prop="sysUser.dept.deptName" />
      <el-table-column label="手机号码" align="center" prop="sysUser.phonenumber" />
      <el-table-column label="员工状态" align="center" prop="status">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === '0'" type="success">在编</el-tag>
          <el-tag v-else-if="scope.row.status === '1'" type="info">退休</el-tag>
          <el-tag v-else type="danger">离职</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="编制类型" align="center" prop="empType" />
      <el-table-column label="入职日期" align="center" prop="hireDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.hireDate, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['hr:employee:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['hr:employee:remove']"
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

    <!-- 添加或修改员工档案对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        
        <el-divider content-position="center">账号基础信息</el-divider>
        <el-row>
            <el-col :span="12">
                <el-form-item label="归属部门" prop="sysUser.deptId">
                   <treeselect v-model="form.sysUser.deptId" :options="deptOptions" :show-count="true" placeholder="请选择归属部门" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="员工姓名" prop="sysUser.nickName">
                    <el-input v-model="form.sysUser.nickName" placeholder="请输入员工姓名" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
            <el-col :span="12">
                <el-form-item label="手机号码" prop="sysUser.phonenumber">
                    <el-input v-model="form.sysUser.phonenumber" placeholder="请输入手机号码" maxlength="11" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="性别" prop="sysUser.sex">
                    <el-select v-model="form.sysUser.sex" placeholder="请选择性别">
                        <el-option
                          v-for="dict in dict.type.sys_user_sex"
                          :key="dict.value"
                          :label="dict.label"
                          :value="dict.value"
                        ></el-option>
                    </el-select>
                </el-form-item>
            </el-col>
        </el-row>

        <el-divider content-position="center">档案详细信息</el-divider>
        <el-row>
            <el-col :span="12">
                <el-form-item label="工号" prop="empNo">
                    <el-input v-model="form.empNo" placeholder="请输入工号" />
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="编制类型" prop="empType">
                    <el-input v-model="form.empType" placeholder="请输入编制类型" />
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
             <el-col :span="12">
                 <el-form-item label="员工状态" prop="status">
                    <el-select v-model="form.status" placeholder="请选择员工状态">
                        <el-option label="在编" value="0" />
                        <el-option label="退休" value="1" />
                        <el-option label="离职" value="2" />
                    </el-select>
                </el-form-item>
            </el-col>
            <el-col :span="12">
                <el-form-item label="入职日期" prop="hireDate">
                    <el-date-picker clearable
                        v-model="form.hireDate"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择入职日期">
                    </el-date-picker>
                </el-form-item>
            </el-col>
        </el-row>
        <el-row>
             <el-col :span="12">
                <el-form-item label="紧急联系人" prop="emergencyContact">
                    <el-input v-model="form.emergencyContact" placeholder="请输入紧急联系人" />
                </el-form-item>
             </el-col>
        </el-row>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listEmployee, getEmployee, delEmployee, addEmployee, updateEmployee, exportEmployee } from "@/api/hr/employee";
import { listDept } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Employee",
  dicts: ['sys_user_sex'],
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
      // 员工档案表格数据
      employeeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 部门树选项
      deptOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        empNo: null,
        nickName: null,
        phonenumber: null,
        status: null
      },
      // 表单参数
      form: {
          sysUser: {}
      },
      // 表单校验
      rules: {
        empNo: [
          { required: true, message: "工号不能为空", trigger: "blur" }
        ],
        "sysUser.nickName": [
             { required: true, message: "员工姓名不能为空", trigger: "blur" }
        ],
        "sysUser.phonenumber": [
             { required: true, message: "手机号码不能为空", trigger: "blur" },
             { pattern: /^1[3|4|5|6|7|8|9][0-9]\d{8}$/, message: "请输入正确的手机号码", trigger: "blur" }
        ],
        "sysUser.deptId": [
             { required: true, message: "归属部门不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.getDeptTree();
  },
  methods: {
    /** 查询员工档案列表 */
    getList() {
      this.loading = true;
      listEmployee(this.queryParams).then(response => {
        this.employeeList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询部门下拉树结构 */
    getDeptTree() {
      listDept().then(response => {
        this.deptOptions = this.handleTree(response.data, "deptId");
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
        employeeId: null,
        userId: null,
        empNo: null,
        status: "0",
        empType: null,
        hireDate: null,
        emergencyContact: null,
        sysUser: {
            deptId: null,
            nickName: null,
            phonenumber: null,
            sex: null
        }
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
      this.ids = selection.map(item => item.employeeId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.getDeptTree();
      this.open = true;
      this.title = "添加员工档案";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getDeptTree();
      const employeeId = row.employeeId || this.ids
      getEmployee(employeeId).then(response => {
        this.form = response.data;
        if (!this.form.sysUser) {
            this.form.sysUser = {}; 
        }
        this.open = true;
        this.title = "修改员工档案";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.employeeId != null) {
            updateEmployee(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addEmployee(this.form).then(response => {
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
      const employeeIds = row.employeeId || this.ids;
      this.$modal.confirm('是否确认删除员工档案编号为"' + employeeIds + '"的数据项？').then(function() {
        return delEmployee(employeeIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('hr/employee/export', {
        ...this.queryParams
      }, `employee_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
