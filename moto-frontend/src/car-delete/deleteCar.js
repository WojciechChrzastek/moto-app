import React, {Component} from 'react';

import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'
import '../styles/style.css';

import Alert from '../alert/registerAndLoginAlert.js';

class DeleteCar extends Component {

    constructor(props) {
        super(props);
        this.alert = React.createRef();
    }

    handleSubmit = event => {
        event.preventDefault();
        this.deleteCar(event.target.id.value);
    }

    deleteCar(id) {
        console.log(id);
        fetch('http://localhost:8080/cars', {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(id),
        }).then(function (response) {
            if (response.status === 200) {
                this.showAlert("success", "Car deleted!", "It should not appear in the \"all cars\" section.");
            } else if (response.status === 422) {
                this.showAlert("danger", "Car not deleted!", "There is no car of given ID.");
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
                <div className="DeleteCar">
                    <h1 className="DeleteCarHeader">Delete car</h1>
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Group controlId="carid">
                            <Form.Label>Car id</Form.Label>
                            <Form.Control autoFocus name="id" placeholder="Enter car id"/>
                        </Form.Group>

                        <Button block size="lg" type="submit">Delete</Button>
                    </Form>
                </div>

                <Alert ref={this.alert}/>
            </>
        );
    }
}

export default DeleteCar;