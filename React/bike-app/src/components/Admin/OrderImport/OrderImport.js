import React, { useState, useEffect, useRef } from "react";
import Page from "../../Pagination";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import { getWithAuth, put, get, del } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { format } from "date-fns";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Row,
  Col,
  Table,
  Button,
  Input,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "reactstrap";
import "./OrderImport.css";
import ModalAdd from "./ModalAddImport";
import { getCookie } from "../../../Utils/Cookie";
import ModalEdt from "./ModalEdtImport";

toast.configure();
export default function OrderImport() {
  const [pagenum, setPageNum] = useState(0);
  const [orderList, setOrderList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [statusListProd, setStatusListProd] = useState(false);
  const [showPagination, setShowPage] = useState(true);
  const [dropdownOpen, setOpen] = useState(false);
  const [totalPage, setTotalPage] = useState(0);
  const role = getCookie("role");
  const size = 6;
  const toggle = () => setOpen(!dropdownOpen);

  useEffect(() => {
    getCountTotalOrderImport();
    getOrderImportList();
  }, []);

  useEffect(() => {
    getOrderImportList();
  }, [pagenum]);

  async function getCountTotalOrderImport() {
    getWithAuth(`/orderImport/totalOrder`).then((response) => {
      if (response.status === 200) {
        setTotalPage(response.data);
      }
    });
  }
  async function getOrderImportList() {
    getWithAuth(`/imports?pagenum=${pagenum}&size=${size}`).then((response) => {
      if (response.status === 200) {
        setOrderList([...response.data]);
      }
    });
  }
  function getProd(id, ammount, price) {
    getWithAuth(`/product/search/${id}`).then((response) => {
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
    orderList[index].orderImportDetails.map((detail) =>
      getProd(detail.productId, detail.amount, detail.unitprice)
    );
  }
  async function handleProductList(id, index) {
    getProdList(index);
    toggle();
    // setStatusListProd(true);
  }
  function handleDeliveryButton(id, index) {
    const body = JSON.stringify({
      id: id,
      status: !orderList[index].status,
    });
    put(`/imports/${id}`, body).then((response) => {
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
      del(`/imports/${id}`)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Delete successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            getCountTotalOrderImport();
            getOrderImportList();
            setProdList([]);
          }
        })
        .catch((error) => {
          if (error.response.status === 400 || error.response.status === 404) {
            toast.error(`Failed: ${error.response.data.message}`, {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
          }
        });
    }
  }
  // function showList() {
  //   if (statusListProd) {
  //     return prodList.map((prod) => (
  //       <Row id="prodOrder-form" key={prod.id}>
  //         <Col className="col-3">
  //           <img
  //             src={`data:image/jpeg;base64,${prod.photo}`}
  //             className="img-order"
  //           />
  //         </Col>
  //         <Col className="info-prod-order">
  //           <h4>{prod.name}</h4>
  //           <Row>
  //             <Col className="col-2">
  //               <h6>Model:</h6>
  //             </Col>
  //             <Col>
  //               <h6>{prod.id}</h6>
  //             </Col>
  //           </Row>
  //           <Row>
  //             <Col className="col-2">
  //               <h6>Quantity:</h6>
  //             </Col>
  //             <Col>
  //               <h6>{prod.quantity}</h6>
  //             </Col>
  //           </Row>
  //           <h6>
  //             <Row>
  //               <Col className="col-2">
  //                 <h6>Unit Price:</h6>
  //               </Col>
  //               <Col>
  //                 <h6>{numberFormat(prod.price)}</h6>
  //               </Col>
  //             </Row>
  //           </h6>
  //           <h5>Total Price: {numberFormat(prod.quantity * prod.price)}</h5>
  //         </Col>
  //       </Row>
  //     ));
  //   }
  // }
  async function handleSearchChange(e) {
    if (e.target.value.trim().length > 0) {
      setOrderList([]);
      let keyword = e.target.value.trim().split(/ +/).join(' ');
      getWithAuth(
        `/imports/search/ImportByEmployee/${keyword}`
      ).then((response) => {
        if (response.status === 200) {
          setOrderList([...response.data]);
          setShowPage(false);
        }
      });
    } else {
      setShowPage(true);
      getOrderImportList();
    }
  }
  function handleAdd(e) {
    if (e) {
      getCountTotalOrderImport();
      getOrderImportList();
    }
  }
  function handleEdt(e) {
    if (e) getOrderImportList();
  }
  return (
    <>
      <h2 className="title-OrderImport m-3">ORDER IMPORT MANAGER</h2>
      <Row>
        <Col className="col-7">
          <ModalAdd onAdd={(e) => handleAdd(e)} />
        </Col>
        <Col>
          <Input
            type="text"
            name="name"
            id="name"
            className="search-OrderImport"
            placeholder="Search Import by Employee's name"
            onChange={(e) => handleSearchChange(e)}
          />
        </Col>
      </Row>
      <Table bordered className="tableImport">
        <thead>
          <tr>
            <th className="titleTable-OrderImportAdmin">ID</th>
            <th className="titleTable-OrderImportAdmin">EMPLOYEE EMAIL</th>
            <th className="titleTable-OrderImportAdmin">EMPLOYEE NAME</th>
            <th className="titleTable-OrderImportAdmin">TIME IMPORT</th>
            <th className="titleTable-OrderImportAdmin">TOTAL PRICE</th>
            <th className="titleTable-OrderImportAdmin">PRODUCT</th>
            {/* <th className="titleTable-OrderImportAdmin"></th> */}

            {role.includes("ADMIN") ? (
              <th className="titleTable-OrderImportAdmin">ACTION</th>
            ) : null}
          </tr>
        </thead>
        <tbody>
          {orderList.map((order, index) => (
            <tr key={order.id}>
              <th scope="row">{order.id}</th>
              <td>{order.employeeEmail}</td>
              <td>{order.employeeFullName}</td>
              <td>
                {format(new Date(order.timeimport), "dd/MM/yyyy HH:mm:ss")}
              </td>
              <td>{numberFormat(order.totalCost)}</td>
              <td>
                {" "}
                <Button
                  color="link"
                  onClick={() => handleProductList(order.id, index)}
                >
                  List Product
                </Button>
              </td>
              {/* <td>
                <Button
                  color="success"
                  disabled="true"
                  onClick={() => handleDeliveryButton(order.id, index)}
                >
                  Completed
                </Button>
              </td> */}
              {role.includes("ADMIN") ? (
                <td>
                  <ModalDeleteConfirm
                    onChoice={(e) => handleDelete(e, order.id)}
                  />
                </td>
              ) : null}
            </tr>
          ))}
        </tbody>
      </Table>
      {showPagination ? (
        <Page
          total={Math.ceil(totalPage / size)}
          onPageChange={(e) => setPageNum(e)}
        />
      ) : null}
      <Modal isOpen={dropdownOpen} toggle={toggle} size="lg">
        <ModalHeader toggle={toggle} className="titleTable-OrderAdmin">
          Product Imported
        </ModalHeader>
        <ModalBody className="scrollable-Order">
          {prodList.map((prod) => (
            <Row id="prodOrder-form" key={prod.id}>
              <Col className="col-3">
                <img
                  src={`data:image/jpeg;base64,${prod.photo}`}
                  className="img-order"
                />
              </Col>
              <Col className="info-prod-order">
                <h4>{prod.name}</h4>
                <Row>
                  <Col className="col-3">
                    <h6>Model:</h6>
                  </Col>
                  <Col>
                    <h6>{prod.id}</h6>
                  </Col>
                </Row>
                <Row>
                  <Col className="col-3">
                    <h6>Quantity:</h6>
                  </Col>
                  <Col>
                    <h6>{prod.quantity}</h6>
                  </Col>
                </Row>
                <h6>
                  <Row>
                    <Col className="col-3">
                      <h6>Unit Price:</h6>
                    </Col>
                    <Col>
                      <h6>{numberFormat(prod.price)}</h6>
                    </Col>
                  </Row>
                </h6>
                <h5>Total Price: {numberFormat(prod.quantity * prod.price)}</h5>
              </Col>
            </Row>
          ))}
        </ModalBody>
        <ModalFooter>
          <Button color="secondary" onClick={toggle} className="btnCancel">
            Cancel
          </Button>
        </ModalFooter>
      </Modal>
    </>
  );
}
