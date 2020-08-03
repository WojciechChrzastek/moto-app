import React, {Component} from 'react';
import Form from 'react-bootstrap/Form'
import Button from 'react-bootstrap/Button'

import '../styles/addCar.css';

import Alert from '../alert/registerAndLoginAlert.js';

class AddCar extends Component {

    constructor(props) {
        super(props);
        this.alert = React.createRef();
    }

    handleSubmit = event => {
        event.preventDefault();
        this.addCar(event.target.brandname.value, event.target.modelname.value, event.target.manufactureyear.value);
    }

    addCar(brandname, modelname, manufactureyear) {
        fetch('http://localhost:8080/carsnouser', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                brandname: brandname,
                modelname: modelname,
                manufactureyear: manufactureyear,
            })
        }).then(function (response) {
            if (response.status === 200) {
                this.showAlert("success", "Car added!", "One day you will be able to see it in \"my cars\" section.");
            } else if (response.status === 406) {
                this.showAlert("danger", "Insufficient data.", "Please fill in all fields.");
            } else {
                this.showAlert("danger", "Car not added!", "Something went wrong.");
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
                <div className="AddCar">
                    <h1 className="AddCarHeader">Add car</h1>
                    <Form onSubmit={this.handleSubmit}>
                        <Form.Group controlId="brandname">
                            <Form.Label>Brand</Form.Label>
                            <Form.Control autoFocus name="brandname" placeholder="Enter brand name"/>
                        </Form.Group>

                        <Form.Group controlId="modelname">
                            <Form.Label>Model</Form.Label>
                            <Form.Control name="modelname" placeholder="Enter model name" />
                        </Form.Group>

                        <Form.Group controlId="manufactureyear">
                            <Form.Label>Manufacture year</Form.Label>
                            <Form.Control name="manufactureyear" placeholder="Enter manufacture year"/>
                        </Form.Group>

                        <Button block size="lg" type="submit">Add</Button>
                    </Form>
                </div>

                <Alert ref={this.alert}/>
            </>
        );
    }
}

export default AddCar;