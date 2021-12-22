import React, { useState, useEffect, useRef } from "react";
import Page from "../../Pagination";
import { getWithAuth, put, get, del } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { format } from "date-fns";
import "./Order.css";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { getCookie } from "../../../Utils/Cookie";
import ModalAddNote from "./ModalAddNote/ModalAddNote.js";
import PrintOrder from "./PrintOrder/PrintOrder.js";
import {
  Row,
  Col,
  Table,
  Button,
  DropdownItem,
  DropdownMenu,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  DropdownToggle,
  Label,
  Input,
  Dropdown,
  UncontrolledTooltip,
} from "reactstrap";

toast.configure();
export default function Order() {
  const [pagenum, setPageNum] = useState(0);
  const [statusListProd, setStatusListProd] = useState(false);
  const [dropdownOpen, setOpen] = useState(false);

  const [showNote, setShowNote] = useState(false);
  const [idNote, setIDNote] = useState("");
  const [statusNote, setStatusNote] = useState("");

  const [orderList, setOrderList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [dropdownOpenChoiceState, setOpenChoiceState] = useState(false);
  const [choice, setChoice] = useState(0);
  const [totalPage, setTotalPage] = useState(0);
  const [search, setSearch] = useState("");
  const [showPagination, setShowPage] = useState(true);
  const role = getCookie("role");
  const size = 6;

  const toggle = () => setOpen(!dropdownOpen);
  const toggleChoiceState = () => setOpenChoiceState(!dropdownOpenChoiceState);
  useEffect(() => {
    getWithAuth(`/order/totalOrder`).then((response) => {
      if (response.status === 200) {
        setTotalPage(response.data);
      }
    });
    getListOrder();
  }, []);
  useEffect(() => {
    getListOrder();
  }, [pagenum]);

  useEffect(() => {
    if (search === "") {
      getListOrder();
      setShowPage(true);
    } else {
      getSearchOrderList(search, choice);
    }
  }, [choice]);

  function getTotalOrderByStatus() {
    getWithAuth(`/order/totalOrderByStatus/${choice}`).then((response) => {
      if (response.status === 200) {
        setTotalPage(response.data);
      }
    });
  }

  function getListOrder() {
    let status = "";
    if (choice > 0) {
      status = choice;
      getTotalOrderByStatus();
    }
    getWithAuth(
      `/orderDTO?pagenum=${pagenum}&size=${size}&status=${status}`
    ).then((response) => {
      if (response.status === 200) {
        setOrderList([...response.data]);
      }
    });
  }

  function getProd(id, ammount, unitPrice) {
    getWithAuth(`/product/search/${id}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        let prod = response.data;
        prod.quantity = ammount;
        prod.price = unitPrice;
        setProdList((oldArr) => [...oldArr, prod]);
      }
    });
  }
  function getProdList(index) {
    setProdList([]);
    orderList[index].orderDetails.map((detail) =>
      getProd(detail.productId, detail.ammount, detail.unitPrice)
    );
  }
  function handleProductList(index) {
    getProdList(index);
    // setStatusListProd(true);
    toggle();
  }
  // function handleDelete(e, id) {
  //   if (e === "OK") {
  //     del(`/order/${id}`)
  //       .then((response) => {
  //         if (response.status === 200) {
  //           toast.success("Delete successfully!!!", {
  //             position: toast.POSITION.TOP_RIGHT,
  //             autoClose: 3000,
  //           });
  //           getListOrder();
  //         }
  //       })
  //       .catch((error) => {
  //         toast.error(error, {
  //           position: toast.POSITION.TOP_RIGHT,
  //           autoClose: 3000,
  //         });
  //       });
  //   }
  // }
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
  function setStatusChoice(id, e) {
    const status = e.target.value;
    if (status < 4) {
      put(`/order/updateStatus/${id}?status=${status}`, "")
        .then((response) => {
          if (response.status === 200) {
            toast.success(`Update order status success`, {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            getListOrder();
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
    } else {
      setShowNote(true);
      setIDNote(id);
      setStatusNote(status);
    }
  }

  function getSearchOrderList(keyword, status) {
    setOrderList([]);
    keyword = keyword.trim().split(/ +/).join(' ');
    getWithAuth(
      `/order/search/OrderByCustomer?keyword=${keyword}&status=${status}`
    ).then((response) => {
      if (response.status === 200) {
        setOrderList([...response.data]);
        setShowPage(false);
      }
    });
  }
  async function handleSearchChange(e) {
    setSearch(e.target.value);
    if (e.target.value.trim().length > 0) {
      let status = choice;
      if (choice == 0) status = "";
      getSearchOrderList(e.target.value.trim(), status);
    } else {
      getListOrder();
      setShowPage(true);
    }
  }
  function setTextStatusChoice() {
    let statusText;
    let stateClass;
    switch (choice) {
      case 0:
        statusText = "All";
        stateClass = "";
        break;
      case 1:
        statusText = "In Process";
        stateClass = "statusColorChoice-InProcess";
        break;
      case 2:
        statusText = "Delivering";
        stateClass = "statusColorChoice-Delivery";
        break;
      case 3:
        statusText = "Complete";
        stateClass = "statusColorChoice-Complete";
        break;
      case 4:
        statusText = "Canceled";
        stateClass = "statusColorChoice-Canceled";
        break;
    }
    return (
      <Label className={`statusChoiceText-Order ${stateClass}`}>
        {statusText}
      </Label>
    );
  }

  function dropDownStatus(id, state, note) {
    let stateClass;
    switch (state) {
      case 1:
        stateClass = "statusColor-InProcess";
        break;
      case 2:
        stateClass = "statusColor-Delivery";
        break;
      case 3:
        stateClass = "statusColor-Complete";
        break;
      case 4:
        stateClass = "statusColor-Canceled";
        break;
    }
    let stateText;
    if (state === 3) stateText = "Complete";
    else if (state === 4) stateText = "Cancel";
    return state >= 3 ? (
      <>
        <option id={`select_tooltip_${id}`} className={stateClass}>
          {stateText}
        </option>
        {note ? (
          <UncontrolledTooltip placement="top" target={`select_tooltip_${id}`}>
            {note}
          </UncontrolledTooltip>
        ) : null}
      </>
    ) : (
      <>
        <select
          id={`select_tooltip_${id}`}
          value={state}
          selected={state}
          className={stateClass}
          onChange={(e) => setStatusChoice(id, e)}
        >
          {state == 1 ? (
            <>
              <option value={1}>In process</option>
              <option value={2}>Delivering</option>
              <option value={4}>Canceled</option>
            </>
          ) : (
            <>
              {role.includes("ADMIN") ? (
                <option value={1}>In process</option>
              ) : null}
              <option value={2}>Delivering</option>
              <option value={3}>Completed</option>
              <option value={4}>Canceled</option>
            </>
          )}
          {/* <option value={1}>In process</option>
        <option value={2}>Delivering</option>
        <option value={3}>Completed</option>
        <option value={4}>Canceled</option> */}
        </select>
        {note ? (
          <UncontrolledTooltip placement="top" target={`select_tooltip_${id}`}>
            {note}
          </UncontrolledTooltip>
        ) : null}
      </>
    );
  }
  return (
    <>
      <h2 className="title-OrderAdmin">ORDER MANAGER</h2>
      <Row>
        <Col className="col-7 statusChoice-Order">
          <Dropdown isOpen={dropdownOpenChoiceState} toggle={toggleChoiceState}>
            <DropdownToggle caret>Order Status</DropdownToggle>
            <DropdownMenu>
              <DropdownItem onClick={() => setChoice(0)}>All</DropdownItem>
              <DropdownItem divider />
              <DropdownItem onClick={() => setChoice(1)}>
                In process
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem onClick={() => setChoice(2)}>
                Delivering
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem onClick={() => setChoice(3)}>
                Completed
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem onClick={() => setChoice(4)}>Canceled</DropdownItem>
            </DropdownMenu>
          </Dropdown>{" "}
          {setTextStatusChoice()}
        </Col>
        <Col>
          <Input
            type="text"
            name="name"
            id="name"
            placeholder="Search Order by Customer's name"
            value={search}
            onChange={(e) => handleSearchChange(e)}
          />
        </Col>
      </Row>
      <Table bordered className="tableOrder">
        <thead>
          <tr>
            <th className="titleTable-OrderAdmin">ID</th>
            {/* <th className="titleTable-OrderAdmin">CUSTOMER EMAIL</th> */}
            <th className="titleTable-OrderAdmin">CUSTOMER NAME</th>
            <th className="titleTable-OrderAdmin">BOUGHT AT</th>
            <th className="titleTable-OrderAdmin">TOTAL</th>
            <th className="titleTable-OrderAdmin">ADDRESS</th>
            <th className="titleTable-OrderAdmin">PAYMENT</th>
            <th className="titleTable-OrderAdmin">PRODUCT</th>
            <th className="titleTable-OrderAdmin">STATUS</th>
            <th className="titleTable-OrderAdmin">EMPLOYEE APPROVED</th>
            {/* <th className="titleTable-OrderAdmin">NOTE</th> */}
            <th></th>
          </tr>
        </thead>
        <tbody>
          {orderList.map((order, index) => (
            <tr key={order.id}>
              <th scope="row">{order.id}</th>
              {/* <td>{order.customersEmail}</td> */}
              <td>{order.customersName}</td>
              <td>
                {format(new Date(order.timebought), "dd/MM/yyyy HH:mm:ss")}
              </td>
              <td>{numberFormat(order.totalCost)}</td>
              <td>{order.address}</td>
              <td
                className={
                  order.payment
                    ? "paymentStatusTrue_UserOrderHistory"
                    : "paymentStatusFalse_UserOrderHistory"
                }
              >
                {order.payment ? "Paid" : "Unpaid"}
              </td>

              <td>
                {" "}
                <Button color="link" onClick={() => handleProductList(index)}>
                  List Product
                </Button>
              </td>
              <td>{dropDownStatus(order.id, order.status, order.note)}</td>
              <td>{order.employeeApprovedName}</td>
              <td><PrintOrder id={order.id} order={order}/></td>
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
          Product Ordered
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
      <ModalAddNote
        modal={showNote}
        id={idNote}
        status={statusNote}
        onAddNote={(e) => getListOrder()}
        onChangeShowNote={(e) => setShowNote(e)}
      />
    </>
  );
}
