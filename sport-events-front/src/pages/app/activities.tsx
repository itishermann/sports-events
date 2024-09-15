import AppShell from '@/components/layout/AppShell';
import { ActivityDetailsModalName } from '@/components/modals/ActivityDetailsModal';
import { useDeleteEventById, useEventListByUserId } from '@/hooks/events.hooks';
import { useUser } from '@/hooks/user.hooks';
import { checkAuthServerSide } from '@/utils/helpers/auth';
import { showSuccessNotification } from '@/utils/notification';
import {
  ActionIcon, Box, Group, Title,
} from '@mantine/core';
import { openConfirmModal, openContextModal } from '@mantine/modals';
import { IconTrash } from '@tabler/icons-react';
import { DataTable } from 'mantine-datatable';

export default function Activities() {
  const cuq = useUser();
  const q = useEventListByUserId(cuq.data?.id as number);
  const dm = useDeleteEventById();
  return (
    <AppShell>
      <Box p="md">
        <Box p="md">
          <Group mt="xl" mb="sm">
            <Title order={3}>Liste de mes activités</Title>
          </Group>
        </Box>
        <Box sx={{ height: '78vh' }}>
          <DataTable
            records={q.data?.pages}
            onScrollToBottom={() => q.hasNextPage && q.fetchNextPage()}
            minHeight={150}
            noRecordsText="Aucune activité retrouvée"
            onRowClick={(row) => openContextModal({
              title: 'Détails de l\'évènement',
              modal: ActivityDetailsModalName,
              size: 'xl',
              innerProps: {
                eventId: row.id,
              },
            })}
            columns={[
              {
                title: '#',
                accessor: 'id',
                width: 30,
              },
              {
                title: 'Titre',
                accessor: 'title',
              },
              {
                title: 'Sport',
                accessor: 'sport.name',
              },
              {
                title: 'Lieu',
                accessor: 'location',
              },
              {
                title: 'Date de l\'évènement',
                accessor: 'event.date',
                render: (v) => `${new Date(v.dateStart).toLocaleDateString()} - ${new Date(v.dateEnd).toLocaleDateString()}`,
              },
              {
                title: '',
                accessor: 'actions',
                width: 50,
                render: (v) => (
                  <ActionIcon
                    variant="filled"
                    color="red"
                    onClick={(e) => {
                      e.stopPropagation();
                      openConfirmModal({
                        title: 'Suppression de l\'activité',
                        children: 'Êtes-vous sûr de vouloir vous supprimer cette activité ? Cette action est irréversible.',
                        labels: { confirm: 'Supprimer', cancel: 'Annuler' },
                        confirmProps: { color: 'red' },
                        onConfirm: () => dm.mutate(v.id, {
                          onSuccess: () => {
                            showSuccessNotification({
                              title: 'Activité supprimée',
                              message: 'Votre activité a bien été supprimée.',
                            });
                          },
                        }),
                      });
                    }}
                  >
                    <IconTrash />
                  </ActionIcon>
                ),
              },
            ]}
          />
        </Box>
      </Box>
    </AppShell>
  );
}

export const getServerSideProps = checkAuthServerSide();
