import React, {Component} from 'react';

import Nav from 'react-bootstrap/Nav'

import {BrowserRouter as Router, Link, Redirect, Route} from 'react-router-dom';

import './styles/app.css';

import Login from './user-login/login.js'
import Register from './user-register/register.js'
import About from './about/about.js'
import AddCar from './car-add/addCar.js'
import ShowAllCars from './car-show-all/showAllCars.js'

class App extends Component {
    state = {username: "", isAuthenticated: false};

    updateUsername = () => {
        const username = localStorage.getItem("username");
        this.setState({username: username});
        if (username.length > 0) {
            this.setState({isAuthenticated: true});
            console.log("true");
        } else {
            this.setState({isAuthenticated: false});
            console.log("false");
        }
    };

    render() {
        return (
            <Router>
                <div className="container">
                    <Nav className="topMenu justify-content-center" activeKey="/home">
                        <Nav.Item>
                            <Nav.Link href="/register">Register</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/addCar">AddCar</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/showAllCars">Show all cars</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/about">About</Nav.Link>
                        </Nav.Item>
                        <Nav.Item>
                            <Nav.Link href="/login">Login</Nav.Link>
                        </Nav.Item>
                    </Nav>

                    <div className="d-flex flex-row-reverse">
                        <span>Logged in user: {localStorage.getItem("username")}</span>
                        <span>&nbsp;</span>
                        <span>{localStorage.getItem("username") !== null ?
                            <Link to="/login" onClick={this.logout}>Log out</Link> : null}</span>
                    </div>
                </div>

                <div className="container">
                    <Route
                        path="/login"
                        render={props => <Login updateUsername={this.updateUsername}/>}
                    />
                    <Route path="/register" component={Register}/>
                    <Route path="/addCar" component={AddCar}/>
                    <PrivateRoute path="/about" component={About}/>
                    <Route path="/showAllCars" component={ShowAllCars}/>
                </div>
            </Router>
        );
    }

}

function PrivateRoute({component: Component, ...rest}) {
    return (
        <Route
            {...rest}
            render={props =>
                localStorage.getItem("username") !== null ? (
                    <Component {...props} />
                ) : (
                    <Redirect
                        to={{
                            pathname: "/login",
                            state: {from: props.location}
                        }}
                    />
                )
            }
        />
    );
}

export default App;
