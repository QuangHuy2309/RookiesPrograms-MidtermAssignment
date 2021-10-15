import React, { useState, useEffect } from "react";
import { Bar } from "react-chartjs-2";
import { getWithAuth } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import "./Report_TopProd.css";
import {
  Col,
  Input,
  Row,
  Form,
  FormGroup,
  Label,
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "reactstrap";

export default function Report() {
  const [today, setToday] = useState("");
  const [selectedDate, setSelectedDate] = useState(today);
  const [prodSelect, setProdSelect] = useState(Object);
  const [labelColChart, setLabelColChart] = useState([]);
  const [topProd, setTopProd] = useState([]);
  const [prodTopShow, setProdTopShow] = useState([]);
  const [modal, setModal] = useState(false);
  const [size, setSize] = useState(3);
  const toggle = () => setModal(!modal);

  const color = [
    "rgba(255, 99, 132, 0.2)",
    "rgba(255, 159, 64, 0.2)",
    "rgba(255, 205, 86, 0.2)",
    "rgba(75, 192, 192, 0.2)",
    "rgba(54, 162, 235, 0.2)",
    "rgba(153, 102, 255, 0.2)",
    "rgba(201, 203, 207, 0.2)",
  ];
  // const labels = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul"];
  const data = {
    labels: labelColChart,
    datasets: [
      {
        label: "# of pcs",
        data: prodTopShow,
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
    setProdTopShow([]);
  }, []);
  // useEffect(async () => {
  //   let year = selectedDate.slice(0, 4);
  //   let month = selectedDate.slice(5);

  //   getTopProd(year);
  // }, [selectedDate]);
  useEffect(() => {
    showReport();
  }, [topProd]);

  useEffect(() => {
    showReport();
  }, [size]);

  async function showReport() {
    setProdTopShow([]);
    setLabelColChart([]);
    topProd.map((prod, index) => {
      if (index >= 0 && index < size) {
        setProdTopShow((oldArr) => [...oldArr, `${prod.totalsold}`]);
        setLabelColChart((oldArr) => [...oldArr, `${prod.id}`]);
      }
    });
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
  // async function handleSelectDate(e) {
  //   setSelectedDate(e.target.value);
  //   let month = e.target.value.slice(5);
  // }
  function handleSubmit(e) {
    e.preventDefault();
    setLabelColChart([]);
    setProdTopShow([]);
    console.log(e.target.dateFromSelect.value);
    console.log(e.target.dateToSelect.value);
    let from = `${e.target.dateFromSelect.value.slice(
      5
    )}-${e.target.dateFromSelect.value.slice(0, 4)}`;
    let to = `${e.target.dateToSelect.value.slice(
      5
    )}-${e.target.dateToSelect.value.slice(0, 4)}`;
    getTopProd(from, to);
  }

  async function getTopProd(from, to) {
    getWithAuth(`/report/topProduct?fromMonth=${from}&toMonth=${to}`).then(
      (response) => {
        if (response.status === 200) {
          setTopProd([...response.data]);
        }
      }
    );
  }

  const options = {
    onClick: (event, elements) => {
      console.log(elements[0].index);
      const index = elements[0].index;
      const prod = topProd[index];
      console.log(prod);
      setProdSelect(prod);
      toggle();
    },
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
      <h2 className="title-Report">TOP PRODUCT OF MONTH</h2>
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
          <Label className="me-3">Top: </Label>
          <select
            value={size}
            className="selectNumProd-ReportTopProd"
            onChange={(e) => setSize(e.target.value)}
          >
            {(topProd.length > 3) ? <option value={3}>3</option> : null}
            {(topProd.length > 5) ? <option value={5}>5</option> : null}
            <option value={topProd.length}>All</option>
          </select>
        </Col>
      </Row>
      {/* <Col className="priceTotal">
                <h4 className="priceTitle">Profit: </h4>
                <h4 className="status-false">{numberFormat(profitNum)}</h4>
              </Col> */}

      <div className="chart-div-TopProd mt-3">
        <Bar width="1200" height="400" data={data} options={options} />
      </div>
      <Modal isOpen={modal} toggle={toggle} className="">
        <ModalHeader toggle={toggle} className="title-AddProductAdmin">
          Product Details
        </ModalHeader>
        <ModalBody>
          <div className="titleProdSelect-ReportTopProd">
            <Label className="titleTextProdSelect-ReportTopProd">
              {prodSelect.name}
            </Label>
            <p>ID: {prodSelect.id}</p>
          </div>
          <Row className="mb-3">
            <Col className="col-5 p-0 ps-2">
              <Label>
                <img
                  src={`data:image/jpeg;base64,${prodSelect.photo}`}
                  className="img-reportTopProd"
                />
              </Label>
            </Col>
            <Col className="infoProdSelect-ReportTopProd">
              <br />
              <p className="infoTextProdSelect-ReportTopProd">
                In Stock: {prodSelect.quantity} pcs
              </p>
              <p className="infoTextProdSelect-ReportTopProd">
                Unit Price: {numberFormat(prodSelect.price)}
              </p>
              <br />
              <p className="infoTextProdSelect-ReportTopProd">
                Total sold: {prodSelect.totalsold} pcs
              </p>
              <p className="infoTextProdSelect-ReportTopProd">
                Sales revenue:{" "}
                {numberFormat(prodSelect.price * prodSelect.totalsold)}
              </p>
            </Col>
          </Row>
        </ModalBody>
        {/* <ModalFooter>
          <Button color="secondary" onClick={toggle} className="btnCancel">
            Cancel
          </Button>
        </ModalFooter> */}
      </Modal>
    </div>
  );
}
