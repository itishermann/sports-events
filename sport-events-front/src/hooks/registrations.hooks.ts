import variables from '@/config/variables';
import registrationApi from '@/api/Registration';
import {
  useInfiniteQuery, useMutation, useQuery, useQueryClient,
} from '@tanstack/react-query';

export function useRegistrationById(id: number) {
  return useQuery(['registration', id], () => registrationApi.getRegistrationById(id).then((res) => res.data.data));
}

export function useDeleteRegistrationById() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (
      id: number,
    ) => registrationApi.deleteRegistrationById(id).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['registration'] });
      queryClient.invalidateQueries({ queryKey: ['event'] });
    },
  });
}

export function useDeleteRegistrationByEventIdAndUserId() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: ({
      eventId,
      userId,
    }:{
      eventId: number,
      userId: number,
    }) => registrationApi.deleteRegistrationByEventIdAndUserId(
      eventId,
      userId,
    ).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['registration'] });
      queryClient.invalidateQueries({ queryKey: ['event'] });
    },
  });
}

export function useCreateRegistrationByEventId() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (
      id: number,
    ) => registrationApi.createRegistrationByEventId(id).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['registration'] });
    },
  });
}

export function useIsUserRegisteredByEventId(eventId: number, userId?: number) {
  return useQuery(['registration', 'user', 'event', eventId, userId], () => {
    if (!userId) throw new Error('userId is required');
    return registrationApi.isUserRegisteredByEventId(eventId, userId)
      .then((res) => res.data.data);
  });
}

export function useListRegistrationsByUserId(id: number, size = variables.app.defaultPageSize) {
  return useInfiniteQuery(
    ['registration', 'user', id, size],
    ({ pageParam = 0 }) => registrationApi.listRegistrationsByUserId(
      id,
      { cursor: pageParam },
    ).then((res) => res.data),
    {
      getNextPageParam: (lastPage) => (
        lastPage.meta.next !== null ? lastPage.meta.next : undefined
      ),
      getPreviousPageParam: (firstPage) => (
        firstPage.meta.previous !== null ? firstPage.meta.previous : undefined
      ),
      select: (data) => ({
        pages: data.pages.flatMap((page) => page.data),
        pageParams: data.pageParams,
      }),
    },
  );
}

export function useListRegisteredUsersByEventId(id: number, size = variables.app.defaultPageSize) {
  return useInfiniteQuery(
    ['registration', 'event', id, size],
    ({ pageParam = 0 }) => registrationApi.listRegisteredUsersByEventId(
      id,
      { cursor: pageParam },
    ).then((res) => res.data),
    {
      getNextPageParam: (lastPage) => (
        lastPage.meta.next !== null ? lastPage.meta.next : undefined
      ),
      getPreviousPageParam: (firstPage) => (
        firstPage.meta.previous !== null ? firstPage.meta.previous : undefined
      ),
      select: (data) => ({
        pages: data.pages.flatMap((page) => page.data),
        pageParams: data.pageParams,
      }),
    },
  );
}

export function useAvailablePlacesByEventId(id: number) {
  return useQuery(['registration', 'event', 'availablePlaces', id], () => registrationApi.getAvailablePlacesByEventId(id).then((res) => res.data.data));
}
