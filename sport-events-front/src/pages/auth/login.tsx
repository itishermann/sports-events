import authApi from '@/api/Auth';
import { saveTokenData } from '@/utils/cookies';
import {
  Anchor,
  Button,
  Container,
  LoadingOverlay,
  Paper,
  PasswordInput,
  Text,
  TextInput,
  Title,
} from '@mantine/core';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useForm } from '@mantine/form';
import { Asserts } from 'yup';
import { showSuccessNotification } from '@/utils/notification';
import { useRouter } from 'next/router';
import { LoginSchema } from '@/utils/validators/auth.yup';

type FormValues = Asserts<typeof LoginSchema>;

export default function Login() {
  const router = useRouter();
  const queryClient = useQueryClient();
  const mutation = useMutation(
    (p: LoginPayload) => authApi.login(p).then((r) => r.data.data),
    {
      onSuccess: (data) => {
        saveTokenData(data);
        queryClient.invalidateQueries({ queryKey: ['current-user'] });
        showSuccessNotification({
          title: 'Connexion réussie',
          message: 'Vous pouvez maintenant accéder à l\'application',
        });
        router.push(router.query.to?.toString() || '/app');
      },
    },
  );
  const form = useForm<FormValues>({
    initialValues: {
      username: '',
      password: '',
    },
    validateInputOnBlur: true,
  });

  const handleSubmit = async (values: FormValues) => mutation.mutate({
    username: values.username,
    password: values.password,
  });

  return (
    <Container size={420} my={40}>
      <Title
        align="center"
        sx={{ fontWeight: 900 }}
      >
        Bon retour parmi nous !
      </Title>
      <Text color="dimmed" size="sm" align="center" mt={5}>
        Vous n'avez pas encore de compte ?
        {' '}
        <Anchor<'a'> size="sm" href="/auth/register">
          Créer un compte
        </Anchor>
      </Text>
      <form onSubmit={form.onSubmit(handleSubmit)}>
        <Paper
          withBorder
          shadow="md"
          p={30}
          mt={30}
          radius="md"
          sx={{ position: 'relative' }}
        >
          <LoadingOverlay visible={mutation.isLoading} />
          <TextInput
            label="Nom d'utilisateur"
            placeholder="jdoe"
            required
            {...form.getInputProps('username')}
          />
          <PasswordInput
            label="Mot de passe"
            placeholder="Votre mot de passe"
            required
            mt="md"
            {...form.getInputProps('password')}
          />
          <Button fullWidth mt="xl" type="submit">
            Se connecter
          </Button>
        </Paper>
      </form>
    </Container>
  );
}
