import BaseApi from './base';

class UserApi extends BaseApi {
  async listUsers(pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListUser>('/api/user', { params: pagination });
  }

  async getUserById(id: number) {
    return this.instance.get<HttpResponseUser>(`/api/user/${id}`);
  }

  async deleteUserById(id: number) {
    return this.instance.delete<HttpResponseVoid>(`/api/user/${id}`);
  }

  async updateUserById(id: number, payload: UpdateUserPayload) {
    return this.instance.put<HttpResponseUser>(`/api/user/${id}`, payload);
  }
}

const userApi = new UserApi();

export default userApi;
