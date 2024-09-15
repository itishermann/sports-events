import UserActions from '@/components/actions/UserActions';
import { useUser, useUserList } from '@/hooks/user.hooks';
import { checkAuthServerSide, checkElevation } from '@/utils/helpers/auth';
import AppShell from '@/components/layout/AppShell';
import { getUserRole } from '@/utils/helpers/formatters';
import {
  Box, Center, Group, Loader, Title,
} from '@mantine/core';
import { DataTable } from 'mantine-datatable';

export default function Users() {
  const curentUser = useUser();
  const q = useUserList();
  return (
    <AppShell loading={q.isLoading}>
      <Box p="md">
        <Group mt="xl" mb="sm">
          <Title order={3}>Liste des utilisateurs</Title>
        </Group>
        <Box sx={{ height: '78vh' }}>
          <DataTable
            records={q.data?.pages}
            onScrollToBottom={() => q.hasNextPage && q.fetchNextPage()}
            minHeight={150}
            noRecordsText="Aucun utilisateur retrouvé"
            columns={[
              {
                title: '#',
                accessor: 'id',
                width: 30,
              },
              {
                title: 'Nom d\'utilisateur',
                accessor: 'username',
              },
              {
                title: 'Email',
                accessor: 'email',
              },
              {
                title: 'Rôle',
                accessor: 'role',
                render: (row) => getUserRole(row.role),
              },
              {
                title: 'Date de création',
                accessor: 'creationDate',
                render: (row) => new Date(row.creationDate).toLocaleDateString(),
              },
              {
                title: 'Date de mise à jour',
                accessor: 'updatedDate',
                render: (row) => new Date(row.updatedDate).toLocaleDateString(),
              },
              {
                title: 'Actions',
                accessor: 'actions',
                render: (row) => <UserActions user={row} />,
                hidden: !checkElevation(curentUser?.data?.role)('ROLE_ADMIN'),
              },
            ]}
          />
          {q.isFetchingNextPage && (
          <Center>
            <Loader mt="xl" variant="dots" />
          </Center>
          )}
        </Box>
      </Box>
    </AppShell>

  );
}

export const getServerSideProps = checkAuthServerSide();
