<template>
  <div class="category-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>分类管理</span>
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加分类
          </el-button>
        </div>
      </template>

      <!-- 分类列表 -->
      <el-table :data="categoryList" style="width: 100%" v-loading="loading">
        <el-table-column prop="categoryName" label="分类名称" min-width="150" />
        <el-table-column prop="description" label="描述" min-width="200" />
        <el-table-column prop="itemCount" label="用品数量" width="100" />
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
            <el-button size="small" type="danger" @click="deleteCategory(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加/编辑分类对话框 -->
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
        <el-form-item label="分类名称" prop="categoryName">
          <el-input v-model="form.categoryName" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入分类描述"
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

// 分类表单
const form = reactive({
  id: null,
  categoryName: '',
  description: ''
})

// 表单验证规则
const rules = {
  categoryName: [
    { required: true, message: '请输入分类名称', trigger: 'blur' }
  ]
}

// 分类列表
const categoryList = ref([])

// 显示添加对话框
const showAddDialog = () => {
  dialogTitle.value = '添加分类'
  dialogVisible.value = true
  resetForm()
}

// 显示编辑对话框
const showEditDialog = (row) => {
  dialogTitle.value = '编辑分类'
  dialogVisible.value = true
  Object.assign(form, row)
}

// 重置表单
const resetForm = () => {
  form.id = null
  form.categoryName = ''
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
      await updateCategory()
    } else {
      // 添加
      await addCategory()
    }
    
    dialogVisible.value = false
    loadCategoryList()
    ElMessage.success(form.id ? '编辑成功' : '添加成功')
  } catch (error) {
    console.error('表单验证失败:', error)
  }
}

// 添加分类
const addCategory = async () => {
  // TODO: 调用API添加分类
  console.log('添加分类:', form)
}

// 更新分类
const updateCategory = async () => {
  // TODO: 调用API更新分类
  console.log('更新分类:', form)
}

// 切换状态
const toggleStatus = async (row) => {
  try {
    await ElMessageBox.confirm(
      `确定要${row.status === 'NORMAL' ? '停用' : '启用'}该分类吗？`,
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
    loadCategoryList()
  } catch (error) {
    // 用户取消
  }
}

// 删除分类
const deleteCategory = async (row) => {
  if (row.itemCount > 0) {
    ElMessage.warning('该分类下还有用品，无法删除')
    return
  }
  
  try {
    await ElMessageBox.confirm(
      '确定要删除该分类吗？删除后无法恢复！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    // TODO: 调用API删除分类
    console.log('删除分类:', row)
    ElMessage.success('删除成功')
    loadCategoryList()
  } catch (error) {
    // 用户取消
  }
}

// 加载分类列表
const loadCategoryList = async () => {
  loading.value = true
  try {
    // TODO: 调用API加载分类列表
    // 模拟数据
    await new Promise(resolve => setTimeout(resolve, 500))
    
    categoryList.value = [
      {
        id: 1,
        categoryName: '办公用品',
        description: '日常办公所需的文具、纸张等用品',
        itemCount: 25,
        status: 'NORMAL',
        createTime: '2023-12-01 10:00:00'
      },
      {
        id: 2,
        categoryName: '电子设备',
        description: '电脑配件、充电器等电子设备',
        itemCount: 15,
        status: 'NORMAL',
        createTime: '2023-12-01 09:30:00'
      },
      {
        id: 3,
        categoryName: '清洁用品',
        description: '办公环境清洁所需的用品',
        itemCount: 8,
        status: 'NORMAL',
        createTime: '2023-12-01 09:00:00'
      }
    ]
  } catch (error) {
    console.error('加载分类列表失败:', error)
    ElMessage.error('加载失败')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadCategoryList()
})
</script>

<style scoped lang="scss">
.category-management {
  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
