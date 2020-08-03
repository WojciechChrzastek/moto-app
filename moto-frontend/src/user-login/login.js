import React, {Component} from 'react';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import '../styles/login.css';

import Alert from "./registerAndLoginAlert.js";

class Login extends Component {
    constructor(props) {
        super(props);
        this.alert = React.createRef();
    }

    handleSubmit = event => {
        event.preventDefault();
        this.loginUser(event.target.username.value, event.target.password.value);
    }

    loginUser(username, password) {
        fetch('http://localhost:8080/login', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
            })
        }).then(function (response) {
            if (response.status === 200) {
                this.showAlert("success", "Login successful!", "You are now logged in.");
                localStorage.setItem("username", username);
                this.props.updateUsername();
            } else {
                this.showAlert("danger", "Wrong credentials", "Username and/or password is wrong.");
            }
        }.bind(this)).catch(function (error) {
            this.showAlert("danger", "Error", "Something went wrong.");
        }.bind(this));
    }

    showAlert(variant, heading, message) {
        this.alert.current.setVariant(variant);
        this.alert.current.setHeading(heading);
        this.alert.current.setMessage(message);
        this.alert.current.setVisible(true);
    }

    render() {
        return (
            <>
                <div className="Login">
                    <h1 className="LoginHeader">Login</h1>
                    <Form onSubmit={this.handleSubmit}>

                        <Form.Group controlId="username" size="lg">
                            <Form.Label>Username</Form.Label>
                            <Form.Control autoFocus name="username" placeholder="Enter username"/>
                        </Form.Group>

                        <Form.Group controlId="password" size="lg">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" name="password" placeholder="Enter password"/>
                        </Form.Group>

                        <Button block size="lg" type="submit">
                            Login
                        </Button>

                    </Form>

                </div>

                <Alert ref={this.alert}/>

            </>
        );
    }

}

export default Login;