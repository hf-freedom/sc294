import axios from 'axios'

const baseUrl = '/api/reviews'

export const reviewApi = {
  getPending(params = {}) {
    return axios.get(`${baseUrl}/pending`, { params })
  },
  review(data) {
    return axios.post(baseUrl, data)
  }
}