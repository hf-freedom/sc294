<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="stat-card">
          <template #header>
            <span>待审核文章</span>
          </template>
          <div class="stat-value">{{ stats.pendingReviewCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card warning">
          <template #header>
            <span>过期文章</span>
          </template>
          <div class="stat-value">{{ stats.expiredCount || 0 }}</div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="stat-card danger">
          <template #header>
            <span>低质量文章</span>
          </template>
          <div class="stat-value">{{ stats.lowQualityCount || 0 }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row style="margin-top: 20px;">
      <el-col :span="8">
        <el-button type="primary" @click="generateStats">生成统计</el-button>
      </el-col>
    </el-row>

    <p style="margin-top: 20px; color: #666;">统计日期：{{ formatDate(stats.date) }}</p>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { statsApi } from '../api/stats'

const stats = ref({
  pendingReviewCount: 0,
  expiredCount: 0,
  lowQualityCount: 0,
  date: null
})

const formatDate = (dateStr) => {
  if (!dateStr) return '-'
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

const loadStats = async () => {
  const response = await statsApi.getToday()
  if (response.data.success) {
    stats.value = response.data.data
  }
}

const generateStats = async () => {
  const response = await statsApi.generate()
  if (response.data.success) {
    stats.value = response.data.data
    alert('统计生成成功')
  }
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.stat-card {
  text-align: center;
}

.stat-card .stat-value {
  font-size: 48px;
  font-weight: bold;
  color: #409eff;
  margin-top: 20px;
}

.stat-card.warning .stat-value {
  color: #e6a23c;
}

.stat-card.danger .stat-value {
  color: #f56c6c;
}
</style>