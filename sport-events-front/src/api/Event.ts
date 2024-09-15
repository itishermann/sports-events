import BaseApi from './base';

class EventApi extends BaseApi {
  async getEventById(id: number) {
    return this.instance.get<HttpResponseEvent>(`/api/event/${id}`);
  }

  async updateEventById(id: number, payload: UpdateEventPayload) {
    return this.instance.put<HttpResponseEvent>(`/api/event/${id}`, payload);
  }

  async deleteEventById(id: number) {
    return this.instance.delete<HttpResponseVoid>(`/api/event/${id}`);
  }

  async listEvents(pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListEvent>('/api/event', { params: pagination });
  }

  async createEvent(payload: CreateEventPayload) {
    return this.instance.post<HttpResponseEvent>('/api/event', payload);
  }

  async listEventsByUserId(id: number, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListEvent>(`/api/event/user/${id}`, { params: pagination });
  }

  async listEventsBySportId(id: number, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListEvent>(`/api/event/sport/${id}`, { params: pagination });
  }

  async listEventsByLocalisation(localisation: LocationArgs, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListEvent>('/api/event/localisation', { params: { ...pagination, ...localisation } });
  }
}

const eventApi = new EventApi();

export default eventApi;
