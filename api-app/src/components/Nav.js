import React from "react";
import {useKeycloak} from "@react-keycloak/web";
import {Link} from "react-router-dom";
import {BsSpeedometer2, BsNewspaper, BsCalculator} from "react-icons/bs";
import {GiGearHammer} from "react-icons/gi";
import {FaUserAlt,FaUserAltSlash} from "react-icons/fa";

const Nav = () =>{
    const {keycloak, initialized } = useKeycloak();

    return (
        <nav className="navbar navbar-expand navbar-dark bg-dark">
            <a href="/dashboard" className="navbar-brand">&nbsp;&nbsp;Cloud Computing</a>
            <div className="navbar-nav mr-auto">
                <li className="nav-item">
                    <Link to={"/dashboard"} className="nav-link">
                        <BsSpeedometer2 className="bi d-block mx-auto mb-1" fontSize="2em"/>
                        Dashboard
                    </Link>
                </li>
                <li className="nav-item">
                    <Link to={"/news"} className="nav-link">
                        <BsNewspaper className="bi d-block mx-auto mb-1" fontSize="2em"/>
                        News
                    </Link>
                </li>
                <li className="nav-item">
                    <Link to={"/jobs"} className="nav-link">
                        <GiGearHammer className="bi d-block mx-auto mb-1" fontSize="2em"/>
                        Jobs
                    </Link>
                </li>
                <li className="nav-item">
                    <Link to={"/statistics"} className="nav-link">
                        <BsCalculator className="bi d-block mx-auto mb-1" fontSize="2em"/>
                        Statistics
                    </Link>
                </li>

                <div className="p-3 text-bg-dark">
                    <div className="d-flex flex-wrap align-items-xxl-end justify-content-center justify-content-lg-start">
                        <div className="text-end">
                        {!keycloak.authenticated && (
                            <button
                                type="button"
                                className="btn btn-outline-light me-2"
                                onClick={() => keycloak.login()}
                            >
                                <FaUserAlt/>
                                Login
                            </button>
                        )}

                        {!!keycloak.authenticated && (
                            <button
                                type="button"
                                className="btn btn-outline-light me-2"
                                onClick={() => keycloak.logout()}
                            >
                                <FaUserAltSlash/>
                                Logout ({keycloak.tokenParsed.preferred_username})
                            </button>
                        )}
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Nav;