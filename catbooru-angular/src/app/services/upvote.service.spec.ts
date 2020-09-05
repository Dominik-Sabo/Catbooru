import { TestBed } from '@angular/core/testing';

import { UpvoteService } from './upvote.service';

describe('UpvoteService', () => {
  let service: UpvoteService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UpvoteService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
