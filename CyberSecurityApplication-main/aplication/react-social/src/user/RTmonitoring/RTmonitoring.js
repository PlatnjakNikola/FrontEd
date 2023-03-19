import React, {Component} from "react";
import './RTmonitoring.css';
import { useState, useEffect} from 'react';

class RTmonitoring extends Component{
    constructor(props) {
        super(props);
        console.log(props);
    }
    render(){
        return (
            <div className="RTmonitoring-container">
                <div className="RTmonitoring-title">
                    <p className="RTmonitoringDescription">
                        Threat intelligence database is a collection of information about known or potential cybersecurity threats, 
                        including malware, vulnerabilities, and indicators of compromise (IOCs). 
                        This database is maintained by cybersecurity companies, government agencies, 
                        and other organizations that specialize in gathering, analyzing, and disseminating threat intelligence.

                        The purpose of a threat intelligence database is to provide organizations with up-to-date 
                        information about potential security risks, 
                        allowing them to proactively identify and mitigate cyber threats before they can cause damage. 
                        Threat intelligence databases can include a wide range of data sources, such as:
                        </p>
                        <ul className="RtList">
                            <li>Malware samples and signatures</li>
                            <li>Vulnerability information, including Common Vulnerabilities and Exposures (CVE) data</li>
                            <li>IP addresses, domains, and other IOCs associated with known cyber threats</li>
                            <li>Indicators of malicious activity, such as command and control servers and phishing domains</li>
                            <li>Analysis of cybercriminal tactics, techniques, and procedures (TTPs)</li>
                        </ul>
                    <p className="RTmonitoringDescription">
                        Organizations can use threat intelligence databases to enhance their cybersecurity defenses by integrating them with security tools and processes. 
                        In summary, threat intelligence databases play a critical role in modern cybersecurity by providing organizations with valuable information about potential threats 
                        and enabling them to take proactive measures to protect their systems and data.
                    </p>
                </div>
            </div>
        );
    }
}

export default RTmonitoring;