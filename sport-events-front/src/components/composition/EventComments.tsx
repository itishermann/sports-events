import {
  useCreateCommentByEventId,
  useExistsByEventIdAndUserId,
  useListCommentsByEventId,
} from '@/hooks/comments.hooks';
import { useIsUserRegisteredByEventId } from '@/hooks/registrations.hooks';
import { useUser } from '@/hooks/user.hooks';
import { showSuccessNotification } from '@/utils/notification';
import { CreateCommentSchema } from '@/utils/validators/comment.yup';
import {
  Box, Button,
  Group, Rating,
  Stack,
  Textarea,
} from '@mantine/core';
import { useForm } from '@mantine/form';
import dayjs from 'dayjs';
import { DataTable } from 'mantine-datatable';
import { Asserts } from 'yup';
import RelativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(RelativeTime);

interface Props {
  event: IEvent;
  height?: number;
}

type FormValues = Asserts<typeof CreateCommentSchema>;

export default function EventComments({ event, height = 250 }:Props) {
  const cuq = useUser();
  const clq = useListCommentsByEventId(event.id);
  const ccm = useCreateCommentByEventId(event.id);
  const iurq = useIsUserRegisteredByEventId(event.id, cuq?.data?.id);
  const eq = useExistsByEventIdAndUserId(event.id, cuq?.data?.id);
  const {
    isValid, values,
    getInputProps, setFieldValue, onSubmit,
  } = useForm<FormValues>({
    initialValues: {
      comment: '',
      rating: 0,
    },
  });

  const handleComment = (v: FormValues) => {
    ccm.mutate(v, {
      onSuccess: () => showSuccessNotification({
        title: 'Commentaire ajouté',
        message: 'Votre commentaire a bien été ajouté',
      }),
    });
  };

  const canComment = cuq?.data?.id !== event.organizer.id && iurq.data && !eq.data;

  return (
    <Box>
      {canComment && (
        <Box>
          <form onSubmit={onSubmit(handleComment)}>
            <Group position="apart">
              <Textarea
                label="Votre commentaire"
                placeholder="Commentaire"
                required
                maxRows={2}
                {...getInputProps('comment')}
                sx={{ width: '75%' }}
              />
              <Stack>
                <Rating
                  value={values.rating}
                  onChange={(value) => setFieldValue('rating', value)}
                  count={5}
                />
                <Button
                  type="submit"
                  disabled={!isValid()}
                >
                  Commenter
                </Button>
              </Stack>
            </Group>
          </form>
        </Box>
      )}
      <Box sx={{ height }}>
        <DataTable
          records={clq.data?.pages}
          onScrollToBottom={() => clq.hasNextPage && clq.fetchNextPage()}
          minHeight={height}
          columns={[
            {
              title: '#',
              accessor: 'id',
            },
            {
              title: 'Commentaire',
              accessor: 'comment',
              width: 250,
              cellsSx: { wordWrap: 'break-word' },
            },
            {
              title: 'Note',
              accessor: 'rating',
              render: (row) => <Rating value={row.rating} readOnly />,
            },
            {
              title: 'Date',
              accessor: 'creationDate',
              render: (row) => dayjs(row.creationDate).fromNow(),
            },
            {
              title: 'Auteur',
              accessor: 'user.username',
            },
          ]}
        />
      </Box>
    </Box>
  );
}
