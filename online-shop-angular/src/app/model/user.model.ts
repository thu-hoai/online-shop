interface JwtUser {
  token: string;
  user: User;
}

interface User {
  id: number;
  username: string;
  firstName?: any;
  lastName?: any;
  email?: any;
  phone?: any;
  address?: any;
  enabled: boolean;
  authorities: Authority[];
}

interface Authority {
  id: number;
  name: string;
  description: string;
  authority: string;
}
