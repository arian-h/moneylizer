import ReactDOM from 'react-dom';
import React from 'react';
import {Router, Route, Switch, Redirect} from 'react-router-dom';
import createHistory from 'history/createBrowserHistory'

import {isAuthenticated} from './helpers/auth_utils';
import FirstComponent from "./components/first_component";
import LoginForm from "./components/authentication/login_form";
import RegisterForm from "./components/authentication/register_form";
import PrivateRoute from "./components/route/private_route";

ReactDOM.render(
    <Router history={createHistory()}>
        <Switch>
            <Route exact path="/login" render={props =>
                isAuthenticated() ? <Redirect to='/'/> :
                    <LoginForm {...props}/>
            }/>
            <Route exact path="/register" render={props =>
                isAuthenticated() ? <Redirect to='/'/> :
                    <RegisterForm {...props}/>
            }/>
            <PrivateRoute component={FirstComponent}/>
        </Switch>
    </Router>
    , document.querySelector('.container'));
