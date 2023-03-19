import React, { Component } from 'react';
import './Profile.css';

class Profile extends Component {
    constructor(props) {
        super(props);
        console.log(props);
    }
    render() {
        return (
            <div className="profile-container">
                <div className="Container">
                    <div className="profile-info">
                        <div className="profile-avatar">
                            { 
                                this.props.currentUser.imageUrl ? (
                                    <img src={this.props.currentUser.imageUrl} alt={this.props.currentUser.name}/>
                                ) : (
                                    <div className="text-avatar">
                                        <span>{this.props.currentUser.name && this.props.currentUser.name[0]}</span>
                                    </div>
                                )
                            }
                        </div>
                        <div className="profile-name">
                           <h2>{this.props.currentUser.name}</h2>
                           <p className="profile-email">{this.props.currentUser.email}</p>
                        </div>
                    </div>
                </div>
                <h2>Database manipulation</h2>
                <div className='DatabasemanuplationClass'>
                    
                    <div className="add User">
                        <form className="form "onSubmit={this.handleSubmit}>
                            <h3>Adding user to database:</h3>
                            <label>
                            Name:
                            <input type="text" onChange={this.handleChange} />
                            </label>
                            <label>
                            Email:
                            <input type="email" onChange={this.handleChange} />
                            </label>
                            <label>
                            Pass:
                            <input type="pasword" onChange={this.handleChange} />
                            </label>
                            <input className ="Submit"type="submit" value="Submit" />
                        </form>
                    </div>
                    <div className="rem User">
                        <form className="form "onSubmit={this.handleSubmit}>
                            <h3>Remove user to database:</h3>
                            <label>
                            Name:
                            <input type="text" onChange={this.handleChange} />
                            </label>
                            <label>
                            Email:
                            <input type="email" onChange={this.handleChange} />
                            </label>
                            <label>
                            Pass:
                            <input type="pasword" onChange={this.handleChange} />
                            </label>
                            <input className ="Remove" type="Submit" value="Remove" />
                        </form>
                    </div>
                </div>
                
            </div>
        );
    }
}

export default Profile