import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import {HttpClientModule} from '@angular/common/http'

import { FormsModule, ReactiveFormsModule } from '@angular/forms'
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { PostsListComponent } from './components/posts-list/posts-list.component';
import { PostsItemComponent } from './components/posts-item/posts-item.component';
import { HomeComponent } from './components/home/home.component';
import { LoginComponent } from './components/login/login.component';
import { PostComponent } from './components/post/post.component';
import { TagsComponent } from './components/tags/tags.component';
import { CommentsComponent } from './components/comments/comments.component';
import { PostDataComponent } from './components/post-data/post-data.component';
import { RegisterComponent } from './components/register/register.component';
import { AccountComponent } from './components/account/account.component';
import { AllTagsComponent } from './components/all-tags/all-tags.component';
import { NewPostComponent } from './components/new-post/new-post.component';

@NgModule({
  declarations: [
    AppComponent,
    PostsListComponent,
    PostsItemComponent,
    HomeComponent,
    LoginComponent,
    PostComponent,
    TagsComponent,
    CommentsComponent,
    PostDataComponent,
    RegisterComponent,
    AccountComponent,
    AllTagsComponent,
    NewPostComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
