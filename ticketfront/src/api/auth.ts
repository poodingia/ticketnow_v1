import axios from 'axios';

type AuthenticationInfoVm = {
  isAuthenticated: boolean;
  authenticatedUser: AuthenticatedUser;
};

type AuthenticatedUser = {
  username: string;
};

export async function getAuthenticationInfo(): Promise<AuthenticationInfoVm> {
  const { data } = await axios.get('/authentication');
  return data;
}