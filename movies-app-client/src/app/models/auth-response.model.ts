import { User, UserRole } from './user.model';

export interface AuthResponse {
  userId : number;
  email: string;
  role : UserRole;
  token: string;
}
