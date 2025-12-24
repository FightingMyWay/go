<template>
  <div class="user-dashboard">
    <!-- 欢迎区域 -->
    <div class="welcome-section">
      <div class="welcome-content">
        <h1 class="welcome-title">欢迎回来，{{ userStore.userInfo.realName || userStore.userInfo.username }}！</h1>
        <p class="welcome-subtitle">今天是 {{ currentDate }}，祝您工作愉快！</p>
      </div>
      <div class="welcome-actions">
        <el-button type="primary" size="large" @click="$router.push('/user/application')">
          <el-icon><DocumentAdd /></el-icon>
          申请用品
        </el-button>
        <el-button size="large" @click="$router.push('/user/supply-list')">
          <el-icon><Goods /></el-icon>
          浏览用品
        </el-button>
      </div>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stats-section">
      <el-row :gutter="20">
        <el-col :span="6" :xs="12" :sm="6">
          <div class="stat-card">
            <div class="stat-icon">
              <el-icon><Document /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.totalApplications }}</div>
              <div class="stat-label">总申请数</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6" :xs="12" :sm="6">
          <div class="stat-card">
            <div class="stat-icon pending">
              <el-icon><Clock /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.pendingApplications }}</div>
              <div class="stat-label">待审批</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6" :xs="12" :sm="6">
          <div class="stat-card">
            <div class="stat-icon approved">
              <el-icon><CircleCheck /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.approvedApplications }}</div>
              <div class="stat-label">已通过</div>
            </div>
          </div>
        </el-col>
        
        <el-col :span="6" :xs="12" :sm="6">
          <div class="stat-card">
            <div class="stat-icon rejected">
              <el-icon><CircleClose /></el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ stats.rejectedApplications }}</div>
              <div class="stat-label">已拒绝</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>
    
    <!-- 快捷操作 -->
    <div class="quick-actions-section">
      <div class="section-header">
        <h2 class="section-title">快捷操作</h2>
      </div>
      <div class="quick-actions">
        <div class="action-item" @click="$router.push('/user/application')">
          <div class="action-icon">
            <el-icon><DocumentAdd /></el-icon>
          </div>
          <div class="action-text">申请用品</div>
        </div>
        
        <div class="action-item" @click="$router.push('/user/supply-list')">
          <div class="action-icon">
            <el-icon><Search /></el-icon>
          </div>
          <div class="action-text">搜索用品</div>
        </div>
        
        <div class="action-item" @click="$router.push('/user/my-applications')">
          <div class="action-icon">
            <el-icon><Document /></el-icon>
          </div>
          <div class="action-text">我的申请</div>
        </div>
        
        <div class="action-item" @click="$router.push('/user/profile')">
          <div class="action-icon">
            <el-icon><User /></el-icon>
          </div>
          <div class="action-text">个人资料</div>
        </div>
      </div>
    </div>
    
    <!-- 最近申请 -->
    <div class="recent-applications-section">
      <div class="section-header">
        <h2 class="section-title">最近申请</h2>
        <el-button type="text" @click="$router.push('/user/my-applications')">
          查看全部
          <el-icon><ArrowRight /></el-icon>
        </el-button>
      </div>
      
      <div class="applications-list">
        <div
          v-for="application in recentApplications"
          :key="application.id"
          class="application-item"
        >
          <div class="application-info">
            <div class="application-title">{{ application.applicationNo }}</div>
            <div class="application-desc">
              申请了 {{ application.itemCount }} 件用品
            </div>
            <div class="application-time">{{ formatDate(application.createTime) }}</div>
          </div>
          
          <div class="application-status">
            <el-tag
              :type="getStatusType(application.status)"
              size="small"
            >
              {{ getStatusText(application.status) }}
            </el-tag>
          </div>
        </div>
        
        <div v-if="recentApplications.length === 0" class="empty-state">
          <el-empty description="暂无申请记录" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 当前日期
const currentDate = computed(() => {
  const now = new Date()
  return now.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
})

// 统计数据
const stats = ref({
  totalApplications: 0,
  pendingApplications: 0,
  approvedApplications: 0,
  rejectedApplications: 0
})

// 最近申请
const recentApplications = ref([])

// 获取统计数据
const getStats = () => {
  // 模拟数据，实际应该从API获取
  stats.value = {
    totalApplications: 15,
    pendingApplications: 3,
    approvedApplications: 10,
    rejectedApplications: 2
  }
}

// 获取最近申请
const getRecentApplications = () => {
  // 模拟数据，实际应该从API获取
  recentApplications.value = [
    {
      id: 1,
      applicationNo: 'AP20241201001',
      itemCount: 3,
      status: 'PENDING',
      createTime: '2024-12-01 10:30:00'
    },
    {
      id: 2,
      applicationNo: 'AP20241130001',
      itemCount: 2,
      status: 'APPROVED',
      createTime: '2024-11-30 14:20:00'
    },
    {
      id: 3,
      applicationNo: 'AP20241129001',
      itemCount: 1,
      status: 'REJECTED',
      createTime: '2024-11-29 09:15:00'
    }
  ]
}

// 格式化日期
const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    month: 'short',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit'
  })
}

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
  return statusMap[status] || '未知'
}

onMounted(() => {
  getStats()
  getRecentApplications()
})
</script>

<style scoped lang="scss">
.user-dashboard {
  .welcome-section {
    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
    border-radius: 16px;
    padding: 30px;
    margin-bottom: 24px;
    color: #fff;
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .welcome-content {
      .welcome-title {
        font-size: 24px;
        font-weight: 600;
        margin: 0 0 8px 0;
      }
      
      .welcome-subtitle {
        font-size: 14px;
        margin: 0;
        opacity: 0.9;
      }
    }
    
    .welcome-actions {
      display: flex;
      gap: 12px;
      
      .el-button {
        border: 1px solid rgba(255, 255, 255, 0.3);
        background: rgba(255, 255, 255, 0.1);
        color: #fff;
        
        &:hover {
          background: rgba(255, 255, 255, 0.2);
          border-color: rgba(255, 255, 255, 0.5);
        }
      }
    }
  }
  
  .stats-section {
    margin-bottom: 24px;
    
    .stat-card {
      background: #fff;
      border-radius: 12px;
      padding: 24px;
      display: flex;
      align-items: center;
      gap: 16px;
      box-shadow: var(--shadow-light);
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-4px);
        box-shadow: var(--shadow-dark);
      }
      
      .stat-icon {
        width: 60px;
        height: 60px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        background: var(--primary-color);
        color: #fff;
        font-size: 24px;
        
        &.pending {
          background: var(--warning-color);
        }
        
        &.approved {
          background: var(--success-color);
        }
        
        &.rejected {
          background: var(--danger-color);
        }
      }
      
      .stat-content {
        .stat-number {
          font-size: 28px;
          font-weight: 700;
          color: var(--text-color-primary);
          line-height: 1;
          margin-bottom: 4px;
        }
        
        .stat-label {
          font-size: 14px;
          color: var(--text-color-secondary);
        }
      }
    }
  }
  
  .quick-actions-section,
  .recent-applications-section {
    background: #fff;
    border-radius: 12px;
    padding: 24px;
    margin-bottom: 24px;
    box-shadow: var(--shadow-light);
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;
      
      .section-title {
        font-size: 18px;
        font-weight: 600;
        color: var(--text-color-primary);
        margin: 0;
      }
    }
  }
  
  .quick-actions {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(120px, 1fr));
    gap: 16px;
    
    .action-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      padding: 20px;
      border-radius: 12px;
      cursor: pointer;
      transition: all 0.3s ease;
      border: 2px solid transparent;
      
      &:hover {
        background: var(--background-color-base);
        border-color: var(--primary-color);
        transform: translateY(-2px);
      }
      
      .action-icon {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        background: var(--primary-color);
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        margin-bottom: 12px;
      }
      
      .action-text {
        font-size: 14px;
        color: var(--text-color-primary);
        font-weight: 500;
        text-align: center;
      }
    }
  }
  
  .applications-list {
    .application-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 16px 0;
      border-bottom: 1px solid var(--border-color-lighter);
      
      &:last-child {
        border-bottom: none;
      }
      
      .application-info {
        .application-title {
          font-size: 14px;
          font-weight: 500;
          color: var(--text-color-primary);
          margin-bottom: 4px;
        }
        
        .application-desc {
          font-size: 12px;
          color: var(--text-color-secondary);
          margin-bottom: 4px;
        }
        
        .application-time {
          font-size: 12px;
          color: var(--text-color-placeholder);
        }
      }
    }
    
    .empty-state {
      padding: 40px 0;
      text-align: center;
    }
  }
}

// 响应式设计
@media (max-width: 768px) {
  .user-dashboard {
    .welcome-section {
      flex-direction: column;
      text-align: center;
      gap: 20px;
      
      .welcome-actions {
        flex-direction: column;
        width: 100%;
        
        .el-button {
          width: 100%;
        }
      }
    }
    
    .stats-section {
      .el-col {
        margin-bottom: 16px;
      }
    }
    
    .quick-actions {
      grid-template-columns: repeat(2, 1fr);
    }
  }
}
</style>
