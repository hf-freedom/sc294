import axios from 'axios'

const baseUrl = '/api/optimize-tasks'

export const optimizeApi = {
  getAll(params = {}) {
    return axios.get(baseUrl, { params })
  },
  complete(id) {
    return axios.post(`${baseUrl}/${id}/complete`)
  },
  check() {
    return axios.post(`${baseUrl}/check`)
  }
}