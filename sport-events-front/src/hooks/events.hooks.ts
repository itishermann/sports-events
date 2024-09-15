import eventApi from '@/api/Event';
import variables from '@/config/variables';
import {
  useInfiniteQuery, useMutation, useQuery, useQueryClient,
} from '@tanstack/react-query';

export function useEventById(id: number) {
  return useQuery({
    queryKey: ['event', id],
    queryFn: () => eventApi.getEventById(id).then((res) => res.data.data),
  });
}

export function useEventListByLocalisation(localisation: LocationArgs, size = variables.app.defaultPageSize) {
  return useInfiniteQuery({
    queryKey: ['event', 'localisation', localisation],
    queryFn: ({ pageParam = 0 }) => eventApi.listEventsByLocalisation(
      localisation,
      { cursor: pageParam, size },
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

export function useEventListBySportId(sportId: number, size = variables.app.defaultPageSize) {
  return useInfiniteQuery({
    queryKey: ['event', 'sport', sportId],
    queryFn: ({ pageParam = 0 }) => eventApi.listEventsBySportId(
      sportId,
      { cursor: pageParam, size },
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

export function useEventListByUserId(userId: number, size = variables.app.defaultPageSize) {
  return useInfiniteQuery({
    queryKey: ['event', 'user', userId],
    queryFn: ({ pageParam = 0 }) => eventApi.listEventsByUserId(
      userId,
      { cursor: pageParam, size },
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

export function useDeleteEventById() {
  const queryClient = useQueryClient();
  return useMutation(
    (id: number) => eventApi.deleteEventById(id).then((res) => res.data.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['event'] });
      },
    },
  );
}

export function useCreateEvent() {
  const queryClient = useQueryClient();
  return useMutation(
    (payload: CreateEventPayload) => eventApi.createEvent(payload).then((res) => res.data.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['event'] });
      },
    },
  );
}

export function useUpdateEventById() {
  const queryClient = useQueryClient();
  return useMutation(
    ({
      id,
      payload,
    }: {
      id: number,
      payload: UpdateEventPayload
    }) => eventApi.updateEventById(id, payload).then((res) => res.data.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['event'] });
      },
    },
  );
}
