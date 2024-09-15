import userApi from '@/api/User';
import { useUser } from '@/hooks/user.hooks';
import { checkElevation } from '@/utils/helpers/auth';
import { ContextModalProps } from '@mantine/modals';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { showSuccessNotification } from '@/utils/notification';
import { useEffect, useState } from 'react';
import { Asserts } from 'yup';
import { useForm } from '@mantine/form';
import {
  Box, Button, Checkbox, Group, LoadingOverlay, PasswordInput, Select, TextInput,
} from '@mantine/core';
import { UpdateUserSchema } from '@/utils/validators/user.yup';
import { roleOptions } from '@/utils/helpers/options';

interface UpdateUserModalProps {
  user_id: number;
  onFinish?: () => void | Promise<void>;
}

type FormValues = Asserts<typeof UpdateUserSchema>;
export default function UpdateUserModal({
  id,
  context,
  innerProps: {
    user_id,
    onFinish = () => {},
  },
}: ContextModalProps<UpdateUserModalProps>) {
  const [checked, setChecked] = useState(false);
  const queryClient = useQueryClient();
  const currentUserQuery = useUser();
  const userQuery = useQuery(
    ['user', user_id],
    () => userApi.getUserById(user_id).then((res) => res.data.data),
  );

  const userMutation = useMutation(
    (p: UpdateUserPayload) => userApi.updateUserById(user_id, p).then((r) => r.data.data),
    {
      onSuccess: () => {
        showSuccessNotification({
          title: 'Utilisateur modifié',
          message: "L'utilisateur a bien été modifié",
        });
        queryClient.invalidateQueries({ queryKey: ['user'] });
        queryClient.invalidateQueries({ queryKey: ['current-user'] });
        onFinish();
        context.closeModal(id);
      },
    },
  );

  const {
    getInputProps,
    isValid,
    onSubmit,
    setFieldValue,
  } = useForm<FormValues>({
    initialValues: {
      username: userQuery.data?.username ?? '',
      role: userQuery.data?.role ?? 'ROLE_USER',
      email: userQuery.data?.email ?? '',
      newPassword: '',
      newPasswordConfirm: '',
      oldPassword: '',
    },
  });

  useEffect(() => {
    if (userQuery.data) {
      setFieldValue('role', userQuery.data.role);
      setFieldValue('username', userQuery.data.username);
      setFieldValue('email', userQuery.data.email);
    }
  }, [userQuery.data, setFieldValue]);

  const handleSubmit = async (values: FormValues) => {
    userMutation.mutate({
      username: values.username !== userQuery.data?.username
        ? values.username as string : undefined,
      role: values.role !== userQuery.data?.role ? values.role as UserRole : undefined,
      email: values.email !== userQuery.data?.email ? values.email as string : undefined,
      newPassword: values.newPassword?.length ? values.newPassword as string : undefined,
      oldPassword: values.oldPassword?.length ? values.oldPassword as string : undefined,
      newPasswordConfirm: values.newPasswordConfirm?.length
        ? values.newPasswordConfirm as string : undefined,
    });
  };

  return (
    <form onSubmit={onSubmit(handleSubmit)}>
      <Box sx={{ position: 'relative' }}>
        <LoadingOverlay visible={userQuery.isLoading || userMutation.isLoading} />
        <TextInput
          label="Nom d'utilisateur"
          placeholder="jdoe"
          {...getInputProps('username')}
        />
        <TextInput
          mt="md"
          label="Email"
          placeholder="jdoe@mail.io"
          {...getInputProps('email')}
        />
        {checkElevation(currentUserQuery.data?.role)('ROLE_ADMIN') && (
        <Select
          mt="md"
          label="Rôle"
          placeholder="Pick one"
          data={roleOptions}
          {...getInputProps('role')}
          hidden={userQuery.data?.role !== 'ROLE_ADMIN'}
        />
        )}
        <Checkbox
          mt="md"
          label="Changer le mot de passe"
          checked={checked}
          onChange={(event) => setChecked(event.currentTarget.checked)}
        />
        {checked && (
        <>
          <PasswordInput
            label="Ancien mot de passe"
            placeholder="Mot de passe"
            mt="md"
            required={checked}
            {...getInputProps('oldPassword')}
          />
          <PasswordInput
            label="Nouveau mot de passe"
            placeholder="Mot de passe"
            mt="md"
            required={checked}
            {...getInputProps('newPassword')}
          />
          <PasswordInput
            label="Confirmer le nouveau mot de passe"
            placeholder="Mot de passe"
            mt="md"
            required={checked}
            {...getInputProps('newPasswordConfirm')}
          />
        </>
        )}
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

export const UpdateUserModalName = 'UpdateUserModal';
