import React, {Component} from 'react';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import '../styles/register.css';

import Alert from './registerAndLoginAlert.js';

class Register extends Component {

    constructor(props) {
        super(props);
        this.alert = React.createRef();
    }

    handleSubmit = event => {
        event.preventDefault();
        this.registerUser(event.target.username.value, event.target.password.value, event.target.email.value);
    }

    registerUser(username, password, email) {
        fetch('http://localhost:8080/users', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                username: username,
                password: password,
                email: email,
            })
        }).then(function (response) {
            if (response.status === 200) {
                this.showAlert("success", "User registered!", "You can now log in using your credentials.");
            } else if (response.status === 422) {
                this.showAlert("danger", "User/email already exists", "Please choose a different name/email.");
            } else if (response.status === 406) {
                this.showAlert("danger", "Insufficient data.", "Please fill in all fields.");
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
                        <Form.Group controlId="username">
                            <Form.Label>Username</Form.Label>
                            <Form.Control autoFocus name="username" placeholder="Enter username"/>
                        </Form.Group>

                        <Form.Group controlId="password">
                            <Form.Label>Password</Form.Label>
                            <Form.Control type="password" name="password" placeholder="Enter password"/>
                        </Form.Group>

                        <Form.Group controlId="email">
                            <Form.Label>Email</Form.Label>
                            <Form.Control type="email" name="email" placeholder="Enter email"/>
                            <Form.Text className="text-muted">
                                Please provide a valid email address.
                            </Form.Text>
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