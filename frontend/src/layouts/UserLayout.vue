<template>
  <div class="user-layout">
    <!-- 顶部导航栏 -->
    <el-header class="header">
      <div class="header-left">
        <div class="logo">
          <el-icon class="logo-icon"><OfficeBuilding /></el-icon>
          <span class="logo-text">办公用品申领系统</span>
        </div>
      </div>
      
      <div class="header-right">
        <el-dropdown @command="handleCommand">
          <div class="user-info">
            <el-avatar :size="32" :src="userStore.userInfo.avatar">
              {{ userStore.userInfo.realName?.charAt(0) || 'U' }}
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
      <el-aside class="sidebar" :width="isCollapse ? '64px' : '200px'">
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
          <el-menu-item index="/user/dashboard">
            <el-icon><House /></el-icon>
            <template #title>用户首页</template>
          </el-menu-item>
          
          <el-menu-item index="/user/supply-list">
            <el-icon><Goods /></el-icon>
            <template #title>用品列表</template>
          </el-menu-item>
          
          <el-menu-item index="/user/application">
            <el-icon><DocumentAdd /></el-icon>
            <template #title>申领申请</template>
          </el-menu-item>
          
          <el-menu-item index="/user/my-applications">
            <el-icon><Document /></el-icon>
            <template #title>我的申请</template>
          </el-menu-item>
          
          <el-menu-item index="/user/profile">
            <el-icon><User /></el-icon>
            <template #title>个人资料</template>
          </el-menu-item>
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
      router.push('/user/profile')
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
.user-layout {
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
