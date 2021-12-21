import React, { useState, useEffect, useRef } from "react";
import { getCookie } from "../../Utils/Cookie";
import { getWithAuth, get, put } from "../../Utils/httpHelper";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";
import { format } from "date-fns";
import Header from "../Header";
import Navbar from "../Navbar";
import "./UserOrderHistory.css";
import Footer from "../Footer";
import Page from "../Pagination";
import ProdList from "./ProdList/ProdList.js";
import ModalDeleteConfirm from "../Admin/ModalDeleteConfirm";
import PayPal from "../Order/Payment/PayPal.js";

export default function UserOrderHistory() {
  const [orderList, setOrderList] = useState([]);
  const [pagenum, setPageNum] = useState(0);
  const size = 3;
  let totalPage = useRef(0);
  const paypal = useRef();
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
  function handleChangeStatus(e, id) {
    const note = "Customer Cancel"
    if (e === "OK") {
      put(`/order/note/${id}?status=4&note=${note}`, "").then((response) => {
        if (response.status === 200) {
          getOrderList();
        }
      });
    }
  }
  function handleStatus(status) {
    let stateClass;
    let stateText;
    switch (status) {
      case 1:
        stateClass = "statusUserHistoryColor-InProcess";
        stateText = "In Process";
        break;
      case 2:
        stateClass = "statusUserHistoryColor-Delivery";
        stateText = "Delivering";
        break;
      case 3:
        stateClass = "statusUserHistoryColor-Complete";
        stateText = "Completed";
        break;
      case 4:
        stateClass = "statusUserHistoryColor-Canceled";
        stateText = "Canceled";
        break;
    }
    return <h5 className={stateClass}>{stateText}</h5>;
  }
  function getTotal(list) {
    let totalCost = 0;
    list.forEach((detail) => {
      totalCost += detail.ammount * detail.unitPrice;
    });
    return totalCost;
  }
  function handlePayment(e){
    if (e) getOrderList();
  }
  return (
    <>
      <Header />
      <Navbar />
      <h1 className="mb-3 mt-5">Order History</h1>
      <div>
        {orderList.map((order, index) => (
          <div className="orderHistory" key={index}>
            <Row className="prod-form" key={order.id}>
              <h4>{order.customers.fullname}</h4>
              <p>
                Time bought:{" "}
                {format(new Date(order.timebought), "dd/MM/yyyy HH:mm:ss")}
              </p>
              <div className="payment_UserOrderHistory">
                <p>Payment: </p>
                <p
                  className={
                    order.payment
                      ? "paymentStatusTrue_UserOrderHistory"
                      : "paymentStatusFalse_UserOrderHistory"
                  }
                >
                  {order.payment ? "Paid" : "Unpaid"}
                </p>
              </div>
              <p>Address: {order.address}</p>
              <Row>
                <Col className="status-order col-7">
                  <h5>Status: </h5>
                  {handleStatus(order.status)}
                </Col>
                <Col className="priceTotal">
                  <h4 className="priceTitle">Total: </h4>
                  <h4 className="status-false priceTitle">
                    {numberFormat(getTotal(order.orderDetails))}
                  </h4>
                </Col>
              </Row>

              {(order.status == 1 && !order.payment) ? (
                <Row>
                  <Col>
                    <div className="mb-3">
                      <ModalDeleteConfirm
                        cancel="true"
                        onChoice={(e) => handleChangeStatus(e, order.id)}
                      />
                    </div>
                  </Col>
                  
                    <Col className="col-6">
                      <PayPal
                        total={getTotal(order.orderDetails)}
                        order_id={order.id}
                        onPaymentChange={(e) => handlePayment(e)}
                      />
                    </Col>
                 
                </Row>
              ) : null}

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
