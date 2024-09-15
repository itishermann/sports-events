import ActivityMarker from '@/components/Map/ActivityMarker';
import NavigationControl from '@/components/Map/NavigationControl';
import SearchField from '@/components/Map/SearchField';
import UserCurrentPositionMarker from '@/components/Map/UserCurrentPositionMarker';
import { CreateActivityModalName } from '@/components/modals/CreateActivityModal';
import { useEventListByLocalisation } from '@/hooks/events.hooks';
import {
  ActionIcon, Button, Paper, Slider, Text,
} from '@mantine/core';
import { openContextModal } from '@mantine/modals';
import { IconMapPin, IconMapPinOff, IconPlus } from '@tabler/icons-react';
import { LatLngExpression, Map as LeafletMap } from 'leaflet';
import {
  useCallback, useEffect, useRef, useState,
} from 'react';
import {
  Circle, MapContainer,
  TileLayer, ZoomControl,
} from 'react-leaflet';
import { useGeolocated } from 'react-geolocated';
import 'leaflet/dist/leaflet.css';
import 'leaflet-defaulticon-compatibility/dist/leaflet-defaulticon-compatibility.css';
import 'leaflet-defaulticon-compatibility';
import Control from 'react-leaflet-custom-control';

const parisCenterCoords:LatLngExpression = {
  lat: 48.856614,
  lng: 2.3522219,
};

export default function EventMap() {
  const map = useRef<LeafletMap>(null);
  const [radiusKm, setRadiusKm] = useState(5);
  const [position, setPosition] = useState(parisCenterCoords);
  const [userPosition, setUserPosition] = useState<LatLngExpression | null>(null);
  const { isGeolocationAvailable, getPosition } = useGeolocated({
    positionOptions: {
      enableHighAccuracy: true,
    },
    onSuccess: ({ coords: c }) => {
      setUserPosition({
        lat: c.latitude,
        lng: c.longitude,
      });
      map.current?.setView({
        lat: c.latitude,
        lng: c.longitude,
      }, 13, {
        animate: true,
      });
    },
  });

  const getPositionAndFlyTo = () => {
    getPosition();
    map.current?.flyTo(position, 14, {
      animate: true,
    });
  };

  const q = useEventListByLocalisation({
    // @ts-expect-error - the coords are always valid
    latitude: position.lat,
    // @ts-expect-error - the coords are always valid
    longitude: position.lng,
    radius: radiusKm,
  }, 100);

  const onMove = useCallback(() => {
    if (!map.current) return;
    setPosition({
      lat: map.current.getCenter().lat,
      lng: map.current.getCenter().lng,
    });
  }, [map.current]);

  useEffect(() => {
    const mapInstance = map.current;
    mapInstance?.on('move', onMove);
    return () => {
      mapInstance?.off('move', onMove);
    };
  }, [map, onMove]);

  return (
    <MapContainer
      ref={map}
      center={position}
      zoom={13}
      style={{ height: '100%', width: '100%', zIndex: 1 }}
      zoomControl={false}
    >
      <TileLayer
        attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
        url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
      />
      <ZoomControl position="bottomright" />
      <Control position="bottomright" prepend>
        <ActionIcon
          variant="filled"
          color="blue"
          size="lg"
          disabled={!isGeolocationAvailable}
          onClick={getPositionAndFlyTo}
        >
          {isGeolocationAvailable ? <IconMapPin /> : <IconMapPinOff />}
        </ActionIcon>
      </Control>
      <NavigationControl />
      <Control position="topright">
        <Button
          compact
          w={300}
          leftIcon={<IconPlus />}
          onClick={() => openContextModal({
            title: 'Créer une activité',
            modal: CreateActivityModalName,
            innerProps: {},
          })}
        >
          Créer une activité
        </Button>
      </Control>
      <Control position="topright">
        <Paper w={300} p="xs">
          <Text sx={{ fontSize: 'bold' }}>
            Rayon de recherche:
            {' '}
            {radiusKm}
            {' '}
            km
          </Text>
          <Slider
            mt="sm"
            value={radiusKm}
            onChangeEnd={setRadiusKm}
            max={150}
            min={2}
            label={(value) => `${value} km`}
          />
        </Paper>
      </Control>
      <Control position="topright">
        <SearchField />
      </Control>
      {q.isSuccess && q.data.pages.map((e) => (
        <ActivityMarker event={e} key={e.id} />
      ))}
      {isGeolocationAvailable && userPosition && (
        <UserCurrentPositionMarker position={userPosition} />
      )}
      <Circle center={position} radius={radiusKm * 1000} fillOpacity={0} />
    </MapContainer>
  );
}
