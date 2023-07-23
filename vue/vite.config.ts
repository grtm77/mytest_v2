import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import {VueBmapGlResolver} from '@vuemap/unplugin-resolver'
// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    AutoImport({
      resolvers: [ElementPlusResolver({
        exclude: /^ElBmap[A-Z]*!/
      }),VueBmapGlResolver()],
    }),
    Components({
      resolvers: [ElementPlusResolver({
        exclude: /^ElBmap[A-Z]*!/
      }),VueBmapGlResolver()],
    }),
  ],
  base: './',
  // 代理配置
  server: {
    proxy: {
      // 指定代理所有/api请求
      '/api': {
        // 代理请求之后的请求地址
        target: 'http://localhost:8081',
        // rewrite: (path) => path.replace(/^\/api/, ''),
        // rewrite: path => path.replace(/^\/api/, '/api'),
        // 跨域
        changeOrigin: true,
      },
    }
  }
})
