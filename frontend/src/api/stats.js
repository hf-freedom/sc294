import axios from 'axios'

const baseUrl = '/api/stats'

export const statsApi = {
  getToday() {
    return axios.get(`${baseUrl}/today`)
  },
  generate() {
    return axios.post(`${baseUrl}/generate`)
  }
}