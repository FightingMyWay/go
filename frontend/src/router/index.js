import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/test',
    name: 'TestApi',
    component: () => import('@/views/TestApi.vue'),
    meta: { title: 'API测试', requiresAuth: false }
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { title: '注册', requiresAuth: false }
  },
  // 用户前端路由
  {
    path: '/user',
    component: () => import('@/layouts/UserLayout.vue'),
    meta: { requiresAuth: true, role: 'USER' },
    children: [
      {
        path: '',
        redirect: '/user/dashboard'
      },
      {
        path: 'dashboard',
        name: 'UserDashboard',
        component: () => import('@/views/user/Dashboard.vue'),
        meta: { title: '用户首页', requiresAuth: true }
      },
      {
        path: 'supply-list',
        name: 'SupplyList',
        component: () => import('@/views/user/SupplyList.vue'),
        meta: { title: '用品列表', requiresAuth: true }
      },
      {
        path: 'application',
        name: 'Application',
        component: () => import('@/views/user/Application.vue'),
        meta: { title: '申领申请', requiresAuth: true }
      },
      {
        path: 'my-applications',
        name: 'MyApplications',
        component: () => import('@/views/user/MyApplications.vue'),
        meta: { title: '我的申请', requiresAuth: true }
      },
      {
        path: 'profile',
        name: 'UserProfile',
        component: () => import('@/views/user/Profile.vue'),
        meta: { title: '个人资料', requiresAuth: true }
      }
    ]
  },
  // 管理后台路由
  {
    path: '/admin',
    component: () => import('@/layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, role: 'ADMIN' },
    children: [
      {
        path: '',
        redirect: '/admin/dashboard'
      },
      {
        path: 'dashboard',
        name: 'AdminDashboard',
        component: () => import('@/views/admin/Dashboard.vue'),
        meta: { title: '管理首页', requiresAuth: true }
      },
      {
        path: 'supply-management',
        name: 'SupplyManagement',
        component: () => import('@/views/admin/SupplyManagement.vue'),
        meta: { title: '用品管理', requiresAuth: true }
      },
      {
        path: 'category-management',
        name: 'CategoryManagement',
        component: () => import('@/views/admin/CategoryManagement.vue'),
        meta: { title: '分类管理', requiresAuth: true }
      },
      {
        path: 'application-management',
        name: 'ApplicationManagement',
        component: () => import('@/views/admin/ApplicationManagement.vue'),
        meta: { title: '申请管理', requiresAuth: true }
      },
      {
        path: 'user-management',
        name: 'UserManagement',
        component: () => import('@/views/admin/UserManagement.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      },
      {
        path: 'dept-management',
        name: 'DeptManagement',
        component: () => import('@/views/admin/DeptManagement.vue'),
        meta: { title: '部门管理', requiresAuth: true }
      },
      {
        path: 'stock-management',
        name: 'StockManagement',
        component: () => import('@/views/admin/StockManagement.vue'),
        meta: { title: '库存管理', requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    name: 'NotFound',
    component: () => import('@/views/NotFound.vue'),
    meta: { title: '页面不存在' }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()
  
  // 设置页面标题
  if (to.meta.title) {
    document.title = `${to.meta.title} - 办公用品申领系统`
  }
  
  // 检查是否需要认证
  if (to.meta.requiresAuth) {
    if (!userStore.isLoggedIn) {
      next('/login')
      return
    }
    
    // 检查角色权限
    if (to.meta.role && userStore.userInfo.role !== to.meta.role) {
      if (userStore.userInfo.role === 'ADMIN') {
        next('/admin/dashboard')
      } else {
        next('/user/dashboard')
      }
      return
    }
  }
  
  next()
})

export default router
