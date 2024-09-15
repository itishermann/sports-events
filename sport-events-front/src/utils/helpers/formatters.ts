export const getUserRole = (role: UserRole) => {
  switch (role) {
    case 'ROLE_USER':
      return 'Utilisateur';
    case 'ROLE_MODERATOR':
      return 'Modérateur';
    case 'ROLE_ADMIN':
      return 'Administrateur';
    default:
      return 'Utilisateur';
  }
};
