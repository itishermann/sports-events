import AddressAutocomplete from '@/components/input/AddressAutocomplete';
import SportAutoComplete from '@/components/input/SportAutoComplete';
import { useCreateEvent } from '@/hooks/events.hooks';
import { showSuccessNotification } from '@/utils/notification';
import {
  Button,
  Group,
  LoadingOverlay,
  NumberInput,
  TextInput,
} from '@mantine/core';
import { DatePickerInput } from '@mantine/dates';
import { useForm } from '@mantine/form';
import { ContextModalProps } from '@mantine/modals';
import dayjs from 'dayjs';
import { Asserts } from 'yup';
import { CreateEventSchema } from '@/utils/validators/event.yup';

type FormValues = Asserts<typeof CreateEventSchema>;
interface Props {}
export const CreateActivityModalName = 'CreateActivityModal';
export default function CreateActivityModal({ id, context }: ContextModalProps<Props>) {
  const q = useCreateEvent();

  const {
    values,
    isValid,
    getInputProps,
    errors,
    setFieldValue,
    onSubmit,
  } = useForm<FormValues>({
    initialValues: {
      title: '',
      location: '',
      maxParticipants: 1,
      latitude: 0,
      longitude: 0,
      dateStart: dayjs().add(1, 'day').toDate(),
      dateEnd: dayjs().add(2, 'day').toDate(),
      sportId: 0,
    },
  });

  const handleSubmit = (v: FormValues) => {
    q.mutate(
      {
        title: v.title,
        location: v.location,
        maxParticipants: v.maxParticipants,
        latitude: v.latitude,
        longitude: v.longitude,
        dateStart: v.dateStart.toISOString(),
        dateEnd: v.dateEnd.toISOString(),
        sportId: v.sportId,
      },
      {
        onSuccess: () => {
          showSuccessNotification({
            title: 'Evènement créé',
            message: 'Votre évènement a bien été créé',
          });
          context.closeModal(id);
        },
      },
    );
  };

  return (
    <form onSubmit={onSubmit(handleSubmit)}>
      <LoadingOverlay visible={q.isLoading} />
      <TextInput
        mt="md"
        label="Titre"
        required
        {...getInputProps('title')}
      />
      <AddressAutocomplete
        label="Adresse"
        mt="md"
        required
        onItemSubmit={(item) => {
          setFieldValue('location', item.properties.geocoding.label);
          setFieldValue('latitude', item.geometry.coordinates[1]);
          setFieldValue('longitude', item.geometry.coordinates[0]);
        }}
        error={errors.location || errors.latitude || errors.longitude}
        onFocus={getInputProps('location').onFocus}
        onBlur={getInputProps('location').onBlur}
      />
      <NumberInput
        label="Nombre max de participants"
        mt="md"
        required
        {...getInputProps('maxParticipants')}
      />
      <DatePickerInput
        type="range"
        required
        mt="md"
        label="Date de début et de fin"
        locale="fr"
        valueFormat="LL"
        value={[values.dateStart, values.dateEnd]}
        onChange={(value) => {
          setFieldValue('dateStart', value[0] as Date);
          setFieldValue('dateEnd', value[1] as Date);
        }}
        onFocus={getInputProps('dateStart').onFocus}
        onBlur={getInputProps('dateStart').onBlur}
        error={errors.dateStart || errors.dateEnd}
        minDate={dayjs().add(1, 'day').toDate()}
      />
      <SportAutoComplete
        onItemSubmit={(item) => setFieldValue('sportId', item.id)}
        mt="md"
        required
        label="Sport"
        error={errors.sportId}
        onBlur={getInputProps('sportId').onBlur}
        onFocus={getInputProps('sportId').onFocus}
      />
      <Group position="apart" mt="xl">
        <Button
          mt="md"
          color="blue"
          onClick={() => context.closeModal(id)}
        >
          Abandonner
        </Button>
        <Button
          mt="md"
          color="green"
          type="submit"
          disabled={!isValid()}
        >
          Confirmer
        </Button>
      </Group>
    </form>
  );
}
