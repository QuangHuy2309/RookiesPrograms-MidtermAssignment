import React, { useState, useEffect, useRef } from "react";
import Page from "../../Pagination";
import { getWithAuth, put, get, del } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { format } from "date-fns";
import "./Order.css";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import {toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import {
  Row,
  Col,
  Table,
  Button,
} from "reactstrap";

toast.configure()
export default function Order() {
  const [pagenum, setPageNum] = useState(0);
  const [statusListProd, setStatusListProd] = useState(false);
  const [orderList, setOrderList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const size = 6;
  let totalPage = useRef(0);

  useEffect(() => {
    getWithAuth(`/order/totalOrder`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
    getListOrder();
  }, []);
  useEffect(() => {
    getListOrder();
  }, [pagenum]);

  function getListOrder(){
    getWithAuth(`/order?pagenum=${pagenum}&size=${size}`).then((response) => {
        if (response.status === 200) {
          setOrderList([...response.data]);
        }
      });
  }
  function handleDeliveryButton(id, index){
    put(`/order/updateStatus/${id}`,"").then((response) => {
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
  function getProd(id, ammount) {
    get(`/public/product/search/${id}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        let prod = response.data;
        prod.quantity = ammount;
        setProdList((oldArr) => [...oldArr, prod]);
      }
    });
  }
  function getProdList(index) {
    setProdList([]);
    orderList[index].orderDetails.map((detail) => getProd(detail.id.productId, detail.ammount));
  }
  function handleProductList(index){
    getProdList(index);
    setStatusListProd(true);
  }
  function handleDelete(e, id) {
    if (e === "OK") {
    del(`/order/${id}`)
      .then((response) => {
        if (response.status === 200) {
          toast.success("Delete successfully!!!", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          getListOrder();
        }
      })
      .catch((error) => {
        toast.error(error, {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
      });
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
                <h6>
                    Quantity: {prod.quantity}
                </h6>
                <h5>
                    Price: {numberFormat(prod.quantity * prod.price)}
                </h5>
                </Col>
                </Row>
      )))
        };
  }
  return (
    <>
      <h2 className="title-user">ORDER MANAGER</h2>
      <Table bordered>
        <thead>
          <tr>
            <th>ID</th>
            <th>CUSTOMER EMAIL</th>
            <th>CUSTOMER NAME</th>
            <th>BOUGHT AT</th>
            <th>TOTAL</th>
            <th>ADDRESS</th>
            <th>PRODUCT</th>
            <th>STATUS</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {orderList.map((order, index) => (
            <tr key={order.id}>
              <th scope="row">{order.id}</th>
              <td>{order.customers.email}</td>
              <td>{order.customers.fullname}</td>
              <td>
                {format(new Date(order.timebought), "dd/MM/yyyy HH:mm:ss")}
              </td>
              <td>{numberFormat(order.totalCost)}</td>
              <td>{order.address}</td>
              <td> <Button color="link" onClick={() => handleProductList(index)}>List Product</Button></td>
              <td>
                {order.status ? (
                  <Button color="success" onClick={() => handleDeliveryButton(order.id, index)}>Delivered</Button>
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
      <Page
        total={Math.ceil(totalPage.current / size)}
        onPageChange={(e) => setPageNum(e)}
      />
      {showList()}
    </>
  );
}
