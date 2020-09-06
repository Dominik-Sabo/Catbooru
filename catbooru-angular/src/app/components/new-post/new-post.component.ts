import { Component, OnInit, Output, EventEmitter } from '@angular/core';
import { UserService } from 'src/app/services/user.service';
import { FormBuilder, FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { PostService } from 'src/app/services/post-service.service';

@Component({
  selector: 'app-new-post',
  templateUrl: './new-post.component.html',
  styleUrls: ['./new-post.component.css']
})
export class NewPostComponent implements OnInit {
  @Output() emitter = new EventEmitter<string>()
  file:File;
  tags:string = '';
  uploadForm:FormGroup;  
  error:boolean = false;
  

  constructor(private formBuilder: FormBuilder, private userService:UserService, private postService:PostService, private router:Router) { }

  ngOnInit(): void {
    if(this.userService.user == null) this.router.navigate(['']);
    this.uploadForm = this.formBuilder.group({
      file: [''],
      tags: [''],
      userId: ['']
    });
  }

  onKey(event) {
    this.tags = event.target.value;
  }

  onFileSelect(event){
      this.file = event.target.files[0];
  }

  onUploadClick(){
    this.uploadForm.get('tags').setValue(this.tags);
    this.uploadForm.get('file').setValue(this.file);
    this.uploadForm.get('userId').setValue(this.userService.user.id);

    const formData = new FormData();
    formData.append('imageFile', this.uploadForm.get('file').value);
    formData.append('userId', this.uploadForm.get('userId').value);
    formData.append('tags', this.uploadForm.get('tags').value);
    
    this.postService.addNewPost(this.userService.user, formData).subscribe(
      uploaded => {
        this.emitter.emit('uploaded');
      },
      error => {
        console.log(error);
        
        this.error = true;
        if(error.error == null){
          this.userService.logout()
          this.router.navigate(["login"]);
      }
    });
  }
}
