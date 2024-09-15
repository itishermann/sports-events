import { UpdateUserModalName } from '@/components/modals/UpdateUserModal';
import { useDeleteUserById } from '@/hooks/user.hooks';
import {
  ActionIcon,
  Group,
} from '@mantine/core';
import { openConfirmModal, openContextModal } from '@mantine/modals';
import { IconEdit, IconTrash } from '@tabler/icons-react';

interface Props {
  user: User;
}
export default function UserActions({
  user,
}: Props) {
  const deleteAccountMutation = useDeleteUserById(user?.id);
  return (
    <Group>
      <ActionIcon
        color="blue"
        variant="filled"
        title="Modifier l'utilisateur"
        onClick={() => openContextModal({
          title: 'Mise à jour de l\'utilisateur',
          modal: UpdateUserModalName,
          innerProps: {
            user_id: user.id,
          },
        })}
      >
        <IconEdit size="1.125rem" />
      </ActionIcon>
      <ActionIcon
        color="red"
        variant="filled"
        title={"Supprimer l'utilisateur"}
        onClick={() => openConfirmModal({
          title: 'Suppression de l\'utilisateur',
          children: 'Êtes-vous sûr de vouloir vous supprimer cet utilisateur ? Cette action est irréversible.',
          labels: { confirm: 'Supprimer', cancel: 'Annuler' },
          confirmProps: { color: 'red' },
          onConfirm: deleteAccountMutation.mutate,
        })}
      >
        <IconTrash size="1.125rem" />
      </ActionIcon>
    </Group>

  );
}
