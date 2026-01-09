import { defineConfig } from 'vite';

export default defineConfig({
  define: {
    global: 'window'
  },
    server: {
        host: 'localhost', // or true / '0.0.0.0'
        port: 5173,
        strictPort: true,     // fail if port is taken
    },
});