import React, { useState, useEffect, useRef } from "react";
import { get, getWithAuth, post } from "../../../Utils/httpHelper";
import { getCookie } from "../../../Utils/Cookie";

import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";
import "./Order.css";

export default function Index() {
  const [prodList, setProdList] = useState([]);
  const [user, setUser] = useState(Object);
  const [quantity, setQuantity] = useState([]);
  let totalCost = useRef(0);

  useEffect(() => {
    getProdList();
    console.log("HELLO");
    getUser();
    countTotal();
  }, []);
  function getUser() {
    const emailUser = getCookie("email");
    getWithAuth(`/persons/search/email/${emailUser}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        setUser(response.data);
      }
    });
  }
  async function getProd(id) {
    await get(`/public/product/search/${id}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        let prod = response.data;
        prod.quantity = 1;
        setProdList((oldArr) => [...oldArr, prod]);

        // setBase64([...base64, `data:image/jpeg;base64,${response.data.photo}`]);
      }
    });
  }
  function countTotal() {
    totalCost.current = 0;
    prodList.forEach((prod) => {
      totalCost.current = totalCost.current + prod.quantity * prod.price;
    });
  }

  function getProdList() {
    const listId = getCookie("cart").split(" ");
    listId.map((id) => getProd(id));
  }
  function toArr(){
      let arr = [];
    prodList.forEach((prod) => {
       let item ={"productId":prod.id,"ammount" : prod.quantity};
       arr = [...arr,item];
      });
      console.log(arr);
      return arr;
  }
  function handleSubmit(e) {
    e.preventDefault();
    // console.log(prodList[0].quantity);
    countTotal();

    // const {orderDetails} = toArr();
  
    // console.log(objStr);
    const body = JSON.stringify({
        customersEmail : e.target.email.value,
        totalCost : totalCost.current,
        status : false,
        address: e.target.address.value,
        orderDetails: toArr()
    });
    console.log(body);
    post("/order", body)
    .then((response) => {
      console.log(response.data);
      alert("ORDER SUCCESS");
    })
    .catch((error) => console.log(error));

  }
  function handleProdFieldChange(e, key, index) {
    let list = [...prodList];
    let prod = { ...list[index] };
    prod.quantity = e.target.value;
    list[index] = prod;

    setProdList(list);
    countTotal();
    // setProdList({ [key]: e.target.value });
    // prodList[index].quantity = e.target.value;
  }
  function handleUserFieldChange(e, key) {
    setUser({ [key]: e.target.value });
  }

  return (
    <div className="login-form">
      <h2 className="head-login">ORDER</h2>
      <Form onSubmit={(e) => handleSubmit(e)}>
        <FormGroup>
          <Row className="login">
            <Col className="col-1">
              <Label for="exampleEmail">Email</Label>
            </Col>
            <Col className="col-4">
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
                required
              />
            </Col>
          </Row>
        </FormGroup>
        <h3>{}</h3>
        <FormGroup>
          {prodList.map((prod, index) => (
            <Row className="prod-form" key={prod.id}>
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
                    <Label for="exampleQuantity">Quantity</Label>
                  </Col>
                  <Col className="col-2">
                    <Input
                      type="number"
                      name={`quantity${index}`}
                      id="exampleQuantity"
                      min="1"
                      required
                      value={prod.quantity}
                      className="quantity-prod"
                      onChange={(e) =>
                        handleProdFieldChange(e, "quantity", index)
                      }
                    />
                  </Col>
                </Row>
              </Col>
            </Row>
          ))}
        </FormGroup>
        <Button outline color="info" type="submit">
          Submit
        </Button>
      </Form>
    </div>
  );
}
