import { LatLngExpression } from 'leaflet';
import {
  CircleMarker, LayerGroup, Popup,
} from 'react-leaflet';

interface Props {
  position: LatLngExpression;
}

export default function UserCurrentPositionMarker({ position }:Props) {
  return (
    <LayerGroup>
      <CircleMarker center={position} radius={15}>
        <Popup>
          <span>Vous êtes ici</span>
        </Popup>
      </CircleMarker>
    </LayerGroup>
  );
}
