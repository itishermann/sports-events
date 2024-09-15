import SportActions from '@/components/actions/SportActions';
import { CreateSportModalName } from '@/components/modals/CreateSportModal';
import { useSportsList } from '@/hooks/sport.hooks';
import { useUser } from '@/hooks/user.hooks';
import { checkAuthServerSide, checkElevation } from '@/utils/helpers/auth';
import AppShell from '@/components/layout/AppShell';
import {
  ActionIcon, Box, Group, TextInput, Title,
} from '@mantine/core';
import { openContextModal } from '@mantine/modals';
import { IconPlus, IconSearch } from '@tabler/icons-react';
import { DataTable } from 'mantine-datatable';
import { useState } from 'react';

export default function Sports() {
  const [query, setQuery] = useState('');
  const cuq = useUser();
  const q = useSportsList(query);
  return (
    <AppShell loading={cuq.isLoading}>
      <Box p="md">
        <Box p="md">
          <Group mt="xl" mb="sm">
            <Title order={3}>Liste des sports</Title>
            {checkElevation(cuq.data?.role)('ROLE_ADMIN') && (
            <ActionIcon
              color="blue"
              size="sm"
              variant="filled"
              onClick={() => openContextModal({
                title: 'Ajouter un sport',
                modal: CreateSportModalName,
                innerProps: {},
              })}
            >
              <IconPlus />
            </ActionIcon>
            )}
            <TextInput
              size="xs"
              icon={<IconSearch size={12} />}
              placeholder="Rechercher un sport"
              value={query}
              onChange={(e) => setQuery(e.currentTarget.value)}
            />
          </Group>
        </Box>
        <Box sx={{ height: '78vh' }}>
          <DataTable
            records={q.data?.pages}
            onScrollToBottom={() => q.hasNextPage && q.fetchNextPage()}
            minHeight={150}
            noRecordsText="Aucun sport retrouvé"
            columns={[
              {
                title: '#',
                accessor: 'id',
              },
              {
                title: 'Nom',
                accessor: 'name',
              },
              {
                title: 'Actions',
                accessor: 'actions',
                render: (row) => <SportActions sport={row} />,
                hidden: !checkElevation(cuq?.data?.role)('ROLE_ADMIN'),
              },
            ]}
          />
        </Box>
      </Box>
    </AppShell>
  );
}

export const getServerSideProps = checkAuthServerSide();
