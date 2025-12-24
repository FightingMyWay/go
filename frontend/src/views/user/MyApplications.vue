<template>
  <div class="my-applications">
    <div class="page-header">
      <h2>我的申请</h2>
      <p class="page-desc">查看和管理您的所有申领申请</p>
    </div>

    <!-- 搜索和筛选 -->
    <el-card class="search-card">
      <el-form :model="searchForm" inline>
        <el-form-item label="申请状态">
          <el-select v-model="searchForm.status" placeholder="选择状态" clearable>
            <el-option label="全部" value="" />
            <el-option label="草稿" value="DRAFT" />
            <el-option label="待审核" value="PENDING" />
            <el-option label="审核通过" value="APPROVED" />
            <el-option label="审核拒绝" value="REJECTED" />
            <el-option label="已发放" value="ISSUED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="申请日期">
          <el-date-picker
            v-model="searchForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="关键词">
          <el-input
            v-model="searchForm.keyword"
            placeholder="搜索用品名称或申请原因"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <el-icon><Search /></el-icon>
            搜索
          </el-button>
          <el-button @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 申请列表 -->
    <el-card class="applications-card">
      <template #header>
        <div class="card-header">
          <span>申请列表</span>
          <el-button type="primary" @click="$router.push('/user/application')">
            <el-icon><Plus /></el-icon>
            新建申请
          </el-button>
        </div>
      </template>

      <el-table
        v-loading="loading"
        :data="applications"
        style="width: 100%"
        @sort-change="handleSortChange"
      >
        <el-table-column prop="id" label="申请编号" width="120" sortable="custom" />
        <el-table-column prop="itemName" label="用品名称" min-width="150" />
        <el-table-column prop="quantity" label="数量" width="80" align="center" />
        <el-table-column prop="urgency" label="紧急程度" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getUrgencyType(row.urgency)">
              {{ getUrgencyLabel(row.urgency) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="applyDate" label="申请日期" width="120" sortable="custom" />
        <el-table-column prop="reviewDate" label="审核日期" width="120" />
        <el-table-column prop="reviewerName" label="审核人" width="100" />
        <el-table-column prop="reviewRemark" label="审核备注" min-width="150" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button
              type="primary"
              size="small"
              @click="viewApplication(row)"
            >
              查看
            </el-button>
            <el-button
              v-if="row.status === 'DRAFT'"
              type="warning"
              size="small"
              @click="editApplication(row)"
            >
              编辑
            </el-button>
            <el-button
              v-if="row.status === 'PENDING'"
              type="danger"
              size="small"
              @click="cancelApplication(row)"
            >
              取消
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

    <!-- 申请详情对话框 -->
    <el-dialog
      v-model="detailDialogVisible"
      title="申请详情"
      width="800px"
      :close-on-click-modal="false"
    >
      <div v-if="selectedApplication" class="application-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请编号">{{ selectedApplication.id }}</el-descriptions-item>
          <el-descriptions-item label="申请状态">
            <el-tag :type="getStatusType(selectedApplication.status)">
              {{ getStatusLabel(selectedApplication.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请人">{{ selectedApplication.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="申请部门">{{ selectedApplication.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ selectedApplication.phone }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ selectedApplication.applyDate }}</el-descriptions-item>
          <el-descriptions-item label="用品名称">{{ selectedApplication.itemName }}</el-descriptions-item>
          <el-descriptions-item label="申领数量">{{ selectedApplication.quantity }}</el-descriptions-item>
          <el-descriptions-item label="紧急程度">
            <el-tag :type="getUrgencyType(selectedApplication.urgency)">
              {{ getUrgencyLabel(selectedApplication.urgency) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="用品规格">{{ selectedApplication.specification || '无' }}</el-descriptions-item>
          <el-descriptions-item label="申领原因" :span="2">{{ selectedApplication.reason }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ selectedApplication.remark || '无' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 审核信息 -->
        <div v-if="selectedApplication.status !== 'DRAFT'" class="review-info">
          <h4>审核信息</h4>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="审核人">{{ selectedApplication.reviewerName || '无' }}</el-descriptions-item>
            <el-descriptions-item label="审核日期">{{ selectedApplication.reviewDate || '无' }}</el-descriptions-item>
            <el-descriptions-item label="审核备注" :span="2">{{ selectedApplication.reviewRemark || '无' }}</el-descriptions-item>
          </el-descriptions>
        </div>

        <!-- 附件列表 -->
        <div v-if="selectedApplication.attachments && selectedApplication.attachments.length > 0" class="attachments">
          <h4>附件列表</h4>
          <ul class="attachment-list">
            <li v-for="attachment in selectedApplication.attachments" :key="attachment.id">
              <el-link :href="attachment.url" target="_blank" type="primary">
                {{ attachment.name }}
              </el-link>
              <span class="file-size">({{ formatFileSize(attachment.size) }})</span>
            </li>
          </ul>
        </div>
      </div>
    </el-dialog>

    <!-- 取消申请确认对话框 -->
    <el-dialog
      v-model="cancelDialogVisible"
      title="取消申请"
      width="400px"
    >
      <p>确定要取消申请 "{{ cancelApplicationName }}" 吗？</p>
      <el-form :model="cancelForm" label-width="80px">
        <el-form-item label="取消原因">
          <el-input
            v-model="cancelForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入取消原因"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancelDialogVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmCancel" :loading="cancelling">
          确认取消
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search, Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getMyApplications, cancelApplication as cancelApp } from '@/api/application'

export default {
  name: 'MyApplications',
  components: {
    Search,
    Plus
  },
  setup() {
    const userStore = useUserStore()
    
    const searchForm = reactive({
      status: '',
      dateRange: [],
      keyword: ''
    })

    const pagination = reactive({
      current: 1,
      size: 20,
      total: 0
    })

    const applications = ref([])
    const loading = ref(false)
    const detailDialogVisible = ref(false)
    const selectedApplication = ref(null)
    const cancelDialogVisible = ref(false)
    const cancelApplicationName = ref('')
    const cancelling = ref(false)

    const cancelForm = reactive({
      reason: ''
    })

    // 获取我的申请列表
    const fetchApplications = async () => {
      loading.value = true
      try {
        const params = {
          page: pagination.current,
          size: pagination.size,
          status: searchForm.status,
          keyword: searchForm.keyword,
          startDate: searchForm.dateRange[0],
          endDate: searchForm.dateRange[1]
        }
        const response = await getMyApplications(params)
        applications.value = response.data.records
        pagination.total = response.data.total
      } catch (error) {
        ElMessage.error('获取申请列表失败')
      } finally {
        loading.value = false
      }
    }

    // 搜索
    const handleSearch = () => {
      pagination.current = 1
      fetchApplications()
    }

    // 重置搜索
    const resetSearch = () => {
      searchForm.status = ''
      searchForm.dateRange = []
      searchForm.keyword = ''
      pagination.current = 1
      fetchApplications()
    }

    // 分页大小改变
    const handleSizeChange = (size) => {
      pagination.size = size
      pagination.current = 1
      fetchApplications()
    }

    // 当前页改变
    const handleCurrentChange = (page) => {
      pagination.current = page
      fetchApplications()
    }

    // 排序改变
    const handleSortChange = ({ prop, order }) => {
      // 这里可以添加排序逻辑
      fetchApplications()
    }

    // 查看申请详情
    const viewApplication = (row) => {
      selectedApplication.value = row
      detailDialogVisible.value = true
    }

    // 编辑申请
    const editApplication = (row) => {
      // 跳转到编辑页面，传递申请ID
      this.$router.push(`/user/application/edit/${row.id}`)
    }

    // 取消申请
    const cancelApplication = (row) => {
      cancelApplicationName.value = row.itemName
      cancelForm.reason = ''
      cancelDialogVisible.value = true
    }

    // 确认取消
    const confirmCancel = async () => {
      if (!cancelForm.reason.trim()) {
        ElMessage.warning('请输入取消原因')
        return
      }

      try {
        cancelling.value = true
        await cancelApp({
          applicationId: selectedApplication.value.id,
          reason: cancelForm.reason
        })
        ElMessage.success('申请取消成功')
        cancelDialogVisible.value = false
        fetchApplications()
      } catch (error) {
        ElMessage.error('申请取消失败')
      } finally {
        cancelling.value = false
      }
    }

    // 获取紧急程度标签类型
    const getUrgencyType = (urgency) => {
      const typeMap = {
        'NORMAL': '',
        'URGENT': 'warning',
        'VERY_URGENT': 'danger'
      }
      return typeMap[urgency] || ''
    }

    // 获取紧急程度标签文本
    const getUrgencyLabel = (urgency) => {
      const labelMap = {
        'NORMAL': '普通',
        'URGENT': '紧急',
        'VERY_URGENT': '非常紧急'
      }
      return labelMap[urgency] || '普通'
    }

    // 获取状态标签类型
    const getStatusType = (status) => {
      const typeMap = {
        'DRAFT': 'info',
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger',
        'ISSUED': 'success',
        'CANCELLED': 'info'
      }
      return typeMap[status] || ''
    }

    // 获取状态标签文本
    const getStatusLabel = (status) => {
      const labelMap = {
        'DRAFT': '草稿',
        'PENDING': '待审核',
        'APPROVED': '审核通过',
        'REJECTED': '审核拒绝',
        'ISSUED': '已发放',
        'CANCELLED': '已取消'
      }
      return labelMap[status] || '未知'
    }

    // 格式化文件大小
    const formatFileSize = (bytes) => {
      if (bytes === 0) return '0 B'
      const k = 1024
      const sizes = ['B', 'KB', 'MB', 'GB']
      const i = Math.floor(Math.log(bytes) / Math.log(k))
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
    }

    onMounted(() => {
      fetchApplications()
    })

    return {
      searchForm,
      pagination,
      applications,
      loading,
      detailDialogVisible,
      selectedApplication,
      cancelDialogVisible,
      cancelApplicationName,
      cancelling,
      cancelForm,
      handleSearch,
      resetSearch,
      handleSizeChange,
      handleCurrentChange,
      handleSortChange,
      viewApplication,
      editApplication,
      cancelApplication,
      confirmCancel,
      getUrgencyType,
      getUrgencyLabel,
      getStatusType,
      getStatusLabel,
      formatFileSize
    }
  }
}
</script>

<style lang="scss" scoped>
.my-applications {
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

  .search-card {
    margin-bottom: 20px;
  }

  .applications-card {
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
  }

  .pagination-wrapper {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .application-detail {
    .review-info {
      margin-top: 20px;

      h4 {
        margin: 0 0 15px 0;
        color: #303133;
        padding-bottom: 10px;
        border-bottom: 1px solid #ebeef5;
      }
    }

    .attachments {
      margin-top: 20px;

      h4 {
        margin: 0 0 15px 0;
        color: #303133;
        padding-bottom: 10px;
        border-bottom: 1px solid #ebeef5;
      }

      .attachment-list {
        margin: 0;
        padding-left: 20px;
        color: #606266;

        li {
          margin-bottom: 8px;
          display: flex;
          align-items: center;
          gap: 10px;

          .file-size {
            color: #909399;
            font-size: 12px;
          }
        }
      }
    }
  }
}
</style>
