import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login, register, getUserInfo } from '@/api/auth'
import { ElMessage } from 'element-plus'

export const useUserStore = defineStore('user', () => {
  // 状态
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('userInfo') || '{}'))

  // 计算属性
  const isLoggedIn = computed(() => !!token.value)
  const isAdmin = computed(() => userInfo.value.role === 'ADMIN')
  const isUser = computed(() => userInfo.value.role === 'USER')

  // 登录
  const loginAction = async (loginForm) => {
    try {
      const response = await login(loginForm)
      if (response.code === 200) {
        const { token: newToken, user } = response.data
        token.value = newToken
        userInfo.value = user
        
        // 保存到本地存储
        localStorage.setItem('token', newToken)
        localStorage.setItem('userInfo', JSON.stringify(user))
        
        ElMessage.success('登录成功')
        return true
      } else {
        ElMessage.error(response.message || '登录失败')
        return false
      }
    } catch (error) {
      ElMessage.error('登录失败，请检查网络连接')
      return false
    }
  }

  // 注册
  const registerAction = async (registerForm) => {
    try {
      const response = await register(registerForm)
      if (response.code === 200) {
        ElMessage.success('注册成功，请登录')
        return true
      } else {
        ElMessage.error(response.message || '注册失败')
        return false
      }
    } catch (error) {
      ElMessage.error('注册失败，请检查网络连接')
      return false
    }
  }

  // 获取用户信息
  const getUserInfoAction = async () => {
    try {
      const response = await getUserInfo()
      if (response.code === 200) {
        userInfo.value = response.data
        localStorage.setItem('userInfo', JSON.stringify(response.data))
        return true
      }
      return false
    } catch (error) {
      return false
    }
  }

  // 登出
  const logout = () => {
    token.value = ''
    userInfo.value = {}
    localStorage.removeItem('token')
    localStorage.removeItem('userInfo')
    ElMessage.success('已退出登录')
  }

  // 更新用户信息
  const updateUserInfo = (info) => {
    userInfo.value = { ...userInfo.value, ...info }
    localStorage.setItem('userInfo', JSON.stringify(userInfo.value))
  }

  return {
    // 状态
    token,
    userInfo,
    
    // 计算属性
    isLoggedIn,
    isAdmin,
    isUser,
    
    // 方法
    loginAction,
    registerAction,
    getUserInfoAction,
    logout,
    updateUserInfo
  }
})
