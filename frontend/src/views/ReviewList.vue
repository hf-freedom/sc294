<template>
  <div>
    <div style="margin-bottom: 20px;">
      <el-select v-model="selectedReviewer" placeholder="选择审核员">
        <el-option label="普通审核员 (ID: 2, 3)" value="reviewer" />
        <el-option label="高级审核员 (ID: 4)" value="senior" />
      </el-select>
      <el-button type="primary" @click="loadPendingArticles" style="margin-left: 10px;">筛选</el-button>
    </div>

    <el-table :data="pendingArticles" border style="width: 100%;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="authorId" label="作者ID" width="100" />
      <el-table-column prop="reviewerId" label="审核员ID" width="120" />
      <el-table-column prop="isHot" label="热门/审批级别" width="150">
        <template #default="scope">
          <template v-if="scope.row.isHot">
            <el-tag type="danger">热门文章</el-tag>
            <el-tag type="warning" style="margin-left: 5px;">高级审批</el-tag>
          </template>
          <template v-else>
            <span>普通文章</span>
            <el-tag type="info" style="margin-left: 5px;">普通审批</el-tag>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="scope">{{ formatDate(scope.row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="250">
        <template #default="scope">
          <el-button size="small" @click="viewArticle(scope.row)">查看内容</el-button>
          <el-button 
            size="small" 
            type="success" 
            @click="approveArticle(scope.row)"
            :disabled="scope.row.isHot && selectedReviewer !== 'senior'"
            :title="scope.row.isHot && selectedReviewer !== 'senior' ? '热门文章需要高级审核员审批' : ''"
          >
            {{ scope.row.isHot ? '高级审批' : '通过' }}
          </el-button>
          <el-button size="small" type="danger" @click="rejectArticle(scope.row)">拒绝</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="文章详情" :visible.sync="showDetail" width="800px">
      <div v-if="currentArticle">
        <h3>{{ currentArticle.title }}</h3>
        <div style="display: flex; gap: 20px; margin: 10px 0;">
          <p style="color: #666;">分类：{{ currentArticle.category }}</p>
          <p style="color: #666;">作者ID：{{ currentArticle.authorId }}</p>
          <p style="color: #666;">审核员ID：{{ currentArticle.reviewerId || '未分配' }}</p>
        </div>
        <div style="margin: 10px 0;">
          <span style="color: #666;">是否热门：</span>
          <el-tag v-if="currentArticle.isHot" type="danger">是（需要高级审批）</el-tag>
          <span v-else style="color: #666;">否</span>
        </div>
        <div style="margin-top: 20px; padding: 20px; background-color: #f9fafc;">
          {{ currentArticle.content }}
        </div>
      </div>
      <template #footer>
        <el-button @click="showDetail = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { reviewApi } from '../api/review'
import { articleApi } from '../api/article'

const pendingArticles = ref([])
const showDetail = ref(false)
const currentArticle = ref(null)
const selectedReviewer = ref('reviewer')

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const getReviewerId = () => {
  return selectedReviewer.value === 'senior' ? '4' : '2'
}

const loadPendingArticles = async () => {
  const response = await reviewApi.getPending()
  if (response.data.success) {
    pendingArticles.value = response.data.data
  }
}

const viewArticle = (article) => {
  currentArticle.value = article
  showDetail.value = true
}

const approveArticle = async (article) => {
  if (article.isHot && selectedReviewer.value !== 'senior') {
    alert('热门文章需要高级审核员审批，请切换到高级审核员')
    return
  }
  
  const reviewerId = getReviewerId()
  
  if (!confirm(`确定要通过文章「${article.title}」吗？\n审核员: ${reviewerId === '4' ? '高级审核员' : '普通审核员'}`)) return
  
  const response = await reviewApi.review({
    articleId: article.id,
    reviewerId: reviewerId,
    approved: true
  })
  
  if (response.data.success) {
    loadPendingArticles()
    if (response.data.data.status === 'APPROVED') {
      const publishResponse = await articleApi.publish(article.id, reviewerId)
      if (publishResponse.data.success) {
        alert(article.isHot ? '高级审批通过并已发布' : '审核通过并已发布')
      }
    }
  } else {
    alert(response.data.message)
  }
}

const rejectArticle = async (article) => {
  const reviewerId = getReviewerId()
  
  if (!confirm(`确定要拒绝文章「${article.title}」吗？`)) return
  
  const response = await reviewApi.review({
    articleId: article.id,
    reviewerId: reviewerId,
    approved: false
  })
  
  if (response.data.success) {
    loadPendingArticles()
    alert('已拒绝，文章已退回修改')
  } else {
    alert(response.data.message)
  }
}

onMounted(() => {
  loadPendingArticles()
})
</script>