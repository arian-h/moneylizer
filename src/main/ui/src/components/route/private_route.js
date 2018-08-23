import React from 'react';
import {Route, Redirect} from 'react-router-dom';
import {isAuthenticated} from "../../helpers/auth_utils";

const PrivateRoute = ({component: Component, ...rest}) => {
    return (<Route {...rest} render={props => {
        if (isAuthenticated()) {
            return <Component {...props}/>;
        } else {
            return <Redirect to={{pathname: '/login', state: {from: props.location}}}/>;
        }
    }
    }/>);
};

export default PrivateRoute;
