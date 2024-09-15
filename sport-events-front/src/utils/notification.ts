import { NotificationProps, showNotification } from '@mantine/notifications';
import {
  IconAlertTriangle, IconCheck, IconInfoCircle, IconX,
} from '@tabler/icons-react';
import { createElement } from 'react';
import errorHandler, { checkIfObjectIsAnError } from '@/utils/errorHandler';

export const showSuccessNotification = (props: NotificationProps) => showNotification({
  color: 'green',
  icon: createElement(IconCheck),
  ...props,
});

export const showErrorNotification = (props: NotificationProps | any) => {
  if (checkIfObjectIsAnError(props)) {
    return showNotification({
      color: 'red',
      icon: createElement(IconX),
      title: 'Une erreur est survenue',
      message: errorHandler(props),
    });
  }
  return showNotification({
    color: 'red',
    icon: createElement(IconX),
    ...props,
  });
};

export const showInfoNotification = (props: NotificationProps) => showNotification({
  color: 'blue',
  icon: createElement(IconInfoCircle),
  ...props,
});

export const showWarningNotification = (props: NotificationProps) => showNotification({
  color: 'orange',
  icon: createElement(IconAlertTriangle),
  ...props,
});
