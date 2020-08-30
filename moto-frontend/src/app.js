import React, {Component} from 'react';

import Nav from 'react-bootstrap/Nav'
import Navbar from 'react-bootstrap/Navbar'

import {BrowserRouter as Router, Link, Redirect, Route} from 'react-router-dom';

import Login from './user/login.js'
import Register from './user/register.js'
import About from './user/about.js'
import Add from './car/add.js'
import Delete from './car/delete.js'
import ShowAll from './car/showAll.js'
import ShowByBrandname from './car/showByBrandname.js'

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
                <Navbar bg="dark" variant="dark" className="topMenu">
                    <Navbar.Brand className="m-auto" href="/">MotoApp</Navbar.Brand>
                    <Nav className="m-auto">
                        <Nav.Link href="/register">Register</Nav.Link>
                        <Nav.Link href="/login">Login</Nav.Link>
                        <Nav.Link href="/add-car">AddCar</Nav.Link>
                        <Nav.Link href="/delete-car">DeleteCar</Nav.Link>
                        <Nav.Link href="/show-all-cars">Show all cars</Nav.Link>
                        <Nav.Link href="/show-by-brandname">Show by brandname</Nav.Link>
                        <Nav.Link href="/about">About</Nav.Link>
                    </Nav>
                </Navbar>

                <div className="container d-flex flex-row-reverse">
                    <span>{localStorage.getItem("username") !== null ?
                        <Link to="/login" onClick={this.logout}>Log out</Link> : null}</span>
                    <span>&nbsp;</span>
                    <span>Logged user: {localStorage.getItem("username")}</span>
                </div>

                <div className="container">
                    <Route
                        path="/login"
                        render={props => <Login updateUsername={this.updateUsername}/>}
                    />
                    <Route path="/register" component={Register}/>
                    <Route path="/add-car" component={Add}/>
                    <Route path="/delete-car" component={Delete}/>
                    <PrivateRoute path="/about" component={About}/>
                    <Route path="/show-all-cars" component={ShowAll}/>
                    <Route path="/show-by-brandname" component={ShowByBrandname}/>
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
