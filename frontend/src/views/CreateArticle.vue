<template>
  <div>
    <el-card class="status-card" v-if="articleId">
      <div class="status-info">
        <el-tag type="info">草稿</el-tag>
        <span class="article-id">文章ID: {{ articleId }}</span>
        <span class="save-time">最后保存: {{ lastSaveTime }}</span>
      </div>
    </el-card>

    <el-form :model="form" label-width="80px" class="article-form">
      <el-form-item label="标题" required>
        <el-input v-model="form.title" placeholder="请输入文章标题" />
      </el-form-item>
      <el-form-item label="分类" required>
        <el-select v-model="form.category" placeholder="请选择分类">
          <el-option label="技术" value="技术" />
          <el-option label="产品" value="产品" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-form-item>
      <el-form-item label="内容" required>
        <el-textarea v-model="form.content" :rows="15" placeholder="请输入文章内容" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitArticle">提交审核</el-button>
        <el-button @click="saveDraft">保存草稿</el-button>
        <el-button @click="goBack">取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { articleApi } from '../api/article'

const form = ref({
  title: '',
  category: '',
  content: '',
  authorId: '1'
})

const articleId = ref(null)
const lastSaveTime = ref('')

const saveDraft = async () => {
  if (!form.value.title) {
    alert('请至少填写标题')
    return
  }

  let response
  if (articleId.value) {
    response = await articleApi.update(articleId.value, {
      title: form.value.title,
      category: form.value.category || '其他',
      content: form.value.content
    })
  } else {
    response = await articleApi.create(form.value)
    if (response.data.success) {
      articleId.value = response.data.data.id
    }
  }

  if (response.data.success) {
    lastSaveTime.value = new Date().toLocaleString('zh-CN')
    alert('草稿保存成功')
  }
}

const submitArticle = async () => {
  if (!form.value.title || !form.value.category || !form.value.content) {
    alert('请填写完整信息')
    return
  }

  let response
  if (articleId.value) {
    response = await articleApi.update(articleId.value, {
      title: form.value.title,
      category: form.value.category,
      content: form.value.content
    })
  } else {
    response = await articleApi.create(form.value)
    if (response.data.success) {
      articleId.value = response.data.data.id
    }
  }

  if (response.data.success) {
    const submitResponse = await articleApi.submitForReview(articleId.value)
    if (submitResponse.data.success) {
      alert('文章已提交审核')
      window.location.href = '#/'
    }
  }
}

const goBack = () => {
  if (articleId.value || form.value.title || form.value.content) {
    if (confirm('当前有未保存的内容，确定要离开吗？')) {
      window.location.href = '#/'
    }
  } else {
    window.location.href = '#/'
  }
}
</script>

<style scoped>
.article-form {
  max-width: 800px;
  margin: 0 auto;
  margin-top: 20px;
}

.status-card {
  max-width: 800px;
  margin: 0 auto;
  background-color: #f5f7fa;
}

.status-info {
  display: flex;
  align-items: center;
  gap: 20px;
}

.article-id {
  color: #666;
  font-size: 14px;
}

.save-time {
  color: #999;
  font-size: 12px;
}
</style>