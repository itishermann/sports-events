import authApi from '@/api/Auth';
import variables from '@/config/variables';
import { deleteCookie, getCookies, setCookie } from 'cookies-next';
import { GetServerSideProps } from 'next';

export const checkElevation = (role?: UserRole) => (minRole: UserRole = 'ROLE_USER') => {
  if (!role) return false;
  const roles: UserRole[] = ['ROLE_USER', 'ROLE_MODERATOR', 'ROLE_ADMIN'];
  const roleIndex = roles.indexOf(role);
  const minRoleIndex = roles.indexOf(minRole);
  return roleIndex >= minRoleIndex;
};

export const checkAuthServerSide = (
  minRole: UserRole = 'ROLE_USER',
  redirectToApp: boolean = false,
  returnedProps: Record<string, any> = { },
): GetServerSideProps => async ({ req, res, resolvedUrl }) => {
  const cookies = getCookies({ req });
  const hasToken = Object.prototype.hasOwnProperty.call(cookies, variables.cookies.jwt);
  const hasRefreshToken = Object.prototype.hasOwnProperty.call(
    cookies,
    variables.cookies.jwtRefresh,
  );
  if (!hasToken && !hasRefreshToken) {
    return {
      props: {},
      redirect: {
        destination: `/auth/login?to=${resolvedUrl}`,
        permanent: false,
      },
    };
  }
  const token = cookies[variables.cookies.jwt];
  try {
    if (!token) throw new Error('Token not found');
    const { data: { data } } = await authApi.getMe(token);
    if (!checkElevation(data.role)(minRole)) {
      return {
        props: {},
        notFound: true,
      };
    }
  } catch (error) {
    deleteCookie(variables.cookies.jwt, { req, res });
    if (hasRefreshToken) {
      const refreshToken = cookies[variables.cookies.jwtRefresh];
      if (!refreshToken) {
        return {
          props: {},
          redirect: {
            destination: `/auth/login?to=${resolvedUrl}`,
            permanent: false,
          },
        };
      }
      try {
        const refreshRes = await authApi.refresh({ refreshToken });
        setCookie(variables.cookies.jwt, refreshRes.data.data.accessToken, { req, res });
        setCookie(variables.cookies.jwtRefresh, refreshRes.data.data.refreshToken, { req, res });
      } catch (e: any) {
        deleteCookie(variables.cookies.jwt, { req, res });
        deleteCookie(variables.cookies.jwtRefresh, { req, res });
        return {
          props: {},
          redirect: {
            destination: `/auth/login?to=${resolvedUrl}`,
            permanent: false,
          },
        };
      }
    }
  }
  if (!redirectToApp) return { props: returnedProps };

  return {
    props: returnedProps,
    redirect: {
      destination: '/',
      permanent: false,
    },
  };
};
