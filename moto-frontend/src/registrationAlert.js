import React, { Component } from 'react'
import Alert from 'react-bootstrap/Alert'
import Registration from './Registration';

import './registrationAlert.css'

class RegistrationAlert extends Component {
    constructor(props) {
        super(props);

        this.state = {
            visible: this.props.visible,
            variant: this.props.variant,
            heading: this.props.heading,
            message: this.props.message
        }
    }

    setMessage = (message) => {
        this.setState({ message: message });
    }

    setHeading = (heading) => {
        this.setState({ heading: heading });
    }

    setVariant = (variant) => {
        this.setState({ variant: variant });
    }

    setVisible = (visible) => {
        this.setState({ visible: visible });
    }

    render() {
        if (this.state.visible) {
          return (
              <>
                <div className = "RegistationAlert">
                    <Alert variant = { this.state.variant } onClose = {() => this.setState({ visible: false})} dismissible>
                        <Alert.heading>{ this.state.Alert.heading }</Alert.heading>
                        <p>
                            {this.state.message}
                        </p>
                    </Alert>
                </div>
              </>
          );  
        }
    return null;
    }
}

export default RegistrationAlert;