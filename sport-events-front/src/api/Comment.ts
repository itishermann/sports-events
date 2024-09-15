import BaseApi from './base';

class CommentApi extends BaseApi {
  async getCommentById(id: number) {
    return this.instance.get<HttpResponseComment>(`/api/comment/${id}`);
  }

  async deleteCommentById(id: number) {
    return this.instance.delete<HttpResponseVoid>(`/api/comment/${id}`);
  }

  async updateCommentById(id: number, payload: UpdateCommentPayload) {
    return this.instance.put<HttpResponseComment>(`/api/comment/${id}`, payload);
  }

  async listCommentsByEventId(id: number, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListComment>(`/api/comment/event/${id}`, { params: pagination });
  }

  async createCommentByEventId(id: number, payload: CreateCommentPayload) {
    return this.instance.post<HttpResponseComment>(`/api/comment/event/${id}`, payload);
  }

  async listUnapprovedComments(pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListComment>('/api/comment/unapproved', { params: pagination });
  }

  async listUnapprovedCommentsByEventId(id: number, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListComment>(`/api/comment/unapproved/event/${id}`, { params: pagination });
  }

  async getAverageRatingByEventId(id: number) {
    return this.instance.get<HttpResponseFloat>(`/api/comment/event/${id}/rating`);
  }

  async existsByEventIdAndUserId(eventId: number, userId: number) {
    return this.instance.get<HttpResponseBoolean>(`/api/comment/event/${eventId}/user/${userId}`);
  }

  async approveCommentById(id: number) {
    return this.instance.put<HttpResponseVoid>(`/api/comment/${id}/approve`);
  }
}

const commentApi = new CommentApi();

export default commentApi;
