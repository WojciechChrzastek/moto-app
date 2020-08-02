import React, {Component} from 'react';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import './register.css';

import Alert from './registerAndLoginAlert.js';

class Register extends Component {

    constructor(props) {
        super(props);
        this.alert = React.createRef();
    }

    handleSubmit = event => {
        event.preventDefault();
        this.registerUser(event.target.username.value, event.target.password.value);
    }

    registerUser(username, password) {
        fetch('http://localhost:8080/users', {
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
                this.showAlert("success", "User registered!", "You can now log in using your credentials.");
            } else if (response.status === 422) {
                this.showAlert("danger", "User already exists", "Please choose a different name.");
            } else {
                this.showAlert("danger", "User not registered!", "Something went wrong.");
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
                <div className="Register">
                    <h1 className="RegisterHeader">Register</h1>
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Group controlId="username" size="lg">
                            <Form.Label>Username</Form.Label>
                            <Form.Control autoFocus name="username"/>
                        </Form.Group>

                        <Form.Group controlId="password" size="lg">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" name="password"/>
                        </Form.Group>

                        <Button block size="lg" type="submit">Register</Button>
                    </Form>
                </div>

                <Alert ref={this.alert}/>
            </>
        );
    }
}

export default Register;