import { useLocalStorage } from '@mantine/hooks';

export default function navbarHooks() {
  const [navbar, setNavbar] = useLocalStorage<'opened' | 'closed'>({ key: 'navbar-status', defaultValue: 'closed' });
  const toggleNavbar = () => setNavbar(navbar === 'opened' ? 'closed' : 'opened');

  const isOpen = navbar === 'opened';

  const close = () => setNavbar('closed');

  const open = () => setNavbar('opened');

  return {
    isOpen, close, open, toggleNavbar,
  };
}
