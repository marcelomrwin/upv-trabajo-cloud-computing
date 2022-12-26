import React from "react";
import {useKeycloak} from "@react-keycloak/web";
import {Link} from "react-router-dom";

const Nav = () =>{
    const {keycloak, initialized } = useKeycloak();

    return (
        <nav className="navbar navbar-expand navbar-dark bg-dark">
            <a href="/dashboard" className="navbar-brand">&nbsp;&nbsp;Cloud Computing</a>
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
                <div className="hidden xl:flex items-center space-x-5">
                    <div className="hover:text-gray-200">
                        {!keycloak.authenticated && (
                            <button
                                type="button"
                                className="text-blue-800"
                                onClick={() => keycloak.login()}
                            >
                                Login
                            </button>
                        )}

                        {!!keycloak.authenticated && (
                            <button
                                type="button"
                                className="text-blue-800"
                                onClick={() => keycloak.logout()}
                            >
                                Logout ({keycloak.tokenParsed.preferred_username})
                            </button>
                        )}
                    </div>
                </div>
            </div>
        </nav>
    );
};

export default Nav;