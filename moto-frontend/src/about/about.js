import React, {Component} from 'react';

class About extends Component {

    render() {
        return (
            <>
                <div className="about container">
                    Logged user: {localStorage.getItem("username")}
                </div>
            </>
        );
    }
}

export default About;