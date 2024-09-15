import { useCreateSport } from '@/hooks/sport.hooks';
import { showSuccessNotification } from '@/utils/notification';
import { CreateSportSchema } from '@/utils/validators/sport.yup';
import { ContextModalProps } from '@mantine/modals';
import { Asserts } from 'yup';
import { useForm } from '@mantine/form';
import {
  Box, Button, Group, LoadingOverlay, TextInput,
} from '@mantine/core';

interface Props {
}

type FormValues = Asserts<typeof CreateSportSchema>;
export default function CreateSportModal({
  id,
  context,
}: ContextModalProps<Props>) {
  const sm = useCreateSport();

  const {
    getInputProps,
    isValid,
    onSubmit,
  } = useForm<FormValues>({
    initialValues: {
      name: '',
    },
  });

  const handleSubmit = async (values: FormValues) => {
    sm.mutate(values, {
      onSuccess: () => {
        showSuccessNotification({
          title: 'Sport créé',
          message: `Le sport ${values.name} a été créé avec succès`,
        });
        context.closeModal(id);
      },
    });
  };

  return (
    <form onSubmit={onSubmit(handleSubmit)}>
      <Box sx={{ position: 'relative' }}>
        <LoadingOverlay visible={sm.isLoading} />
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

export const CreateSportModalName = 'CreateSportModal';
