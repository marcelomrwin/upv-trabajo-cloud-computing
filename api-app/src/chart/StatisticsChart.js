import {React, useEffect, useRef, useState} from "react";
import {CartesianGrid, Legend, Line, LineChart, Tooltip, XAxis, YAxis} from "recharts";
import {Container} from "react-bootstrap";

const StatisticsChart = (props) => {
    // const data = [
    //     {name: 'Page A', uv: 400, pv: 2400, amt: 2400},
    //     {name: 'Page B', uv: 300, pv: 1800, amt: 2400},
    //     {name: 'Page C', uv: 350, pv: 2000, amt: 2400},
    //     {name: 'Page D', uv: 200, pv: 1400, amt: 2400},
    //     {name: 'Page D', uv: 400, pv: 2400, amt: 2400},
    //     {name: 'Page E', uv: 250, pv: 2500, amt: 2000}
    // ];

    const {data,setData} = useState([]);

    useEffect(()=>{

    })

    const renderLines = () => {
        console.log(props.data);
        const lines = Array.from(props.data).map((item) => {
            <Line
                key={item.totalJobRequest}
                name={item.totalJobRequest}
                type="monotone"
                dataKey={item.totalJobRequest}
                stroke="#8884d8"
                strokeWidth={2}
            />
        });
        return lines;
    };

    return (
        <Container>
            <LineChart width={600} height={300} data={props.data} margin={{top: 5, right: 20, bottom: 5, left: 0}}>
                <CartesianGrid stroke="#ccc" strokeDasharray="5 5"/>
                <XAxis dataKey="reportDate"/>
                <YAxis/>
                <Tooltip/>
                <Legend/>
                {/*{renderLines()}*/}
            </LineChart>
        </Container>
    );
}

export default StatisticsChart;