import React, {Component} from 'react';
import axios from 'axios';
import createHistory from 'history/createBrowserHistory'

class LoginForm extends Component {

    constructor(props) {
        super(props);
        this.onLogin = this.onLogin.bind(this);
        this.state = {
            username: '',
            password: ''
        };
    }

    login(username, password, redirected_from, errorCallback) {
        axios.post('http://localhost:8443/api/authentication/login',
            {
                username: username,
                password: password
            }
        ).then(response => {
            const {headers: {authorization}} = response;
            localStorage.setItem('jwt_token', authorization);
            localStorage.setItem('user_id', response.data);
            createHistory().push(redirected_from);
        }).catch(({response}) => {
            if (!response) {
                //Network error
                //show a sticky message with offline message
            } else {
                if (response.status === 401) { // Unauthorized
                    errorCallback();
                }
            }
        });
    }

    onLogin() {
        const redirected_from = this.props.location.state ? this.props.location.state.from.pathname : '/';
        this.login(this.state.username, this.state.password, redirected_from);
    }

    render() {
        return (
            <div>
                <input type='text' name='username' onChange={event => this.setState({username: event.target.value})}/>
                <input type='text' name='password' onChange={event => this.setState({password: event.target.value})}/>
                <button onClick={this.onLogin}>Login</button>
            </div>
        );
    }
}

export default LoginForm;