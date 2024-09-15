import EventComments from '@/components/composition/EventComments';
import { useAverageRatingByEventId } from '@/hooks/comments.hooks';
import { useDeleteEventById, useEventById } from '@/hooks/events.hooks';
import {
  useAvailablePlacesByEventId,
  useCreateRegistrationByEventId,
  useDeleteRegistrationByEventIdAndUserId,
  useDeleteRegistrationById,
  useIsUserRegisteredByEventId,
  useListRegisteredUsersByEventId,
} from '@/hooks/registrations.hooks';
import { useUser } from '@/hooks/user.hooks';
import { checkElevation } from '@/utils/helpers/auth';
import { showSuccessNotification } from '@/utils/notification';
import {
  ActionIcon,
  Box,
  Button, Group,
  LoadingOverlay, Rating,
  Text, Title,
} from '@mantine/core';
import { ContextModalProps, openConfirmModal } from '@mantine/modals';
import {
  IconCalendar, IconMapPinFilled, IconRun, IconTrash, IconUser, IconUsers,
} from '@tabler/icons-react';
import dayjs from 'dayjs';
import { DataTable } from 'mantine-datatable';

interface Props {
  eventId: number;
}
export const ActivityDetailsModalName = 'ActivityDetailsModal';
export default function ActivityDetailsModal({
  innerProps: { eventId },
  id,
  context,
}: ContextModalProps<Props>) {
  const currentUser = useUser();
  const eq = useEventById(eventId);
  const rq = useAverageRatingByEventId(eventId);
  const apq = useAvailablePlacesByEventId(eventId);
  const pq = useListRegisteredUsersByEventId(eventId);
  const crq = useCreateRegistrationByEventId();
  const drm = useDeleteRegistrationById();
  const drueq = useDeleteRegistrationByEventIdAndUserId();
  const iurq = useIsUserRegisteredByEventId(eventId, currentUser?.data?.id);
  const dm = useDeleteEventById();

  const canRegister = currentUser?.data?.id !== eq.data?.organizer.id
    && (apq.data && apq.data > 0)
    && !iurq.data;

  const canUnregister = currentUser?.data?.id !== eq.data?.organizer.id && iurq.data;

  const canDelete = currentUser?.data?.id === eq.data?.organizer.id || checkElevation(currentUser?.data?.role)('ROLE_ADMIN');

  return (
    <Box>
      <LoadingOverlay visible={eq.isLoading || rq.isLoading || apq.isLoading || dm.isLoading} />
      <Title order={3}>
        {eq.data?.title}
      </Title>
      <Group spacing="xs">
        <IconMapPinFilled size={16} />
        <Text>{eq.data?.location}</Text>
      </Group>
      <Group spacing="xs">
        <IconUsers size={16} />
        <Text>
          {apq.data || 0}
          {' '}
          /
          {' '}
          {eq.data?.max_participants}
          {' '}
          places disponibles
        </Text>
      </Group>
      <Group spacing="xs">
        <IconRun size={16} />
        <Text>
          {eq.data?.sport.name}
        </Text>
      </Group>
      <Group spacing="xs">
        <IconCalendar size={16} />
        <Text>
          Du
          {' '}
          {dayjs(eq.data?.dateStart).format('LL')}
          {' '}
          au
          {' '}
          {dayjs(eq.data?.dateEnd).format('LL')}
        </Text>
      </Group>
      <Group spacing="xs">
        <IconUser size={16} />
        <Text>
          {eq.data?.organizer.username}
          {' '}
          (
          {eq.data?.organizer.email}
          )
        </Text>
      </Group>
      <Rating value={rq.data} mt="xs" />
      <Group mt="md">
        <Text sx={{ fontWeight: 'bold' }}>Participants</Text>
        {canRegister && (
        <Button
          compact
          onClick={() => crq.mutate(eventId)}
        >
          S'inscrire
        </Button>
        )}
        {canUnregister && (
        <Button
          compact
          onClick={() => drueq.mutate({ eventId, userId: currentUser?.data?.id as number }, {
            onSuccess: () => showSuccessNotification({ title: 'Désinscription réussie', message: 'Vous vous êtes bien désinscrit de cette activité.' }),
          })}
          color="red"
        >
          Se désinscrire
        </Button>
        )}
      </Group>
      <Box h={250}>
        <DataTable
          records={pq.data?.pages || []}
          onScrollToBottom={() => pq.hasNextPage && pq.fetchNextPage()}
          noRecordsText={pq.isLoading ? 'Chargement...' : 'Aucun participant'}
          columns={[
            {
              title: '#',
              accessor: 'id',
              width: 30,
            },
            {
              title: 'Nom d\'utilisateur',
              accessor: 'user.username',
            },
            {
              title: 'Email',
              accessor: 'user.email',
            },
            {
              title: 'Date d\'inscription',
              accessor: 'date',
              render: (row) => new Date(row.date).toLocaleDateString(),
            },
            {
              title: 'Actions',
              accessor: 'actions',
              hidden: currentUser?.data?.id !== eq.data?.organizer.id,
              render: (row) => (
                <ActionIcon
                  color="red"
                  variant="filled"
                  title={"Supprimer l'inscription"}
                  onClick={() => openConfirmModal({
                    title: 'Suppression de l\'inscription',
                    children: 'Êtes-vous sûr de vouloir vous supprimer cette inscription ? Cette action est irréversible.',
                    labels: { confirm: 'Supprimer', cancel: 'Annuler' },
                    confirmProps: { color: 'red' },
                    onConfirm: () => drm.mutate(row.id),
                  })}
                >
                  <IconTrash size="1.125rem" />
                </ActionIcon>
              ),
            },
          ]}
        />
      </Box>
      <Group mt="md">
        <Text sx={{ fontWeight: 'bold' }}>Commentaires</Text>
      </Group>
      <Box mb="xl">
        {eq.data && <EventComments event={eq.data} />}
      </Box>
      {canDelete && (
      <Button
        fullWidth
        color="gray"
        variant="subtle"
        leftIcon={<IconTrash />}
        onClick={() => openConfirmModal({
          title: 'Suppression de l\'activité',
          children: 'Êtes-vous sûr de vouloir vous supprimer cette activité ? Cette action est irréversible.',
          labels: { confirm: 'Supprimer', cancel: 'Annuler' },
          confirmProps: { color: 'red' },
          onConfirm: () => dm.mutate(eventId, {
            onSuccess: () => {
              showSuccessNotification({
                title: 'Activité supprimée',
                message: 'L\'activité a bien été supprimée',
              });
              context.closeModal(id);
            },
          }),
        })}
      >
        Supprimer l'activité
      </Button>
      )}
    </Box>
  );
}
