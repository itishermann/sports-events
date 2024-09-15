import * as yup from 'yup';

export const CreateEventSchema = yup.object().shape({
  title: yup.string().min(1).max(255).required('Le titre est requis'),
  location: yup.string().min(1).max(255).required('Le lieu est requis'),
  dateStart: yup.date().required('La date de début est requise'),
  dateEnd: yup.date().required('La date de fin est requise'),
  maxParticipants: yup.number().min(1).required('Le nombre de participants maximal est requis'),
  sportId: yup.number().min(1).required('Le sport est requis'),
  latitude: yup.number().required('La latitude est requise'),
  longitude: yup.number().required('La longitude est requise'),
});
