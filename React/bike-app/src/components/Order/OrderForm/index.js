import React, { useState, useEffect, useRef } from "react";
import { get, getWithAuth, post } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { getCookie } from "../../../Utils/Cookie";
import { useHistory } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";
import ModalDelte from "../../Admin/ModalDeleteConfirm";
import "./Order.css";

toast.configure();
export default function Index() {
  const history = useHistory();
  const [prodList, setProdList] = useState([]);
  const [user, setUser] = useState(Object);
  const [nameError, setNameError] = useState("");
  const [addressError, setAddressError] = useState("");
  const [total, setTotal] = useState(0);

  useEffect(() => {
    getProdList();
    getUser();
    setNameError("");
    setAddressError("");
  }, []);
  useEffect(() => {
    // setCartCookie();
    getTotalPrice(prodList);
  }, [prodList]);

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
      let item = { productId: prod.id, ammount: prod.quantity };
      arr = [...arr, item];
    });
    console.log(arr);
    return arr;
  }
  function handleSubmit(e) {
    e.preventDefault();
    // console.log(prodList[0].quantity);

    // const {orderDetails} = toArr();

    // console.log(objStr);
    if (nameError == "" && addressError == "") {
      const body = JSON.stringify({
        customersEmail: e.target.email.value,
        totalCost: total,
        status: 0,
        address: e.target.address.value,
        orderDetails: toArr(),
      });
      console.log(body);
      post("/order", body)
        .then((response) => {
          if (response.status === 200) {
            toast.success(
              `Other Success! A confirmation email will send to your email`,
              {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 3000,
              }
            );
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
          }
          console.log(error.response.status);
        });
      document.cookie = `cart=; max-age=86400; path=/;`;
      history.push("/");
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
    let list = [...prodList];
    let prod = { ...list[index] };
    prod.quantity = e.target.value;
    list[index] = prod;

    await setProdList(list);
    setCartCookie(list);
    getTotalPrice(list);
    // setProdList({ [key]: e.target.value });
    // prodList[index].quantity = e.target.value;
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
  return (
    <div className="login-form">
      <h2 className="head-login">ORDER</h2>
      <Form onSubmit={(e) => handleSubmit(e)}>
        <FormGroup>
          <Row className="login">
            <Col className="col-1">
              <Label for="nameExample">Name</Label>
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
              <Label for="exampleEmail">Email</Label>
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
              <Label for="exampleAddress">Address</Label>
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
        <Row className="order">
          <Col className="col-7 priceTotal">
            <h3>Total :: </h3>{" "}
            <h3 className="priceNumTotal"> {numberFormat(total)}</h3>
          </Col>
          <Col className="btnDelProd">
            <Button outline color="info" type="submit">
              Order
            </Button>
          </Col>
        </Row>
        <FormGroup>
          {prodList.map((prod, index) => (
            <Row className="prod-form-orderUser" key={prod.id}>
              <Col className="col-3">
                <img
                  src={`data:image/jpeg;base64,${prod.photo}`}
                  className="img-order"
                />
              </Col>
              <Col className="info-prod-order">
                <h4>{prod.name}</h4>
                <p>MODEL :: {prod.id}</p>
                <Row>
                  <Col className="col-1">
                    <Label for="exampleQuantity">Qty</Label>
                  </Col>
                  <Col className="col-3">
                    <Input
                      type="number"
                      name={`quantity${index}`}
                      id="exampleQuantity"
                      min="1"
                      required
                      value={prod.quantity}
                      onChange={(e) =>
                        handleProdFieldChange(e, "quantity", index)
                      }
                    />
                  </Col>
                  <Col className="btnDelProd">
                    <ModalDelte onChoice={(e) => handleDelete(e, index)} />
                  </Col>

                  <Row className="priceCart">
                    <Col className="col-3 priceTitle">
                      <h3>Price </h3>
                    </Col>
                    <Col>
                      <h3 for="exampleQuantity" className="priceNum">
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
    </div>
  );
}
