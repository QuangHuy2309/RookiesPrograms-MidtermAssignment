import React, { useState, useEffect, useRef } from "react";
import { get, getWithAuth, post } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { getCookie } from "../../../Utils/Cookie";
import { useHistory, Link } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import {
  Button,
  Form,
  FormGroup,
  Label,
  Input,
  Row,
  Col,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "reactstrap";
import ModalDelte from "../../Admin/ModalDeleteConfirm";
import PayPal from "../Payment/PayPal.js";
import "./Order.css";

toast.configure();
export default function Index() {
  const history = useHistory();
  const [prodList, setProdList] = useState([]);
  const [user, setUser] = useState(Object);
  const [nameError, setNameError] = useState("");
  const [addressError, setAddressError] = useState("");
  const [total, setTotal] = useState(0);
  const [checkout, setCheckOut] = useState(false);
  const [modal, setModal] = useState(false);
  const paypal = useRef();
  const order_id = useRef();
  const toggle = () => setModal(!modal);

  useEffect(() => {
    getProdList();
    getUser();
    setNameError("");
    setAddressError("");
  }, []);
  useEffect(() => {
    // setCartCookie();
    setCheckOut(false);
    getTotalPrice(prodList);
  }, [prodList]);
  useEffect(() => {
    if (total > 0) setCheckOut(true);
  }, [total]);

  function getUser() {
    const emailUser = getCookie("email");
    getWithAuth(`/persons/search/email/${emailUser}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        setUser(response.data);
      }
    });
  }
  async function getProd(prod) {
    let prodDetail = prod.split("#$");
    await get(`/public/product/search/${prodDetail[0]}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        let prod = response.data;
        prod.description = prod.quantity;
        prod.quantity = prodDetail[1];
        setProdList((oldArr) => [...oldArr, prod]);
      }
    });
  }
  async function getTotalPrice(list) {
    let totalCost = 0;
    list.map((prod) => {
      totalCost += prod.price * prod.quantity;
    });
    setTotal(totalCost);
  }

  async function getProdList() {
    let cartCookie = getCookie("cart");
    if (cartCookie.trim().length !== 0) {
      // history.push(`/Ordering`);
      setProdList([]);
      const listprod = getCookie("cart").split(" ");
      await listprod.map((prod) => getProd(prod));
    }
  }

  function toArr() {
    let arr = [];
    prodList.forEach((prod) => {
      let item = {
        productId: prod.id,
        ammount: prod.quantity,
        unitPrice: prod.price,
      };
      arr = [...arr, item];
    });
    console.log(arr);
    return arr;
  }
  function handleSubmit(e) {
    e.preventDefault();
    if (nameError == "" && addressError == "") {
      const body = JSON.stringify({
        customersEmail: e.target.email.value,
        isPay: false,
        status: 1,
        address: e.target.address.value,
        note: e.target.note.value,
        orderDetails: toArr(),
      });
      console.log(body);
      post("/order", body)
        .then((response) => {
          if (response.status === 200) {
            toast.success(
              `Order Success! A confirmation email will send to your email`,
              {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 3000,
              }
            );
            order_id.current = response.data.id;
            document.cookie = `cart=; max-age=86400; path=/;`;
            toggle();
          }
        })
        .catch((error) => {
          if (error.response.status === 400) {
            toast.error(
              `Sorry, we dont have enought for your quantity of product.`,
              {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 3000,
              }
            );
            history.push("/");
          }
          console.log(error.response.status);
        });      
    }
  }
  async function setCartCookie(list) {
    let strListProd = "";
    list.map((prod) => {
      strListProd = strListProd.concat(`${prod.id}#$${prod.quantity} `);
    });
    document.cookie = `cart=${strListProd}; max-age=86400; path=/;`;
  }

  async function handleProdFieldChange(e, key, index) {
    // if (e.target.value > 0 && e.target.value <= prodList[index].description) {
      let list = [...prodList];
      let prod = { ...list[index] };
      prod.quantity = e.target.value;
      list[index] = prod;

      await setProdList(list);
      setCartCookie(list);
      setCheckOut(false);
      getTotalPrice(list);
    // }
  }
  function handleUserFieldChange(e, key) {
    setUser({ [key]: e.target.value });
    if (key === "fullname") {
      if (e.target.value.trim() == "") {
        setNameError("Name must not blank");
      } else {
        setNameError("");
      }
    } else if (key === "address") {
      if (e.target.value.trim() == "") {
        setAddressError("Address must not blank");
      } else {
        setAddressError("");
      }
    }
  }
  async function handleDelete(e, index) {
    if (e === "OK") {
      let list = [...prodList];
      list.splice(index, 1);
      await setProdList(list);
      setCartCookie(list);
    }
  }
  async function paypalBody() {
    if (prodList.length !== "") {
      let arr = [];
      prodList.forEach((prod) => {
        let amount_sample = { currency_code: "USD", value: prod.price };
        let item = { description: prod.name, ammount: amount_sample };
        arr = [...arr, item];
      });
      console.log(`ez: ${arr}`);
      return arr.toString();
    }
  }
  function createOrder(data, actions) {
    let arr = [];
    prodList.forEach((prod) => {
      let amount_sample = { value: prod.price };
      let item = { description: prod.name, ammount: amount_sample };
      arr = [...arr, item];
    });
    console.log(`paypal: ${arr}`);
    return actions.order.create({
      purchase_units: arr,
    });
  }
  async function payOnDeli() {
    toggle();
    history.push("/");
  }

  return (
    <div className="login-form mt-5">
      <h2 className="head-Order">ORDER</h2>
      <Form onSubmit={(e) => handleSubmit(e)}>
        <FormGroup>
          <Row className="login">
            <Col className="col-1 priceTitle">
              <Label for="nameExample" className="labelText-Order py-auto">
                Name
              </Label>
            </Col>
            <Col className="col-4">
              <Input
                type="text"
                name="name"
                id="nameExample"
                required="required"
                value={user.fullname}
                onChange={(e) => handleUserFieldChange(e, "fullname")}
                className="input-login"
              />
              <div style={{ color: "red" }}>{nameError}</div>
            </Col>
            <Col className="col-1"></Col>
            <Col className="col-1">
              <Label for="exampleEmail" className="labelText-Order py-auto">
                Email
              </Label>
            </Col>
            <Col className="col">
              <Input
                type="email"
                name="email"
                id="emailExample"
                required="required"
                value={user.email}
                onChange={(e) => handleUserFieldChange(e, "email")}
                className="input-login"
              />
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className="login">
            <Col className="col-1">
              <Label for="exampleAddress" className="labelText-Order py-auto">
                Address
              </Label>
            </Col>
            <Col>
              <Input
                type="text"
                name="address"
                id="exampleAddress"
                value={user.address}
                onChange={(e) => handleUserFieldChange(e, "address")}
                required="required"
              />
              <div style={{ color: "red" }}>{addressError}</div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className="login">
            <Col className="col-1">
              <Label for="exampleNote" className="labelText-Order py-auto">
                Note
              </Label>
            </Col>
            <Col>
              <Input
                type="textarea"
                name="note"
                id="exampleNote"
              />
            </Col>
          </Row>
        </FormGroup>
        <Row className="order">
          <Col className="col-7 priceTotal-Order">
            <h3 className="totalText-Order">Total :: </h3>{" "}
            <h3 className="priceNumTotal"> {numberFormat(total)}</h3>
          </Col>
          <Col className="btnDelProd">
            <Button
              outline
              color="info"
              type="submit"
              className="btnOrder-Order"
            >
              Place an Order
            </Button>
            {/* {checkout ? <PayPal total={total} /> : null} */}
          </Col>
        </Row>
        <FormGroup>
          {prodList.map((prod, index) => (
            <Row className="prod-form-orderUser" key={prod.id}>
              <Col className="col-3">
                <Link
                  to={`/prodDetail/${prod.id}`}
                  style={{
                    textDecoration: "none",
                    color: "black",
                  }}
                >
                  <img
                    src={`data:image/jpeg;base64,${prod.photo}`}
                    className="img-order"
                  />
                </Link>
              </Col>
              <Col className="info-prod-order">
                <Link
                  to={`/prodDetail/${prod.id}`}
                  style={{
                    textDecoration: "none",
                    color: "black",
                  }}
                >
                  <h5>{prod.name}</h5>
                </Link>
                <p className="modalProd-Order">MODEL :: {prod.id}</p>
                <Row>
                  <Col className="col-1">
                    <Label for="exampleQuantity" className="pt-2">
                      Qty
                    </Label>
                  </Col>
                  <Col className="col-3">
                    <Input
                      type="number"
                      name={`quantity${index}`}
                      id="exampleQuantity"
                      min="1"
                      required
                      value={prod.quantity}
                      max={prod.description}
                      onChange={(e) =>
                        handleProdFieldChange(e, "quantity", index)
                      }
                    />
                  </Col>
                  <Col className="btnDelProd">
                    <ModalDelte onChoice={(e) => handleDelete(e, index)} />
                  </Col>

                  <Row className="priceCart mt-3">
                    <Col className="col-3 priceTitle">
                      <h3 className="priceText-OrderForm">Price </h3>
                    </Col>
                    <Col>
                      <h3 className="priceNum-OrderForm">
                        {" "}
                        {numberFormat(prod.quantity * prod.price)}
                      </h3>
                    </Col>
                  </Row>
                </Row>
              </Col>
            </Row>
          ))}
        </FormGroup>
      </Form>
      {/* <Button onclick={()=>paypalClick()}>paypal click</Button> */}
      {/* <div ref={paypal} /> */}
      <Modal isOpen={modal} toggle={toggle} className="">
        <ModalHeader toggle={toggle} className="title-AddProductAdmin">
          Payment
        </ModalHeader>
        <ModalBody>
          <button color="info" className="payOndeli_Order" onClick={payOnDeli}>Payment on delivery</button>
          <PayPal total={total} order_id={order_id.current}/>
        </ModalBody>
      </Modal>
    </div>
  );
}
