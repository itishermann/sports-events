import commentApi from '@/api/Comment';
import registrationApi from '@/api/Registration';
import { ActivityDetailsModalName } from '@/components/modals/ActivityDetailsModal';
import {
  Box, Button, LoadingOverlay, Text,
} from '@mantine/core';
import { openContextModal } from '@mantine/modals';
import { useQuery } from '@tanstack/react-query';
import dayjs from 'dayjs';
import { Marker, Popup } from 'react-leaflet';

interface Props {
  event: IEvent;
}

export default function ActivityMarker({ event: e }: Props) {
  const rq = useQuery(
    ['event', e.id, 'rating'],
    () => commentApi.getAverageRatingByEventId(e.id).then((res) => res.data.data),
    {
      enabled: false,
    },
  );
  const ap = useQuery(
    ['event', e.id, 'availablePlaces'],
    () => registrationApi.getAvailablePlacesByEventId(e.id).then((res) => res.data.data),
    {
      enabled: false,
    },
  );
  return (
    <Marker
      position={[e.latitude, e.longitude]}
      title={e.title}
      eventHandlers={{
        popupopen: () => {
          rq.refetch();
          ap.refetch();
        },
      }}
    >
      <Popup interactive>
        <Box sx={(t) => ({ borderRadius: t.radius.md })}>
          <LoadingOverlay visible={rq.isLoading || ap.isLoading} />
          <Text sx={{ fontWeight: 'bold' }}>{e.title}</Text>
          <Text>{e.location}</Text>
          <Text>
            {e.max_participants}
            {' '}
            participants maximum
          </Text>
          <Text>
            Du
            {' '}
            {dayjs(e.dateStart).format('LL')}
            {' '}
            au
            {' '}
            {dayjs(e.dateEnd).format('LL')}
          </Text>
          {ap.data && (
          <Text>
            {ap.data}
            {' '}
            places disponibles
          </Text>
          )}
          {rq.data !== undefined ? (
            <Text>
              Note moyenne :
              {' '}
              {rq.data}
              /5
            </Text>
          ) : null}
          <Box>
            <Button
              mt="sm"
              compact
              size="xs"
              onClick={() => openContextModal({
                title: 'Détails de l\'évènement',
                modal: ActivityDetailsModalName,
                size: 'xl',
                innerProps: {
                  eventId: e.id,
                },
              })}
            >
              Plus d&apos;informations
            </Button>
          </Box>
        </Box>
      </Popup>
    </Marker>
  );
}
