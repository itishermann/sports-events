import BaseApi from './base';

class RegistrationApi extends BaseApi {
  async getRegistrationById(id: number) {
    return this.instance.get<HttpResponseRegistration>(`/api/registration/${id}`);
  }

  async deleteRegistrationById(id: number) {
    return this.instance.delete<HttpResponseVoid>(`/api/registration/${id}`);
  }

  async createRegistrationByEventId(id: number) {
    return this.instance.post<HttpResponseRegistration>(`/api/registration/event/${id}`);
  }

  async listRegisteredUsersByEventId(id: number, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListRegistration>(`/api/registration/event/${id}/user`, { params: pagination });
  }

  async listRegistrationsByUserId(id: number, pagination: PaginationArgs) {
    return this.instance.get<HttpResponseListRegistration>(`/api/registration/user/${id}`, { params: pagination });
  }

  async getAvailablePlacesByEventId(id: number) {
    return this.instance.get<HttpResponseAvailablePlaces>(`/api/registration/event/${id}/available-places`);
  }

  async isUserRegisteredByEventId(eventId: number, userId: number) {
    return this.instance.get<HttpResponseBoolean>(`/api/registration/event/${eventId}/user/${userId}`);
  }

  async deleteRegistrationByEventIdAndUserId(eventId: number, userId: number) {
    return this.instance.delete<HttpResponseVoid>(`/api/registration/event/${eventId}/user/${userId}`);
  }
}

const registrationApi = new RegistrationApi();

export default registrationApi;
