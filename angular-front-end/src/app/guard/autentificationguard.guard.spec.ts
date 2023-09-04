import { TestBed, async, inject } from '@angular/core/testing';

import { AutentificationguardGuard } from './autentificationguard.guard';

describe('AutentificationguardGuard', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AutentificationguardGuard]
    });
  });

  it('should ...', inject([AutentificationguardGuard], (guard: AutentificationguardGuard) => {
    expect(guard).toBeTruthy();
  }));
});
