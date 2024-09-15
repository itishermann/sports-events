import AppShell from '@/components/layout/AppShell';
import { ActivityDetailsModalName } from '@/components/modals/ActivityDetailsModal';
import { useDeleteRegistrationById, useListRegistrationsByUserId } from '@/hooks/registrations.hooks';
import { useUser } from '@/hooks/user.hooks';
import { checkAuthServerSide } from '@/utils/helpers/auth';
import { showSuccessNotification } from '@/utils/notification';
import {
  ActionIcon, Box, Group, Title,
} from '@mantine/core';
import { openConfirmModal, openContextModal } from '@mantine/modals';
import { IconTrash } from '@tabler/icons-react';
import { DataTable } from 'mantine-datatable';

export default function Registrations() {
  const cuq = useUser();
  const q = useListRegistrationsByUserId(cuq.data?.id as number);
  const dm = useDeleteRegistrationById();
  return (
    <AppShell>
      <Box p="md">
        <Box p="md">
          <Group mt="xl" mb="sm">
            <Title order={3}>Liste de mes participations</Title>
          </Group>
        </Box>
        <Box sx={{ height: '78vh' }}>
          <DataTable
            records={q.data?.pages}
            onScrollToBottom={() => q.hasNextPage && q.fetchNextPage()}
            minHeight={150}
            noRecordsText="Aucune participation retrouvée"
            onRowClick={(row) => openContextModal({
              title: 'Détails de l\'évènement',
              modal: ActivityDetailsModalName,
              size: 'xl',
              innerProps: {
                eventId: row.event.id,
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
                accessor: 'event.title',
              },
              {
                title: 'Sport',
                accessor: 'event.sport.name',
              },
              {
                title: 'Lieu',
                accessor: 'event.location',
              },
              {
                title: 'Date de l\'évènement',
                accessor: 'event.date',
                render: (v) => `${new Date(v.event.dateStart).toLocaleDateString()} - ${new Date(v.event.dateEnd).toLocaleDateString()}`,
              },
              {
                title: 'Date d\'inscription',
                accessor: 'date',
                render: (v) => new Date(v.date).toLocaleDateString(),
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
                        title: 'Suppression de l\'inscription',
                        children: 'Êtes-vous sûr de vouloir vous supprimer cette inscription ? Cette action est irréversible.',
                        labels: { confirm: 'Supprimer', cancel: 'Annuler' },
                        confirmProps: { color: 'red' },
                        onConfirm: () => dm.mutate(v.id, {
                          onSuccess: () => {
                            showSuccessNotification({
                              title: 'Inscription supprimée',
                              message: 'Votre inscription a bien été supprimée.',
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
