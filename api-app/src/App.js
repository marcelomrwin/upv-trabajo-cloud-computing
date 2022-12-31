import React from "react";
import {Route, Routes} from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "@fortawesome/fontawesome-free/css/all.css";
import "@fortawesome/fontawesome-free/js/all.js";
import './App.css';
import {Container} from "react-bootstrap";
import Row from 'react-bootstrap/Row';

import NewsList from "./components/NewsList";
import JobList from "./components/JobList";
import Dashboard from "./components/Dashboard";
import PrivateRoute from "./helpers/PrivateRoute";
import Nav from "./components/Nav";
import JobStatistics from "./components/JobStatistics";

function App() {

    return (

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
                            <Route path="/statistics" element={<JobStatistics/>}/>
                        </Routes>

                    </div>
                </Row>
            </Container>
        </div>

    );
}

export default App;
