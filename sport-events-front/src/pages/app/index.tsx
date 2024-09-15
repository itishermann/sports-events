import { checkAuthServerSide } from '@/utils/helpers/auth';
import dynamic from 'next/dynamic';

const EventMap = dynamic(
  () => import('@/components/Map/EventMap'),
  { ssr: false },
);

export default function App() {
  return (
    <main style={{ height: '100vh', width: '100vw', zIndex: 1 }}>
      <EventMap />
    </main>
  );
}

export const getServerSideProps = checkAuthServerSide();
