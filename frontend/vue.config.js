module.exports = {
  devServer: {
    port: 3007,
    proxy: {
      '/api': {
        target: 'http://localhost:8007',
        changeOrigin: true
      }
    }
  }
}