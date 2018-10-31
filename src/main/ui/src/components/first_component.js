import React, { Component } from 'react';
import Stomp from '@stomp/stompjs';

class FirstComponent extends Component {

  componentDidMount() {
    const socket = new WebSocket('ws://localhost:8443/wsocket/websocket');
    // Connection opened
    socket.addEventListener('open', function (event) {
        debugger;
        socket.send('salam');
    });

    // Listen for messages
    socket.addEventListener('message', function (event) {
        debugger;
    });

  }

  render() {
    return (<div>the first component!</div>);
  }
}

export default FirstComponent;
