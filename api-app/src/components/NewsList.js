import React, {useEffect, useMemo, useRef, useState} from "react";
import {Pagination} from "@mui/material";
import NewsService from "../services/NewsService";
import {useTable} from "react-table";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Col} from "react-bootstrap";
import JobsService from "../services/JobsService";
import useAlert from "./alert/useAlert";
import AlertPopup from "./alert/AlertPopup";
import AuthorizedElement from "../security/AuthorizedElement";
import keycloak from "../Keycloak";
import {BsNewspaper} from "react-icons/bs";
import {generateClientId} from "../helpers/generateId";

const NewsList = (props) => {
    const [items, setNews] = useState([]);
    const [searchTitle, setSearchTitle] = useState("");
    const newsRef = useRef();

    const [page, setPage] = useState(1);
    const [count, setCount] = useState(0);
    const [pageSize, setPageSize] = useState(10);

    const pageSizes = [5, 10, 20];

    newsRef.current = items;

    const onChangeSearchTitle = (e) => {
        const searchTitle = e.target.value;
        setSearchTitle(searchTitle);
    };

    const getRequestParams = (searchTitle, page, pageSize) => {
        let params = {};

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
        retrieveNews();
        wsconnect();
    }, [page, pageSize]);

    const retrieveNews = () => {
        const params = getRequestParams(searchTitle, page, pageSize);
        NewsService.getNews(params)
            .then((resp) => {

                const {items, totalPages} = resp.data;
                setNews(items);
                setCount(totalPages);

            })
            .catch((e) => {
                console.error(e);
            });
    };

    const findByTitle = () => {
        setPage(1);
        retrieveNews();
    }

    const refreshList = () => {
        retrieveNews();
    }

    const handlePageChange = (event, value) => {
        setPage(value);
    }

    const handlePageSizeChange = (event) => {
        setPageSize(event.target.value);
        setPage(1);
    }

    const {setAlert} = useAlert();

    const submitJob = (rowIndex) => {
        const id = newsRef.current[rowIndex].id;
        JobsService.submitJob(id).then(
            (response) => {
                setPage(page);
                refreshList();
                setAlert('Job ' + id + ' Submitted', 'Work submitted. To follow up navigate to the Jobs tab', 'success');
            }
        ).catch((e) => {
            console.error(e.response)
            setAlert('Error in submit job: ' + e.message, e.response.data, 'error');
        });
    }

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
                Header: "Title",
                accessor: "title"
            },
            {
                Header: "Published",
                accessor: "publishedAt"
            },
            {
                Header: "Publish",
                accessor: "actions",
                Cell: (props) => {
                    const rowIdx = props.row.id;
                    return (
                        <div>
                            <AuthorizedElement roles={['USER','ADMIN']}>
                              <span role={"button"} onClick={() => submitJob(rowIdx)}
                                    data-placement="bottom" title='Submit Job'
                                    className="btn btn-primary btn-lg">
                                  <i className="far fa-solid fa-hammer action mr-2 pe-0"></i>
                              </span>
                            </AuthorizedElement>
                        </div>
                    );
                }
            }
        ],
        []
    );

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
            var wsUrl = "ws://" + "localhost:8083" + "/ws/hotnews/" + clientId;
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
                    console.log("Got hotnews: " + m.data);
                    setAlert('Hot News arrived', 'Refreshing table list with News', 'info');
                    refreshList();
                }
            };
            socket.onerror = (ev) => {
                console.error(ev.data);
            }
        }
    }

    /* websocket end */

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
            <div className="bg-dark w-100 pe-auto text-white text-center"><BsNewspaper className="bi  mx-auto mb-1" fontSize="1em"/>  Recent News</div>
            <div className="p-1"></div>
            <div className="list row">
                <Row className="justify-content-md-center">
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
                                onClick={findByTitle}>
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

export default NewsList;