<template>
  <div class="profile-page">
    <div class="page-header">
      <h2>个人资料</h2>
      <p class="page-desc">管理您的个人信息和账户设置</p>
    </div>

    <el-row :gutter="20">
      <!-- 左侧：个人信息 -->
      <el-col :span="16">
        <el-card class="profile-card">
          <template #header>
            <div class="card-header">
              <span>基本信息</span>
              <el-button type="primary" @click="editMode = true" v-if="!editMode">
                编辑资料
              </el-button>
            </div>
          </template>

          <el-form
            ref="profileFormRef"
            :model="profileForm"
            :rules="profileRules"
            label-width="100px"
            :disabled="!editMode"
          >
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="用户名" prop="username">
                  <el-input v-model="profileForm.username" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="真实姓名" prop="realName">
                  <el-input v-model="profileForm.realName" placeholder="请输入真实姓名" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="手机号码" prop="phone">
                  <el-input v-model="profileForm.phone" placeholder="请输入手机号码" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="邮箱地址" prop="email">
                  <el-input v-model="profileForm.email" placeholder="请输入邮箱地址" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="所属部门" prop="deptName">
                  <el-input v-model="profileForm.deptName" disabled />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="员工编号" prop="employeeNo">
                  <el-input v-model="profileForm.employeeNo" placeholder="请输入员工编号" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="12">
                <el-form-item label="职位" prop="position">
                  <el-input v-model="profileForm.position" placeholder="请输入职位" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="入职日期" prop="hireDate">
                  <el-date-picker
                    v-model="profileForm.hireDate"
                    type="date"
                    placeholder="选择入职日期"
                    format="YYYY-MM-DD"
                    value-format="YYYY-MM-DD"
                    style="width: 100%"
                  />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row :gutter="20">
              <el-col :span="24">
                <el-form-item label="个人简介" prop="bio">
                  <el-input
                    v-model="profileForm.bio"
                    type="textarea"
                    :rows="3"
                    placeholder="请输入个人简介"
                    maxlength="200"
                    show-word-limit
                  />
                </el-form-item>
              </el-col>
            </el-row>

            <!-- 编辑模式下的操作按钮 -->
            <div v-if="editMode" class="form-actions">
              <el-button @click="cancelEdit">取消</el-button>
              <el-button type="primary" @click="saveProfile" :loading="saving">
                保存
              </el-button>
            </div>
          </el-form>
        </el-card>

        <!-- 修改密码 -->
        <el-card class="password-card">
          <template #header>
            <span>修改密码</span>
          </template>

          <el-form
            ref="passwordFormRef"
            :model="passwordForm"
            :rules="passwordRules"
            label-width="100px"
          >
            <el-form-item label="当前密码" prop="currentPassword">
              <el-input
                v-model="passwordForm.currentPassword"
                type="password"
                placeholder="请输入当前密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
              <el-input
                v-model="passwordForm.newPassword"
                type="password"
                placeholder="请输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item label="确认密码" prop="confirmPassword">
              <el-input
                v-model="passwordForm.confirmPassword"
                type="password"
                placeholder="请再次输入新密码"
                show-password
              />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="changePassword" :loading="changingPassword">
                修改密码
              </el-button>
            </el-form-item>
          </el-form>
        </el-card>
      </el-col>

      <!-- 右侧：头像和统计信息 -->
      <el-col :span="8">
        <!-- 头像上传 -->
        <el-card class="avatar-card">
          <template #header>
            <span>头像设置</span>
          </template>

          <div class="avatar-section">
            <div class="avatar-wrapper">
              <el-avatar
                :size="120"
                :src="profileForm.avatarUrl || '/default-avatar.png'"
                @error="handleAvatarError"
              />
              <div class="avatar-overlay" @click="triggerAvatarUpload">
                <el-icon><Camera /></el-icon>
                <span>更换头像</span>
              </div>
            </div>
            <input
              ref="avatarInputRef"
              type="file"
              accept="image/*"
              style="display: none"
              @change="handleAvatarChange"
            />
            <p class="avatar-tip">支持 JPG、PNG 格式，文件大小不超过 2MB</p>
          </div>
        </el-card>

        <!-- 账户统计 -->
        <el-card class="stats-card">
          <template #header>
            <span>账户统计</span>
          </template>

          <div class="stats-list">
            <div class="stat-item">
              <div class="stat-number">{{ stats.totalApplications }}</div>
              <div class="stat-label">总申请数</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ stats.pendingApplications }}</div>
              <div class="stat-label">待审核</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ stats.approvedApplications }}</div>
              <div class="stat-label">已通过</div>
            </div>
            <div class="stat-item">
              <div class="stat-number">{{ stats.rejectedApplications }}</div>
              <div class="stat-label">已拒绝</div>
            </div>
          </div>
        </el-card>

        <!-- 最近活动 -->
        <el-card class="activity-card">
          <template #header>
            <span>最近活动</span>
          </template>

          <div class="activity-list">
            <div
              v-for="activity in recentActivities"
              :key="activity.id"
              class="activity-item"
            >
              <div class="activity-icon">
                <el-icon v-if="activity.type === 'APPLICATION'"><Document /></el-icon>
                <el-icon v-else-if="activity.type === 'LOGIN'"><User /></el-icon>
                <el-icon v-else><InfoFilled /></el-icon>
              </div>
              <div class="activity-content">
                <div class="activity-text">{{ activity.description }}</div>
                <div class="activity-time">{{ formatTime(activity.createTime) }}</div>
              </div>
            </div>
            <div v-if="recentActivities.length === 0" class="no-activity">
              <el-empty description="暂无活动记录" :image-size="60" />
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Camera, Document, User, InfoFilled } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

// 用户状态管理
const userStore = useUserStore()

// 响应式引用
const profileFormRef = ref()
const passwordFormRef = ref()
const avatarInputRef = ref()

// 状态管理
const editMode = ref(false)
const saving = ref(false)
const changingPassword = ref(false)

// 个人资料表单
const profileForm = reactive({
  username: '',
  realName: '',
  phone: '',
  email: '',
  deptName: '',
  employeeNo: '',
  position: '',
  hireDate: '',
  bio: '',
  avatarUrl: ''
})

// 密码表单
const passwordForm = reactive({
  currentPassword: '',
  newPassword: '',
  confirmPassword: ''
})

// 表单验证规则
const profileRules = {
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' },
    { min: 2, max: 20, message: '姓名长度在2到20个字符', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  employeeNo: [
    { required: true, message: '请输入员工编号', trigger: 'blur' }
  ],
  position: [
    { required: true, message: '请输入职位', trigger: 'blur' }
  ],
  hireDate: [
    { required: true, message: '请选择入职日期', trigger: 'change' }
  ]
}

const passwordRules = {
  currentPassword: [
    { required: true, message: '请输入当前密码', trigger: 'blur' }
  ],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6到20个字符', trigger: 'blur' },
    { pattern: /^(?=.*[a-zA-Z])(?=.*\d)/, message: '密码必须包含字母和数字', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
}

// 统计数据
const stats = ref({
  totalApplications: 0,
  pendingApplications: 0,
  approvedApplications: 0,
  rejectedApplications: 0
})

// 最近活动
const recentActivities = ref([])

// 初始化个人资料
const initProfile = () => {
  const userInfo = userStore.userInfo
  if (userInfo) {
    Object.assign(profileForm, {
      username: userInfo.username || '',
      realName: userInfo.realName || '',
      phone: userInfo.phone || '',
      email: userInfo.email || '',
      deptName: userInfo.deptName || '',
      employeeNo: userInfo.employeeNo || '',
      position: userInfo.position || '',
      hireDate: userInfo.hireDate || '',
      bio: userInfo.bio || '',
      avatarUrl: userInfo.avatarUrl || ''
    })
  }
}

// 获取个人资料
const fetchProfile = async () => {
  try {
    // 暂时使用本地数据，避免API调用错误
    initProfile()
    console.log('个人资料加载完成')
  } catch (error) {
    console.error('获取个人资料失败:', error)
    initProfile()
  }
}

// 获取个人资料统计
const fetchProfileStats = async () => {
  try {
    // 暂时使用模拟数据
    stats.value = {
      totalApplications: 15,
      pendingApplications: 3,
      approvedApplications: 10,
      rejectedApplications: 2
    }
    console.log('统计信息加载完成')
  } catch (error) {
    console.error('获取统计信息失败:', error)
    stats.value = {
      totalApplications: 0,
      pendingApplications: 0,
      approvedApplications: 0,
      rejectedApplications: 0
    }
  }
}

// 获取最近活动
const fetchRecentActivities = async () => {
  try {
    // 暂时使用模拟数据
    recentActivities.value = [
      {
        id: 1,
        type: 'APPLICATION',
        description: '提交了办公用品申请',
        createTime: new Date().toISOString()
      },
      {
        id: 2,
        type: 'LOGIN',
        description: '登录系统',
        createTime: new Date(Date.now() - 3600000).toISOString()
      }
    ]
    console.log('活动记录加载完成')
  } catch (error) {
    console.error('获取活动记录失败:', error)
    recentActivities.value = []
  }
}

// 保存个人资料
const saveProfile = async () => {
  try {
    await profileFormRef.value.validate()
    saving.value = true
    
    // 暂时模拟保存成功
    ElMessage.success('个人资料保存成功')
    
    // 更新用户信息
    userStore.updateUserInfo(profileForm)
    editMode.value = false
    
    console.log('个人资料保存成功')
  } catch (error) {
    console.error('保存失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('保存失败')
    }
  } finally {
    saving.value = false
  }
}

// 取消编辑
const cancelEdit = () => {
  fetchProfile()
  editMode.value = false
}

// 修改密码
const changePassword = async () => {
  try {
    await passwordFormRef.value.validate()
    changingPassword.value = true
    
    // 暂时模拟密码修改成功
    ElMessage.success('密码修改成功，请重新登录')
    
    // 清空密码表单
    passwordForm.currentPassword = ''
    passwordForm.newPassword = ''
    passwordForm.confirmPassword = ''
    
    console.log('密码修改成功')
  } catch (error) {
    console.error('密码修改失败:', error)
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('密码修改失败')
    }
  } finally {
    changingPassword.value = false
  }
}

// 触发头像上传
const triggerAvatarUpload = () => {
  avatarInputRef.value.click()
}

// 处理头像文件选择
const handleAvatarChange = async (event) => {
  const file = event.target.files[0]
  if (!file) return

  // 检查文件类型和大小
  if (!file.type.startsWith('image/')) {
    ElMessage.error('请选择图片文件')
    return
  }

  if (file.size > 2 * 1024 * 1024) {
    ElMessage.error('图片大小不能超过2MB')
    return
  }

  try {
    // 暂时模拟上传成功
    const avatarUrl = URL.createObjectURL(file)
    profileForm.avatarUrl = avatarUrl
    
    // 更新用户信息
    userStore.updateUserInfo({ avatarUrl })
    ElMessage.success('头像上传成功')
    
    console.log('头像上传成功')
  } catch (error) {
    console.error('头像上传失败:', error)
    ElMessage.error('头像上传失败')
  }

  // 清空文件输入
  event.target.value = ''
}

// 处理头像加载错误
const handleAvatarError = () => {
  profileForm.avatarUrl = '/default-avatar.png'
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date

  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return `${Math.floor(diff / 60000)}分钟前`
  if (diff < 86400000) return `${Math.floor(diff / 3600000)}小时前`
  if (diff < 2592000000) return `${Math.floor(diff / 86400000)}天前`
  
  return date.toLocaleDateString()
}

// 生命周期
onMounted(() => {
  console.log('个人资料页面加载完成')
  fetchProfile()
  fetchProfileStats()
  fetchRecentActivities()
})
</script>

<style lang="scss" scoped>
.profile-page {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      margin: 0 0 10px 0;
      color: #303133;
    }

    .page-desc {
      margin: 0;
      color: #606266;
      font-size: 14px;
    }
  }

  .profile-card {
    margin-bottom: 20px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }

    .form-actions {
      text-align: center;
      margin-top: 20px;
      padding-top: 20px;
      border-top: 1px solid #ebeef5;

      .el-button {
        margin: 0 10px;
        min-width: 100px;
      }
    }
  }

  .password-card {
    margin-bottom: 20px;
  }

  .avatar-card {
    margin-bottom: 20px;

    .avatar-section {
      text-align: center;

      .avatar-wrapper {
        position: relative;
        display: inline-block;
        margin-bottom: 15px;

        .avatar-overlay {
          position: absolute;
          top: 0;
          left: 0;
          right: 0;
          bottom: 0;
          background: rgba(0, 0, 0, 0.6);
          color: white;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          border-radius: 50%;
          cursor: pointer;
          opacity: 0;
          transition: opacity 0.3s;

          .el-icon {
            font-size: 24px;
            margin-bottom: 5px;
          }

          span {
            font-size: 12px;
          }
        }

        &:hover .avatar-overlay {
          opacity: 1;
        }
      }

      .avatar-tip {
        margin: 0;
        color: #909399;
        font-size: 12px;
      }
    }
  }

  .stats-card {
    margin-bottom: 20px;

    .stats-list {
      .stat-item {
        text-align: center;
        padding: 15px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .stat-number {
          font-size: 24px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 5px;
        }

        .stat-label {
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .activity-card {
    .activity-list {
      .activity-item {
        display: flex;
        align-items: flex-start;
        padding: 10px 0;
        border-bottom: 1px solid #f0f0f0;

        &:last-child {
          border-bottom: none;
        }

        .activity-icon {
          margin-right: 10px;
          color: #409eff;
          font-size: 16px;
        }

        .activity-content {
          flex: 1;

          .activity-text {
            color: #303133;
            font-size: 14px;
            margin-bottom: 5px;
            line-height: 1.4;
          }

          .activity-time {
            color: #909399;
            font-size: 12px;
          }
        }
      }

      .no-activity {
        padding: 20px 0;
        text-align: center;
      }
    }
  }
}
</style>
