import navbarHooks from '@/hooks/navbar.hooks';
import { NavLink, NavLinkProps } from '@mantine/core';
import Link from 'next/link';
import router from 'next/router';
import React from 'react';

interface NavbarLinkProps extends NavLinkProps {
  icon?: React.ReactNode;
  label: string;
  description?: string;
  href: string;
  nested?: boolean;
}

const NavbarLink = ({
  icon,
  label,
  description,
  href,
  nested = false,
  ...others
}: NavbarLinkProps) => {
  const navbar = navbarHooks();
  return (
    <Link href={href} passHref>
      <NavLink
        icon={icon}
        label={label}
        description={description}
        active={router.pathname === href}
        onClick={navbar.close}
        sx={(theme) => ({
          borderLeft: nested ? `1px solid ${theme.colorScheme === 'dark' ? theme.colors.dark[4] : theme.colors.gray[3]}` : 'none',
        })}
        {...others}
      />
    </Link>
  );
};

export default NavbarLink;
