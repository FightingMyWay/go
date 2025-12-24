<template>
  <div class="supply-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>办公用品列表</span>
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
        <el-table-column prop="stockQuantity" label="库存数量" width="100">
          <template #default="scope">
            <el-tag :type="getStockTagType(scope.row.stockQuantity, scope.row.minStock)">
              {{ scope.row.stockQuantity }}
            </el-tag>
          </template>
        </el-table-column>
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
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              @click="showDetail(scope.row)"
              :disabled="scope.row.status !== 1"
            >
              查看详情
            </el-button>
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

    <!-- 用品详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="用品详情"
      width="600px"
    >
      <el-descriptions :column="2" border>
        <el-descriptions-item label="用品名称">{{ currentItem?.itemName }}</el-descriptions-item>
        <el-descriptions-item label="分类">{{ currentItem?.categoryName }}</el-descriptions-item>
        <el-descriptions-item label="规格型号">{{ currentItem?.specification || '无' }}</el-descriptions-item>
        <el-descriptions-item label="单位">{{ currentItem?.unit || '无' }}</el-descriptions-item>
        <el-descriptions-item label="库存数量">
          <el-tag :type="getStockTagType(currentItem?.stockQuantity, currentItem?.minStock)">
            {{ currentItem?.stockQuantity }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="最低库存">{{ currentItem?.minStock || 0 }}</el-descriptions-item>
        <el-descriptions-item label="单价">¥{{ currentItem?.price }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="currentItem?.status === 1 ? 'success' : 'danger'">
            {{ currentItem?.status === 1 ? '正常' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">
          {{ currentItem?.description || '暂无描述' }}
        </el-descriptions-item>
      </el-descriptions>
      
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="detailDialogVisible = false">关闭</el-button>
          <el-button 
            type="primary" 
            @click="goToApplication"
            :disabled="currentItem?.status !== 1 || currentItem?.stockQuantity <= 0"
          >
            申请用品
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getSupplyItems, getCategories } from '@/api/supply'

const router = useRouter()

// 响应式数据
const loading = ref(false)
const detailDialogVisible = ref(false)
const currentItem = ref(null)

// 搜索表单
const searchForm = reactive({
  itemName: '',
  categoryId: ''
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

const showDetail = (row) => {
  currentItem.value = row
  detailDialogVisible.value = true
}

const goToApplication = () => {
  detailDialogVisible.value = false
  router.push({
    name: 'Application',
    query: { 
      itemId: currentItem.value.id,
      itemName: currentItem.value.itemName
    }
  })
}

const getStockTagType = (stock, minStock) => {
  if (stock <= 0) return 'danger'
  if (stock <= (minStock || 0)) return 'warning'
  return 'success'
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
.supply-list {
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
