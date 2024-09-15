import { Autocomplete, AutocompleteProps } from '@mantine/core';
import { BANProperties, geocode, parse } from '@p-j/geocodejson-ban';
import { GeocodeFeatureProperties } from '@p-j/geocodejson-types';
import { useQuery } from '@tanstack/react-query';
import { Feature, Point } from 'geojson';
import { useState } from 'react';
import Fuse from 'fuse.js';

type Props = Omit<AutocompleteProps, 'onItemSubmit' | 'data'> & {
  onItemSubmit: (value: Feature<Point, GeocodeFeatureProperties<BANProperties>>) => void;
};

const fuse = new Fuse<string>([]);

export default function AddressAutocomplete({
  onItemSubmit,
  ...props
}: Props) {
  const [query, setQuery] = useState('');
  const ql = useQuery({
    queryKey: ['address', query],
    queryFn: async () => {
      const rawResponseFromBANApi = await geocode({
        address: query,
        limit: 10,
      });
      return parse(rawResponseFromBANApi);
    },
    onSuccess: (data) => {
      fuse.setCollection(data.features.map((f) => f.properties.geocoding.label as string));
    },
  });

  return (
    <Autocomplete
      {...props}
      data={ql.data?.features.map((f) => ({
        value: f.properties.geocoding.label as string,
        data: f,
      })) ?? []}
      onChange={setQuery}
      onItemSubmit={(item) => onItemSubmit(item.data)}
      filter={(q, item) => {
        if (!item) return false;
        return fuse.search(q).map((r) => r.item).includes(item.value);
      }}
    />
  );
}
