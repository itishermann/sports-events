import navbarHooks from '@/hooks/navbar.hooks';
import { getUserRole } from '@/utils/helpers/formatters';
import { forwardRef } from 'react';
import {
  IconChevronDown, IconChevronUp,
  IconDoorExit, IconEdit, IconTrash,
} from '@tabler/icons-react';
import {
  Avatar, Group, MediaQuery, Menu, Text, UnstyledButton,
} from '@mantine/core';
import { openConfirmModal, openContextModal } from '@mantine/modals';
import { UpdateUserModalName } from '@/components/modals/UpdateUserModal';
import { useDeleteUserById, useLogout, useUser } from '@/hooks/user.hooks';

interface UserButtonProps extends React.ComponentPropsWithoutRef<'button'> {
  username: string;
  role: UserRole;
}

const UserButton = forwardRef<HTMLButtonElement, UserButtonProps>(
  ({
    username, role, ...others
  }: UserButtonProps, ref) => (
    <UnstyledButton
      ref={ref}
      sx={(theme) => ({
        color: theme.colorScheme === 'dark' ? theme.colors.dark[0] : theme.black,
        padding: theme.spacing.xs,
      })}
      {...others}
    >
      <Group>
        <MediaQuery largerThan="sm" styles={{ display: 'none' }}>
          <IconChevronUp size="1rem" />
        </MediaQuery>
        <MediaQuery smallerThan="sm" styles={{ display: 'none' }}>
          <Avatar radius="xl">{username?.split(' ')?.map((n) => n[0]?.toUpperCase())?.join('')}</Avatar>
        </MediaQuery>
        <div style={{ flex: 1 }}>
          <Text size="sm" weight={500}>
            {username}
          </Text>

          <Text color="dimmed" size="xs">
            {getUserRole(role)}
          </Text>
        </div>
        <MediaQuery largerThan="sm" styles={{ display: 'none' }}>
          <Avatar radius="xl">{username?.split(' ')?.map((n) => n[0]?.toUpperCase())?.join('')}</Avatar>
        </MediaQuery>
        <MediaQuery smallerThan="sm" styles={{ display: 'none' }}>
          <IconChevronDown size="1rem" />
        </MediaQuery>
      </Group>
    </UnstyledButton>
  ),
);

export default function UserBurger() {
  const navbar = navbarHooks();
  const {
    data: user,
    isLoading,
  } = useUser();
  const logoutMutation = useLogout();
  const deleteAccountMutation = useDeleteUserById(user?.id);

  return (
    <Menu withArrow withinPortal disabled={isLoading}>
      <Menu.Target>
        <UserButton
          username={user?.username || ''}
          role={user?.role || 'ROLE_USER'}
        />
      </Menu.Target>
      <Menu.Dropdown>
        <Menu.Item
          color="blue"
          disabled={!user}
          icon={<IconEdit size="0.9rem" stroke={1.5} />}
          onClick={() => {
            if (!user?.id) return;
            navbar.close();
            openContextModal({
              title: 'Mettre à jour mon profil',
              modal: UpdateUserModalName,
              innerProps: {
                user_id: user?.id,
              },
            });
          }}
        >
          Mettre à jour mon profil
        </Menu.Item>
        <Menu.Item
          color="red"
          icon={<IconDoorExit size="0.9rem" stroke={1.5} />}
          disabled={logoutMutation.isLoading}
          onClick={() => {
            navbar.close();
            openConfirmModal({
              title: 'Déconnexion',
              children: 'Êtes-vous sûr de vouloir vous déconnecter ?',
              labels: { confirm: 'Déconnexion', cancel: 'Annuler' },
              confirmProps: { color: 'red' },
              onConfirm: logoutMutation.mutate,
            });
          }}
        >
          Déconnexion
        </Menu.Item>
        <Menu.Divider />
        <Menu.Label>Zone de danger</Menu.Label>
        <Menu.Item
          color="red"
          icon={<IconTrash size="0.9rem" stroke={1.5} />}
          disabled={deleteAccountMutation.isLoading}
          onClick={() => {
            navbar.close();
            openConfirmModal({
              title: 'Supprimer mon compte',
              children: 'Êtes-vous sûr de vouloir vous supprimer ce compte ? Cette action est irréversible.',
              labels: { confirm: 'Supprimer', cancel: 'Annuler' },
              confirmProps: { color: 'red' },
              onConfirm: () => {
                if (!user?.id) return;
                deleteAccountMutation.mutate(user?.id);
                logoutMutation.mutate();
              },
            });
          }}
        >
          Supprimer mon compte
        </Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}
