<template>
  <div class="app-container">
    <!-- 顶部工具栏 -->
    <el-form :inline="true" size="small">
      <el-form-item label="排班月份">
        <el-date-picker
          v-model="queryMonth"
          type="month"
          value-format="yyyy-MM"
          placeholder="选择月份"
          :clearable="false"
          @change="handleMonthChange"
        />
      </el-form-item>
      <el-form-item label="科室">
        <treeselect v-model="deptId" :options="deptOptions" :show-count="true" placeholder="选择科室" style="width: 200px" @select="handleDeptChange" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="fetchData">查询</el-button>
        <el-button type="success" icon="el-icon-check" @click="handleSave">保存排班</el-button>
        <el-button type="warning" icon="el-icon-magic-stick" @click="autoSchedule">自动排班(模拟)</el-button>
      </el-form-item>
    </el-form>

    <!-- 排班表格 -->
    <el-table
      v-loading="loading"
      :data="matrixData"
      border
      style="width: 100%"
      height="calc(100vh - 200px)"
    >
      <!-- 固定列：员工姓名 -->
      <el-table-column
        prop="userName"
        label="员工姓名"
        width="100"
        fixed
        align="center"
      >
         <template slot-scope="scope">
             <div class="user-cell" @click="handleBatchFill(scope.row)">
                 {{ scope.row.userName }}
                 <i class="el-icon-edit-outline" style="color: #409EFF; font-size: 12px"></i>
             </div>
         </template>
      </el-table-column>

      <!-- 动态列：日期 -->
      <el-table-column
        v-for="(day, index) in daysInMonth"
        :key="day.date"
        :label="day.label"
        width="85"
        align="center"
      >
        <!-- 自定义表头：显示 星期几 -->
        <template slot="header">
           <div>{{ day.dayNum }}</div>
           <div style="font-size: 12px; font-weight: normal">{{ day.week }}</div>
        </template>
        
        <template slot-scope="scope">
          <el-select 
            v-model="scope.row.scheduleMap[day.dayNum]" 
            size="mini" 
            placeholder="-" 
            :class="getCellClass(scope.row.scheduleMap[day.dayNum])"
          >
            <el-option
              v-for="shift in shiftOptions"
              :key="shift.shiftId"
              :label="shift.shiftName"
              :value="shift.shiftId"
            />
          </el-select>
        </template>
      </el-table-column>
    </el-table>
    
    <!-- 批量填充弹窗 -->
    <el-dialog title="批量填充" :visible.sync="fillDialogVisible" width="400px">
        <el-form label-width="80px">
            <el-form-item label="选择班次">
                 <el-select v-model="fillShiftId" placeholder="请选择">
                    <el-option
                      v-for="shift in shiftOptions"
                      :key="shift.shiftId"
                      :label="shift.shiftName"
                      :value="shift.shiftId"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="填充范围">
                <el-radio-group v-model="fillScope">
                    <el-radio label="empty">仅填充空缺</el-radio>
                    <el-radio label="all">覆盖所有日期</el-radio>
                </el-radio-group>
            </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
            <el-button @click="fillDialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="confirmBatchFill">确 定</el-button>
        </div>
    </el-dialog>

  </div>
</template>

<script>
import { getScheduleMatrix, batchSaveSchedule, listShift } from "@/api/hr/schedule";
import { treeselect } from "@/api/system/dept";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Schedule",
  components: { Treeselect },
  data() {
    return {
      loading: false,
      queryMonth: '', // yyyy-MM
      deptId: null,
      deptOptions: [],
      
      // 班次选项
      shiftOptions: [],
      
      // 矩阵数据
      matrixData: [],
      
      // 动态表头
      daysInMonth: [],
      
      // 批量填充
      fillDialogVisible: false,
      currentRow: null,
      fillShiftId: null,
      fillScope: 'empty'
    };
  },
  created() {
    // 默认下个月
    const now = new Date();
    now.setMonth(now.getMonth() + 1);
    this.queryMonth = this.formatDate(now, 'yyyy-MM');
    
    this.getDeptTree();
    this.getShiftList();
  },
  mounted() {
     // Init columns for default month
     this.generateDays();
  },
  methods: {
    // 1. 初始化基础数据
    getDeptTree() {
      treeselect().then(response => {
        this.deptOptions = response.data;
      });
    },
    getShiftList() {
      listShift().then(res => {
        // 假设后端返回rows或data
        this.shiftOptions = res.rows || res.data || [];
      });
    },
    
    // 2. 生成当月日期列
    generateDays() {
      if (!this.queryMonth) return;
      const [year, month] = this.queryMonth.split('-').map(Number);
      const daysCount = new Date(year, month, 0).getDate();
      
      const days = [];
      const weeks = ['日', '一', '二', '三', '四', '五', '六'];
      
      for (let i = 1; i <= daysCount; i++) {
        const date = new Date(year, month - 1, i);
        const dayNum = String(i).padStart(2, '0');
        days.push({
          dayNum: dayNum,
          week: weeks[date.getDay()],
          label: `${i}号`
        });
      }
      this.daysInMonth = days;
    },
    
    // 3. 监听月份变化
    handleMonthChange() {
      this.generateDays();
      this.fetchData();
    },
    handleDeptChange() {
      this.fetchData();
    },
    
    // 4. 查询矩阵
    fetchData() {
      if (!this.deptId) return;
      this.loading = true;
      getScheduleMatrix({ deptId: this.deptId, month: this.queryMonth }).then(res => {
        this.matrixData = res.data;
        this.loading = false;
      });
    },
    
    // 5. 样式处理
    getCellClass(shiftId) {
      // 这里可以根据 shiftId 查找 shiftOptions 来决定颜色
      // 简单示例：假设 shiftId=1 是休(灰色)，2是晚班(红色)
      // 实际应遍历 shiftOptions 获取
      // TODO: 完善样式逻辑
      return '';
    },
    
    // 6. 批量填充交互
    handleBatchFill(row) {
        this.currentRow = row;
        this.fillShiftId = null;
        this.fillScope = 'empty';
        this.fillDialogVisible = true;
    },
    confirmBatchFill() {
        if (!this.fillShiftId) {
            this.$modal.msgError("请选择班次");
            return;
        }
        
        const row = this.currentRow;
        this.daysInMonth.forEach(day => {
            const currentVal = row.scheduleMap[day.dayNum];
            if (this.fillScope === 'all' || !currentVal) {
                // Vue2 响应式陷阱：Map可能需要 $set，但这里直接赋值看是否生效
                // 如果 row.scheduleMap 是普通对象，直接赋值即可
                this.$set(row.scheduleMap, day.dayNum, this.fillShiftId);
            }
        });
        
        this.fillDialogVisible = false;
        this.$modal.msgSuccess("填充完成，记得点击保存生效");
    },
    
    // 7. 保存
    handleSave() {
      if (!this.matrixData.length) return;
      
      // 将矩阵转换为 List<HrSchedule>
      const list = [];
      this.matrixData.forEach(row => {
        for (const [day, shiftId] of Object.entries(row.scheduleMap)) {
          if (shiftId) {
            list.push({
              userId: row.userId,
              workDate: `${this.queryMonth}-${day}`,
              shiftId: shiftId,
              deptId: this.deptId // 冗余存储方便查询
            });
          }
        }
      });
      
      if (list.length === 0) {
          this.$modal.msgWarning("没有排班数据");
          return;
      }
      
      this.loading = true;
      batchSaveSchedule(list).then(res => {
        this.$modal.msgSuccess("保存成功");
        this.loading = false;
        this.fetchData(); // 刷新
      });
    },
    
    autoSchedule() {
        this.$modal.msgSuccess("自动排班算法开发中...");
    },
    
    formatDate(date, fmt) {
        // 简易实现
        const o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate()
        };
        if (/(y+)/.test(fmt))
            fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        for (const k in o)
            if (new RegExp("(" + k + ")").test(fmt))
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
        return fmt;
    }
  }
};
</script>

<style scoped>
.user-cell {
    cursor: pointer;
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 5px;
}
.user-cell:hover {
    color: #409EFF;
}
/* 深度选择器修改select样式使其更紧凑 */
::v-deep .el-table .cell {
    padding-left: 2px;
    padding-right: 2px;
}
::v-deep .el-input__inner {
    padding: 0 5px;
    text-align: center;
}
</style>
