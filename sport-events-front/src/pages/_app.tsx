import RouterTransition from '@/components/layout/RouterTransition';
import setupDayJs from '@/config/dayjs';
import modals from '@/config/modals';
import variables from '@/config/variables';
import { showErrorNotification } from '@/utils/notification';
import { DatesProvider } from '@mantine/dates';
import { ModalsProvider } from '@mantine/modals';
import { Notifications } from '@mantine/notifications';
import { AppProps } from 'next/app';
import Head from 'next/head';
import { MantineProvider } from '@mantine/core';
import {
  Hydrate, MutationCache,
  QueryClient,
  QueryClientProvider,
} from '@tanstack/react-query';
import { ReactQueryDevtools } from '@tanstack/react-query-devtools';
import { useState } from 'react';

setupDayJs();
export default function App({ Component, pageProps }: AppProps) {
  const mutationCache = new MutationCache({
    onError: (error, _variables, _context, mutation) => {
      // If this mutation has an onError defined, skip this
      if (mutation.options.onError) return;
      showErrorNotification(error);
    },
  });
  const [queryClient] = useState(() => new QueryClient({
    mutationCache,
    defaultOptions: {
      mutations: {
        retry: (failureCount) => failureCount <= 1,
      },
      queries: {
        retry: (failureCount) => failureCount <= variables.api.maxRetryCount,
      },
    },
  }));

  return (
    <MantineProvider
      withGlobalStyles
      withNormalizeCSS
      theme={{
        colorScheme: 'dark',
      }}
    >
      <Head>
        <meta name="viewport" content="minimum-scale=1, initial-scale=1, width=device-width" />
      </Head>
      <QueryClientProvider client={queryClient}>
        <Hydrate state={pageProps.dehydratedState}>
          <Notifications position="top-center" />
          <DatesProvider settings={{ locale: 'fr' }}>
            <ModalsProvider modals={modals}>
              <RouterTransition />
              <Component {...pageProps} />
            </ModalsProvider>
          </DatesProvider>
        </Hydrate>
        <ReactQueryDevtools initialIsOpen={false} />
      </QueryClientProvider>
    </MantineProvider>
  );
}
