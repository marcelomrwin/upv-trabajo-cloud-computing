import React, {useState} from "react";
import {Link} from "react-router-dom";
import {BsCalculator, BsNewspaper, BsSpeedometer2} from "react-icons/bs";
import {GiGearHammer} from "react-icons/gi";
import {FaUserAlt, FaUserAltSlash} from "react-icons/fa";
import UserService from "../security/keycloak/UserService";

const Nav = () => {
    const kc = UserService;
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

                <div className="p-3 text-bg-dark text-end d-grid gap-3">
                    <div
                        className="d-flex flex-wrap align-items-xxl-end justify-content-center justify-content-lg-start">
                        <div className="text-end flex-wrap w-auto p-2">
                            {!kc.isLoggedIn() && (
                                <button
                                    type="button"
                                    className="btn btn-outline-light me-2"
                                    onClick={() => kc.doLogin()}
                                >
                                    <FaUserAlt/>
                                    Login
                                </button>
                            )}

                            {kc.isLoggedIn() && (
                                <div className="w-auto">
                                    Signed in as <b>{kc.getUsername()}</b> &nbsp;
                                    <button
                                        type="button"
                                        className="btn btn-outline-light"
                                        onClick={() => kc.doLogout()}
                                    ><FaUserAltSlash/>
                                        Logout
                                    </button>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Nav;