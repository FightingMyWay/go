<template>
  <div class="dept-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加部门
          </el-button>
        </div>
      </template>

      <!-- 部门列表 -->
      <el-table :data="deptList" style="width: 100%" v-loading="loading">
        <el-table-column prop="deptName" label="部门名称" min-width="150" />
        <el-table-column prop="deptCode" label="部门编码" width="120" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="userCount" label="用户数量" width="100" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'NORMAL' ? 'success' : 'danger'">
              {{ scope.row.status === 'NORMAL' ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="warning" @click="toggleStatus(scope.row)">
              {{ scope.row.status === 'NORMAL' ? '停用' : '启用' }}
            </el-button>
            <el-button size="small" type="danger" @click="deleteDept(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑部门对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门编码" prop="deptCode">
          <el-input v-model="form.deptCode" placeholder="请输入部门编码" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入部门描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitForm">确定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'

// 响应式数据
const loading = ref(false)
const dialogVisible = ref(false)
const dialogTitle = ref('')
const formRef = ref()

// 部门表单
const form = reactive({
  id: null,
  deptName: '',
  deptCode: '',
  description: ''
})

// 表单验证规则
const rules = {
  deptName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  deptCode: [
    { required: true, message: '请输入部门编码', trigger: 'blur' }
  ]
}

// 部门列表
const deptList = ref([])

// 显示添加对话框
const showAddDialog = () => {
  dialogTitle.value = '添加部门'
  dialogVisible.value = true
  resetForm()
}

// 显示编辑对话框
const showEditDialog = (row) => {
  dialogTitle.value = '编辑部门'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.deptName = ''
  form.deptCode = ''
  form.description = ''
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

// 提交表单
const submitForm = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    
    if (form.id) {
      // 编辑
      await updateDept()
    } else {
      // 添加
      await addDept()
    }
    
    dialogVisible.value = false
    loadDeptList()
    ElMessage.success(form.id ? '编辑成功' : '添加成功')
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 添加部门
const addDept = async () => {
  // TODO: 调用API添加部门
  console.log('添加部门:', form)
}

// 更新部门
const updateDept = async () => {
  // TODO: 调用API更新部门
  console.log('更新部门:', form)
}

// 切换状态
const toggleStatus = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要${row.status === 'NORMAL' ? '停用' : '启用'}该部门吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API切换状态
    console.log('切换状态:', row)
    ElMessage.success('操作成功')
    loadDeptList()
  } catch (error) {
    // 用户取消
  }
}

// 删除部门
const deleteDept = async (row) => {
  if (row.userCount > 0) {
    ElMessage.warning('该部门下还有用户，无法删除')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      '确定要删除该部门吗？删除后无法恢复！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API删除部门
    console.log('删除部门:', row)
    ElMessage.success('删除成功')
    loadDeptList()
  } catch (error) {
    // 用户取消
  }
}

// 加载部门列表
const loadDeptList = async () => {
  loading.value = true
  try {
    // TODO: 调用API加载部门列表
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    deptList.value = [
      {
        id: 1,
        deptName: '技术部',
        deptCode: 'TECH',
        description: '负责公司技术研发和系统维护',
        userCount: 25,
        status: 'NORMAL',
        createTime: '2023-12-01 10:00:00'
      },
      {
        id: 2,
        deptName: '人事部',
        deptCode: 'HR',
        description: '负责人力资源管理和招聘',
        userCount: 8,
        status: 'NORMAL',
        createTime: '2023-12-01 09:30:00'
      },
      {
        id: 3,
        deptName: '财务部',
        deptCode: 'FIN',
        description: '负责公司财务管理和会计核算',
        userCount: 12,
        status: 'NORMAL',
        createTime: '2023-12-01 09:00:00'
      },
      {
        id: 4,
        deptName: '行政部',
        deptCode: 'ADMIN',
        description: '负责公司行政事务和后勤保障',
        userCount: 15,
        status: 'NORMAL',
        createTime: '2023-12-01 08:30:00'
      }
    ]
  } catch (error) {
    console.error('加载部门列表失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadDeptList()
})
</script>

<style scoped lang="scss">
.dept-management {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
