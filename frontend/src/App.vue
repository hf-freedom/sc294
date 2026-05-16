<template>
  <div class="app-container">
    <el-container>
      <el-aside width="200px" class="aside">
        <el-menu :default-active="$route.path" class="el-menu-vertical-demo" router>
          <el-menu-item index="/">
            <el-icon><FolderOpened /></el-icon>
            <span>文章列表</span>
          </el-menu-item>
          <el-menu-item index="/create">
            <el-icon><Plus /></el-icon>
            <span>创建文章</span>
          </el-menu-item>
          <el-menu-item index="/reviews">
            <el-icon><Search /></el-icon>
            <span>审核列表</span>
          </el-menu-item>
          <el-menu-item index="/stats">
            <el-icon><BarChart /></el-icon>
            <span>统计报表</span>
          </el-menu-item>
          <el-menu-item index="/optimize-tasks">
            <el-icon><Wrench /></el-icon>
            <span>优化任务</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      <el-container>
        <el-header class="header">
          <h1>知识库审核发布系统</h1>
        </el-header>
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>

<script setup>
import { FolderOpened, Plus, Search, BarChart, Wrench } from '@element-plus/icons-vue'
</script>

<style scoped>
.app-container {
  height: 100vh;
}

.aside {
  background-color: #2b3a42;
}

.el-menu-vertical-demo {
  height: 100%;
  border-right: none;
}

.el-menu-item {
  color: #b8c5d6;
}

.el-menu-item:hover,
.el-menu-item.is-active {
  background-color: #1f2d38;
  color: #fff;
}

.header {
  background-color: #fff;
  border-bottom: 1px solid #eee;
  padding: 0 20px;
}

.header h1 {
  margin: 0;
  font-size: 18px;
  line-height: 60px;
  color: #333;
}

.main {
  padding: 20px;
  background-color: #f5f7fa;
}
</style>