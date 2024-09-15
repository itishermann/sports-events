import BaseApi from './base';

class AuthApi extends BaseApi {
  async register(payload: RegisterPayload) {
    return this.instance.post<HttpResponseUser>('/api/auth/register', payload);
  }

  async login(payload: LoginPayload) {
    return this.instance.post<HttpResponseTokenResponse>('/api/auth/login', payload);
  }

  async refresh(payload: RefreshTokenPayload) {
    return this.instance.post<HttpResponseTokenResponse>('/api/auth/refresh', payload);
  }

  async getMe(token?: string) {
    return this.instance.get<HttpResponseUser>(
      '/api/auth/me',
      token ? {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      } : undefined,
    );
  }
}

const authApi = new AuthApi();

export default authApi;
