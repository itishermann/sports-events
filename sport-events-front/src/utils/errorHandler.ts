import axios, { AxiosError } from 'axios';
import variables from '@/config/variables';

/**
 * Handles errors thrown by thunks, and throws an error if the error is not an axios error.
 * @param e {unknown} - the thrown error
 * @param code {boolean} - if true, the status code will be thrown instead of the error message
 * @returns {string} - the error message or the status code
 */

const errorHandler = (e: unknown, code = false) => {
  if (variables.dev) {
    console.debug(e);
  }
  const isAxiosError = axios.isAxiosError(e);
  if (!isAxiosError) {
    // @ts-expect-error - this is an error so the message property exists
    return e.message;
  }
  const error = e as AxiosError | undefined;
  if (e?.response?.status && code) {
    const statusCode = error?.response?.status;
    return statusCode?.toString();
  }
  if (error?.response?.data) {
    const data = error.response.data as any | undefined;
    return data?.message ?? 'Une erreur s\'est produite';
  }
  return error?.message ?? 'Une erreur s\'est produite';
};

export const checkIfObjectIsAnError = (
  e: unknown,
): e is Error | AxiosError => e instanceof Error || axios.isAxiosError(e);

export default errorHandler;
