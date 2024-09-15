import BaseApi from './base';

class SportApi extends BaseApi {
  async listSports(pagination: PaginationArgs, query?: string) {
    return this.instance.get<HttpResponseListSport>('/api/sport', { params: { ...pagination, query } });
  }

  async getSportById(id: number) {
    return this.instance.get<HttpResponseSport>(`/api/sport/${id}`);
  }

  async deleteSportById(id: number) {
    return this.instance.delete<HttpResponseVoid>(`/api/sport/${id}`);
  }

  async createSport(payload: CreateSportPayload) {
    return this.instance.post<HttpResponseSport>('/api/sport', payload);
  }

  async updateSportById(id: number, payload: UpdateSportPayload) {
    return this.instance.put<HttpResponseSport>(`/api/sport/${id}`, payload);
  }
}

const sportApi = new SportApi();

export default sportApi;
