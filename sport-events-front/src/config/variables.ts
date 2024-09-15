import getConfig from 'next/config';

const { publicRuntimeConfig } = getConfig();

const APP_ENV = publicRuntimeConfig.APP_ENV || 'production';

const dev = !['production', 'staging'].includes(APP_ENV);

const app = {
  defaultPageSize: 25,
};

const api = {
  url: publicRuntimeConfig.API_URL,
  maxRetryCount: 3,
};

const cookies = {
  jwt: 'x-jwt',
  jwtRefresh: 'x-jwt-refresh',
};

const variables = {
  APP_ENV,
  dev,
  app,
  api,
  cookies,
};

export default variables;
