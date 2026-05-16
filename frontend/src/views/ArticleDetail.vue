<template>
  <div v-if="article" class="article-detail">
    <el-card>
      <template #header>
        <span>{{ article.title }}</span>
      </template>
      <el-row :gutter="20" style="margin-bottom: 20px;">
        <el-col :span="4">
          <span class="label">分类：</span>{{ article.category }}
        </el-col>
        <el-col :span="4">
          <span class="label">状态：</span>
          <el-tag :type="getStatusType(article.status)">{{ getStatusText(article.status) }}</el-tag>
        </el-col>
        <el-col :span="4">
          <span class="label">版本：</span>{{ article.version }}
        </el-col>
        <el-col :span="4">
          <span class="label">评分：</span>{{ article.score }}
        </el-col>
        <el-col :span="4">
          <span class="label">浏览量：</span>{{ article.viewCount }}
        </el-col>
      </el-row>
      <div class="content">
        {{ article.content }}
      </div>
      <div style="margin-top: 20px;">
        <el-button @click="goBack">返回列表</el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { articleApi } from '../api/article'

const route = useRoute()
const article = ref(null)

const getStatusText = (status) => {
  const map = {
    DRAFT: '草稿',
    PENDING_REVIEW: '待审核',
    UNDER_REVIEW: '审核中',
    APPROVED: '已批准',
    PUBLISHED: '已发布',
    REJECTED: '已拒绝'
  }
  return map[status] || status
}

const getStatusType = (status) => {
  const map = {
    DRAFT: 'info',
    PENDING_REVIEW: 'warning',
    UNDER_REVIEW: 'primary',
    APPROVED: 'success',
    PUBLISHED: 'success',
    REJECTED: 'danger'
  }
  return map[status] || 'info'
}

const loadArticle = async () => {
  const id = route.params.id
  const response = await articleApi.getById(id)
  if (response.data.success) {
    article.value = response.data.data
  }
}

const goBack = () => {
  window.location.href = '#/'
}

onMounted(() => {
  loadArticle()
})
</script>

<style scoped>
.article-detail {
  max-width: 1000px;
}

.label {
  color: #666;
}

.content {
  padding: 20px;
  background-color: #f9fafc;
  border-radius: 4px;
  min-height: 300px;
  white-space: pre-wrap;
}
</style>