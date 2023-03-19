import React, {Component} from "react";
import './ThreatIntelligence.css';
import { useState, useEffect} from 'react';

class ThreatIntelligence extends Component{
    constructor(props) {
        super(props);
        console.log(props);
    }
    render(){
        return (
            <div className="threatIntelligence-container">
                <div className="threatIntelligence-title">
                    <h2>
                        Threat Intelligence
                    </h2>
                    <p className="threatDescription">
                        Threat intelligence database is a collection of information about known or potential cybersecurity threats, 
                        The purpose of a threat intelligence database is to provide organizations with up-to-date 
                        information about potential security risks, 
                        allowing them to proactively identify and mitigate cyber threats before they can cause damage. 
                        Threat intelligence databases can include a wide range of data sources, such as:
                        </p>
                        <ul className="threatList">
                            <li>Malware samples and signatures</li>
                            <li>Vulnerability information, including Common Vulnerabilities and Exposures (CVE) data</li>
                            <li>IP addresses, domains, and other IOCs associated with known cyber threats</li>
                            <li>Indicators of malicious activity, such as command and control servers and phishing domains</li>
                            <li>Analysis of cybercriminal tactics, techniques, and procedures (TTPs)</li>
                        </ul>
                </div>
            </div>
        );
    }
}

export default ThreatIntelligence;