module.exports = {
  transpileDependencies: [
    'vuetify'
  ],
  devServer: {
    port: 8000,
    proxy: {
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        pathRewrite: {
          "^/api": ""
        }
      }
    },
    // disableHostCheck: true
    historyApiFallback: true,
    allowedHosts: 'all'
  },
  lintOnSave: false,
  // 将websocket.config.js中的配置对象合并到Webpack配置中
  // fallback（回退）策略，即当Webpack无法解析一个模块路径时，会尝试使用fallback中指定的模块路径进行解析
  // 弹幕组件中需要使用timers-browserify，当Webpack无法解析timers模块路径时，会自动使用timers-browserify模块的路径进行解析，避免报错
  chainWebpack: config => {
    config.merge({
      resolve: {
        fallback: {
          "timers": require.resolve("timers-browserify")
        }
      }
    });
  }
}
