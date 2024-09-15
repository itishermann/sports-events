type HttpResponse<T> = {
  message: string;
  code: string;
  errors?: Record<string, never>;
  data: T;
  meta?: CursorMeta;
};

type HttpResponseList<T> = HttpResponse<T[]> & {
  meta: CursorMeta;
};

type UserRole = 'ROLE_USER' | 'ROLE_MODERATOR' | 'ROLE_ADMIN';

interface CursorMeta {
  cursor: string;
  next: string;
  previous: string;
}
interface UpdateUserPayload {
  username?: string;
  oldPassword?: string;
  newPassword?: string;
  newPasswordConfirm?: string;
  email?: string;
  /** @enum {string} */
  role?: UserRole;
}

interface User {
  /** Format: int32 */
  id: number;
  username: string;
  email: string;
  /** @enum {string} */
  role: UserRole;
  /** Format: date-time */
  creationDate: string;
  /** Format: date-time */
  updatedDate: string;
}
interface UpdateSportPayload {
  name?: string;
}
interface Sport {
  /** Format: int32 */
  id: number;
  name: string;
}
interface UpdateEventPayload {
  title?: string;
  location?: string;
  /** Format: date-time */
  dateStart?: string;
  /** Format: date-time */
  dateEnd?: string;
  /** Format: int32 */
  maxParticipants?: number;
  /** Format: double */
  latitude?: number;
  /** Format: double */
  longitude?: number;
  /** Format: int32 */
  sportId?: number;
}
interface IEvent {
  /** Format: int32 */
  id: number;
  title: string;
  location: string;
  /** Format: date-time */
  dateStart: string;
  /** Format: date-time */
  dateEnd: string;
  /** Format: int32 */
  max_participants: number;
  /** Format: double */
  latitude: number;
  /** Format: double */
  longitude: number;
  sport: Sport;
  organizer: User;
  /** Format: date-time */
  creationDate: string;
  /** Format: date-time */
  updatedDate: string;
}
interface UpdateCommentPayload {
  comment?: string;
  /** Format: float */
  rating?: number;
  approved?: boolean;
}
interface Comment {
  /** Format: int32 */
  id: number;
  comment: string;
  /** Format: float */
  rating: number;
  approved: boolean;
  user: User;
  eventId: number;
  /** Format: date-time */
  creationDate: string;
  /** Format: date-time */
  updatedDate: string;
}
interface CreateSportPayload {
  name: string;
}
interface RefreshTokenPayload {
  refreshToken: string;
}

interface Registration {
  /** Format: int32 */
  id: number;
  /** Format: date-time */
  date: string;
  event: IEvent;
  user: User;
  /** Format: date-time */
  creationDate: string;
  /** Format: date-time */
  updatedDate: string;
}
interface CreateEventPayload {
  title: string;
  location: string;
  /** Format: date-time */
  dateStart: string;
  /** Format: date-time */
  dateEnd: string;
  /** Format: int32 */
  maxParticipants: number;
  /** Format: double */
  latitude: number;
  /** Format: double */
  longitude: number;
  /** Format: int32 */
  sportId: number;
}
interface CreateCommentPayload {
  comment: string;
  /** Format: float */
  rating: number;
}
interface RegisterPayload {
  username: string;
  password: string;
  email: string;
}
interface LoginPayload {
  username: string;
  password: string;
}
interface TokenResponse {
  accessToken: string;
  refreshToken: string;
  tokenPrefix: string;
  header: string;
}
interface PaginationArgs {
  /** Format: int32 */
  cursor?: number;
  /** Format: int32 */
  size?: number;
}
interface LocationArgs {
  /** Format: double */
  latitude: number;
  /** Format: double */
  longitude: number;
  /** Format: double */
  radius: number;
}
type HttpResponseListUser = HttpResponseList<User>;
type HttpResponseListSport = HttpResponseList<Sport>;
type HttpResponseListRegistration = HttpResponseList<Registration>;
type HttpResponseListEvent = HttpResponseList<IEvent>;
type HttpResponseListComment = HttpResponseList<Comment>;
type HttpResponseFloat = HttpResponse<number>;
type HttpResponseRegistration = HttpResponse<Registration>;
type HttpResponseUser = HttpResponse<User>;
type HttpResponseSport = HttpResponse<Sport>;
type HttpResponseEvent = HttpResponse<IEvent>;
type HttpResponseComment = HttpResponse<Comment>;
type HttpResponseTokenResponse = HttpResponse<TokenResponse>;
type HttpResponseAvailablePlaces = HttpResponse<number>;
type HttpResponseBoolean = HttpResponse<boolean>;
interface HttpResponseVoid {
  message?: string;
  code?: string;
  errors?: Record<string, never>;
  data?: Record<string, never>;
  meta?: Record<string, never>;
}
