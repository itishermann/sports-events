import {
  Button, Container, Group, Overlay, Text, Title, createStyles, rem,
} from '@mantine/core';
import Link from 'next/link';

const useStyles = createStyles((theme) => ({
  wrapper: {
    position: 'relative',
    paddingTop: rem(180),
    paddingBottom: rem(130),
    backgroundImage:
      'url(https://images.unsplash.com/photo-1587280501635-68a0e82cd5ff?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=870&q=80)',
    backgroundSize: 'cover',
    backgroundPosition: 'center',

    [theme.fn.smallerThan('xs')]: {
      paddingTop: rem(80),
      paddingBottom: rem(50),
    },
    height: '100vh',
    justifyContent: 'center',
    alignItems: 'center',
    display: 'flex',

    // smooth transition between background images
    transition: 'background-image 1000ms ease-in-out',

  },

  inner: {
    position: 'relative',
    zIndex: 1,
  },

  title: {
    fontWeight: 800,
    fontSize: rem(40),
    letterSpacing: rem(-1),
    paddingLeft: theme.spacing.md,
    paddingRight: theme.spacing.md,
    color: theme.white,
    marginBottom: theme.spacing.xs,
    textAlign: 'center',
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,

    [theme.fn.smallerThan('xs')]: {
      fontSize: rem(28),
      textAlign: 'left',
    },
  },

  highlight: {
    color: theme.colors[theme.primaryColor][4],
  },

  description: {
    color: theme.colors.gray[0],
    textAlign: 'center',

    [theme.fn.smallerThan('xs')]: {
      fontSize: theme.fontSizes.md,
      textAlign: 'left',
    },
  },

  controls: {
    marginTop: `calc(${theme.spacing.xl} * 1.5)`,
    textDecoration: 'none',
    paddingLeft: theme.spacing.md,
    paddingRight: theme.spacing.md,

    [theme.fn.smallerThan('xs')]: {
      flexDirection: 'column',
    },
  },

  control: {
    height: rem(42),
    fontSize: theme.fontSizes.md,
    display: 'flex',
    justifyContent: 'space-between',
    '&:not(:first-of-type)': {
      marginLeft: theme.spacing.md,
    },

    [theme.fn.smallerThan('xs')]: {
      '&:not(:first-of-type)': {
        marginTop: theme.spacing.md,
        marginLeft: 0,
      },
    },
  },

  secondaryControl: {
    color: theme.white,
    backgroundColor: 'rgba(255, 255, 255, .4)',

    '&:hover': {
      backgroundColor: 'rgba(255, 255, 255, .45) !important',
    },
  },
}));

export default function Home() {
  const { classes, cx } = useStyles();
  return (
    <div className={classes.wrapper} id="wapper">
      <Overlay color="#000" opacity={0.65} zIndex={1} />

      <div className={classes.inner}>
        <Title className={classes.title}>
          Vivez des expériences sportives
          {' '}
          <Text component="span" inherit className={classes.highlight}>
            inoubliables
          </Text>
        </Title>

        <Container size={640}>
          <Text size="lg" className={classes.description}>
            Rejoignez la communauté passionnée de sportifs locaux
            et vivez des expériences sportives uniques dans votre région.
          </Text>
        </Container>

        <Group align="center" position="center" className={classes.controls}>
          <Link href="/auth/register" passHref>
            <Button className={classes.control} variant="white" size="lg">
              Rejoindre
            </Button>
          </Link>
          <Link href="/auth/login" passHref>
            <Button className={cx(classes.control, classes.secondaryControl)} size="lg">
              Se connecter
            </Button>
          </Link>
        </Group>
      </div>
    </div>
  );
}
