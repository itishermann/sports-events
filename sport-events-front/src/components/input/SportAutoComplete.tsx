import { useSportsList } from '@/hooks/sport.hooks';
import { Autocomplete, AutocompleteProps } from '@mantine/core';
import { useState } from 'react';

type Props = Omit<AutocompleteProps, 'onItemSubmit' | 'data'> & {
  onItemSubmit: (value: Sport) => void;
};

export default function SportAutoComplete({ onItemSubmit, ...props }:Props) {
  const [query, setQuery] = useState('');
  const ql = useSportsList(query);
  return (
    <Autocomplete
      {...props}
      data={ql.data?.pages.map((page) => ({ data: page, value: page.name })) ?? []}
      onChange={setQuery}
      onItemSubmit={(item) => onItemSubmit(item.data)}
    />
  );
}
