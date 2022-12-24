import React, {useEffect, useMemo, useRef, useState} from "react";
import {Pagination} from "@mui/material";
import JobsService from "../services/JobsService";
import {useTable} from "react-table";
import Container from 'react-bootstrap/Container';
import Row from 'react-bootstrap/Row';
import 'bootstrap/dist/css/bootstrap.min.css';
import {Col} from "react-bootstrap";

const JobList = (props) => {
    const [items, setJobs] = useState([]);
    const [searchTitle, setSearchTitle] = useState("");
    const jobsRef = useRef();
    const [page, setPage] = useState(1);
    const [count, setCount] = useState(0);
    const [pageSize, setPageSize] = useState(5);

    const pageSizes = [5, 10, 20];

    jobsRef.current = items;

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
        retrieveJobs();
    }, [page, pageSize]);

    const retrieveJobs = () => {
        const params = getRequestParams(searchTitle, page, pageSize);
        JobsService.getJobs(params)
            .then((resp) => {
                const {items, totalPages} = resp.data;
                setJobs(items);
                setCount(totalPages);

                console.log(resp.data);
            })
            .catch((e) => {
                console.error(e);
            });
    };

    const findByTitle = () => {
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
                Header: "Published",
                accessor: "publishedAt"
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
                Header: "Actions",
                accessor: "actions",
                Cell: (props) => {
                    const rowIdx = props.row.id;
                    return (
                        <div>
                          <span onClick={() => console.log(rowIdx)}>
                              <i className="far fa-edit action mr-2"></i>
                          </span>

                            <span onClick={() => console.log(rowIdx)}>
                              <i className="far fa-trash action"></i>
                          </span>
                        </div>
                    );
                }
            }
        ],
        []
    );

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

export default JobList;