import { Injectable } from '@angular/core';
import * as Stomp from 'stompjs';
import * as SockJS from 'sockjs-client';
import { Server } from '../constants/server';

@Injectable({
  providedIn: 'root'
})
export class SocketService {
  private constant: Server = new Server();
  private stompClient;
  private ws;

  constructor() { }

  connect() {
    this.ws = new SockJS( this.constant.host + '/socket');
    this.stompClient = Stomp.over(this.ws);
    return this.stompClient;
  }
}
