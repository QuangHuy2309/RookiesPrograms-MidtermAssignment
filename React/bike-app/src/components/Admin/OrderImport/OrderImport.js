import React, { useState, useEffect, useRef } from "react";
import Page from "../../Pagination";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import { getWithAuth, put, get, del } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { format } from "date-fns";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Row, Col, Table, Button } from "reactstrap";
import "./OrderImport.css";
import ModalAdd from "./ModalAddImport";

toast.configure();
export default function OrderImport() {
  const [pagenum, setPageNum] = useState(0);
  const [orderList, setOrderList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [statusListProd, setStatusListProd] = useState(false);
  const size = 6;
  let totalPage = useRef(0);
  useEffect(() => {
    getOrderImportList();
  }, []);
  async function getOrderImportList(){
    getWithAuth(`/imports?pagenum=${pagenum}&size=${size}`).then((response) => {
      if (response.status === 200) {
        setOrderList([...response.data]);
      }
    });
  }
  function getProd(id, ammount,price) {
    get(`/public/product/search/${id}`).then((response) => {
      if (response.status === 200) {
        let prod = response.data;
        prod.quantity = ammount;
        prod.price = price;
        setProdList((oldArr) => [...oldArr, prod]);
      }
    });
  }
  function getProdList(index) {
    setProdList([]);
    orderList[index].orderImportDetails.map((detail) => getProd(detail.id.productId, detail.ammount, detail.price));
  }
  async function handleProductList(id, index){
    getProdList(index);
    setStatusListProd(true);
  }
  function handleDeliveryButton(id, index){

    const body = JSON.stringify({
      id: id,
      status: !orderList[index].status,
    });
    put(`/imports/${id}`,body).then((response) => {

        if (response.status === 200) {
          console.log(response.data);
        }
      });
      let list = [...orderList];
      let order = { ...list[index] };
      order.status = !order.status;
      list[index] = order;
      setOrderList(list);
      
  }
  function handleDelete(e, id) {
    if (e === "OK") {
    // del(`/order/${id}`)
    //   .then((response) => {
    //     if (response.status === 200) {
    //       toast.success("Delete successfully!!!", {
    //         position: toast.POSITION.TOP_RIGHT,
    //         autoClose: 3000,
    //       });
    //       getListOrder();
    //     }
    //   })
    //   .catch((error) => {
    //     toast.error(error, {
    //       position: toast.POSITION.TOP_RIGHT,
    //       autoClose: 3000,
    //     });
    //   });
    }
  }
  function showList(){
    if(statusListProd){
        return (prodList.map((prod) => (
          <Row id="prodOrder-form" key={prod.id}>
            <Col className="col-3">
              <img
                src={`data:image/jpeg;base64,${prod.photo}`}
                className="img-order"
              />
            </Col>
            <Col className="info-prod-order">
              <h4>
                {prod.name}
              </h4>
              <Row>
                <Col className="col-2">
                  <h6>
                  Quantity: 
                  </h6>
              </Col>
              <Col>
              <h6>
                  {prod.quantity}
              </h6>
              </Col>
              </Row>
              <h6>
                  <Row>
                <Col className="col-2">
                  <h6>
                  Unit Price: 
                  </h6>
              </Col>
              <Col>
              <h6>
              {numberFormat(prod.price)}
              </h6>
              </Col>
              </Row>
              </h6>
              <h5>
                  Total Price: {numberFormat(prod.quantity * prod.price)}
              </h5>
              </Col>
              </Row>
    )))
      };
}
  return (
  <>
   <h2 className="title-user">ORDER IMPORT MANAGER</h2>
   <ModalAdd />
      <Table bordered className="tableImport">
        <thead>
          <tr>
            <th>ID</th>
            <th>EMPLOYEE EMAIL</th>
            <th>EMPLOYEE NAME</th>
            <th>TIME IMPORT</th>
            <th>TOTAL PRICE</th>
            <th>PRODUCT</th>
            <th>STATUS</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
        {orderList.map((order, index) => (
          <tr key={order.id}>
            <th scope="row">{order.id}</th>
            <td>{order.employee.email}</td>
            <td>{order.employee.fullname}</td>
            <td>{format(new Date(order.timeimport), "dd/MM/yyyy HH:mm:ss")}</td>
            <td>{numberFormat(order.totalCost)}</td>
            <td> <Button color="link" onClick={() => handleProductList(order.id, index)}>List Product</Button></td>
            <td>
                {order.status ? (
                  <Button color="success" disabled="true" onClick={() => handleDeliveryButton(order.id, index)}>Delivered</Button>
                ) : (
                    <Button color="warning" onClick={() => handleDeliveryButton(order.id, index)}>Not Delivery</Button>
                )}
              </td>
              <td>
              <ModalDeleteConfirm onChoice={(e) => handleDelete(e,order.id)} />
              </td>
         </tr>
         ))}
         </tbody>
        </Table>
        {showList()}
        </>
        );
}
