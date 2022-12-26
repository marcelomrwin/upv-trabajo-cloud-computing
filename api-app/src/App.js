import React from "react";
import {Link, Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";
import './App.css';
import {Container} from "react-bootstrap";
import Row from 'react-bootstrap/Row';

import NewsList from "./components/NewsList";
import JobList from "./components/JobList";
import keycloak from "./Keycloak";
import Dashboard from "./components/Dashboard";
import PrivateRoute from "./helpers/PrivateRoute";
import {ReactKeycloakProvider, useKeycloak} from "@react-keycloak/web";
import Nav from "./components/Nav";

function App() {

    return (
        <ReactKeycloakProvider authClient={keycloak}>
            <div className="App">
                <Container>
                    <Row>
                        <Nav/>
                        <div className="container mt-3">

                            <Routes>
                                <Route path='/' element={<Dashboard/>}/>
                                <Route path='/dashboard' element={<Dashboard/>}/>
                                <Route path='/news' element={
                                    <PrivateRoute>
                                        <NewsList/>
                                    </PrivateRoute>
                                }/>
                                <Route path='/jobs' element={
                                    <PrivateRoute>
                                        <JobList/>
                                    </PrivateRoute>
                                }/>
                            </Routes>

                        </div>
                    </Row>
                </Container>
            </div>
        </ReactKeycloakProvider>
    );
}

export default App;
