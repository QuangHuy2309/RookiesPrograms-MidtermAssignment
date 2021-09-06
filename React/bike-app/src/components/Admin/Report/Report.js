import React, { useState, useEffect } from 'react'
import { Line } from "react-chartjs-2";
import "./Report.css";
import {Input,} from "reactstrap";
export default function Report() {
  const [today, setToday] = useState("");
  const data = {
    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    datasets: [
      {
        label: "First dataset",
        data: [33, 53, 85, 41, 44, 65],
        fill: true,
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)"
      },
      {
        label: "Second dataset",
        data: [33, 25, 35, 51, 54, 76],
        fill: false,
        borderColor: "#742774"
      }
    ]
  };
  useEffect(() => {
      setDate();
    
  }, []);
  function setDate() {
    let today = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1; //January is 0!
    let yyyy = today.getFullYear();
    if (dd < 10) {
      dd = "0" + dd;
    }
    if (mm < 10) {
      mm = "0" + mm;
    }

    today = yyyy + "-" + mm + "-" + dd;
    setToday(today);
  }
  async function handleSelectDate(e){
    console.log(e.target.value);
  }
    return (
        <div>
            <h2 className="title-user">REPORT</h2>
            <div className="datepicker">
            <Input
                type="month"
                name="dateselect"
                id="exampleBrand"
                required="required"
                max={today}
                onChange={(e) => handleSelectDate(e)}
              />
              </div>
            <div className="chart-div">
            <Line data={data} height="500" options={{
          responsive: true,
          maintainAspectRatio: false,
        }}/>
        </div>
        </div>
    )
}
