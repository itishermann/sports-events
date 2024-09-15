import navbarHooks from '@/hooks/navbar.hooks';
import {
  Burger,
  Center,
  Container,
  Group,
  Header as MantineHeader,
  Menu,
  Title,
  createStyles,
  rem,
} from '@mantine/core';
import { IconChevronDown } from '@tabler/icons-react';
import { useRouter } from 'next/router';
import UserBurger from '@/components/layout/UserBurger';

const useStyles = createStyles((theme) => ({
  inner: {
    height: rem(56),
    display: 'flex',
    justifyContent: 'space-between',
    alignItems: 'center',
  },

  links: {
    [theme.fn.smallerThan('sm')]: {
      display: 'none',
    },
  },

  burger: {
    [theme.fn.largerThan('sm')]: {
      display: 'none',
    },
  },

  link: {
    display: 'block',
    lineHeight: 1,
    padding: `${rem(8)} ${rem(12)}`,
    borderRadius: theme.radius.sm,
    textDecoration: 'none',
    color: theme.colorScheme === 'dark' ? theme.colors.dark[0] : theme.colors.gray[7],
    fontSize: theme.fontSizes.sm,
    fontWeight: 500,

    '&:hover': {
      backgroundColor: theme.colorScheme === 'dark' ? theme.colors.dark[6] : theme.colors.gray[0],
    },
  },

  linkLabel: {
    marginRight: rem(5),
  },
}));

export interface HeaderSearchProps {
  links: HeaderLink[];
}

function Header({ links }: HeaderSearchProps) {
  const navbar = navbarHooks();
  const { classes } = useStyles();
  const router = useRouter();
  const items = links.map((link) => {
    const menuItems = link.links?.map((item) => (
      <Menu.Item key={item.link}>{item.label}</Menu.Item>
    ));

    if (menuItems) {
      return (
        <Menu key={link.label} trigger="hover" transitionProps={{ exitDuration: 0 }} withinPortal>
          <Menu.Target>
            <a
              href={link.link}
              className={classes.link}
              onClick={(event) => {
                event.preventDefault();
                router.push(link.link);
              }}
            >
              <Center>
                <span className={classes.linkLabel}>{link.label}</span>
                <IconChevronDown size="0.9rem" stroke={1.5} />
              </Center>
            </a>
          </Menu.Target>
          <Menu.Dropdown>{menuItems}</Menu.Dropdown>
        </Menu>
      );
    }

    return (
      <a
        key={link.label}
        href={link.link}
        className={classes.link}
        onClick={(event) => {
          event.preventDefault();
          router.push(link.link);
        }}
      >
        {link.label}
      </a>
    );
  });

  return (
    <MantineHeader height={56} mb={120}>
      <Container>
        <div className={classes.inner}>
          <Title order={4}>
            Sport Events
          </Title>
          <Group spacing={5} className={classes.links}>
            {items}
            <UserBurger />
          </Group>
          <Burger
            opened={navbar.isOpen}
            onClick={navbar.toggleNavbar}
            className={classes.burger}
            size="sm"
          />
        </div>
      </Container>
    </MantineHeader>
  );
}

export default Header;
