import React, { useState, useEffect } from 'react'
import { Line } from "react-chartjs-2";
import { getWithAuth } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import "./Report.css";
import {Col, Input, Row} from "reactstrap";
export default function Report() {
  const [today, setToday] = useState("");
  const [selectedDate, setSelectedDate] = useState(today);
  const [profitNum,setProfitNum] = useState(0);
  const [profit,setProfit] = useState([]);
  const [purchase, setPurchase] = useState([]);

  let data = {
    labels: ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"],
    datasets: [
      {
        label: "Profit",
        data: profit,
        fill: true,
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)"
      },
      {
        label: "Cost",
        data: purchase,
        fill: false,
        borderColor: "#742774"
      }
    ]
  };
  useEffect(() => {
      setDate();
      calProfit();
  }, []);
  useEffect(async() => {
      let year = selectedDate.slice(0,4);
      let month = selectedDate.slice(5);
      
      getProfitList(year);
      getPurchaseList(year);
      calProfit(month);
  }, [selectedDate]);
  async function calProfit(month){
    if (profit.length < 1) setProfitNum(0);
    else {
      let index = month-1;
    setProfitNum(profit[index] - purchase[index]);
    }
  }
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

    today = yyyy + "-" + mm;
    setToday(today);
  }
  async function handleSelectDate(e){
    
    setSelectedDate(e.target.value);
  }
  async function getProfitList(year){
    setProfit([]);
    getWithAuth(`/report/profit/${year}`).then((response) => {
      if (response.status === 200) {
        setProfit([...response.data]);
      }
    });
  }
  async function getPurchaseList(year){
    setPurchase([]);
    getWithAuth(`/report/purchasecost/${year}`).then((response) => {
      if (response.status === 200) {
        setPurchase([...response.data]);
      }
    });
  }

    return (
        <div>
            <h2 className="title-user">REPORT</h2>
            <Row>
              <Col className="col-5">
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
              </Col>
              <Col className="priceTotal">
                <h4 className="priceTitle">Profit: </h4>
                <h4 className="status-false">{numberFormat(profitNum)}</h4>
              </Col>
              </Row>
            <div className="chart-div">
            <Line data={data} height="500" options={{
          responsive: true,
          maintainAspectRatio: false,
        }}/>
        </div>
        </div>
    )
}
