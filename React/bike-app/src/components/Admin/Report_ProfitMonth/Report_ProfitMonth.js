import React, { useState, useEffect } from "react";
import { Bar } from "react-chartjs-2";
import { getWithAuth } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import ReactExport from "react-data-export";
import "./Report_ProfitMonth.css";
import {
  Col,
  Input,
  Row,
  Form,
  FormGroup,
  Label,
  Button,
} from "reactstrap";

const ExcelFile = ReactExport.ExcelFile;
const ExcelSheet = ExcelFile.ExcelSheet;
export default function Report() {
  const [today, setToday] = useState("");
  const [labelColChart, setLabelColChart] = useState([]);
  const [revenue, setRevenue] = useState([]);
  const [dataExport, setDataExport] = useState([]);
  

  let DataSet = [
    {
      columns: [
        { title: "Month" },
        { title: "Revenue" },
      ],
      data: revenue.map((revenue,index) => [
        { value: labelColChart[index]},
        { value: revenue},
      ]),
    },
  ];

  const color = [
    "rgba(255, 99, 132, 0.2)",
    "rgba(255, 159, 64, 0.2)",
    "rgba(255, 205, 86, 0.2)",
    "rgba(75, 192, 192, 0.2)",
    "rgba(54, 162, 235, 0.2)",
    "rgba(153, 102, 255, 0.2)",
    "rgba(201, 203, 207, 0.2)",
  ];
  
  const data = {
    labels: labelColChart,
    datasets: [
      {
        label: "$/month",
        data: revenue,
        backgroundColor: [
          "rgba(255, 99, 132, 0.2)",
          "rgba(54, 162, 235, 0.2)",
          "rgba(255, 206, 86, 0.2)",
          "rgba(75, 192, 192, 0.2)",
          "rgba(153, 102, 255, 0.2)",
          "rgba(255, 159, 64, 0.2)",
        ],
        borderColor: [
          "rgba(255, 99, 132, 1)",
          "rgba(54, 162, 235, 1)",
          "rgba(255, 206, 86, 1)",
          "rgba(75, 192, 192, 1)",
          "rgba(153, 102, 255, 1)",
          "rgba(255, 159, 64, 1)",
        ],
        borderWidth: 1,
      },
    ],
  };

  useEffect(() => {
    setDate();
    setLabelColChart([]);
    setRevenue([]);
  }, []);
  
  useEffect(() => {
    setDataExport([
      {
        columns: [
          { title: "Month" },
          { title: "Revenue" },
        ],
        data: revenue.map((index, revenue) => [
          { value: labelColChart[index]},
          { value: revenue},
        ]),
      },
    ]);
  }, [labelColChart]);

  
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
  function handleSubmit(e) {
    e.preventDefault();
    setLabelColChart([]);
    setRevenue([]);
    let from = `${e.target.dateFromSelect.value.slice(
      5
    )}-${e.target.dateFromSelect.value.slice(0, 4)}`;
    let to = `${e.target.dateToSelect.value.slice(
      5
    )}-${e.target.dateToSelect.value.slice(0, 4)}`;
    getRevenue(from, to);
    calLabelColChart(from, to);
  }

  function calLabelColChart(from, to) {
    let yearFrom = from.slice(3);
    let monthFrom = from.slice(0, 2);
    let yearTo = to.slice(3);
    let monthTo = to.slice(0, 2);

    if (yearFrom > yearTo) {
      [monthFrom, monthTo] = [monthTo, monthFrom];
      [yearFrom, yearTo] = [yearTo, yearFrom];
    } else if (yearFrom == yearTo && monthFrom > monthTo) {
      [monthFrom, monthTo] = [monthTo, monthFrom];
    }
    let list = []
    // if (monthFrom < 10) monthFrom = from.slice(1,2);
    console.log(monthFrom)
    while (monthFrom <= monthTo || yearFrom < yearTo) {
      // setLabelColChart((oldArr) => [...oldArr, `${monthFrom}-${yearFrom}`]);
      // console.log(`${monthFrom}-${yearFrom}`)
      list.push(`${Number(monthFrom)}-${yearFrom}`)
      if (monthFrom == monthTo && yearFrom == yearTo) break;
    	monthFrom++;
    	if (monthFrom > 12) {
    		yearFrom++;
    		monthFrom=1;
    	}
    }
    setLabelColChart(list);
  }

  async function getRevenue(from, to) {
    getWithAuth(`/report/profitByMonth?fromMonth=${from}&toMonth=${to}`).then(
      (response) => {
        if (response.status === 200) {
          setRevenue([...response.data]);
        }
      }
    );
  }
  const options = {
    scales: {
      // responsive: true,
      // maintainAspectRatio: false,
      yAxes: [
        {
          ticks: {
            beginAtZero: true,
          },
        },
      ],
    },
  };

  return (
    <div>
      <h2 className="title-Report">REVENUE OF MONTH</h2>
      <Row>
        <Col className="col-7 ">
          <Form onSubmit={(e) => handleSubmit(e)}>
            <div className="selectDateDiv-TopProd">
              <FormGroup>
                <div className="datepicker">
                  <Label for="dateFromSelect" className="">
                    From Month
                  </Label>
                  <Input
                    type="month"
                    name="dateFromSelect"
                    id="dateFromSelect"
                    required="required"
                    max={today}
                  />
                </div>
              </FormGroup>
              <FormGroup>
                <div className="datepicker">
                  <Label for="dateToSelect" className="">
                    From Month
                  </Label>
                  <Input
                    type="month"
                    name="dateToSelect"
                    id="dateToSelect"
                    required="required"
                    max={today}
                  />
                </div>
              </FormGroup>
              <div className="btnDiv-TopProd">
                <Button
                  outline
                  color="primary"
                  type="submit"
                  className="btn-TopProd"
                >
                  Submit
                </Button>
              </div>
            </div>
          </Form>
        </Col>
        <Col className="divNumProd-ReportTopProd">
        <ExcelFile
            filename={`RevenueMonth`}
            element={
              <Button outline color="success">
                Export Revenue
              </Button>
            }
          >
            <ExcelSheet dataSet={DataSet} name="Report_Sheet" />
          </ExcelFile>
        </Col>
      </Row>
      <div className="chart-div-TopProd mt-3">
        <Bar width="1200" height="400" data={data} options={options} />
      </div>
      
    </div>
  );
}
