<template>
  <div>
    <el-row :gutter="20" style="margin-bottom: 20px;">
      <el-col :span="8">
        <el-button type="primary" @click="checkLowScore">检查低评分文章</el-button>
      </el-col>
      <el-col :span="8">
        <el-select v-model="statusFilter" placeholder="按状态筛选">
          <el-option label="全部" value="" />
          <el-option label="待处理" value="PENDING" />
          <el-option label="处理中" value="PROCESSING" />
          <el-option label="已完成" value="COMPLETED" />
        </el-select>
      </el-col>
    </el-row>

    <el-table :data="tasks" border style="width: 100%;">
      <el-table-column prop="id" label="任务ID" width="100" />
      <el-table-column prop="articleTitle" label="文章标题" min-width="200" />
      <el-table-column prop="currentScore" label="当前评分" width="100" />
      <el-table-column prop="reason" label="原因" min-width="200" />
      <el-table-column prop="status" label="状态" width="120">
        <template #default="scope">
          <el-tag :type="getStatusType(scope.row.status)">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="创建时间" width="180">
        <template #default="scope">{{ formatDate(scope.row.createdAt) }}</template>
      </el-table-column>
      <el-table-column prop="completedAt" label="完成时间" width="180">
        <template #default="scope">{{ formatDate(scope.row.completedAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="150">
        <template #default="scope">
          <el-button 
            v-if="scope.row.status === 'PENDING'" 
            size="small" 
            type="success" 
            @click="completeTask(scope.row.id)"
          >完成</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { optimizeApi } from '../api/optimize'

const tasks = ref([])
const statusFilter = ref('')

const getStatusText = (status) => {
  const map = {
    PENDING: '待处理',
    PROCESSING: '处理中',
    COMPLETED: '已完成'
  }
  return map[status] || status
}

const getStatusType = (status) => {
  const map = {
    PENDING: 'warning',
    PROCESSING: 'primary',
    COMPLETED: 'success'
  }
  return map[status] || 'info'
}

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleString('zh-CN')
}

const loadTasks = async () => {
  const params = statusFilter.value ? { status: statusFilter.value } : {}
  const response = await optimizeApi.getAll(params)
  if (response.data.success) {
    tasks.value = response.data.data
  }
}

const checkLowScore = async () => {
  const response = await optimizeApi.check()
  if (response.data.success) {
    loadTasks()
    alert('检查完成')
  }
}

const completeTask = async (id) => {
  if (!confirm('确定要标记此任务为完成吗？')) return
  const response = await optimizeApi.complete(id)
  if (response.data.success) {
    loadTasks()
    alert('任务已完成')
  }
}

onMounted(() => {
  loadTasks()
})

watch(statusFilter, () => {
  loadTasks()
})
</script>