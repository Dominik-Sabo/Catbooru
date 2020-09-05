import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { TagsService } from 'src/app/services/tags.service';

@Component({
  selector: 'app-all-tags',
  templateUrl: './all-tags.component.html',
  styleUrls: ['./all-tags.component.css']
})
export class AllTagsComponent implements OnInit {
  @Output() emitter = new EventEmitter();

  tags:string[]

  constructor(private tagsService:TagsService) { }

  ngOnInit(): void {
    this.tagsService.getTags().subscribe(tags => this.tags = tags)
  }

  onClick(tag:string){
    this.emitter.emit('posts/'+tag);
  }

}
