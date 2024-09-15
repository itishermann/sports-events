import * as yup from 'yup';

export const LoginSchema = yup.object().shape({
  username: yup.string().min(1).max(255).required('Le nom d\'utilisateur est requis'),
  password: yup.string().min(1).max(255).required('Le mot de passe est requis'),
});

export const RegisterSchema = yup.object().shape({
  email: yup.string().email('L\'email est invalide').required('L\'email est requis'),
  username: yup.string().min(1).max(255).required('Le nom d\'utilisateur est requis'),
  password: yup.string().min(1).max(255).required('Le mot de passe est requis'),
  passwordConfirm: yup
    .string()
    .min(1).max(255)
    .required('La confirmation du mot de passe est requise')
    .oneOf(
      [yup.ref('password')],
      'Les mots de passe ne correspondent pas',
    ),
});
