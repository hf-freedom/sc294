import axios from 'axios'

const baseUrl = '/api/articles'

export const articleApi = {
  getAll(params = {}) {
    return axios.get(baseUrl, { params })
  },
  getById(id) {
    return axios.get(`${baseUrl}/${id}`)
  },
  create(data) {
    return axios.post(baseUrl, data)
  },
  update(id, data) {
    return axios.put(`${baseUrl}/${id}`, data)
  },
  delete(id) {
    return axios.delete(`${baseUrl}/${id}`)
  },
  submitForReview(id) {
    return axios.post(`${baseUrl}/${id}/submit`)
  },
  publish(id, reviewerId) {
    return axios.post(`${baseUrl}/${id}/publish`, null, { params: { reviewerId } })
  },
  rollback(id, version) {
    return axios.post(`${baseUrl}/${id}/rollback/${version}`)
  },
  getVersions(id) {
    return axios.get(`${baseUrl}/${id}/versions`)
  }
}