import authApi from '@/api/Auth';
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
import { useMutation } from '@tanstack/react-query';
import { useForm } from '@mantine/form';
import { RegisterSchema } from '@/utils/validators/auth.yup';
import { Asserts } from 'yup';
import { showSuccessNotification } from '@/utils/notification';
import { useRouter } from 'next/router';

type FormValues = Asserts<typeof RegisterSchema>;

export default function Register() {
  const router = useRouter();
  const mutation = useMutation(
    (p: RegisterPayload) => authApi.register(p).then((r) => r.data),
    {
      onSuccess: () => {
        showSuccessNotification({
          title: 'Votre compte a été créé avec succès',
          message: 'Vous pouvez maintenant vous connecter',
        });
        router.push('/auth/login');
      },
    },
  );
  const form = useForm<FormValues>({
    initialValues: {
      email: '',
      username: '',
      password: '',
      passwordConfirm: '',
    },
    validateInputOnBlur: true,
  });

  const handleSubmit = (values: FormValues) => mutation.mutate({
    email: values.email,
    username: values.username,
    password: values.password,
  });

  return (
    <Container size={420} my={40}>
      <Title
        align="center"
        sx={{ fontWeight: 900 }}
      >
        Bienvenue parmi nous !
      </Title>
      <Text color="dimmed" size="sm" align="center" mt={5}>
        Vous avez déjà un compte ?
        {' '}
        <Anchor<'a'> size="sm" href="/auth/login">
          Connectez-vous
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
          <TextInput
            label="Email"
            placeholder="jdoe@mail.io"
            required
            mt="md"
            type="email"
            {...form.getInputProps('email')}
          />
          <PasswordInput
            label="Mot de passe"
            placeholder="Votre mot de passe"
            required
            mt="md"
            {...form.getInputProps('password')}
          />
          <PasswordInput
            label="Confirmer le mot de passe"
            placeholder="Votre mot de passe"
            required
            mt="md"
            {...form.getInputProps('passwordConfirm')}
          />
          <Button fullWidth mt="xl" type="submit">
            Créer un compte
          </Button>
        </Paper>
      </form>
    </Container>
  );
}
