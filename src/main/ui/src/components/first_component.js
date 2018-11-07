import React, { Component } from 'react';
import SockJS from 'sockjs-client'; //TODO can we get rid of this?
import { Client } from '@stomp/stompjs';

class FirstComponent extends Component {

  componentDidMount() {
    this.connect();
  }

  connect() {
    let jwtToken = localStorage.getItem('jwt_token');
    if (jwtToken == null) {
      throw new Error('You need to first log in');
    }
    jwtToken = jwtToken.replace('Bearer', '');
    this.client = new Client();
    this.client.configure({
      brokerURL: `ws://localhost:8663/api/wsocket?token=${jwtToken}`,
      onConnect: () => {
        this.client.subscribe('/quote/arian', message => {
          console.log(message);
          debugger;
        });
        // this.client.publish({
        //   destination: '/ws/quote',
        //   body: 'salam'
        // });
      },
      // Helps during debugging, remove in production
      debug: (str) => {
        console.log(new Date(), str);
      }
    });
    this.client.activate();
    // const socket = new SockJS('http://localhost:8443/api/wsocket?token=%s'.replace('%s', jwtToken));
    // let client = Stomp.over(socket);
    //
    // client.connect({}, function() {
    //   debugger;
    // }, function() {
    //   debugger;
    // });
    //
    // socket.onclose = function() {
    //   debugger;
    // }
  }

  onConnected() {
    debugger;
  }

  onError() {
    debugger;
  }
  render() {
    return (<div>the first component!</div>);
  }
}

export default FirstComponent;
