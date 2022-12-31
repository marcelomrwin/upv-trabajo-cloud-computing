import React, {useEffect, useState} from "react";
import AlertPopup from "./alert/AlertPopup";
import Container from "react-bootstrap/Container";
import {generateClientId} from "../helpers/generateId";
import useAlert from "./alert/useAlert";
import {Card, Col, Row} from "react-bootstrap";
import {BsCalculator} from "react-icons/bs";
import UserService from "../security/keycloak/UserService";

const JobStatistics = () => {

    const API_URL_BASE = window._env_.API_URL_BASE;

    const [stats, setStats] = useState({
        currentStatistics: {
            reportDate: new Date(),
            totalJobRequest: 0,
            totalJobResponse: 0,
            averageRateArrival: 0,
            averageResponseTime: 0,
            averageResponseTimeFormatted: '00:00:00.000'
        },
        timeFrameStatistics:{
            reportDate: new Date(),
            totalJobRequest: 0,
            totalJobResponse: 0,
            averageRateArrival: 0,
            averageResponseTime: 0,
            averageResponseTimeFormatted: '00:00:00.000'
        }
    });

    useEffect(() => {
        wsconnect();
    });

    const refresh = (json) => {
        setStats(JSON.parse(json));
        // console.log(stats.currentStatistics);
    }

    const {setAlert} = useAlert();

    /* websocket begin */

    var connected = false;
    var socket;

    const wsconnect = () => {
        if (!connected) {
            var clientId = generateClientId(6);

            if (UserService.isLoggedIn()) {
                clientId = UserService.getUsername();
            }

            // eslint-disable-next-line no-restricted-globals
            var wsUrl = "ws://" + API_URL_BASE + "/ws/statistics/" + clientId;
            console.log('Connect to ' + wsUrl);
            socket = new WebSocket(wsUrl);

            socket.onopen = (event) => {
                connected = true;
                console.log("Connected to the web socket with clientId [" + clientId + "]");
            };
            socket.onmessage = (m) => {
                if (m.data === 'PING') {
                    console.log('Receiving PING from the server, respond with PONG');
                    socket.send('PONG');
                } else {
                    // const json = JSON.parse(m.data);
                    // setAlert('Job id ' + json.id + ' update received', 'Job updated with status ' + json.status, 'info');
                    refresh(m.data);
                }
            };
            socket.onerror = (ev) => {
                console.error(ev.data);
                setAlert('Error submitting job', ev.data, 'error');

            }
        }
    }

    /* websocket end */

    return (
        <Container>
            <AlertPopup/>

            <div className="h2 mb-1 pt-1 font-weight-bold bg-dark text-white text-secondary">
                <BsCalculator className="bi mx-auto mb-1" fontSize="1em"/>
                Statistics
            </div>


            <Row xs={1} md={2} className="g-4">
                <Col>
                    <Card>
                        <Card.Header>General Statistics</Card.Header>
                        <Card.Body>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Report Date
                                </Col>
                                <Col sm={8}>
                                    <p>{new Date(stats.currentStatistics.reportDate).toLocaleString()}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Total Job requests
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.currentStatistics.totalJobRequest}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Total Jobs Completed
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.currentStatistics.totalJobResponse}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Average rate of arrival (per minute)
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.currentStatistics.averageRateArrival}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Average Worker Response Time
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.currentStatistics.averageResponseTimeFormatted}</p>
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>
                </Col>

                {/*LAST FIVE MINUTES*/}

                <Col>
                    <Card>
                        <Card.Header>Statistics of the last 5 minutes</Card.Header>
                        <Card.Body>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Report Date
                                </Col>
                                <Col sm={8}>
                                    <p>{new Date(stats.timeFrameStatistics.reportDate).toLocaleString()}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Total Job requests
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.timeFrameStatistics.totalJobRequest}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Total Jobs Completed
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.timeFrameStatistics.totalJobResponse}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Average rate of arrival (per minute)
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.timeFrameStatistics.averageRateArrival}</p>
                                </Col>
                            </Row>
                            <Row className="mb-2" className="text-start">
                                <Col sm={4} className="fw-bold">
                                    Average Worker Response Time
                                </Col>
                                <Col sm={8}>
                                    <p>{stats.timeFrameStatistics.averageResponseTimeFormatted}</p>
                                </Col>
                            </Row>
                        </Card.Body>
                    </Card>
                </Col>

            </Row>

        </Container>
    );
};

export default JobStatistics;