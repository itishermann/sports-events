import { UpdateSportModalName } from '@/components/modals/UpdateSportModal';
import { useDeleteSportById } from '@/hooks/sport.hooks';
import {
  ActionIcon,
  Group,
} from '@mantine/core';
import { openConfirmModal, openContextModal } from '@mantine/modals';
import { IconEdit, IconTrash } from '@tabler/icons-react';

interface Props {
  sport: Sport;
}
export default function SportActions({
  sport,
}: Props) {
  const dm = useDeleteSportById();
  return (
    <Group>
      <ActionIcon
        color="blue"
        variant="filled"
        title="Modifier le sport"
        onClick={() => openContextModal({
          title: 'Mise à jour du sport',
          modal: UpdateSportModalName,
          innerProps: {
            sport_id: sport.id,
          },
        })}
      >
        <IconEdit size="1.125rem" />
      </ActionIcon>
      <ActionIcon
        color="red"
        variant="filled"
        title="Supprimer le sport"
        onClick={() => openConfirmModal({
          title: 'Suppression du sport',
          children: 'Êtes-vous sûr de vouloir vous supprimer cet sport ? Cette action est irréversible.',
          labels: { confirm: 'Supprimer', cancel: 'Annuler' },
          confirmProps: { color: 'red' },
          onConfirm: () => dm.mutate(sport.id),
        })}
      >
        <IconTrash size="1.125rem" />
      </ActionIcon>
    </Group>

  );
}
