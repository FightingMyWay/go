<template>
  <div class="application-management">
    <el-card>
      <template #header>
        <span>申请管理</span>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="申请编号">
            <el-input v-model="searchForm.applicationNo" placeholder="请输入申请编号" clearable />
          </el-form-item>
          <el-form-item label="申请人">
            <el-input v-model="searchForm.userName" placeholder="请输入申请人姓名" clearable />
          </el-form-item>
          <el-form-item label="部门">
            <el-select v-model="searchForm.deptId" placeholder="请选择部门" clearable>
              <el-option
                v-for="dept in departments"
                :key="dept.id"
                :label="dept.deptName"
                :value="dept.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
              <el-option label="待审批" value="PENDING" />
              <el-option label="已通过" value="APPROVED" />
              <el-option label="已拒绝" value="REJECTED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 申请列表 -->
      <el-table :data="applicationList" style="width: 100%" v-loading="loading">
        <el-table-column prop="applicationNo" label="申请编号" width="180" />
        <el-table-column prop="userName" label="申请人" width="100" />
        <el-table-column prop="deptName" label="部门" width="120" />
        <el-table-column prop="totalAmount" label="总金额" width="100">
          <template #default="scope">
            ¥{{ scope.row.totalAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="申请时间" width="180" />
        <el-table-column prop="approveTime" label="审批时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="viewApplication(scope.row)">查看</el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              size="small"
              type="success"
              @click="approveApplication(scope.row)"
            >
              通过
            </el-button>
            <el-button
              v-if="scope.row.status === 'PENDING'"
              size="small"
              type="danger"
              @click="rejectApplication(scope.row)"
            >
              拒绝
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          v-model:current-page="pagination.current"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
        />
      </div>
    </el-card>

    <!-- 申请详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="申请详情"
      width="800px"
    >
      <div v-if="currentApplication" class="application-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请编号">
            {{ currentApplication.applicationNo }}
          </el-descriptions-item>
          <el-descriptions-item label="申请人">
            {{ currentApplication.userName }}
          </el-descriptions-item>
          <el-descriptions-item label="部门">
            {{ currentApplication.deptName }}
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">
            {{ currentApplication.createTime }}
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(currentApplication.status)">
              {{ getStatusText(currentApplication.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="总金额">
            ¥{{ currentApplication.totalAmount }}
          </el-descriptions-item>
        </el-descriptions>

        <div class="detail-section">
          <h4>申请明细</h4>
          <el-table :data="currentApplication.details" style="width: 100%">
            <el-table-column prop="itemName" label="用品名称" />
            <el-table-column prop="quantity" label="数量" width="100" />
            <el-table-column prop="price" label="单价" width="100">
              <template #default="scope">
                ¥{{ scope.row.price }}
              </template>
            </el-table-column>
            <el-table-column prop="amount" label="小计" width="100">
              <template #default="scope">
                ¥{{ scope.row.amount }}
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div v-if="currentApplication.status === 'PENDING'" class="approval-section">
          <h4>审批操作</h4>
          <el-form :model="approvalForm" label-width="80px">
            <el-form-item label="审批意见">
              <el-input
                v-model="approvalForm.comment"
                type="textarea"
                :rows="3"
                placeholder="请输入审批意见"
              />
            </el-form-item>
            <el-form-item>
              <el-button type="success" @click="approveApplication(currentApplication)">
                通过申请
              </el-button>
              <el-button type="danger" @click="rejectApplication(currentApplication)">
                拒绝申请
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentApplication = ref(null)

// 搜索表单
const searchForm = reactive({
  applicationNo: '',
  userName: '',
  deptId: '',
  status: ''
})

// 审批表单
const approvalForm = reactive({
  comment: ''
})

// 申请列表
const applicationList = ref([])

// 部门列表
const departments = ref([
  { id: 1, deptName: '技术部' },
  { id: 2, deptName: '人事部' },
  { id: 3, deptName: '财务部' },
  { id: 4, deptName: '行政部' }
])

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 获取状态类型
const getStatusType = (status) => {
  const statusMap = {
    'PENDING': 'warning',
    'APPROVED': 'success',
    'REJECTED': 'danger'
  }
  return statusMap[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const statusMap = {
    'PENDING': '待审批',
    'APPROVED': '已通过',
    'REJECTED': '已拒绝'
  }
  return statusMap[status] || status
}

// 查看申请详情
const viewApplication = (application) => {
  currentApplication.value = application
  detailDialogVisible.value = true
}

// 通过申请
const approveApplication = async (application) => {
  try {
    if (!approvalForm.comment.trim()) {
      ElMessage.warning('请输入审批意见')
      return
    }

    await ElMessageBox.confirm(
      '确定要通过该申请吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API通过申请
    console.log('通过申请:', application, approvalForm.comment)
    ElMessage.success('审批成功')
    detailDialogVisible.value = false
    loadApplicationList()
  } catch (error) {
    // 用户取消
  }
}

// 拒绝申请
const rejectApplication = async (application) => {
  try {
    if (!approvalForm.comment.trim()) {
      ElMessage.warning('请输入审批意见')
      return
    }

    await ElMessageBox.confirm(
      '确定要拒绝该申请吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API拒绝申请
    console.log('拒绝申请:', application, approvalForm.comment)
    ElMessage.success('审批成功')
    detailDialogVisible.value = false
    loadApplicationList()
  } catch (error) {
    // 用户取消
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  loadApplicationList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    applicationNo: '',
    userName: '',
    deptId: '',
    status: ''
  })
  handleSearch()
}

// 分页大小改变
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadApplicationList()
}

// 当前页改变
const handleCurrentChange = (current) => {
  pagination.current = current
  loadApplicationList()
}

// 加载申请列表
const loadApplicationList = async () => {
  loading.value = true
  try {
    // TODO: 调用API加载申请列表
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    applicationList.value = [
      {
        id: 1,
        applicationNo: 'AP20231201001',
        userName: '张三',
        deptName: '技术部',
        totalAmount: 125.5,
        status: 'PENDING',
        createTime: '2023-12-01 09:30:00',
        approveTime: null,
        details: [
          {
            itemName: '签字笔',
            quantity: 10,
            price: 2.5,
            amount: 25.0
          },
          {
            itemName: 'A4纸',
            quantity: 4,
            price: 25.0,
            amount: 100.0
          }
        ]
      },
      {
        id: 2,
        applicationNo: 'AP20231201002',
        userName: '李四',
        deptName: '人事部',
        totalAmount: 50.0,
        status: 'APPROVED',
        createTime: '2023-12-01 10:15:00',
        approveTime: '2023-12-01 14:30:00',
        details: [
          {
            itemName: '文件夹',
            quantity: 5,
            price: 10.0,
            amount: 50.0
          }
        ]
      }
    ]
    
    pagination.total = 2
  } catch (error) {
    console.error('加载申请列表失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadApplicationList()
})
</script>

<style scoped lang="scss">
.application-management {
  .search-bar {
    margin-bottom: 20px;
    
    .search-form {
      .el-form-item {
        margin-bottom: 0;
      }
    }
  }
  
  .pagination-wrapper {
    margin-top: 20px;
    text-align: right;
  }
  
  .application-detail {
    .detail-section {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 15px 0;
        color: var(--el-text-color-primary);
        font-size: 16px;
        font-weight: 600;
      }
    }
    
    .approval-section {
      margin-top: 20px;
      
      h4 {
        margin: 0 0 15px 0;
        color: var(--el-text-color-primary);
        font-size: 16px;
        font-weight: 600;
      }
    }
  }
}
</style>
