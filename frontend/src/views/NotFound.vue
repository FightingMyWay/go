<template>
  <div class="not-found">
    <div class="not-found-content">
      <div class="error-code">404</div>
      <div class="error-title">页面不存在</div>
      <div class="error-description">
        抱歉，您访问的页面不存在或已被删除
      </div>
      <div class="error-actions">
        <el-button type="primary" @click="$router.push('/')">
          <el-icon><House /></el-icon>
          返回首页
        </el-button>
        <el-button @click="$router.go(-1)">
          <el-icon><Back /></el-icon>
          返回上页
        </el-button>
      </div>
    </div>
    
    <div class="not-found-illustration">
      <div class="illustration-content">
        <div class="search-icon">
          <el-icon><Search /></el-icon>
        </div>
        <div class="question-mark">?</div>
      </div>
    </div>
  </div>
</template>

<script setup>
// 无需额外的逻辑
</script>

<style scoped lang="scss">
.not-found {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
  
  .not-found-content {
    text-align: center;
    color: #fff;
    max-width: 500px;
    margin-right: 60px;
    
    .error-code {
      font-size: 120px;
      font-weight: 700;
      line-height: 1;
      margin-bottom: 20px;
      text-shadow: 0 4px 8px rgba(0, 0, 0, 0.3);
    }
    
    .error-title {
      font-size: 32px;
      font-weight: 600;
      margin-bottom: 16px;
      text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
    }
    
    .error-description {
      font-size: 16px;
      margin-bottom: 32px;
      opacity: 0.9;
      line-height: 1.6;
    }
    
    .error-actions {
      display: flex;
      gap: 16px;
      justify-content: center;
      
      .el-button {
        padding: 12px 24px;
        font-size: 16px;
        border: 2px solid rgba(255, 255, 255, 0.3);
        background: rgba(255, 255, 255, 0.1);
        color: #fff;
        
        &:hover {
          background: rgba(255, 255, 255, 0.2);
          border-color: rgba(255, 255, 255, 0.5);
        }
        
        .el-icon {
          margin-right: 8px;
        }
      }
    }
  }
  
  .not-found-illustration {
    .illustration-content {
      position: relative;
      width: 200px;
      height: 200px;
      
      .search-icon {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        font-size: 80px;
        color: rgba(255, 255, 255, 0.8);
        animation: float 3s ease-in-out infinite;
      }
      
      .question-mark {
        position: absolute;
        top: 20%;
        right: 20%;
        font-size: 60px;
        font-weight: 700;
        color: rgba(255, 255, 255, 0.6);
        animation: bounce 2s ease-in-out infinite;
      }
    }
  }
}

@keyframes float {
  0%, 100% {
    transform: translate(-50%, -50%) translateY(0px);
  }
  50% {
    transform: translate(-50%, -50%) translateY(-20px);
  }
}

@keyframes bounce {
  0%, 100% {
    transform: scale(1);
  }
  50% {
    transform: scale(1.1);
  }
}

// 响应式设计
@media (max-width: 768px) {
  .not-found {
    flex-direction: column;
    text-align: center;
    
    .not-found-content {
      margin-right: 0;
      margin-bottom: 40px;
      
      .error-code {
        font-size: 80px;
      }
      
      .error-title {
        font-size: 24px;
      }
      
      .error-actions {
        flex-direction: column;
        align-items: center;
        
        .el-button {
          width: 200px;
        }
      }
    }
    
    .not-found-illustration {
      .illustration-content {
        width: 150px;
        height: 150px;
        
        .search-icon {
          font-size: 60px;
        }
        
        .question-mark {
          font-size: 40px;
        }
      }
    }
  }
}
</style>
