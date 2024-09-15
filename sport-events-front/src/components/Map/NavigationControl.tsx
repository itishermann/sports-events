import UserBurger from '@/components/layout/UserBurger';
import { useUser } from '@/hooks/user.hooks';
import { checkElevation } from '@/utils/helpers/auth';
import {
  Box, Button, Group, Paper,
} from '@mantine/core';
import { useRouter } from 'next/router';
import Control from 'react-leaflet-custom-control';

export default function NavigationControl() {
  const router = useRouter();
  const cuq = useUser();
  return (
    <Control position="topleft">
      <Box
        sx={{
          zIndex: 2,
          width: 'calc(100vw - 300px)',
        }}
        id="map-overlay"
      >
        <Paper sx={{ width: 'fit-content' }}>
          <Group>
            {checkElevation(cuq.data?.role)('ROLE_ADMIN') && (
            <Button
              color="red"
              variant="ghost"
              size="xs"
              onClick={() => router.push('/app/moderation')}
            >
              Modération
            </Button>
            )}
            <Button
              variant="ghost"
              size="xs"
              onClick={() => router.push('/app/activities')}
            >
              Mes activités
            </Button>
            <Button
              variant="ghost"
              size="xs"
              onClick={() => router.push('/app/registrations')}
            >
              Mes participations
            </Button>
            <Button
              variant="ghost"
              size="xs"
              onClick={() => router.push('/app/users')}
            >
              Utilisateurs
            </Button>
            <Button
              variant="ghost"
              size="xs"
              onClick={() => router.push('/app/sports')}
            >
              Sports
            </Button>
            <UserBurger />
          </Group>
        </Paper>
      </Box>
    </Control>
  );
}
