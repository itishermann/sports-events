import navbarHooks from '@/hooks/navbar.hooks';
import {
  Drawer,
  Navbar,
  ScrollArea,
  createStyles,
} from '@mantine/core';
import UserBurger from './UserBurger';
import NavbarLink from './NavBarLink';

const useStyles = createStyles((theme) => ({
  navbar: {
    backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.white,
    paddingBottom: 0,
  },

  header: {
    padding: theme.spacing.md,
  },

  links: {
    marginLeft: -theme.spacing.md,
    marginRight: -theme.spacing.md,
  },
}));

export interface NavbarProps {
  links: NavLink[];
}
const NavbarNested = ({ links }: NavbarProps) => {
  const { classes } = useStyles();
  const navbar = navbarHooks();

  const items = links.map((link) => {
    const menuItems = link.links?.map((item) => (
      <NavbarLink
        href={item.link}
        icon={item.icon}
        description={item.description}
        label={item.label}
        nested
        key={item.label}
      />
    ));

    if (menuItems) {
      return (
        <NavbarLink
          href={link.link}
          icon={link.icon}
          description={link.description}
          label={link.label}
        >
          {menuItems}
        </NavbarLink>
      );
    }

    return (
      <NavbarLink
        href={link.link}
        icon={link.icon}
        description={link.description}
        label={link.label}
      />
    );
  });

  return (
    <Drawer
      opened={navbar.isOpen}
      onClose={navbar.close}
      padding="0"
      size="sm"
      title="Sports Events"
      position="right"
      classNames={{
        header: classes.header,
      }}
    >
      <Navbar>
        <Navbar.Section px="xs">Menu</Navbar.Section>
        <Navbar.Section grow mx="-xs" px="xs" component={ScrollArea}>
          {items}
        </Navbar.Section>
        <Navbar.Section>
          <UserBurger />
        </Navbar.Section>
      </Navbar>
    </Drawer>
  );
};

export default NavbarNested;
