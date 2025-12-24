<template>
  <div class="admin-dashboard">
    <el-row :gutter="20" class="dashboard-header">
      <el-col :span="24">
        <h1>管理员仪表板</h1>
        <p>欢迎回来，{{ userStore.userInfo?.realName || '管理员' }}</p>
      </el-col>
    </el-row>

    <!-- 统计卡片 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><User /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalUsers }}</div>
              <div class="stat-label">总用户数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Box /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.totalSupplies }}</div>
              <div class="stat-label">用品总数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.pendingApplications }}</div>
              <div class="stat-label">待审批申请</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon">
              <el-icon><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stats.lowStockItems }}</div>
              <div class="stat-label">库存不足</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 快速操作 -->
    <el-row :gutter="20" class="quick-actions">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>快速操作</span>
          </template>
          <div class="action-buttons">
            <el-button type="primary" @click="$router.push('/admin/supply-management')">
              <el-icon><Plus /></el-icon>
              添加用品
            </el-button>
            <el-button type="success" @click="$router.push('/admin/application-management')">
              <el-icon><Check /></el-icon>
              审批申请
            </el-button>
            <el-button type="warning" @click="$router.push('/admin/stock-management')">
              <el-icon><Box /></el-icon>
              库存管理
            </el-button>
            <el-button type="info" @click="$router.push('/admin/user-management')">
              <el-icon><User /></el-icon>
              用户管理
            </el-button>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 最近申请 -->
    <el-row :gutter="20" class="recent-applications">
      <el-col :span="24">
        <el-card>
          <template #header>
            <span>最近申请</span>
          </template>
          <el-table :data="recentApplications" style="width: 100%">
            <el-table-column prop="applicationNo" label="申请编号" width="180" />
            <el-table-column prop="userName" label="申请人" width="120" />
            <el-table-column prop="deptName" label="部门" width="120" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getStatusType(scope.row.status)">
                  {{ getStatusText(scope.row.status) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="申请时间" width="180" />
            <el-table-column label="操作" width="120">
              <template #default="scope">
                <el-button size="small" @click="viewApplication(scope.row)">
                  查看
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { User, Box, Document, Warning, Plus, Check } from '@element-plus/icons-vue'

const userStore = useUserStore()

// 统计数据
const stats = ref({
  totalUsers: 0,
  totalSupplies: 0,
  pendingApplications: 0,
  lowStockItems: 0
})

// 最近申请
const recentApplications = ref([])

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
  // TODO: 实现查看申请详情的逻辑
  console.log('查看申请:', application)
}

// 加载数据
const loadData = () => {
  // TODO: 从API加载实际数据
  stats.value = {
    totalUsers: 156,
    totalSupplies: 89,
    pendingApplications: 23,
    lowStockItems: 7
  }
  
  recentApplications.value = [
    {
      id: 1,
      applicationNo: 'AP20231201001',
      userName: '张三',
      deptName: '技术部',
      status: 'PENDING',
      createTime: '2023-12-01 09:30:00'
    },
    {
      id: 2,
      applicationNo: 'AP20231201002',
      userName: '李四',
      deptName: '人事部',
      status: 'APPROVED',
      createTime: '2023-12-01 10:15:00'
    },
    {
      id: 3,
      applicationNo: 'AP20231201003',
      userName: '王五',
      deptName: '财务部',
      status: 'PENDING',
      createTime: '2023-12-01 11:00:00'
    }
  ]
}

onMounted(() => {
  loadData()
})
</script>

<style scoped lang="scss">
.admin-dashboard {
  padding: 20px;
  
  .dashboard-header {
    margin-bottom: 30px;
    
    h1 {
      margin: 0 0 10px 0;
      color: var(--el-text-color-primary);
      font-size: 28px;
      font-weight: 600;
    }
    
    p {
      margin: 0;
      color: var(--el-text-color-regular);
      font-size: 16px;
    }
  }
  
  .stats-row {
    margin-bottom: 30px;
    
    .stat-card {
      .stat-content {
        display: flex;
        align-items: center;
        
        .stat-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
          background: linear-gradient(135deg, var(--el-color-primary), var(--el-color-primary-light-3));
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 20px;
          
          .el-icon {
            font-size: 28px;
            color: white;
          }
        }
        
        .stat-info {
          .stat-number {
            font-size: 32px;
            font-weight: 600;
            color: var(--el-text-color-primary);
            line-height: 1;
            margin-bottom: 8px;
          }
          
          .stat-label {
            font-size: 14px;
            color: var(--el-text-color-regular);
          }
        }
      }
    }
  }
  
  .quick-actions {
    margin-bottom: 30px;
    
    .action-buttons {
      display: flex;
      gap: 15px;
      flex-wrap: wrap;
      
      .el-button {
        display: flex;
        align-items: center;
        gap: 8px;
        padding: 12px 20px;
        font-size: 14px;
      }
    }
  }
  
  .recent-applications {
    .el-table {
      .el-button {
        padding: 6px 12px;
        font-size: 12px;
      }
    }
  }
}
</style>
