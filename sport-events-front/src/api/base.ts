import variables from '@/config/variables';
import { getApiToken, saveTokenData } from '@/utils/cookies';
import axios, { AxiosInstance } from 'axios';
import { getCookie } from 'cookies-next';

export default class BaseApi {
  protected instance: AxiosInstance;

  constructor() {
    this.instance = axios.create({
      baseURL: variables.api.url,
      headers: {
        'Content-Type': 'application/json',
        Accept: 'application/json',
      },
    });

    this.instance.interceptors.request.use(
      (config) => {
        const token = getApiToken();
        if (token) {
          config.headers.setAuthorization(`Bearer ${token}`);
        }
        return config;
      },
    );

    this.instance.interceptors.response.use(
      (response) => response,
      (error) => {
        const originalRequest = error.config;
        if (error.response.status !== 401
          // eslint-disable-next-line no-underscore-dangle
          || originalRequest._retry_count >= variables.api.maxRetryCount) {
          return Promise.reject(error);
        }
        const refreshToken = getCookie(variables.cookies.jwtRefresh);
        if (!refreshToken || typeof refreshToken !== 'string') {
          return Promise.reject(error);
        }
        const payload: RefreshTokenPayload = { refreshToken };
        return this.instance.post<HttpResponseTokenResponse>('/api/auth/refresh', payload)
          .then(({ data }) => {
            saveTokenData(data.data);
            this.instance.defaults.headers.common.Authorization = `Bearer ${data.data.accessToken}`;
            // eslint-disable-next-line no-underscore-dangle
            originalRequest._retry_count = originalRequest._retry_count + 1 || 0;
            return this.instance(originalRequest);
          });
      },
    );
  }
}
