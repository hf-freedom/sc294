<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-select v-model="statusFilter" placeholder="按状态筛选">
          <el-option label="全部" value="" />
          <el-option label="草稿" value="DRAFT" />
          <el-option label="待审核" value="PENDING_REVIEW" />
          <el-option label="审核中" value="UNDER_REVIEW" />
          <el-option label="已批准" value="APPROVED" />
          <el-option label="已发布" value="PUBLISHED" />
          <el-option label="已拒绝" value="REJECTED" />
        </el-select>
      </el-col>
      <el-col :span="8">
        <el-select v-model="categoryFilter" placeholder="按分类筛选">
          <el-option label="全部" value="" />
          <el-option label="技术" value="技术" />
          <el-option label="产品" value="产品" />
          <el-option label="其他" value="其他" />
        </el-select>
      </el-col>
    </el-row>

    <el-table :data="articles" border style="width: 100%; margin-top: 20px;">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="200" />
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="version" label="版本" width="80" />
      <el-table-column prop="score" label="评分" width="80" />
      <el-table-column prop="viewCount" label="浏览量" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="scope">
          {{ formatDate(scope.row.createdAt) }}
        </template>
      </el-table-column>
      <el-table-column prop="referencedByTicket" label="工单引用" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.referencedByTicket" type="danger">是</el-tag>
          <span v-else>否</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="220">
        <template #default="scope">
          <el-button size="small" @click="viewArticle(scope.row.id)">查看</el-button>
          <el-button size="small" type="primary" @click="editArticle(scope.row)">编辑</el-button>
          <el-button 
            v-if="scope.row.status === 'DRAFT'" 
            size="small" 
            type="success" 
            @click="submitReview(scope.row.id)"
          >提交审核</el-button>
          <el-button 
            v-if="scope.row.status === 'PUBLISHED'" 
            size="small" 
            type="warning" 
            @click="showVersions(scope.row.id)"
          >版本管理</el-button>
          <el-button 
            size="small" 
            type="danger" 
            @click="showDeleteConfirm(scope.row)"
            :disabled="scope.row.referencedByTicket"
            :title="scope.row.referencedByTicket ? '文章被工单引用，无法删除' : ''"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog title="版本管理" :visible.sync="showVersionDialog" width="600px">
      <el-table :data="versions" border>
        <el-table-column prop="version" label="版本号" />
        <el-table-column prop="publishedAt" label="发布时间">
          <template #default="scope">{{ formatDate(scope.row.publishedAt) }}</template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="rollback(scope.row.version)">回滚到此版本</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-dialog>

    <el-dialog title="编辑文章" :visible.sync="showEditDialog" width="800px">
      <el-form :model="editForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="editForm.title" />
        </el-form-item>
        <el-form-item label="分类">
          <el-select v-model="editForm.category">
            <el-option label="技术" value="技术" />
            <el-option label="产品" value="产品" />
            <el-option label="其他" value="其他" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容">
          <el-textarea v-model="editForm.content" :rows="10" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="saveEdit">保存</el-button>
      </template>
    </el-dialog>

    <el-dialog title="删除确认" :visible.sync="showDeleteDialog" width="400px">
      <div v-if="deleteArticleData">
        <p style="margin-bottom: 10px;">确定要删除文章「{{ deleteArticleData.title }}」吗？</p>
        <el-alert 
          v-if="deleteArticleData.referencedByTicket" 
          type="error" 
          title="警告" 
          description="该文章被工单引用，无法删除！"
          show-icon 
          style="margin-bottom: 10px;"
        />
      </div>
      <template #footer>
        <el-button @click="showDeleteDialog = false">取消</el-button>
        <el-button 
          v-if="!deleteArticleData?.referencedByTicket" 
          type="danger" 
          @click="confirmDelete"
        >确认删除</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { articleApi } from '../api/article'

const articles = ref([])
const statusFilter = ref('')
const categoryFilter = ref('')
const showVersionDialog = ref(false)
const showEditDialog = ref(false)
const showDeleteDialog = ref(false)
const versions = ref([])
const currentArticleId = ref(null)
const deleteArticleData = ref(null)
const editForm = ref({
  id: null,
  title: '',
  category: '',
  content: ''
})

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

const formatDate = (dateStr) => {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleString('zh-CN')
}

const loadArticles = async () => {
  const params = {}
  if (statusFilter.value) params.status = statusFilter.value
  if (categoryFilter.value) params.category = categoryFilter.value
  
  const response = await articleApi.getAll(params)
  if (response.data.success) {
    articles.value = response.data.data
  }
}

const viewArticle = (id) => {
  window.location.href = `#/article/${id}`
}

const editArticle = (article) => {
  editForm.value = {
    id: article.id,
    title: article.title,
    category: article.category,
    content: article.content
  }
  showEditDialog.value = true
}

const saveEdit = async () => {
  const response = await articleApi.update(editForm.value.id, {
    title: editForm.value.title,
    category: editForm.value.category,
    content: editForm.value.content
  })
  if (response.data.success) {
    showEditDialog.value = false
    loadArticles()
    alert('更新成功')
  }
}

const submitReview = async (id) => {
  const response = await articleApi.submitForReview(id)
  if (response.data.success) {
    loadArticles()
    alert('已提交审核')
  }
}

const showDeleteConfirm = (article) => {
  deleteArticleData.value = article
  showDeleteDialog.value = true
}

const confirmDelete = async () => {
  if (!deleteArticleData.value) return
  
  const response = await articleApi.delete(deleteArticleData.value.id)
  if (response.data.success) {
    showDeleteDialog.value = false
    loadArticles()
    alert('删除成功')
  } else {
    alert(response.data.message)
  }
}

const showVersions = async (id) => {
  currentArticleId.value = id
  const response = await articleApi.getVersions(id)
  if (response.data.success) {
    versions.value = response.data.data
    showVersionDialog.value = true
  }
}

const rollback = async (version) => {
  if (!confirm(`确定要回滚到版本 ${version} 吗？`)) return
  const response = await articleApi.rollback(currentArticleId.value, version)
  if (response.data.success) {
    showVersionDialog.value = false
    loadArticles()
    alert('回滚成功')
  }
}

onMounted(() => {
  loadArticles()
})

watch([statusFilter, categoryFilter], () => {
  loadArticles()
})
</script>