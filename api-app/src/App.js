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
import AlertPopup from "./components/alert/AlertPopup";

function App() {

    return (

        <div className="App">
            <Container>
                <Row>
                    <nav className="navbar navbar-expand navbar-dark bg-dark">
                        <a href="/news" className="navbar-brand">&nbsp;&nbsp;Cloud Computing</a>
                        <div className="navbar-nav mr-auto">
                            <li className="nav-item">
                                <Link to={"/dashboard"} className="nav-link">
                                    Dashboard
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/news"} className="nav-link">
                                    News
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/jobs"} className="nav-link">
                                    Jobs
                                </Link>
                            </li>
                            <li className="nav-item">
                                <Link to={"/statistics"} className="nav-link">
                                    Statistics
                                </Link>
                            </li>
                        </div>
                    </nav>

                    <div className="container mt-3">

                        <Routes>
                            <Route path='/' element={<NewsList/>}/>
                            <Route path='/news' element={<NewsList/>}/>
                            <Route path='/jobs' element={<JobList/>}/>
                        </Routes>

                    </div>
                </Row>
            </Container>
        </div>
    );
}

export default App;
