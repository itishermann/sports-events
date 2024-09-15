import commentApi from '@/api/Comment';
import variables from '@/config/variables';
import {
  useInfiniteQuery, useMutation, useQuery, useQueryClient,
} from '@tanstack/react-query';

export function useCommentById(id: number) {
  return useQuery(['comment', id], () => commentApi.getCommentById(id).then((res) => res.data.data));
}

export function useDeleteCommentById() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => commentApi.deleteCommentById(id).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['comment'] });
    },
  });
}

export function useApproveCommentById() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (id: number) => commentApi.approveCommentById(id).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['comment'] });
    },
  });
}

export function useUpdateCommentById() {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (
      { id, payload }:
      {
        id: number,
        payload: UpdateCommentPayload,
      },
    ) => commentApi.updateCommentById(id, payload).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['comment'] });
    },
  });
}

export function useListCommentsByEventId(id: number, size = variables.app.defaultPageSize) {
  return useInfiniteQuery(
    ['comment', id, size],
    ({ pageParam = 0 }) => commentApi.listCommentsByEventId(
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

export function useCreateCommentByEventId(eventId: number) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (
      payload:CreateCommentPayload,
    ) => commentApi.createCommentByEventId(eventId, payload).then((res) => res.data.data),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['comment', eventId] });
      queryClient.invalidateQueries({ queryKey: ['event', eventId, 'rating'] });
    },
  });
}

export function useExistsByEventIdAndUserId(eventId: number, userId?: number) {
  return useQuery(['comment', eventId, 'user', userId], () => {
    if (!userId) throw new Error('userId is required');
    return commentApi.existsByEventIdAndUserId(eventId, userId)
      .then((res) => res.data.data);
  });
}

export function useListUnapprovedCommentsByEventId(
  id: number,
  size = variables.app.defaultPageSize,
) {
  return useInfiniteQuery(
    ['comment', id, size],
    ({ pageParam = 0 }) => commentApi.listUnapprovedCommentsByEventId(
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

export function useListUnapprovedComments(size = variables.app.defaultPageSize) {
  return useInfiniteQuery(
    ['comment', size],
    ({ pageParam = 0 }) => commentApi.listUnapprovedComments(
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

export function useAverageRatingByEventId(id: number) {
  return useQuery(['event', id, 'rating'], () => commentApi.getAverageRatingByEventId(id).then((res) => res.data.data));
}
