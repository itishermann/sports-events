import {
  Center, Loader, AppShell as MantineAppShell,
  useMantineTheme,
} from '@mantine/core';
import Header from '@/components/layout/Header';
import NavbarNested from '@/components/layout/Navbar';
import { IconCalendar, IconList, IconUsers } from '@tabler/icons-react';

const navbarLinks: NavLink[] = [
  {
    label: 'Evenements', link: '/app', description: 'List des activités', icon: <IconCalendar />,
  },
  {
    label: 'Utilisateurs', link: '/app/users', description: 'Liste des utilisateurs', icon: <IconUsers />,
  },
  {
    label: 'Mes participations', link: '/app/registrations', description: 'Liste de mes participations', icon: <IconList />,
  },
  {
    label: 'Mes activités', link: '/app/activities', description: 'Liste de mes activités', icon: <IconList />,
  },
];

const headerLinks: HeaderLink[] = navbarLinks.map((link) => ({
  label: link.label,
  link: link.link,
}));

interface AppShellProps {
  children?: React.ReactNode;
  loading?: boolean;
  headerPaddingOnly?: boolean;
}

export default function AppShell({ children, loading, headerPaddingOnly }: AppShellProps) {
  const theme = useMantineTheme();
  return (
    <MantineAppShell
      styles={{
        main: {
          background: theme.colorScheme === 'dark' ? theme.colors.dark[8] : theme.colors.gray[0],
          paddingLeft: headerPaddingOnly ? 0 : undefined,
          paddingRight: headerPaddingOnly ? 0 : undefined,
          paddingBottom: headerPaddingOnly ? 0 : undefined,
          paddingTop: headerPaddingOnly ? theme.spacing.xl : undefined,
        },
      }}
      navbarOffsetBreakpoint="sm"
      asideOffsetBreakpoint="sm"
      header={<Header links={headerLinks} />}
      navbar={<NavbarNested links={navbarLinks} />}
    >
      {loading ? <Center><Loader size="xl" variant="dots" /></Center> : children}
    </MantineAppShell>
  );
}
