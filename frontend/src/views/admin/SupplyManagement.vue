<template>
  <div class="supply-management">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>用品管理</span>
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            添加用品
          </el-button>
        </div>
      </template>

      <!-- 搜索栏 -->
      <div class="search-bar">
        <el-form :inline="true" :model="searchForm" class="search-form">
          <el-form-item label="用品名称">
            <el-input v-model="searchForm.itemName" placeholder="请输入用品名称" clearable />
          </el-form-item>
          <el-form-item label="分类">
            <el-select v-model="searchForm.categoryId" placeholder="请选择分类" clearable>
              <el-option
                v-for="category in categories"
                :key="category.id"
                :label="category.name"
                :value="category.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSearch">搜索</el-button>
            <el-button @click="resetSearch">重置</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 用品列表 -->
      <el-table :data="supplyList" style="width: 100%" v-loading="loading">
        <el-table-column prop="itemName" label="用品名称" min-width="150" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="specification" label="规格型号" width="150" />
        <el-table-column prop="unit" label="单位" width="80" />
        <el-table-column prop="stockQuantity" label="库存数量" width="100" />
        <el-table-column prop="price" label="单价" width="100">
          <template #default="scope">
            ¥{{ scope.row.price }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 1 ? 'success' : 'danger'">
              {{ scope.row.status === 1 ? '正常' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="scope">
            <el-button size="small" @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button size="small" type="info" @click="showStockDialog(scope.row)">库存</el-button>
            <el-button size="small" type="warning" @click="toggleStatus(scope.row)">
              {{ scope.row.status === 1 ? '停用' : '启用' }}
            </el-button>
            <el-button size="small" type="danger" @click="deleteSupply(scope.row)">删除</el-button>
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

    <!-- 添加/编辑用品对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-width="100px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="用品名称" prop="itemName">
              <el-input v-model="form.itemName" placeholder="请输入用品名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="分类" prop="categoryId">
              <el-select v-model="form.categoryId" placeholder="请选择分类" style="width: 100%">
                <el-option
                  v-for="category in categories"
                  :key="category.id"
                  :label="category.name"
                  :value="category.id"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="规格型号" prop="specification">
              <el-input v-model="form.specification" placeholder="请输入规格型号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单位" prop="unit">
              <el-input v-model="form.unit" placeholder="请输入单位" />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="单价" prop="price">
              <el-input-number 
                v-model="form.price" 
                :precision="2" 
                :min="0" 
                :step="0.01"
                style="width: 100%"
                placeholder="请输入单价"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="库存数量" prop="stockQuantity">
              <el-input-number 
                v-model="form.stockQuantity" 
                :min="0" 
                style="width: 100%"
                placeholder="请输入库存数量"
              />
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="最低库存" prop="minStock">
              <el-input-number 
                v-model="form.minStock" 
                :min="0" 
                style="width: 100%"
                placeholder="请输入最低库存"
              />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" style="width: 100%">
                <el-option label="正常" :value="1" />
                <el-option label="停用" :value="0" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        
        <el-form-item label="描述" prop="description">
          <el-input 
            v-model="form.description" 
            type="textarea" 
            :rows="3"
            placeholder="请输入描述信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
            {{ isEdit ? '更新' : '创建' }}
          </el-button>
        </span>
      </template>
    </el-dialog>

    <!-- 库存更新对话框 -->
    <el-dialog
      v-model="stockDialogVisible"
      title="库存管理"
      width="400px"
    >
      <el-form :model="stockForm" label-width="100px">
        <el-form-item label="用品名称">
          <span>{{ currentItem?.itemName }}</span>
        </el-form-item>
        <el-form-item label="当前库存">
          <span>{{ currentItem?.stockQuantity }}</span>
        </el-form-item>
        <el-form-item label="调整数量" prop="adjustQuantity">
          <el-input-number 
            v-model="stockForm.adjustQuantity" 
            :min="-currentItem?.stockQuantity"
            style="width: 100%"
            placeholder="正数为增加，负数为减少"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input 
            v-model="stockForm.remark" 
            type="textarea" 
            :rows="3"
            placeholder="请输入调整原因"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="stockDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleStockUpdate" :loading="stockLoading">
            确认调整
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import { getSupplyItems, getCategories, createSupplyItem, updateSupplyItem, deleteSupplyItem, updateStock } from '@/api/supply'

// 响应式数据
const loading = ref(false)
const submitLoading = ref(false)
const stockLoading = ref(false)
const dialogVisible = ref(false)
const stockDialogVisible = ref(false)
const isEdit = ref(false)
const currentItem = ref(null)
const formRef = ref()

// 搜索表单
const searchForm = reactive({
  itemName: '',
  categoryId: ''
})

// 用品表单
const form = reactive({
  id: null,
  itemName: '',
  categoryId: '',
  specification: '',
  unit: '',
  price: 0,
  stockQuantity: 0,
  minStock: 0,
  description: '',
  status: 1
})

// 库存表单
const stockForm = reactive({
  adjustQuantity: 0,
  remark: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 数据列表
const supplyList = ref([])
const categories = ref([])

// 表单验证规则
const rules = {
  itemName: [
    { required: true, message: '请输入用品名称', trigger: 'blur' },
    { min: 2, max: 100, message: '长度在 2 到 100 个字符', trigger: 'blur' }
  ],
  categoryId: [
    { required: true, message: '请选择分类', trigger: 'change' }
  ],
  price: [
    { required: true, message: '请输入单价', trigger: 'blur' },
    { type: 'number', min: 0, message: '单价不能小于0', trigger: 'blur' }
  ],
  stockQuantity: [
    { required: true, message: '请输入库存数量', trigger: 'blur' },
    { type: 'number', min: 0, message: '库存数量不能小于0', trigger: 'blur' }
  ],
  minStock: [
    { required: true, message: '请输入最低库存', trigger: 'blur' },
    { type: 'number', min: 0, message: '最低库存不能小于0', trigger: 'blur' }
  ]
}

// 计算属性
const dialogTitle = computed(() => isEdit.value ? '编辑用品' : '添加用品')

// 方法
const loadData = async () => {
  loading.value = true
  try {
    const params = {
      page: pagination.current,
      size: pagination.size,
      name: searchForm.itemName || undefined,
      categoryId: searchForm.categoryId || undefined
    }
    
    const response = await getSupplyItems(params)
    if (response.code === 200) {
      supplyList.value = response.data.records || []
      pagination.total = response.data.total || 0
    }
  } catch (error) {
    ElMessage.error('加载数据失败')
  } finally {
    loading.value = false
  }
}

const loadCategories = async () => {
  try {
    const response = await getCategories()
    if (response.code === 200) {
      categories.value = response.data || []
    }
  } catch (error) {
    ElMessage.error('加载分类失败')
  }
}

const showAddDialog = () => {
  isEdit.value = false
  resetForm()
  dialogVisible.value = true
}

const showEditDialog = (row) => {
  isEdit.value = true
  currentItem.value = row
  Object.assign(form, row)
  dialogVisible.value = true
}

const showStockDialog = (row) => {
  currentItem.value = row
  stockForm.adjustQuantity = 0
  stockForm.remark = ''
  stockDialogVisible.value = true
}

const resetForm = () => {
  Object.assign(form, {
    id: null,
    itemName: '',
    categoryId: '',
    specification: '',
    unit: '',
    price: 0,
    stockQuantity: 0,
    minStock: 0,
    description: '',
    status: 1
  })
  if (formRef.value) {
    formRef.value.resetFields()
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  
  try {
    await formRef.value.validate()
    submitLoading.value = true
    
    if (isEdit.value) {
      await updateSupplyItem(form)
      ElMessage.success('更新成功')
    } else {
      await createSupplyItem(form)
      ElMessage.success('创建成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    if (error.message) {
      ElMessage.error(error.message)
    } else {
      ElMessage.error('操作失败')
    }
  } finally {
    submitLoading.value = false
  }
}

const handleStockUpdate = async () => {
  if (stockForm.adjustQuantity === 0) {
    ElMessage.warning('请输入调整数量')
    return
  }
  
  stockLoading.value = true
  try {
    await updateStock(currentItem.value.id, stockForm.adjustQuantity)
    ElMessage.success('库存更新成功')
    stockDialogVisible.value = false
    loadData()
  } catch (error) {
    ElMessage.error(error.message || '库存更新失败')
  } finally {
    stockLoading.value = false
  }
}

const toggleStatus = async (row) => {
  try {
    const newStatus = row.status === 1 ? 0 : 1
    const statusText = newStatus === 1 ? '启用' : '停用'
    
    await ElMessageBox.confirm(
      `确定要${statusText}该用品吗？`,
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await updateSupplyItem({ ...row, status: newStatus })
    ElMessage.success(`${statusText}成功`)
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '操作失败')
    }
  }
}

const deleteSupply = async (row) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除该用品吗？删除后无法恢复！',
      '警告',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    await deleteSupplyItem(row.id)
    ElMessage.success('删除成功')
    loadData()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleSearch = () => {
  pagination.current = 1
  loadData()
}

const resetSearch = () => {
  Object.assign(searchForm, {
    itemName: '',
    categoryId: ''
  })
  pagination.current = 1
  loadData()
}

const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  loadData()
}

const handleCurrentChange = (page) => {
  pagination.current = page
  loadData()
}

// 生命周期
onMounted(() => {
  loadData()
  loadCategories()
})
</script>

<style scoped>
.supply-management {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-bar {
  margin-bottom: 20px;
}

.search-form {
  margin-bottom: 0;
}

.pagination-wrapper {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
