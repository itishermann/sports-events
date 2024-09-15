import { useSportById, useUpdateSportById } from '@/hooks/sport.hooks';
import { showSuccessNotification } from '@/utils/notification';
import { UpdateSportSchema } from '@/utils/validators/sport.yup';
import { ContextModalProps } from '@mantine/modals';
import { useEffect } from 'react';
import { Asserts } from 'yup';
import { useForm } from '@mantine/form';
import {
  Box, Button, Group, LoadingOverlay, TextInput,
} from '@mantine/core';

interface Props {
  sport_id: number;
}

type FormValues = Asserts<typeof UpdateSportSchema>;
export default function UpdateSportModal({
  id,
  context,
  innerProps: {
    sport_id,
  },
}: ContextModalProps<Props>) {
  const sq = useSportById(sport_id);
  const sm = useUpdateSportById();

  const {
    getInputProps,
    isValid,
    onSubmit,
    setFieldValue,
  } = useForm<FormValues>({
    initialValues: {
      name: sq.data?.name,
    },
  });

  useEffect(() => {
    if (sq.data?.name) {
      setFieldValue('name', sq.data?.name);
    }
  }, [setFieldValue, sq.data?.name]);

  const handleSubmit = async (values: FormValues) => {
    sm.mutate({
      id: sport_id,
      payload: values,
    }, {
      onSuccess: () => {
        showSuccessNotification({
          title: 'Sport mis à jour',
          message: `Le sport ${values.name} a été mis à jour avec succès`,
        });
        context.closeModal(id);
      },
    });
  };

  return (
    <form onSubmit={onSubmit(handleSubmit)}>
      <Box sx={{ position: 'relative' }}>
        <LoadingOverlay visible={sq.isLoading || sm.isLoading} />
        <TextInput
          label="Nom du sport"
          placeholder="Nom du sport"
          {...getInputProps('name')}
        />
        <Group position="apart" mt="xl">
          <Button mt="md" color="blue" onClick={() => context.closeModal(id)}>
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

      </Box>
    </form>
  );
}

export const UpdateSportModalName = 'UpdateSportModal';
