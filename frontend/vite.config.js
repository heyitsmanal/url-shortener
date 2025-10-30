import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
    plugins: [react()],
    server: {
        proxy: {
            '/api': 'http://localhost:8080',
            '/r':   'http://localhost:8080',
            '/v3':  'http://localhost:8080',
        }
    }
})
