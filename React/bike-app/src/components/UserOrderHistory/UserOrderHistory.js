import React, { useState, useEffect, useRef } from "react";
import { getCookie } from "../../Utils/Cookie";
import { getWithAuth, get } from "../../Utils/httpHelper";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";
import { format } from "date-fns";
import Header from "../Header";
import Navbar from "../Navbar";
import "./UserOrderHistory.css";
import Footer from "../Footer";
import Page from "../Pagination";
import ProdList from "./ProdList/ProdList.js";

export default function UserOrderHistory() {
  const [orderList, setOrderList] = useState([]);
  const [pagenum, setPageNum] = useState(0);
  const size = 3;
  let totalPage = useRef(0);
  const emailUser = getCookie("email");
  useEffect(() => {
    getOrderList();
    getTotalOrder();
  }, []);
  useEffect(() => {
    getOrderList();
  }, [pagenum]);
  async function getOrderList() {
    getWithAuth(
      `/order/customeremail?pagenum=${pagenum}&size=${size}&email=${emailUser}`
    ).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        setOrderList(response.data);
      }
    });
  }
  async function getTotalOrder() {
    getWithAuth(`/order/totalOrderByUser/${emailUser}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
  }
  function handlePageChange(e) {
    setPageNum(e);
  }

  return (
    <>
      <Header />
      <Navbar />
      <h1 className="mb-3">Order History</h1>
      <div>
        {orderList.map((order, index) => (
          <div className="orderHistory">
            <Row className="prod-form" key={order.id}>
              <h4>{order.customers.fullname}</h4>
              <p>
                Time bought:{" "}
                {format(new Date(order.timebought), "dd/MM/yyyy HH:mm:ss")}
              </p>
              <p>Address: {order.address}</p>
              <Row>
                <Col className="status-order col-7">
                  <h5>Status: </h5>
                  <h5 className={order.status ? "status-true" : "status-false"}>
                    {order.status ? "Completed" : "In process"}
                  </h5>
                </Col>
                <Col className="priceTotal">
                  <h4 className="priceTitle">Total: </h4>
                  <h4 className="status-false">
                    {numberFormat(order.totalCost)}
                  </h4>
                </Col>
              </Row>

              <ProdList orderDetail={order.orderDetails} />
            </Row>
            {/* {getProdList(index)} */}
          </div>
        ))}
      </div>
      <Page
        total={Math.ceil(totalPage.current / size)}
        onPageChange={(e) => handlePageChange(e)}
      />
      <Footer />
    </>
  );
}
