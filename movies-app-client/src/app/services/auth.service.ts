import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Router } from '@angular/router';
import { User } from '../models/user.model';
import { AuthResponse } from '../models/auth-response.model';
import { AUTH_URL } from '../constants';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  constructor(private http: HttpClient, private router: Router) {}

  login(email: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${AUTH_URL}/login`, {
      email,
      password,
    });
  }

  register(
    email: string,
    password: string,
    password2: string
  ): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${AUTH_URL}register`, {
      email,
      password,
      password2,
    });
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.router.navigate(['/login']);
  }

  getToken(): string | null {
    return localStorage.getItem('token');
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  isAdmin(): boolean {
    const user = this.getCurrentUser();
    return user?.role === 'ADMIN';
  }

  getCurrentUser(): User | null {
    if (localStorage.getItem('currentUser')) {
      const user = JSON.parse(localStorage.getItem('currentUser')!);
      return user
    }
    return null;
  }
}
