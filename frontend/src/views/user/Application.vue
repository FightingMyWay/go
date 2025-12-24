<template>
  <div class="application-page">
    <div class="page-header">
      <h2>申领申请</h2>
      <p class="page-desc">填写申领申请表单，提交给管理员审核</p>
    </div>

    <el-card class="application-form-card">
      <el-form
        ref="applicationFormRef"
        :model="applicationForm"
        :rules="applicationRules"
        label-width="120px"
        class="application-form"
      >
        <!-- 基本信息 -->
        <div class="form-section">
          <h3 class="section-title">基本信息</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申请人" prop="applicantName">
                <el-input v-model="applicationForm.applicantName" disabled />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请部门" prop="departmentName">
                <el-input v-model="applicationForm.departmentName" disabled />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="联系电话" prop="phone">
                <el-input v-model="applicationForm.phone" placeholder="请输入联系电话" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="申请日期" prop="applyDate">
                <el-date-picker
                  v-model="applicationForm.applyDate"
                  type="date"
                  placeholder="选择申请日期"
                  format="YYYY-MM-DD"
                  value-format="YYYY-MM-DD"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 用品信息 -->
        <div class="form-section">
          <h3 class="section-title">用品信息</h3>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="用品分类" prop="categoryId">
                <el-select
                  v-model="applicationForm.categoryId"
                  placeholder="选择用品分类"
                  @change="handleCategoryChange"
                  style="width: 100%"
                >
                  <el-option
                    v-for="category in categories"
                    :key="category.id"
                    :label="category.name"
                    :value="category.id"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="用品名称" prop="itemId">
                <el-select
                  v-model="applicationForm.itemId"
                  placeholder="选择用品"
                  @change="handleItemChange"
                  style="width: 100%"
                  :disabled="!applicationForm.categoryId"
                >
                  <el-option
                    v-for="item in filteredItems"
                    :key="item.id"
                    :label="`${item.name} (库存: ${item.stock})`"
                    :value="item.id"
                    :disabled="item.stock <= 0"
                  />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="申领数量" prop="quantity">
                <el-input-number
                  v-model="applicationForm.quantity"
                  :min="1"
                  :max="maxQuantity"
                  controls-position="right"
                  style="width: 100%"
                  placeholder="请输入申领数量"
                />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="紧急程度" prop="urgency">
                <el-select v-model="applicationForm.urgency" placeholder="选择紧急程度" style="width: 100%">
                  <el-option label="普通" value="NORMAL" />
                  <el-option label="紧急" value="URGENT" />
                  <el-option label="非常紧急" value="VERY_URGENT" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="20">
            <el-col :span="24">
              <el-form-item label="用品规格" prop="specification">
                <el-input
                  v-model="applicationForm.specification"
                  placeholder="请输入用品规格要求（可选）"
                />
              </el-form-item>
            </el-col>
          </el-row>
        </div>

        <!-- 申请原因 -->
        <div class="form-section">
          <h3 class="section-title">申请原因</h3>
          <el-form-item label="申领原因" prop="reason">
            <el-input
              v-model="applicationForm.reason"
              type="textarea"
              :rows="4"
              placeholder="请详细说明申领原因和用途"
              maxlength="500"
              show-word-limit
            />
          </el-form-item>
        </div>

        <!-- 附件上传 -->
        <div class="form-section">
          <h3 class="section-title">附件上传</h3>
          <el-form-item label="相关附件">
            <el-upload
              ref="uploadRef"
              :action="uploadUrl"
              :headers="uploadHeaders"
              :file-list="fileList"
              :on-success="handleUploadSuccess"
              :on-error="handleUploadError"
              :on-remove="handleFileRemove"
              :before-upload="beforeUpload"
              multiple
              :limit="5"
              list-type="text"
            >
              <el-button type="primary">
                <el-icon><Upload /></el-icon>
                选择文件
              </el-button>
              <template #tip>
                <div class="el-upload__tip">
                  支持 jpg/png/pdf/doc/docx 格式，单个文件不超过 10MB
                </div>
              </template>
            </el-upload>
          </el-form-item>
        </div>

        <!-- 备注信息 -->
        <div class="form-section">
          <h3 class="section-title">备注信息</h3>
          <el-form-item label="备注">
            <el-input
              v-model="applicationForm.remark"
              type="textarea"
              :rows="3"
              placeholder="其他需要说明的信息（可选）"
              maxlength="200"
              show-word-limit
            />
          </el-form-item>
        </div>

        <!-- 表单操作 -->
        <div class="form-actions">
          <el-button @click="resetForm">重置</el-button>
          <el-button type="primary" @click="submitApplication" :loading="submitting">
            提交申请
          </el-button>
          <el-button type="info" @click="saveDraft" :loading="savingDraft">
            保存草稿
          </el-button>
        </div>
      </el-form>
    </el-card>

    <!-- 申请预览对话框 -->
    <el-dialog
      v-model="previewDialogVisible"
      title="申请预览"
      width="800px"
      :close-on-click-modal="false"
    >
      <div class="preview-content">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="申请人">{{ applicationForm.applicantName }}</el-descriptions-item>
          <el-descriptions-item label="申请部门">{{ applicationForm.departmentName }}</el-descriptions-item>
          <el-descriptions-item label="联系电话">{{ applicationForm.phone }}</el-descriptions-item>
          <el-descriptions-item label="申请日期">{{ applicationForm.applyDate }}</el-descriptions-item>
          <el-descriptions-item label="用品名称">{{ selectedItemName }}</el-descriptions-item>
          <el-descriptions-item label="申领数量">{{ applicationForm.quantity }}</el-descriptions-item>
          <el-descriptions-item label="紧急程度">{{ urgencyLabel }}</el-descriptions-item>
          <el-descriptions-item label="用品规格">{{ applicationForm.specification || '无' }}</el-descriptions-item>
          <el-descriptions-item label="申领原因" :span="2">{{ applicationForm.reason }}</el-descriptions-item>
          <el-descriptions-item label="备注" :span="2">{{ applicationForm.remark || '无' }}</el-descriptions-item>
        </el-descriptions>
        
        <div v-if="fileList.length > 0" class="preview-files">
          <h4>附件列表：</h4>
          <ul>
            <li v-for="file in fileList" :key="file.uid">{{ file.name }}</li>
          </ul>
        </div>
      </div>
      <template #footer>
        <el-button @click="previewDialogVisible = false">返回修改</el-button>
        <el-button type="primary" @click="confirmSubmit" :loading="submitting">
          确认提交
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { getCategories, getSupplyItems } from '@/api/supply'
import { createApplication } from '@/api/application'

export default {
  name: 'Application',
  components: {
    Upload
  },
  setup() {
    const userStore = useUserStore()
    const applicationFormRef = ref()
    const uploadRef = ref()
    
    const applicationForm = reactive({
      applicantName: userStore.userInfo?.realName || '',
      departmentName: userStore.userInfo?.departmentName || '',
      phone: userStore.userInfo?.phone || '',
      applyDate: new Date().toISOString().split('T')[0],
      categoryId: '',
      itemId: '',
      quantity: 1,
      urgency: 'NORMAL',
      specification: '',
      reason: '',
      remark: '',
      attachments: []
    })

    const applicationRules = {
      phone: [
        { required: true, message: '请输入联系电话', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
      ],
      applyDate: [
        { required: true, message: '请选择申请日期', trigger: 'change' }
      ],
      categoryId: [
        { required: true, message: '请选择用品分类', trigger: 'change' }
      ],
      itemId: [
        { required: true, message: '请选择用品', trigger: 'change' }
      ],
      quantity: [
        { required: true, message: '请输入申领数量', trigger: 'blur' },
        { type: 'number', min: 1, message: '数量必须大于0', trigger: 'blur' }
      ],
      urgency: [
        { required: true, message: '请选择紧急程度', trigger: 'change' }
      ],
      reason: [
        { required: true, message: '请输入申领原因', trigger: 'blur' },
        { min: 10, max: 500, message: '原因长度在10到500个字符', trigger: 'blur' }
      ]
    }

    const categories = ref([])
    const supplyItems = ref([])
    const filteredItems = ref([])
    const fileList = ref([])
    const submitting = ref(false)
    const savingDraft = ref(false)
    const previewDialogVisible = ref(false)
    const maxQuantity = ref(1)

    const uploadUrl = `${import.meta.env.VITE_API_BASE_URL}/api/upload`
    const uploadHeaders = computed(() => ({
      Authorization: `Bearer ${userStore.token}`
    }))

    const selectedItemName = computed(() => {
      const item = supplyItems.value.find(item => item.id === applicationForm.itemId)
      return item ? item.name : ''
    })

    const urgencyLabel = computed(() => {
      const urgencyMap = {
        'NORMAL': '普通',
        'URGENT': '紧急',
        'VERY_URGENT': '非常紧急'
      }
      return urgencyMap[applicationForm.urgency] || '普通'
    })

    // 获取分类列表
    const fetchCategories = async () => {
      try {
        const response = await getCategories()
        categories.value = response.data
      } catch (error) {
        ElMessage.error('获取分类列表失败')
      }
    }

    // 获取用品列表
    const fetchSupplyItems = async () => {
      try {
        const response = await getSupplyItems({ page: 1, size: 1000 })
        supplyItems.value = response.data.records
      } catch (error) {
        ElMessage.error('获取用品列表失败')
      }
    }

    // 分类改变
    const handleCategoryChange = () => {
      applicationForm.itemId = ''
      filteredItems.value = supplyItems.value.filter(item => 
        item.categoryId === applicationForm.categoryId
      )
    }

    // 用品改变
    const handleItemChange = () => {
      const item = supplyItems.value.find(item => item.id === applicationForm.itemId)
      if (item) {
        maxQuantity.value = item.stock
        applicationForm.quantity = Math.min(applicationForm.quantity, item.stock)
      }
    }

    // 文件上传成功
    const handleUploadSuccess = (response, file) => {
      if (response.code === 200) {
        file.url = response.data.url
        applicationForm.attachments.push({
          name: file.name,
          url: response.data.url,
          size: file.size
        })
        ElMessage.success('文件上传成功')
      } else {
        ElMessage.error('文件上传失败')
      }
    }

    // 文件上传失败
    const handleUploadError = () => {
      ElMessage.error('文件上传失败')
    }

    // 文件移除
    const handleFileRemove = (file) => {
      const index = applicationForm.attachments.findIndex(item => item.name === file.name)
      if (index > -1) {
        applicationForm.attachments.splice(index, 1)
      }
    }

    // 上传前检查
    const beforeUpload = (file) => {
      const isValidType = ['image/jpeg', 'image/png', 'application/pdf', 'application/msword', 'application/vnd.openxmlformats-officedocument.wordprocessingml.document'].includes(file.type)
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isValidType) {
        ElMessage.error('只能上传 JPG/PNG/PDF/DOC/DOCX 格式的文件!')
        return false
      }
      if (!isLt10M) {
        ElMessage.error('文件大小不能超过 10MB!')
        return false
      }
      return true
    }

    // 重置表单
    const resetForm = () => {
      applicationFormRef.value.resetFields()
      fileList.value = []
      applicationForm.attachments = []
      applicationForm.applyDate = new Date().toISOString().split('T')[0]
    }

    // 保存草稿
    const saveDraft = async () => {
      try {
        savingDraft.value = true
        // 暂时保存到localStorage，后续可以对接后端API
        const draft = {
          ...applicationForm,
          status: 'DRAFT',
          id: Date.now(),
          createTime: new Date().toISOString()
        }
        const drafts = JSON.parse(localStorage.getItem('applicationDrafts') || '[]')
        drafts.push(draft)
        localStorage.setItem('applicationDrafts', JSON.stringify(drafts))
        ElMessage.success('草稿保存成功')
      } catch (error) {
        ElMessage.error('草稿保存失败')
      } finally {
        savingDraft.value = false
      }
    }

    // 提交申请
    const submitApplication = async () => {
      try {
        await applicationFormRef.value.validate()
        previewDialogVisible.value = true
      } catch (error) {
        ElMessage.error('请完善表单信息')
      }
    }

    // 确认提交
    const confirmSubmit = async () => {
      try {
        submitting.value = true
        await createApplication({
          ...applicationForm,
          status: 'PENDING'
        })
        ElMessage.success('申请提交成功')
        previewDialogVisible.value = false
        resetForm()
      } catch (error) {
        if (error.message) {
          ElMessage.error(error.message)
        } else {
          ElMessage.error('申请提交失败')
        }
      } finally {
        submitting.value = false
      }
    }

    onMounted(() => {
      fetchCategories()
      fetchSupplyItems()
    })

    return {
      applicationFormRef,
      uploadRef,
      applicationForm,
      applicationRules,
      categories,
      filteredItems,
      fileList,
      submitting,
      savingDraft,
      previewDialogVisible,
      maxQuantity,
      uploadUrl,
      uploadHeaders,
      selectedItemName,
      urgencyLabel,
      handleCategoryChange,
      handleItemChange,
      handleUploadSuccess,
      handleUploadError,
      handleFileRemove,
      beforeUpload,
      resetForm,
      saveDraft,
      submitApplication,
      confirmSubmit
    }
  }
}
</script>

<style lang="scss" scoped>
.application-page {
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

  .application-form-card {
    .application-form {
      .form-section {
        margin-bottom: 30px;

        .section-title {
          margin: 0 0 20px 0;
          padding-bottom: 10px;
          border-bottom: 2px solid #409eff;
          color: #303133;
          font-size: 16px;
        }
      }

      .form-actions {
        text-align: center;
        margin-top: 40px;
        padding-top: 20px;
        border-top: 1px solid #ebeef5;

        .el-button {
          margin: 0 10px;
          min-width: 100px;
        }
      }
    }
  }

  .preview-content {
    .preview-files {
      margin-top: 20px;
      padding: 15px;
      background: #f5f7fa;
      border-radius: 4px;

      h4 {
        margin: 0 0 10px 0;
        color: #303133;
      }

      ul {
        margin: 0;
        padding-left: 20px;
        color: #606266;

        li {
          margin-bottom: 5px;
        }
      }
    }
  }
}
</style>
