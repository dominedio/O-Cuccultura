import { Injectable } from '@angular/core';
import { CanActivate, CanActivateChild, CanLoad, Route, UrlSegment, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AutentificationguardGuard implements CanActivate, CanActivateChild, CanLoad {

  constructor(private router: Router) { }
  
  canActivate(
     next: ActivatedRouteSnapshot,
      state: RouterStateSnapshot): boolean | UrlTree {
      let url: string = state.url;

      return this.checkLogin(url);
   
  }
  canActivateChild(
    next: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return true;
  }
  canLoad(
    route: Route,
    segments: UrlSegment[]): Observable<boolean> | Promise<boolean> | boolean {
    return true;
  }

  checkLogin(url: string): true | UrlTree {
    console.log("Url: " + url)
    let val: string = localStorage.getItem('isUserLoggedIn');

    if (val != null && val == "true") {
       if (url == "/login") {
          //this.router.parseUrl(next_url);
          console.log(url);
          this.router.navigateByUrl(url);
       }
       else
          return true;
    } else {
       return this.router.parseUrl('/login');
    }
 }

}
