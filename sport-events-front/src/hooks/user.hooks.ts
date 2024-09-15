import authApi from '@/api/Auth';
import userApi from '@/api/User';
import variables from '@/config/variables';
import { removeTokenData } from '@/utils/cookies';
import { showSuccessNotification } from '@/utils/notification';
import {
  useInfiniteQuery, useMutation, useQuery, useQueryClient,
} from '@tanstack/react-query';
import { useRouter } from 'next/router';

export function useUser() {
  return useQuery(
    ['current-user'],
    () => authApi.getMe().then((res) => res.data.data),
  );
}

export function useUserById(id: number) {
  return useQuery(
    ['user', id],
    () => userApi.getUserById(id).then((res) => res.data.data),
  );
}

export function useDeleteUserById(id?: number) {
  const queryClient = useQueryClient();
  return useMutation(
    () => {
      if (!id) throw new Error('userId is required');
      return userApi.deleteUserById(id)
        .then((res) => res.data.data);
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['user', id] });
        queryClient.invalidateQueries({ queryKey: ['users'] });
      },
    },
  );
}

export function useLogout() {
  const router = useRouter();
  const queryClient = useQueryClient();
  return useMutation(
    async () => {
      removeTokenData();
      showSuccessNotification({
        title: 'Déconnexion réussie',
        message: 'Vous êtes maintenant déconnecté',
      });
      router.push('/auth/login');
    },
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['current-user'] });
      },
    },
  );
}

export function useUserList(size = variables.app.defaultPageSize) {
  return useInfiniteQuery({
    queryKey: ['user', { size }],
    queryFn: ({ pageParam = 0 }) => userApi.listUsers(
      { size, cursor: pageParam },
    ).then((res) => res.data),
    getNextPageParam: (lastPage) => (lastPage.meta.next !== null ? lastPage.meta.next : undefined),
    getPreviousPageParam: (firstPage) => (
      firstPage.meta.previous !== null ? firstPage.meta.previous : undefined
    ),
    select: (data) => ({
      pages: data.pages.flatMap((page) => page.data),
      pageParams: data.pageParams,
    }),
  });
}
