import sportApi from '@/api/Sport';
import variables from '@/config/variables';
import {
  useInfiniteQuery, useMutation, useQuery, useQueryClient,
} from '@tanstack/react-query';

export function useSportById(id: number) {
  return useQuery(['sport', id], () => sportApi.getSportById(id).then((res) => res.data.data));
}

export function useSportsList(query = '', size = variables.app.defaultPageSize) {
  return useInfiniteQuery({
    queryKey: ['sport', { size, query }],
    queryFn: ({
      pageParam = 0,
    }) => sportApi.listSports({ cursor: pageParam, size }, query).then((res) => res.data),
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

export function useDeleteSportById() {
  const queryClient = useQueryClient();
  return useMutation(
    (id: number) => sportApi.deleteSportById(id).then((res) => res.data.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['sport'] });
      },
    },
  );
}

export function useCreateSport() {
  const queryClient = useQueryClient();
  return useMutation(
    (payload: CreateSportPayload) => sportApi.createSport(payload).then((res) => res.data.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['sport'] });
      },
    },
  );
}

export function useUpdateSportById() {
  const queryClient = useQueryClient();
  return useMutation(
    ({
      id,
      payload,
    }: {
      id: number,
      payload: UpdateSportPayload
    }) => sportApi.updateSportById(id, payload).then((res) => res.data.data),
    {
      onSuccess: () => {
        queryClient.invalidateQueries({ queryKey: ['sport'] });
      },
    },
  );
}
