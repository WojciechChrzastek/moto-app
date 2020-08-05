import React, {Component} from 'react';

import '../styles/style.css';
import Table from "react-bootstrap/Table";

class ShowAllCars extends Component {

    state = {
        data: [],
    }

    componentDidMount() {
        fetch('http://localhost:8080/cars')
            .then(response => response.json())
            .then(data => {
                this.setState({data})
            });
    }

    render() {
        return (
            <div className="container">
                <Table striped bordered hover>
                    <thead>
                    <tr>
                        <th>Id</th>
                        <th>Brand</th>
                        <th>Model</th>
                        <th>Year</th>
                    </tr>
                    </thead>
                    <tbody>
                    {this.state.data.map(function (car, key) {
                        return (
                            <tr key={key}>
                                <td>{car.id}</td>
                                <td>{car.brandname}</td>
                                <td>{car.modelname}</td>
                                <td>{car.manufactureyear}</td>
                            </tr>
                        )
                    })}
                    </tbody>
                </Table>
            </div>
        )
    }
}

export default ShowAllCars;