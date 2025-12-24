<template>
  <div class="test-api">
    <h2>API测试页面</h2>
    
    <el-card class="test-card">
      <template #header>
        <span>测试后端连接</span>
      </template>
      
      <el-button type="primary" @click="testGet" :loading="loading">测试GET请求</el-button>
      <el-button type="success" @click="testPost" :loading="loading">测试POST请求</el-button>
      
      <div v-if="result" class="result">
        <h4>响应结果：</h4>
        <pre>{{ JSON.stringify(result, null, 2) }}</pre>
      </div>
      
      <div v-if="error" class="error">
        <h4>错误信息：</h4>
        <pre>{{ error }}</pre>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/api/request'

const loading = ref(false)
const result = ref(null)
const error = ref(null)

const testGet = async () => {
  loading.value = true
  error.value = null
  result.value = null
  
  try {
    const response = await request({
      url: '/test/hello',
      method: 'get'
    })
    result.value = response
    ElMessage.success('GET请求成功')
  } catch (err) {
    error.value = err.message || err.toString()
    ElMessage.error('GET请求失败')
  } finally {
    loading.value = false
  }
}

const testPost = async () => {
  loading.value = true
  error.value = null
  result.value = null
  
  try {
    const response = await request({
      url: '/test/echo',
      method: 'post',
      data: { message: 'Hello from Vue!' }
    })
    result.value = response
    ElMessage.success('POST请求成功')
  } catch (err) {
    error.value = err.message || err.toString()
    ElMessage.error('POST请求失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.test-api {
  padding: 20px;
}

.test-card {
  max-width: 800px;
  margin: 20px auto;
}

.result, .error {
  margin-top: 20px;
  padding: 15px;
  border-radius: 4px;
}

.result {
  background: #f0f9ff;
  border: 1px solid #b3d8ff;
}

.error {
  background: #fef0f0;
  border: 1px solid #fbc4c4;
}

pre {
  background: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  overflow-x: auto;
}
</style>
