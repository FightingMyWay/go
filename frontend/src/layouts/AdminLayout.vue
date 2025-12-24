<template>
  <div class="admin-layout">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="header-left">
        <div class="logo">
          <el-icon class="logo-icon"><Setting /></el-icon>
          <span class="logo-text">管理后台</span>
        </div>
      </div>
      
      <div class="header-center">
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/admin/dashboard' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item v-if="$route.meta.title">{{ $route.meta.title }}</el-breadcrumb-item>
        </el-breadcrumb>
      </div>
      
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="32" :src="userStore.userInfo.avatar">
              {{ userStore.userInfo.realName?.charAt(0) || 'A' }}
            </el-avatar>
            <span class="username">{{ userStore.userInfo.realName || userStore.userInfo.username }}</span>
            <el-icon><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><User /></el-icon>
                个人资料
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>
                退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </el-header>
    
    <div class="main-container">
      <!-- 侧边栏 -->
      <el-aside class="sidebar" :width="isCollapse ? '64px' : '220px'">
        <div class="sidebar-header">
          <el-button
            type="text"
            class="collapse-btn"
            @click="toggleCollapse"
          >
            <el-icon>
              <component :is="isCollapse ? 'Expand' : 'Fold'" />
            </el-icon>
          </el-button>
        </div>
        
        <el-menu
          :default-active="$route.path"
          :collapse="isCollapse"
          :unique-opened="true"
          router
          class="sidebar-menu"
        >
          <el-menu-item index="/admin/dashboard">
            <el-icon><DataBoard /></el-icon>
            <template #title>管理首页</template>
          </el-menu-item>
          
          <el-sub-menu index="supply">
            <template #title>
              <el-icon><Goods /></el-icon>
              <span>用品管理</span>
            </template>
            <el-menu-item index="/admin/supply-management">用品管理</el-menu-item>
            <el-menu-item index="/admin/category-management">分类管理</el-menu-item>
            <el-menu-item index="/admin/stock-management">库存管理</el-menu-item>
          </el-sub-menu>
          
          <el-menu-item index="/admin/application-management">
            <el-icon><Document /></el-icon>
            <template #title>申请管理</template>
          </el-menu-item>
          
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统管理</span>
            </template>
            <el-menu-item index="/admin/user-management">用户管理</el-menu-item>
            <el-menu-item index="/admin/dept-management">部门管理</el-menu-item>
          </el-sub-menu>
        </el-menu>
      </el-aside>
      
      <!-- 主内容区 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)

const toggleCollapse = () => {
  isCollapse.value = !isCollapse.value
}

const handleCommand = async (command) => {
  switch (command) {
    case 'profile':
      // 可以跳转到管理员个人资料页面
      break
    case 'logout':
      try {
        await ElMessageBox.confirm(
          '确定要退出登录吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        userStore.logout()
        router.push('/login')
      } catch {
        // 用户取消
      }
      break
  }
}
</script>

<style scoped lang="scss">
.admin-layout {
  height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: #fff;
  border-bottom: 1px solid var(--border-color-light);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px;
  height: 60px;
  box-shadow: var(--shadow-base);
  z-index: 1000;
}

.header-left {
  .logo {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .logo-icon {
      font-size: 24px;
      color: var(--primary-color);
    }
    
    .logo-text {
      font-size: 18px;
      font-weight: 600;
      color: var(--text-color-primary);
    }
  }
}

.header-center {
  flex: 1;
  display: flex;
  justify-content: center;
  
  .el-breadcrumb {
    font-size: 14px;
  }
}

.header-right {
  .user-info {
    display: flex;
    align-items: center;
    gap: 8px;
    cursor: pointer;
    padding: 8px 12px;
    border-radius: 6px;
    transition: all 0.3s ease;
    
    &:hover {
      background: var(--background-color-base);
    }
    
    .username {
      font-size: 14px;
      color: var(--text-color-primary);
      font-weight: 500;
    }
  }
}

.main-container {
  flex: 1;
  display: flex;
  overflow: hidden;
}

.sidebar {
  background: #fff;
  border-right: 1px solid var(--border-color-light);
  transition: width 0.3s ease;
  overflow: hidden;
}

.sidebar-header {
  height: 50px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid var(--border-color-light);
  
  .collapse-btn {
    width: 100%;
    height: 100%;
    border: none;
    border-radius: 0;
    
    &:hover {
      background: var(--background-color-base);
    }
  }
}

.sidebar-menu {
  border: none;
  
  .el-menu-item {
    height: 50px;
    line-height: 50px;
    
    &.is-active {
      background: var(--primary-color);
      color: #fff;
      
      &::before {
        content: '';
        position: absolute;
        left: 0;
        top: 0;
        bottom: 0;
        width: 4px;
        background: #fff;
      }
    }
    
    &:hover {
      background: var(--background-color-base);
    }
  }
  
  .el-sub-menu {
    .el-sub-menu__title {
      height: 50px;
      line-height: 50px;
      
      &:hover {
        background: var(--background-color-base);
      }
    }
    
    .el-menu-item {
      padding-left: 54px !important;
    }
  }
}

.main-content {
  background: var(--background-color-base);
  padding: 20px;
  overflow-y: auto;
}

// 响应式设计
@media (max-width: 768px) {
  .header {
    padding: 0 15px;
    
    .logo-text {
      display: none;
    }
    
    .header-center {
      display: none;
    }
  }
  
  .sidebar {
    position: fixed;
    top: 60px;
    left: 0;
    bottom: 0;
    z-index: 999;
    transform: translateX(-100%);
    transition: transform 0.3s ease;
    
    &.show {
      transform: translateX(0);
    }
  }
  
  .main-content {
    padding: 15px;
  }
}
</style>
