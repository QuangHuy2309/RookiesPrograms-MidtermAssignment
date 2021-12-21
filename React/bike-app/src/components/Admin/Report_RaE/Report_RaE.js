import React, { useState, useEffect } from "react";
import { Line } from "react-chartjs-2";
import { getWithAuth } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import "./Report_RaE.css";
import { Col, Input, Row } from "reactstrap";
export default function Report() {
  const [today, setToday] = useState("");
  const [selectedDate, setSelectedDate] = useState(today);
  const [profitNum, setProfitNum] = useState(0);
  const [profitNumMonthBefore, setProfitNumBefore] = useState(0);
  const [profit, setProfit] = useState([]);
  const [purchase, setPurchase] = useState([]);

  let data = {
    labels: [
      "Jan",
      "Feb",
      "Mar",
      "Apr",
      "May",
      "Jun",
      "Jul",
      "Aug",
      "Sep",
      "Oct",
      "Nov",
      "Dec",
    ],
    datasets: [
      {
        label: "Revenue",
        data: profit,
        fill: true,
        backgroundColor: "rgba(75,192,192,0.2)",
        borderColor: "rgba(75,192,192,1)",
      },
      {
        label: "Expenditure",
        data: purchase,
        fill: false,
        borderColor: "#742774",
      },
    ],
  };
  useEffect(() => {
    setDate();
  }, []);

  useEffect(async () => {
    let year = selectedDate.slice(0, 4);
    let month = selectedDate.slice(5);

    getProfitList(year);
    getPurchaseList(year);
    calProfit(month, year);
  }, [selectedDate]);

  async function calProfit(month, year) {
    if (month !== "" && year !== "") {
      getWithAuth(`/report/profit?month=${month}&year=${year}`).then(
        (response) => {
          if (response.status === 200) {
            // console.log(response.data);
            setProfitNum(response.data);
          }
        }
      );
      getWithAuth(`/report/profit?month=${month-1}&year=${year}`).then(
        (response) => {
          if (response.status === 200) {
            // console.log(response.data);
            setProfitNumBefore(response.data);
          }
        }
      );
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
  async function handleSelectDate(e) {
    setSelectedDate(e.target.value);
  }
  async function getProfitList(year) {
    if (year !== "") {
      setProfit([]);
      getWithAuth(`/report/profit/${year}`).then((response) => {
        if (response.status === 200) {
          setProfit([...response.data]);
        }
      });
    }
  }
  async function getPurchaseList(year) {
    if (year !== "") {
      setPurchase([]);
      getWithAuth(`/report/purchasecost/${year}`).then((response) => {
        if (response.status === 200) {
          setPurchase([...response.data]);
        }
      });
    }
  }

  return (
    <div>
      <h2 className="title-Report">REVENUE & EXPENDITURE</h2>
      <Row>
        <Col className="col-8">
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

        <Col>
          <Row>
            <Col className="col-6 profit-text-RaE">
              <h4 className="priceTitle ">Profit in month: </h4>
            </Col>
            <Col className="profit-num-RaE">
              <h4 className="price_Rae">{numberFormat(profitNum-profitNumMonthBefore)}</h4>
            </Col>
          </Row>
          <Row>
            <Col className="col-6 profit-text-RaE">
              <h4 className="priceTitle ">Profit: </h4>
            </Col>
            <Col className="profit-num-RaE">
              <h4 className="price_Rae">{numberFormat(profitNum)}</h4>
            </Col>
          </Row>
        </Col>
      </Row>
      <div className="chart-div">
        <Line
          data={data}
          height="500"
          options={{
            responsive: true,
            maintainAspectRatio: false,
          }}
        />
      </div>
    </div>
  );
}
