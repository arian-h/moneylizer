import React, {Component} from 'react';
import axios from 'axios';
import createHistory from 'history/createBrowserHistory'

class RegisterForm extends Component {

    constructor(props) {
        super(props);
        this.onRegister = this.onRegister.bind(this);
        this.state = {
            username: '',
            password: ''
        };
    }

    register(username, password) {
        axios.post('http://localhost:8443/api/authentication/signup',
            {
                username: username,
                password: password
            }
        ).then(({status, headers: {authorization}}) => {
            localStorage.setItem('jwt_token', authorization);
            createHistory().push('/');
        }).catch(response => {
            debugger;
        });
    }

    onRegister() {
        this.register(this.state.username, this.state.password);
    }

    render() {
        return (
            <div>
                <input type='text' name='username' onChange={event => this.setState({username: event.target.value})}/>
                <input type='text' name='password' onChange={event => this.setState({password: event.target.value})}/>
                <button onClick={this.onRegister}>Register</button>
            </div>
        );
    }
}

export default RegisterForm;