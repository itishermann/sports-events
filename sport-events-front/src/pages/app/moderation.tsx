import AppShell from '@/components/layout/AppShell';
import { ActivityDetailsModalName } from '@/components/modals/ActivityDetailsModal';
import {
  useApproveCommentById,
  useDeleteCommentById,
  useListUnapprovedComments,
} from '@/hooks/comments.hooks';
import { checkAuthServerSide } from '@/utils/helpers/auth';
import { showSuccessNotification } from '@/utils/notification';
import {
  ActionIcon, Box, Group, Rating, Title, rem,
} from '@mantine/core';
import { openConfirmModal, openContextModal } from '@mantine/modals';
import { IconCheck, IconTrash } from '@tabler/icons-react';
import dayjs from 'dayjs';
import { DataTable } from 'mantine-datatable';
import RelativeTime from 'dayjs/plugin/relativeTime';

dayjs.extend(RelativeTime);

export default function Moderation() {
  const q = useListUnapprovedComments();
  const dm = useDeleteCommentById();
  const am = useApproveCommentById();
  return (
    <AppShell>
      <Box p="md">
        <Box p="md">
          <Group mt="xl" mb="sm">
            <Title order={3}>Liste des commentaires en attente d'approbation</Title>
          </Group>
        </Box>
        <Box sx={{ height: '78vh' }}>
          <DataTable
            records={q.data?.pages}
            onScrollToBottom={() => q.hasNextPage && q.fetchNextPage()}
            minHeight={150}
            noRecordsText="Aucun commentaire retrouvé"
            onRowClick={(row) => openContextModal({
              title: 'Détails de l\'évènement',
              modal: ActivityDetailsModalName,
              size: 'xl',
              innerProps: {
                eventId: row.eventId,
              },
            })}
            columns={[
              {
                title: '#',
                accessor: 'id',
              },
              {
                title: 'Commentaire',
                accessor: 'comment',
                cellsSx: { wordWrap: 'break-word' },
                width: rem(1200),
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
              {
                title: '',
                accessor: 'actions',
                width: 100,
                render: (v) => (
                  <Group>
                    <ActionIcon
                      variant="filled"
                      color="green"
                      onClick={(e) => {
                        e.stopPropagation();
                        openConfirmModal({
                          title: 'Approbation du commentaire',
                          children: 'Êtes-vous sûr de vouloir approuver cet commentaire',
                          labels: { confirm: 'Confirmer', cancel: 'Annuler' },
                          confirmProps: { color: 'green' },
                          onConfirm: () => am.mutate(v.id, {
                            onSuccess: () => {
                              showSuccessNotification({
                                title: 'Commentaire approuvé',
                                message: 'Le commentaire a bien été approuvé.',
                              });
                            },
                          }),
                        });
                      }}
                    >
                      <IconCheck />
                    </ActionIcon>
                    <ActionIcon
                      variant="filled"
                      color="red"
                      onClick={(e) => {
                        e.stopPropagation();
                        openConfirmModal({
                          title: 'Suppression du commentaire',
                          children: 'Êtes-vous sûr de vouloir supprimer cet commentaire ? Cette action est irréversible.',
                          labels: { confirm: 'Supprimer', cancel: 'Annuler' },
                          confirmProps: { color: 'red' },
                          onConfirm: () => dm.mutate(v.id, {
                            onSuccess: () => {
                              showSuccessNotification({
                                title: 'Commentaire supprimé',
                                message: 'Le commentaire a bien été supprimée.',
                              });
                            },
                          }),
                        });
                      }}
                    >
                      <IconTrash />
                    </ActionIcon>
                  </Group>
                ),
              },
            ]}
          />
        </Box>
      </Box>
    </AppShell>
  );
}

export const getServerSideProps = checkAuthServerSide('ROLE_MODERATOR');
