import * as yup from 'yup';

export const CreateCommentSchema = yup.object().shape({
  comment: yup.string().min(1).max(255).required(),
  rating: yup.number().min(1).max(5).required(),
});
