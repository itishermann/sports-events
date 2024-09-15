import * as yup from 'yup';

export const UpdateUserSchema = yup.object().shape({
  username: yup.string().notRequired().min(2).max(255),
  role: yup.string().notRequired().oneOf(['ROLE_USER', 'ROLE_ADMIN', 'ROLE_MODERATOR']).notRequired(),
  email: yup.string().notRequired().email(),
  oldPassword: yup.string().notRequired().min(6).max(255),
  newPassword: yup.string().notRequired().min(6).max(255),
  newPasswordConfirm: yup.string().notRequired().min(6).max(255)
    .oneOf([yup.ref('newPassword')], 'Les mots de passe ne correspondent pas'),
});
