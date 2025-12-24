<template>
  <div class="stock-management">
    <div class="page-header">
      <h1 class="page-title">库存管理</h1>
      <div class="page-actions">
        <el-button type="primary" @click="showAddStockDialog = true">
          <el-icon><Plus /></el-icon>
          入库
        </el-button>
        <el-button type="warning" @click="showOutStockDialog = true">
          <el-icon><Minus /></el-icon>
          出库
        </el-button>
      </div>
    </div>

    <!-- 搜索和筛选 -->
    <div class="search-section">
      <el-form :model="searchForm" inline>
        <el-form-item label="用品名称">
          <el-input
            v-model="searchForm.name"
            placeholder="请输入用品名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="分类">
          <el-select
            v-model="searchForm.categoryId"
            placeholder="请选择分类"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="category in categories"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 库存列表 -->
    <div class="table-section">
      <el-table
        :data="stockList"
        v-loading="loading"
        border
        stripe
        style="width: 100%"
      >
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="itemName" label="用品名称" min-width="150" />
        <el-table-column prop="categoryName" label="分类" width="120" />
        <el-table-column prop="currentStock" label="当前库存" width="100" align="center">
          <template #default="{ row }">
            <el-tag
              :type="getStockLevelType(row.currentStock, row.minStock)"
              size="small"
            >
              {{ row.currentStock }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="minStock" label="最低库存" width="100" align="center" />
        <el-table-column prop="unit" label="单位" width="80" align="center" />
        <el-table-column prop="lastUpdateTime" label="最后更新时间" width="180" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="handleEdit(row)"
            >
              编辑
            </el-button>
            <el-button
              type="success"
              size="small"
              @click="handleInStock(row)"
            >
              入库
            </el-button>
            <el-button
              type="warning"
              size="small"
              @click="handleOutStock(row)"
            >
              出库
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-section">
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
    </div>

    <!-- 入库对话框 -->
    <el-dialog
      v-model="showAddStockDialog"
      title="入库操作"
      width="500px"
    >
      <el-form :model="stockForm" :rules="stockRules" ref="stockFormRef" label-width="100px">
        <el-form-item label="用品名称" prop="itemId">
          <el-select
            v-model="stockForm.itemId"
            placeholder="请选择用品"
            style="width: 100%"
            @change="handleItemChange"
          >
            <el-option
              v-for="item in supplyItems"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="入库数量" prop="quantity">
          <el-input-number
            v-model="stockForm.quantity"
            :min="1"
            :precision="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="入库原因" prop="reason">
          <el-input
            v-model="stockForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入入库原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddStockDialog = false">取消</el-button>
        <el-button type="primary" @click="submitStockIn">确认入库</el-button>
      </template>
    </el-dialog>

    <!-- 出库对话框 -->
    <el-dialog
      v-model="showOutStockDialog"
      title="出库操作"
      width="500px"
    >
      <el-form :model="outStockForm" :rules="outStockRules" ref="outStockFormRef" label-width="100px">
        <el-form-item label="用品名称" prop="itemId">
          <el-select
            v-model="outStockForm.itemId"
            placeholder="请选择用品"
            style="width: 100%"
            @change="handleOutItemChange"
          >
            <el-option
              v-for="item in supplyItems"
              :key="item.id"
              :label="`${item.name} (库存: ${item.currentStock})`"
              :value="item.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="出库数量" prop="quantity">
          <el-input-number
            v-model="outStockForm.quantity"
            :min="1"
            :precision="0"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="出库原因" prop="reason">
          <el-input
            v-model="outStockForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入出库原因"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showOutStockDialog = false">取消</el-button>
        <el-button type="primary" @click="submitStockOut">确认出库</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'

// 响应式数据
const loading = ref(false)
const stockList = ref([])
const categories = ref([])
const supplyItems = ref([])
const showAddStockDialog = ref(false)
const showOutStockDialog = ref(false)

// 搜索表单
const searchForm = reactive({
  name: '',
  categoryId: ''
})

// 分页
const pagination = reactive({
  current: 1,
  size: 10,
  total: 0
})

// 入库表单
const stockForm = reactive({
  itemId: '',
  quantity: 1,
  reason: ''
})

// 出库表单
const outStockForm = reactive({
  itemId: '',
  quantity: 1,
  reason: ''
})

// 表单验证规则
const stockRules = {
  itemId: [{ required: true, message: '请选择用品', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入入库数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入入库原因', trigger: 'blur' }]
}

const outStockRules = {
  itemId: [{ required: true, message: '请选择用品', trigger: 'change' }],
  quantity: [{ required: true, message: '请输入出库数量', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入出库原因', trigger: 'blur' }]
}

// 获取库存列表
const getStockList = async () => {
  loading.value = true
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    stockList.value = [
      {
        id: 1,
        itemName: 'A4打印纸',
        categoryName: '办公用纸',
        currentStock: 500,
        minStock: 100,
        unit: '包',
        lastUpdateTime: '2024-12-01 10:30:00'
      },
      {
        id: 2,
        itemName: '黑色签字笔',
        categoryName: '书写用品',
        currentStock: 200,
        minStock: 50,
        unit: '支',
        lastUpdateTime: '2024-12-01 09:15:00'
      },
      {
        id: 3,
        itemName: '文件夹',
        categoryName: '文件管理',
        currentStock: 80,
        minStock: 30,
        unit: '个',
        lastUpdateTime: '2024-11-30 16:45:00'
      }
    ]
    pagination.total = stockList.value.length
  } catch (error) {
    ElMessage.error('获取库存列表失败')
  } finally {
    loading.value = false
  }
}

// 获取分类列表
const getCategories = async () => {
  try {
    // 模拟API调用
    categories.value = [
      { id: 1, name: '办公用纸' },
      { id: 2, name: '书写用品' },
      { id: 3, name: '文件管理' },
      { id: 4, name: '电子设备' }
    ]
  } catch (error) {
    ElMessage.error('获取分类列表失败')
  }
}

// 获取用品列表
const getSupplyItems = async () => {
  try {
    // 模拟API调用
    supplyItems.value = [
      {
        id: 1,
        name: 'A4打印纸',
        currentStock: 500
      },
      {
        id: 2,
        name: '黑色签字笔',
        currentStock: 200
      },
      {
        id: 3,
        name: '文件夹',
        currentStock: 80
      }
    ]
  } catch (error) {
    ElMessage.error('获取用品列表失败')
  }
}

// 搜索
const handleSearch = () => {
  pagination.current = 1
  getStockList()
}

// 重置搜索
const resetSearch = () => {
  Object.assign(searchForm, {
    name: '',
    categoryId: ''
  })
  handleSearch()
}

// 分页处理
const handleSizeChange = (size) => {
  pagination.size = size
  pagination.current = 1
  getStockList()
}

const handleCurrentChange = (current) => {
  pagination.current = current
  getStockList()
}

// 获取库存级别类型
const getStockLevelType = (current, min) => {
  if (current <= min) return 'danger'
  if (current <= min * 1.5) return 'warning'
  return 'success'
}

// 编辑库存
const handleEdit = (row) => {
  ElMessage.info('编辑功能开发中...')
}

// 入库操作
const handleInStock = (row) => {
  stockForm.itemId = row.id
  stockForm.quantity = 1
  stockForm.reason = ''
  showAddStockDialog.value = true
}

// 出库操作
const handleOutStock = (row) => {
  outStockForm.itemId = row.id
  outStockForm.quantity = 1
  outStockForm.reason = ''
  showOutStockDialog.value = true
}

// 用品选择变化
const handleItemChange = (itemId) => {
  // 处理用品选择变化
}

const handleOutItemChange = (itemId) => {
  // 处理出库用品选择变化
}

// 提交入库
const submitStockIn = async () => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('入库成功')
    showAddStockDialog.value = false
    getStockList()
  } catch (error) {
    ElMessage.error('入库失败')
  }
}

// 提交出库
const submitStockOut = async () => {
  try {
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    ElMessage.success('出库成功')
    showOutStockDialog.value = false
    getStockList()
  } catch (error) {
    ElMessage.error('出库失败')
  }
}

onMounted(() => {
  getStockList()
  getCategories()
  getSupplyItems()
})
</script>

<style scoped lang="scss">
.stock-management {
  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;
    
    .page-title {
      font-size: 24px;
      font-weight: 600;
      margin: 0;
    }
    
    .page-actions {
      display: flex;
      gap: 12px;
    }
  }
  
  .search-section {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 24px;
    box-shadow: var(--shadow-light);
  }
  
  .table-section {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    box-shadow: var(--shadow-light);
    
    .pagination-section {
      margin-top: 20px;
      text-align: center;
    }
  }
}
</style>
