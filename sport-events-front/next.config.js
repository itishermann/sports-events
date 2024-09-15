/** @type {import('next').NextConfig} */

if(!process.env.API_URL) {
  console.warn('API_URL is not set, using default value: http://localhost:8090. Please set API_URL environment variable in .env.local or in your environement to change it.')
}

const nextConfig = {
  reactStrictMode: true,
  serverRuntimeConfig: {
    // Will only be available on the server side
  },
  publicRuntimeConfig: {
    // Will be available on both server side and client side
    APP_ENV: process.env.APP_ENV || 'production',
    API_URL: process.env.API_URL || 'http://localhost:8090',
  },
}

module.exports = nextConfig
