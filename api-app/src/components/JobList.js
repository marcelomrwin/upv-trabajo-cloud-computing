import React, {useEffect, useMemo, useRef, useState} from "react";
import {Pagination} from "@mui/material";
import JobsService from "../services/JobsService";
import {useTable} from "react-table";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Col} from "react-bootstrap";
import useAlert from "./alert/useAlert";
import AlertPopup from "./alert/AlertPopup";
import keycloak from "../Keycloak";
import AuthorizedElement from "../security/AuthorizedElement";
import {GiGearHammer} from "react-icons/gi";
import {generateClientId} from "../helpers/generateId";
const JobList = (props) => {
    const [items, setJobs] = useState([]);
    const [searchTitle, setSearchTitle] = useState("");
    const [searchId, setSearchId] = useState("");
    const jobsRef = useRef();
    const [page, setPage] = useState(1);
    const [count, setCount] = useState(0);
    const [pageSize, setPageSize] = useState(10);

    const pageSizes = [5, 10, 20];

    jobsRef.current = items;

    const onChangeSearchTitle = (e) => {
        const searchTitle = e.target.value;
        setSearchTitle(searchTitle);
    };

    const onChangeSearchId = (e) => {
        const searchId = e.target.value;
        setSearchId(searchId);
    }

    const getRequestParams = (searchId, searchTitle, page, pageSize) => {
        let params = {};

        if (searchId) {
            params["id"] = searchId;
        }

        if (searchTitle) {
            params["title"] = searchTitle;
        }

        if (page) {
            params["page"] = page - 1;
        }

        if (pageSize) {
            params["size"] = pageSize;
        }

        return params;
    };

    useEffect(() => {
        retrieveJobs();
        wsconnect();
    }, [page, pageSize]);

    const retrieveJobs = () => {
        const params = getRequestParams(searchId, searchTitle, page, pageSize);

        if (keycloak.hasRealmRole('ADMIN') || keycloak.hasResourceRole('ADMIN')) {
            JobsService.getJobs(params)
                .then(success)
                .catch(fail);
        } else {
            JobsService.getJobsSubmitted(params)
                .then(success)
                .catch(fail);
        }
    };

    function success(resp) {
        const {items, totalPages} = resp.data;
        setJobs(items);
        setCount(totalPages);
    }

    function fail(e) {
        console.error(e);
        setAlert('Error retrieving Jobs', e, 'error');
    }

    const search = () => {
        setPage(1);
        retrieveJobs();
    }

    const refreshList = () => {
        retrieveJobs();
    }

    const handlePageChange = (event, value) => {
        setPage(value);
    }

    const handlePageSizeChange = (event) => {
        setPageSize(event.target.value);
        setPage(1);
    }

    const {setAlert} = useAlert();

    /* websocket begin */

    var connected = false;
    var socket;

    const wsconnect = () => {
        if (!connected) {
            var clientId = generateClientId(6);

            if (keycloak && keycloak.token) {
                clientId = keycloak.tokenParsed.preferred_username;
            }

            // eslint-disable-next-line no-restricted-globals
            var wsUrl = "ws://" + "localhost:8083" + "/ws/jobs/" + clientId;
            console.log('Connect to ' + wsUrl);
            socket = new WebSocket(wsUrl);

            socket.onopen = (event) => {
                connected = true;
                console.log("Connected to the web socket with clientId [" + clientId + "]");
            };
            socket.onmessage = (m) => {
                if (m.data == 'PING') {
                    console.log('Receiving PING from the server, respond with PONG');
                    socket.send('PONG');
                } else {
                    const json = JSON.parse(m.data);
                    setAlert('Job id ' + json.id + ' update received', 'Job updated with status ' + json.status, 'info');
                    refreshList();
                }
            };
            socket.onerror = (ev) => {
                console.error(ev.data);
                setAlert('Error submitting job', ev.data, 'error');

            }
        }
    }

    /* websocket end */

    const columns = useMemo(
        () => [
            {
                Header: "",
                accessor: "thumbnail",
                Cell: (props) => {
                    return <img src={'data:image/jpeg;base64,' + props.value}/>
                }
            },
            {
                Header: "Id",
                accessor: "id"
            },
            {
                Header: "Title",
                accessor: "title"
            },
            {
                Header: "Status",
                accessor: "status"
            },
            {
                Header: "Requested",
                accessor: "requestedAt"
            },
            {
                Header: "Processed",
                accessor: "processedAt"
            },
            {
                Header: "Elapsed Time",
                accessor: "elapsedTime"
            },
            {
                Header: "Download",
                accessor: "actions",
                Cell: (props) => {
                    const rowIdx = props.row.id;
                    return (
                        <div>
                            <AuthorizedElement roles={['USER', 'ADMIN']}>
                                {JobResultButton(props)}
                            </AuthorizedElement>
                        </div>
                    );
                }
            }
        ],
        []
    );

    function JobResultButton(props) {
        const rowIdx = props.row.id;
        const jobStatus = jobsRef.current[rowIdx].status;

        if (jobStatus === 'FINISHED') {
            return <span role={"button"} onClick={() => getJobResult(rowIdx)}
                         data-placement="bottom" title='Get Job Result'
                         className="btn btn-primary btn-lg">
                                  <i className="far fa-solid fa-download action mr-2 pe-0"></i>
                              </span>
        } else {
            return <span role={"button"}
                         data-placement="bottom" title='Get Job Result'
                         className="btn btn-primary btn-lg disabled">
                                  <i className="far fa-solid fa-download action mr-2 pe-0"></i>
                              </span>
        }
    }

    const getJobResult = (rowIndex) => {
        const id = jobsRef.current[rowIndex].id;

        JobsService.downloadJobResult(id).then(
            (response) => {
                var downloadWindow = window.open('url', id + '.html', "_blank,toolbar=yes,location=yes,directories=yes,height=600,width=800,scrollbars=yes,resizable=yes,status=yes,menubar=yes");
                downloadWindow.document.write(response.data);
                downloadWindow.document.close();
            }
        ).catch((e) => {
            console.error(e);
            setAlert('Error while downloading Job Result: ' + e.message, e.response.data, 'error');
        });

    }

    const {
        getTableProps,
        getTableBodyProps,
        headerGroups,
        rows,
        prepareRow,
    } = useTable({
            columns,
            data: items,
        }
    );

    return (
        <Container>
            <AlertPopup/>
            <div className="bg-dark w-100 pe-auto text-white text-center"><GiGearHammer className="bi  mx-auto mb-1"
                                                                                        fontSize="1em"/> Submitted Jobs
            </div>
            <div className="p-1"></div>
            <div className="list row">
                <Row className="justify-content-md-center">
                    <Col md="2">
                        <input type="number"
                               className="form-control text-center"
                               placeholder="Search by Job ID"
                               value={searchId}
                               onChange={onChangeSearchId}
                        />
                    </Col>
                    <Col>
                        <input
                            type="text"
                            className="form-control"
                            placeholder="Search by title"
                            value={searchTitle}
                            onChange={onChangeSearchTitle}
                        />
                    </Col>
                    <Col md="auto">
                        <div className="input-group-append">
                            <button
                                className="btn btn-outline-secondary"
                                type="button"
                                onClick={search}>
                                Search
                            </button>
                        </div>
                    </Col>
                </Row>
                <Row>
                    <div className="col-md-12 list">
                        <div className="mt-3">
                            {"Items per Page: "}
                            <select onChange={handlePageSizeChange} value={pageSize}>
                                {pageSizes.map((size) => (
                                    <option key={size} value={size}>
                                        {size}
                                    </option>
                                ))}
                            </select>

                            <Pagination
                                className="my-3"
                                count={count}
                                page={page}
                                siblingCount={1}
                                boundaryCount={1}
                                variant="outlined"
                                shape="rounded"
                                onChange={handlePageChange}
                            />
                        </div>

                        <table
                            className="table table-striped table-bordered"
                            {...getTableProps()}
                        >
                            <thead>
                            {headerGroups.map((headerGroup) => (
                                <tr {...headerGroup.getHeaderGroupProps()}>
                                    {headerGroup.headers.map((column) => (
                                        <th {...column.getHeaderProps()}>
                                            {column.render("Header")}
                                        </th>
                                    ))}
                                </tr>
                            ))}
                            </thead>
                            <tbody {...getTableBodyProps()}>
                            {
                                rows.map((row, i) => {
                                    prepareRow(row);
                                    return (
                                        <tr {...row.getRowProps()}>
                                            {row.cells.map((cell) => {
                                                return (
                                                    <td {...cell.getCellProps()}>{cell.render("Cell")}</td>
                                                );
                                            })}
                                        </tr>
                                    );
                                })
                            }
                            </tbody>
                        </table>

                    </div>
                </Row>
            </div>
        </Container>
    );
};

export default JobList;