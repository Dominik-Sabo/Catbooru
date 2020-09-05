import { Component, OnInit, Input, Output, EventEmitter } from '@angular/core';
import { UserService } from 'src/app/services/user.service';

@Component({
  selector: 'app-tags',
  templateUrl: './tags.component.html',
  styleUrls: ['./tags.component.css']
})
export class TagsComponent implements OnInit {
  @Input() tag: string;
  @Output() emitter = new EventEmitter<string>();
  @Output() delete = new EventEmitter<string>();
  loggedIn:boolean = false;

  constructor(private userService:UserService) { }

  ngOnInit(): void {
    if(this.userService.user != null) this.loggedIn = true;
  }

  onClick(){
    this.emitter.emit(this.tag);
  }

  onDeleteClick(){
    if(confirm("remove " + this.tag + " from post?")) this.delete.emit(this.tag);
  }
}
