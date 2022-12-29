import React from "react";
import hld from '../images/HLD.png';
import {Accordion} from "react-bootstrap";
import {ImWarning} from "react-icons/im";
import {AiFillThunderbolt} from "react-icons/ai"
import {BsFillLockFill, BsBricks, BsFillHandThumbsDownFill, BsFillHandThumbsUpFill} from "react-icons/bs"
import {FaBook, FaTools} from "react-icons/fa"
import {TbView360, TbStairsUp} from "react-icons/tb"
import {GrServices} from "react-icons/gr"

const Dashboard = () => {
    return (
        <div>
            <Accordion defaultActiveKey={['0']} flush alwaysOpen>
                <Accordion.Item eventKey="0">
                    <Accordion.Header>Cloud Computing Homework - UPV</Accordion.Header>
                    <Accordion.Body className="text-start">
                        <p className="fw-bold">HomeWork prepared for the discipline of cloud computing implemented
                            by:</p>
                        <ul>
                            <li>Marcelo Daniel das Silva Sales</li>
                            <li>Giovanni Sabogal Cespedes</li>
                        </ul>
                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="1">
                    <Accordion.Header className="bg-secondary"><FaBook/>&nbsp;Tutorial - Get Started</Accordion.Header>
                    <Accordion.Body className="text-start"></Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="2">
                    <Accordion.Header><TbView360/>&nbsp;High Level Definition (HLD)</Accordion.Header>
                    <Accordion.Body>
                        <img src={hld} alt="HLD" width="100%" height="100%"/>
                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="3">
                    <Accordion.Header><GrServices/>&nbsp; Services Components list</Accordion.Header>
                    <Accordion.Body className="text-start">
                        <Accordion defaultActiveKey={['0']} flush alwaysOpen>
                            <Accordion.Item eventKey="0">
                                <Accordion.Header><h4>GUI</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>Web GUI</h4>
                                    <h5>Performs communication through REST and Websocket requests with the backend
                                        API</h5>
                                    <hr/>
                                    <li>Developed using <a href="https://reactjs.org/">ReactJS</a> 18 and <a
                                        href="https://getbootstrap.com/">Bootstrap</a> 5.2
                                    </li>
                                    <li>Integrates with keycloak to get JWT token</li>
                                    <li>Uses <a href="https://axios-http.com/">Axios</a> to communicate with the Backend
                                    </li>
                                    <li>The Dashboard and Statistics pages have public access</li>
                                    <li>The News and Jobs pages are only allowed for authenticated users</li>
                                    <li><b>News Page -> </b>Displays the news available for publication
                                        <ul>
                                            <li><ImWarning className="text-warning"/> Only users with
                                                the <i>USER</i> or <i>ADMIN</i> roles can submit new work
                                            </li>
                                            <li><AiFillThunderbolt color="orange"/> It has a websocket channel to
                                                receive news in real time coming from the server
                                            </li>
                                        </ul>
                                    </li>
                                    <li><b>Jobs Page -> </b>Displays requested jobs. In the scenario in question, the
                                        news for publication.
                                        <ul>
                                            <li><ImWarning className="text-warning"/> Only users with
                                                the <i>USER</i> or <i>ADMIN</i> can access the requested jobs and
                                                download the published news. The end result of the work
                                            </li>
                                            <li><AiFillThunderbolt color="orange"/> It has a websocket channel to
                                                receive jobs updates in real time coming from the server
                                            </li>
                                        </ul>
                                    </li>
                                    <li>
                                        <b>Statistics Page -></b> displays information accounted for through the
                                        Observer component
                                    </li>
                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="1">
                                <Accordion.Header className="bg-secondary bg-gradient"><h4>API</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>REST API</h4>
                                    <h5>Responsible for exposing internal services using the REST model</h5>
                                    <hr/>
                                    <li>Developed with <a href="https://quarkus.io/">Quarkus</a> 2.14</li>
                                    <li>Publish the API documentation in the OpenAPI 3 standard. Published in <a
                                        href="/openapi">/openapi</a></li>
                                    <li>It has protected services that require authentication and authorization
                                        token in JWT format
                                    </li>
                                    <li>Integrated with Keycloak</li>
                                    <li>
                                        Provides websocket connection channel for web GUI
                                        <ul>
                                            <li>Periodically sends PING to clients<br/>
                                                &ensp;<BsFillHandThumbsUpFill className="text-success"/>&nbsp; If the
                                                client responds with a <i>PONG</i>, keep the session active<br/>
                                                &ensp;<BsFillHandThumbsDownFill className="text-danger"/>&nbsp;If the
                                                client does not respond, remove the session from the server
                                            </li>
                                        </ul>
                                    </li>
                                    <li>Has integration with Kafka for:
                                        <ul>
                                            <li>To publish new job orders. In the scenario in question, publish
                                                news
                                            </li>
                                            <li>To receive notification about news suitable for publication</li>
                                            <li>To receive notifications about changes to the status of submitted jobs
                                            </li>
                                            <li>To receive notifications regarding metrics generated by
                                                the <b>Observer</b> component
                                            </li>
                                        </ul>
                                    </li>
                                    <li>
                                        Publish the following services
                                        <div className="container-fluid">
                                            <div className="row bg-secondary bg-gradient text-white">
                                                <div className="col text-center">Security</div>
                                                <div className="col text-center">Method</div>
                                                <div className="col-2">Operation</div>
                                                <div className="col-2">Path</div>
                                                <div className="col-6">Description</div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillLockFill title="USER or ADMIN"/>
                                                </div>
                                                <div className="col text-center text-success fw-bold">GET</div>
                                                <div className="col-2">getHotNews</div>
                                                <div className="col-2">/api/hotnews</div>
                                                <div className="col-6">Returns all news suitable for publication without
                                                    pagination. Allows querying by title
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillLockFill title="USER or ADMIN"/>
                                                </div>
                                                <div className="col text-center text-success fw-bold">GET</div>
                                                <div className="col-2">getHotNewsPaged</div>
                                                <div className="col-2">/api/hotnews/paged</div>
                                                <div className="col-6">Returns all news suitable for publication with
                                                    pagination. Allows querying by title
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillLockFill title="USER or ADMIN"/>
                                                </div>
                                                <div className="col text-center text-warning fw-bold">POST</div>
                                                <div className="col-2">submitJob</div>
                                                <div className="col-2">/api/jobs</div>
                                                <div className="col-6">Submit the news for publication. Starts a job
                                                    request
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillLockFill title="USER or ADMIN"/>
                                                </div>
                                                <div className="col text-center text-success fw-bold">GET</div>
                                                <div className="col-2">getJobsSubmittedBy</div>
                                                <div className="col-2">/api/jobs/myjobs</div>
                                                <div className="col-6">Returns jobs submitted by the authenticated user.
                                                    Check job status. Allows querying by title or id
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillLockFill title="ADMIN"/></div>
                                                <div className="col text-center text-success fw-bold">GET</div>
                                                <div className="col-2">getJobsInfo</div>
                                                <div className="col-2">/api/jobs/paged</div>
                                                <div className="col-6">Return all jobs from repository using paging.
                                                    Allows querying by title or id
                                                </div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillLockFill title="USER or ADMIN"/>
                                                </div>
                                                <div className="col text-center text-success fw-bold">GET</div>
                                                <div className="col-2">downloadJobResult</div>
                                                <div className="col-2">/api/jobs/result</div>
                                                <div className="col-6">Allows the download of the completed work. The
                                                    published news
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="2">
                                <Accordion.Header><h4>EXECUTOR</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>Work Executor</h4>
                                    <h5>Responsible for the execution of the work. Generates the publication of a
                                        news</h5>
                                    <hr/>
                                    <li>Developed with <a href="https://quarkus.io/">Quarkus</a> 2.14</li>
                                    <li>Has integration with Kafka for:
                                        <ul>
                                            <li>Receive notifications of recent news and save in the local base for
                                                future processing
                                            </li>
                                            <li>Receive requests for publication of news. Final work to be done</li>
                                            <li>Notify interested parties about the termination of work</li>
                                        </ul>
                                    </li>
                                    <li>Uses the NoSQL <a href="https://www.mongodb.com/">MongoDB</a> database</li>
                                    <li>Uses <a href="https://redis.io/">Redis</a> as distributed cache integrated
                                        with <b>Observer</b> and <b>Injector</b> components
                                    </li>
                                    <li>Integrates with <a href="https://min.io/"><b>MIN</b>IO</a> to obtain files</li>
                                    <li>Performs integration with GitHub to obtain a python script responsible for
                                        executing the work
                                        <ul>
                                            <li>Validates the necessary tag in the configuration file, if the repository
                                                has already been cloned, it does not perform the action again.
                                            </li>
                                        </ul>
                                    </li>
                                    <li>Prepare the entire environment with the necessary variables to delegate
                                        execution to the python script
                                    </li>
                                    <li>Makes use of the <i>imagemagick</i> feature to manipulate images during news
                                        publication
                                    </li>
                                    <li>Upon receiving the news publication request <i>(Job Request)</i>, the Executor
                                        obtains the metadata as follows:
                                        <ol>
                                            <li>First do a search in the Redis cache</li>
                                            <li>Then search its local base</li>
                                            <li>Finally, if it does not find it, it makes a remote call to
                                                the <b>Injector</b> component requesting the data
                                            </li>
                                        </ol>
                                    </li>
                                    <li>Makes use of <a
                                        href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-retries">retries</a>, <a
                                        href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-circuit-breaker">circuit
                                        breaker</a>, <a
                                        href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-fallbacks">fallback</a> and <a
                                        href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-timeouts">timeouts</a> techniques
                                    </li>
                                    <li>Records when work starts and when it ends</li>
                                    <li>It does not expose API or any other form of communication, it is only triggered
                                        by posting messages in Kafka topics
                                    </li>
                                    <li>It has optimization for multithreading. Makes heavy use of reactive,
                                        non-blocking programming
                                    </li>
                                    <li>Offers the possibility to simulate a <i>deliberate</i> delay in message
                                        processing so that it is possible to observe job status changes
                                    </li>
                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="3">
                                <Accordion.Header><h4>INJECTOR</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>Injector</h4>
                                    <h5>SUBTITLE</h5>
                                    <hr/>
                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="4">
                                <Accordion.Header><h4>OBSERVER</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>WORK OBSERVER</h4>
                                    <h5>SUBTITLE</h5>
                                    <hr/>
                                </Accordion.Body>
                            </Accordion.Item>
                        </Accordion>
                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="4">
                    <Accordion.Header><BsBricks/>&nbsp; Infrastructure Components list</Accordion.Header>
                    <Accordion.Body className="text-start"></Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="5">
                    <Accordion.Header><FaTools/>&nbsp; Management Tools - Only for Development</Accordion.Header>
                    <Accordion.Body className="text-start"></Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="6">
                    <Accordion.Header><TbStairsUp/>&nbsp; Future Works</Accordion.Header>
                    <Accordion.Body className="text-start"></Accordion.Body>
                </Accordion.Item>
            </Accordion>


        </div>
    )
        ;
};

export default Dashboard;