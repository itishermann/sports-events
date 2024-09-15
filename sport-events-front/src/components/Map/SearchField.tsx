import AddressAutocomplete from '@/components/input/AddressAutocomplete';
import { useMap } from 'react-leaflet';

export default function SearchField() {
  const map = useMap();

  return (
    <AddressAutocomplete
      w={300}
      placeholder="Rechercher une adresse"
      onItemSubmit={(r) => {
        map.flyTo({
          lat: r.geometry.coordinates[1],
          lng: r.geometry.coordinates[0],
        }, 14, { animate: true });
      }}
    />
  );
}
