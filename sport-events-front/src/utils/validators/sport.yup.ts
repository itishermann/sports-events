import * as yup from 'yup';

export const CreateSportSchema = yup.object().shape({
  name: yup.string().min(1).max(255).required(),
});

export const UpdateSportSchema = yup.object().shape({
  name: yup.string().min(1).max(255),
});
