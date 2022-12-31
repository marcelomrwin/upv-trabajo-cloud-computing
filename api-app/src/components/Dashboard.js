import React from "react";
import {ReactComponent as HLD} from '../images/HLD.svg';
import {ReactComponent as ExecutorFlow} from '../images/Executor-Flow.svg';
import {ReactComponent as APIFlow} from '../images/API-Integrations.svg';
import {ReactComponent as InjectorFlow} from '../images/Injector-Interactions.svg';
import {ReactComponent as ObserverFlow} from '../images/Observer-Interactions.svg';
import {ReactComponent as Infra} from '../images/Infrastructure.svg';
import {ReactComponent as HldFlow} from '../images/HLD-Interactions.svg';
import {Accordion, Card} from "react-bootstrap";
import {ImWarning} from "react-icons/im";
import {AiFillThunderbolt} from "react-icons/ai"
import {
    BsBricks,
    BsFillHandThumbsDownFill,
    BsFillHandThumbsUpFill,
    BsFillLockFill,
    BsFillUnlockFill,
    BsFillInfoCircleFill,
    BsSpeedometer2, BsNewspaper, BsCalculator
} from "react-icons/bs"
import {FaBook, FaTools, FaHammer} from "react-icons/fa"
import {TbStairsUp, TbView360} from "react-icons/tb"
import {GrServices} from "react-icons/gr"
import {Link} from "react-router-dom";
import {GiGearHammer} from "react-icons/gi";

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
                    <Accordion.Body className="text-start">
                        <h5>
                            <center>Project for the Cloud Computing discipline of the University Masters in Cloud
                                Computing and High Performance
                            </center>
                        </h5>
                        <p>A solution to publish standalone news files that can be downloaded and read offline. The
                            service collects news from websites and public portals that offer the news service through
                            the Rest API. Collects and stores its metadata and generates static HTML pages, including
                            images, to allow offline reading. A common problem is with images. The solution downloads
                            the image and, in order to keep the final file with a smaller size, it manipulates it using
                            the imagemagick utility.</p>
                        <p>The entire solution is based on the concept of distributed services and event-driven
                            architecture.</p>

                        <strong>Getting Started</strong>
                        <ol>
                            <li>First, log in to the application by clicking on the <b>login</b> button in the header of
                                the
                                page.<br/>
                                The application will point to the Keycloak identity manager.<br/>
                                <BsFillInfoCircleFill className="text-primary"/> If you received a specific
                                username and password, use it to login.<br/>If you haven't received a specific user,
                                there are three test
                                users:
                                <ul>
                                    <li><b>user1</b> - roles: USER</li>
                                    <li><b>user2</b> - roles: USER</li>
                                    <li><b>admin</b> - member of group: ADMIN</li>
                                </ul>
                                All user's password is <i>password</i>
                            </li>
                            <li>After login, navigate to the <Link to={"/news"}><BsNewspaper/> News</Link> page.</li>
                            <li>It is expected that there will be a list of news ready to be published.<br/>
                                <ImWarning className="text-warning"/><p className="text-warning bg-dark">If there are no
                                    news available for publication, it
                                    is possible that the Injector is not active, in this case, it is necessary to
                                    evaluate
                                    the reason why the injector is not generating news.</p>
                            </li>
                            <li>Choose a news item and click on the publish button &nbsp;
                                <i className="btn btn-primary fa-solid fa-hammer action mr-2 pe-auto"></i>
                                &nbsp; This action starts the work submission process to generate the final publication.<br/>
                                <ImWarning className="text-warning"/>&nbsp;Only users who are members of the ADMIN group
                                or who have the USER role can submit job requests.
                            </li>
                            <li>After the job requisition is submitted, navigate to the <Link
                                to={"/jobs"}><GiGearHammer/> Jobs</Link> page and track the progress of the job.
                            </li>
                            <li>When the job is finished, the server sends a notification to the browser enabling the
                                download button <i
                                    className="btn btn-primary fa-solid fa-download action mr-2 pe-auto"></i> to
                                visualize the result of the job. In the scenario in question, a new
                                page will open the news published with the static images.<br/>
                                <BsFillInfoCircleFill className="text-primary"/>&nbsp;You can use browser functionality
                                and choose "save page as..." to save the html generated as a result of processing.
                            </li>
                        </ol>
                        <p>The diagram below details the interactions of the components serving the flow described
                            above.</p>
                        <center>
                            <HldFlow width="100%" height="100%"/>
                        </center>
                        <ol className="list-group list-group-numbered">
                            <li className="list-group-item">
                                The injector retrieves news from external sources and then:<br/>
                                <ul>
                                    <li>Save to its local database</li>
                                    <li>Stores the image in MiniIO</li>
                                    <li>Store metadata in Redis</li>
                                    <li>Sends notification to Kafka that new news is available</li>
                                </ul>

                            </li>
                            <li className="list-group-item">Kafka notifies:<br/>
                                <ul>
                                    <li>API to store metadata in its local base and display to users as news available
                                        for publication
                                    </li>
                                    <li>Executor to save the metadata in its local database</li>
                                </ul>
                            </li>
                            <li className="list-group-item">User uses the GUI and authenticates in Keycloak</li>
                            <li className="list-group-item">User selects a news item and requests its publication.<br/>
                                <ul>
                                    <li>The authentication token goes along with the request</li>
                                    <li>API validates authentication token against keycloak</li>
                                </ul>
                            </li>
                            <li className="list-group-item">API stores job request in its local base before sending to
                                Kafka
                            </li>
                            <li className="list-group-item">API sends notification to Kafka requesting news
                                publication
                            </li>
                            <li className="list-group-item">Kafka receives the message and notifies:<br/>
                                <ul>
                                    <li>The executor to start the news publication process</li>
                                    <li>The observer to count metrics and store in Redis</li>
                                </ul>
                            </li>
                            <li className="list-group-item">The executor fetches the news metadata in the following
                                order:<br/>
                                <ol>
                                    <ul>In the Redis cache</ul>
                                    <ul>In its local database</ul>
                                    <ul>Performs a HTTP GET request against Injector</ul>
                                </ol>
                            </li>
                            <li className="list-group-item">It retrieves the original news image from MinIO and performs
                                the processing for publication.
                            </li>
                            <li className="list-group-item">Executor finalizes the publishing process and:
                                <ul>
                                    <li>Stores the publication result in the local database</li>
                                    <li>Notifies Kafka about the processing result</li>
                                </ul>
                            </li>
                            <li className="list-group-item">Kafka receives the message and notifies:
                                <ul>
                                    <li>The observer to add another finished publication</li>
                                    <li>The API regarding the result of the performed publish request.</li>
                                </ul>
                            </li>
                            <li className="list-group-item">API receives notification about job result
                                <ul>
                                    <li>Save the result in the local database</li>
                                    <li>Notifies the client about the result via Websockets</li>
                                </ul>
                            </li>
                        </ol>
                        <hr/>
                        <h4>Other application features</h4>
                        <strong>Notifications via websockets</strong>
                        <p>The News, Jobs and Statistics pages have the feature enabled and receive notifications from
                            the server. Be it notifications about recent news added to the portfolio or about the
                            processing result or simply to update the displayed metrics.</p>
                        <strong>Authentication and Authorization</strong>
                        <p>To view recent news, you must be authenticated and to carry out job requests, you must have
                            the USER role or be part of the ADMIN group. Work requests made by a user are only seen by
                            him or by the administrator.</p>

                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="2">
                    <Accordion.Header><TbView360/>&nbsp;High Level Definition (HLD)</Accordion.Header>
                    <Accordion.Body>
                        <HLD width="100%" height="100%"/>
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
                                    <br/>
                                    <p>The figure below summarizes the flow of integrations performed by the API</p>
                                    <center><APIFlow width="50%" height="50%"/></center>
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
                                    <li>The "main" program was written in python 3</li>
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
                                        href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-fallbacks">fallback</a>, <a
                                        href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-timeouts">timeouts</a> and <a
                                        href="https://microservices.io/patterns/data/transactional-outbox.html">=Transactional
                                        outbox pattern</a> techniques
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
                                    <br/>
                                    <p>The image below summarizes the Executor operation flow</p>
                                    <center><ExecutorFlow width="75%" height="75%"/></center>
                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="3">
                                <Accordion.Header><h4>INJECTOR</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>News Generator</h4>
                                    <h5>Responsible for generating metadata about news to be consumed by the
                                        Executor</h5>
                                    <hr/>
                                    <li>Developed with <a href="https://quarkus.io/">Quarkus</a> 2.14</li>
                                    <li>It has a customizable scheduling service to get new news.</li>
                                    <li>It uses fault tolerance techniques to get news on every run. With automatic news
                                        source rotation
                                    </li>
                                    <li>Retrieves news metadata and standardizes it in the format established by the
                                        application<br/>
                                        <Card className="w-auto ms-3">
                                            <Card.Header className="bg-primary text-white">Info</Card.Header>
                                            <Card.Body>
                                                <p>With the exception of the feed service provided by Quarkus, all other
                                                    APIs are REST services that require a subscription.</p>
                                                <p>It is necessary to subscribe to the mentioned services and provide
                                                    the API key so that the application can consume these services.</p>
                                                <p>For testing and development purposes, free accounts were created that
                                                    have usage limitations, basically restricting it to a few daily
                                                    calls.</p>
                                                <p>The application uses news from the quarkus feed as the last call
                                                    alternative to respond with some news</p>
                                                <p>The <a
                                                    href="https://quarkus.io/guides/smallrye-fault-tolerance#adding-resiliency-circuit-breaker">Circuit
                                                    Breaker</a> handles exactly this scenario, where the service
                                                    responds with a <a
                                                        href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/426">426</a> or <a
                                                        href="https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/429">429</a> code,
                                                    which means that the invocation limit has been reached for the day
                                                    and then automatically tries to get news from the new provider.</p>
                                                <p>Also when a call to one of the services does not respond within the
                                                    established maximum time</p>
                                            </Card.Body>
                                        </Card>
                                    </li>
                                    <li>Download the figures associated with the news and use imagemagick to reduce the
                                        size and generate a thumbnail
                                    </li>
                                    <li>Save metadata in local database and also publish in distributed cache<br/>
                                        &emsp;&emsp;<ImWarning className="text-warning"/>&nbsp;Caching lasts for up to
                                        one hour
                                    </li>
                                    <li>Saves the figures in the file repository and generates a link for sharing<br/>
                                        &emsp;&emsp;<ImWarning className="text-warning"/>&nbsp;The generated link lasts
                                        for 7 days. If the processing of the news occurs after this period, the figure
                                        will not be available and a new link must be generated.
                                    </li>
                                    <li>Has integration with Kafka for:
                                        <ul>
                                            <li>Notify recent news</li>
                                        </ul>
                                    </li>
                                    <li>Uses <a href="https://www.postgresql.org/">Postgresql</a> as local database</li>
                                    <li>Uses <a href="https://redis.io/">Redis</a> as a distributed cache</li>
                                    <li>Uses <a href="https://min.io/"><b>MIN</b>IO</a> as a file storage</li>
                                    <li>Exposes a REST API to provide news metadata<br/>
                                        <div className="container-fluid">
                                            <div className="row bg-secondary bg-gradient text-white">
                                                <div className="col text-center">Security</div>
                                                <div className="col text-center">Method</div>
                                                <div className="col-2">Operation</div>
                                                <div className="col-2">Path</div>
                                                <div className="col-6">Description</div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillUnlockFill title="OPEN"/>
                                                </div>
                                                <div className="col text-center text-success fw-bold">GET</div>
                                                <div className="col-2">getNewsById</div>
                                                <div className="col-2">/news/id/&#123;id&#125;</div>
                                                <div className="col-6">Returns news metadata by identifier</div>
                                            </div>
                                            <div className="row">
                                                <div className="col text-center"><BsFillUnlockFill title="OPEN"/>
                                                </div>
                                                <div className="col text-center text-info fw-bold">PUT</div>
                                                <div className="col-2">rotateNewsSource</div>
                                                <div className="col-2">/news/rotate</div>
                                                <div className="col-6">Forces Circuit Breaker behavior. Changes the
                                                    search source for the next schedule. This method was created for
                                                    testing purposes and analyzing the health of news sources
                                                </div>
                                            </div>
                                        </div>
                                    </li>
                                    <br/>
                                    <p>The figure below summarizes the interactions performed by the Injector</p>
                                    <InjectorFlow width="100%" height="100%"/>
                                </Accordion.Body>
                            </Accordion.Item>
                            <Accordion.Item eventKey="4">
                                <Accordion.Header><h4>OBSERVER</h4></Accordion.Header>
                                <Accordion.Body>
                                    <h4>WORK OBSERVER</h4>
                                    <h5>Responsible for monitoring job queues and notifying a summary of operations
                                        performed</h5>
                                    <hr/>
                                    <li>Developed with <a href="https://quarkus.io/">Quarkus</a> 2.14</li>
                                    <li>It has a customizable scheduling service to generate reports</li>
                                    <li>Integrates with Kafka to
                                        <ul>
                                            <li>Receive notifications about job requests</li>
                                            <li>Receive notification about the result of job requests</li>
                                            <li>Publish a notification about monitoring report</li>
                                        </ul>
                                    </li>
                                    <li>Store metrics in Redis</li>

                                    <br/>
                                    <p>The figure below summarizes the interactions performed by the Observer</p>
                                    <ObserverFlow width="100%" height="100%"/>
                                </Accordion.Body>
                            </Accordion.Item>
                        </Accordion>
                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="4">
                    <Accordion.Header><BsBricks/>&nbsp; Infrastructure Components list</Accordion.Header>
                    <Accordion.Body className="text-start">
                        <div className="text-center">
                            <Infra width="75%" height="75%"/>
                        </div>
                        <hr/>
                        <p><b>Keycloak</b> <a
                            href="https://www.keycloak.org/index.html">https://www.keycloak.org/index.html</a><br/>Is an
                            open-source identity and access management tool with a focus on
                            modern applications <br/>Offers identity provider services and supports authentication and
                            authorization functionality for the <b>GUI</b> and <b>API</b>.<br/>Uses OIDC and Oauth2 with
                            JWT.<br/>
                            It has a Realm called <b>cc</b> with the following settings:
                            <br/>
                        </p>
                        <div className="ps-4">
                            <strong>Clients:</strong>
                            <li>api-app -> GUI Web Application</li>
                            <li>api-backend -> Application API</li>
                            <strong>Roles:</strong>
                            <table className="table table-bordered table-secondary w-25">
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Members</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>ADMIN</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>MONITOR</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>USER</td>
                                    <td>user1, user2</td>
                                </tr>
                                </tbody>
                            </table>
                            <strong>GROUPS:</strong>
                            <table className="table table-bordered table-secondary w-25">
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Members</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td>ADMIN</td>
                                    <td>admin</td>
                                </tr>
                                <tr>
                                    <td>DEVELOPERS</td>
                                    <td></td>
                                </tr>
                                <tr>
                                    <td>STUDENTS</td>
                                    <td></td>
                                </tr>
                                </tbody>
                            </table>
                            <strong>Users:</strong>
                            <li>admin</li>
                            <li>user1</li>
                            <li>user2</li>
                            <li>usermonitor</li>
                        </div>

                        <p><b>MinIO</b> <a href="https://min.io/">https://min.io/</a> <br/>High Performance Object
                            Storage. Used by the <b>Injector</b> to store the
                            news images and used by the <b>Executor</b> to retrieve the images via a public link.</p>

                        <p><b>PostgreSQL</b><a href="https://www.postgresql.org/">https://www.postgresql.org/</a> <br/>PostgreSQL
                            is an advanced, enterprise class open source relational
                            database that supports both SQL (relational) and JSON (non-relational)
                            querying.<br/><b>API</b>, <b>Injector</b> and <b>Keycloak</b> services are supported by
                            PostgreSQL instances.</p>

                        <p><b>MongoDB</b> <a href="https://www.mongodb.com/">https://www.mongodb.com/</a> <br/>MongoDB
                            is a document-oriented NoSQL database used for high volume data
                            storage.<br/>The <b>Injector</b> service is supported by this database</p>

                        <p><b>Kafka</b> <a href="https://kafka.apache.org/">https://kafka.apache.org/</a> <br/>Apache
                            Kafka is an open-source distributed streaming system used for stream
                            processing, real-time data pipelines, and data integration at scale.<br/>It plays a central
                            role in the solution, being responsible for managing events in different components.
                            Supports threads generated and consumed by various components.<br/>It is configured to
                            create
                            topics with at least 3 partitions and messages are kept in the database for a maximum of 7
                            days.<br/>The default message deletion policy is based on the lifetime of the messages.</p>

                        <p><b>Redis</b> <a href="https://redis.io/">https://redis.io/</a> <br/>Redis, which stands for
                            Remote Dictionary Server, is a fast, open source,
                            in-memory, key-value data store.<br/>Used in the project to share data between components
                            and provide faster access than consuming a database.</p>

                        <p><b>Zookeeper</b> <a href="https://zookeeper.apache.org/">https://zookeeper.apache.org/</a>
                            <br/>ZooKeeper is a centralized service for maintaining configuration
                            information, naming, providing distributed synchronization, and providing group
                            services.<br/>Used to manage the kafka cluster.</p>

                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="5">
                    <Accordion.Header><FaTools/>&nbsp; Management Tools - Only for Development</Accordion.Header>
                    <Accordion.Body className="text-start">
                        <li><b>Docker compose</b> <a
                            href="https://docs.docker.com/compose/">https://docs.docker.com/compose/</a><br/>&emsp;&emsp;Starts
                            all the necessary infrastructure to run the application in a local environment
                        </li>
                        <li><b>pgAdmin</b> <a
                            href="https://www.pgadmin.org/">https://www.pgadmin.org/</a><br/>&emsp;&emsp;Web GUI client
                            for
                            Postgres. Used to connect to Postgres databases
                        </li>
                        <li><b>Kafka-ui</b> <a
                            href="https://github.com/provectus/kafka-ui">https://github.com/provectus/kafka-ui</a><br/>&emsp;&emsp;Web
                            GUI client for Kafka
                        </li>
                        <li>
                            <b>Mongo-express</b> <a
                            href="https://github.com/mongo-express/mongo-express">https://github.com/mongo-express/mongo-express</a><br/>&emsp;&emsp;Web
                            GUI client for MongoDB
                        </li>
                        <li>
                            <b>RedisInsight</b> <a
                            href="https://redis.io/docs/stack/insight/">https://redis.io/docs/stack/insight/</a><br/>&emsp;&emsp;
                            GUI client for Redis
                        </li>
                        <li>
                            <b>MinIO Console</b> <a
                            href="https://min.io/docs/minio/linux/administration/minio-console.html">https://min.io/docs/minio/linux/administration/minio-console.html</a><br/>&emsp;&emsp;
                            Web GUI client for MinIO
                        </li>
                    </Accordion.Body>
                </Accordion.Item>

                <Accordion.Item eventKey="6">
                    <Accordion.Header><TbStairsUp/>&nbsp; Future Works</Accordion.Header>
                    <Accordion.Body className="text-start">
                        <p>We believe that for purposes of didactic purpose this homework has achieved its objectives.
                            However, many requirements and improvements were left out of scope for time and effort.
                            Below we list some interesting items that can be better studied and or better work for
                            future versions of the work developed.</p>
                        <li>Work out a method to renew MinIO links after expiration</li>
                        <li>The solution works with event-driven architecture managed by Kafka. Being a small project it
                            is considered acceptable to share structure of common objects to facilitate communication. A
                            more elegant alternative would be to use schema registration and a standard such as <a
                                href="https://avro.apache.org/">Avro Schemas</a></li>
                        <li>Restructure projects with packages and classes that adhere more to the hexagonal
                            architecture model.
                        </li>
                        <li>Centralize the rules more in the domain model. Convert the project to a model more adherent
                            to Domain Driven Design
                        </li>
                        <li>Completely eliminate dependency on the injector executor by implementing a mechanism for
                            monitoring news generated by the injector and ensuring that they are always in the cache,
                            thus avoiding the executor having to call the injector service as a last resort
                        </li>
                        <li>Implement tighter distributed transaction control with the <a
                            href="https://microservices.io/patterns/data/saga.html">SAGA</a> pattern.
                        </li>
                        <li>Injector - Insert method to receive REST or Notification message to arbitrarily execute
                            schedule
                        </li>
                        <li>Observer - during report generation, based on the metrics obtained, it is possible to
                            trigger a service or even invoke the platform to take actions, such as autoscaling or
                            increasing partitions in kafka.
                        </li>
                        <li>Secrets and settings are basically obtained through environment variables. It is possible to
                            insert a password vault in the solution and receive the secrets through this vault.
                        </li>
                        <li>Service to generate news in an arbitrary way, without the need to use the injector</li>
                        <li>Produce metrics on request fulfillment during a load or stress test with <a
                            href="https://jmeter.apache.org/">JMeter</a> or <a href="https://gatling.io/">Gatling</a>
                        </li>
                        <li>Making a <a href="https://www.postman.com/">Postman</a> collection available for testing
                        </li>
                        <li>Implement handling for dead letter queue concepts and poisonous or hot potato messages. The
                            project, as it stands, just discards the message. In a real project, certainly the message
                            could not be discarded.
                        </li>
                        <li>
                            Generating images on a machine with ARM architecture forced us to generate the image for
                            multiple architectures, amd64 and arm64. This made the image extremely larger than we would
                            have liked. One of the reasons was to use an image with python and Imagemagick. One way out
                            would be to find an alternative to the Imagemagick utility which requires a lot of packages
                            installed on the machine and makes the image grow too large.
                        </li>

                    </Accordion.Body>
                </Accordion.Item>
            </Accordion>


        </div>
    )
        ;
};

export default Dashboard;