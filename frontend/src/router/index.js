import { createRouter, createWebHistory } from 'vue-router'
import ArticleList from '../views/ArticleList.vue'
import ArticleDetail from '../views/ArticleDetail.vue'
import CreateArticle from '../views/CreateArticle.vue'
import ReviewList from '../views/ReviewList.vue'
import Stats from '../views/Stats.vue'
import OptimizeTasks from '../views/OptimizeTasks.vue'

const routes = [
  { path: '/', name: 'ArticleList', component: ArticleList },
  { path: '/article/:id', name: 'ArticleDetail', component: ArticleDetail },
  { path: '/create', name: 'CreateArticle', component: CreateArticle },
  { path: '/reviews', name: 'ReviewList', component: ReviewList },
  { path: '/stats', name: 'Stats', component: Stats },
  { path: '/optimize-tasks', name: 'OptimizeTasks', component: OptimizeTasks }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router