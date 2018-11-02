import React, { Component } from 'react';
import SockJS from 'sockjs-client';
import { Stomp } from '@stomp/stompjs';

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

    const socket = new SockJS('http://localhost:8443/api/wsocket?token=%s'.replace('%s', jwtToken));
    let client = Stomp.over(socket);

    client.connect({}, function() {
      debugger;
    }, function() {
      debugger;
    });

    socket.onclose = function() {
      debugger;
    }
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
