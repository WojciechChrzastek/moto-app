import React, {Component} from 'react';
import './App.css';

import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

class Registration extends Component {
  render() {
    return <div className = "Register">
      <Form>
        <Form.Group controlId = "username" size = "lg">
          <Form.Label>Username</Form.Label>
          <Form.Control autofocus name = "username"/>
        </Form.Group>
       
        <Form.Group controlId = "password" size = "lg">
          <Form.Label>Password</Form.Label>
          <Form.Control type = "password" name = "password"/>
        </Form.Group>

        <Button block size = "lg" type = "submit">Register</Button>
      </Form>
    </div>
  }
}

export default Registration;
