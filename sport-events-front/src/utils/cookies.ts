import variables from '@/config/variables';
import { deleteCookie, getCookie, setCookie } from 'cookies-next';

export const saveTokenData = (data: TokenResponse) => {
  setCookie(
    variables.cookies.jwt,
    data.accessToken,
  );
  setCookie(
    variables.cookies.jwtRefresh,
    data.refreshToken,
  );
};

export const removeTokenData = () => {
  deleteCookie(variables.cookies.jwt);
  deleteCookie(variables.cookies.jwtRefresh);
};

export const getApiToken = () => getCookie(
  variables.cookies.jwt,
)?.toString();
