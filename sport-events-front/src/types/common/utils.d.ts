interface HeaderLink {
  link: string;
  label: string;
  links?: HeaderLink[]
}

interface NavLink {
  link: string;
  label: string;
  description?: string;
  icon?: React.ReactNode;
  links?: NavLink[];
}
