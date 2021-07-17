import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Row, Col, Button } from "reactstrap";
import img from "../../assets/img/test.jpg";
import { get } from "../../Utils/httpHelper";
import "./ProductDetail.css";
export default function Index() {
  let { id } = useParams();
  const [prod, setProd] = useState(Object);
  useEffect(async() => {
     await get(`/public/product/search/${id}`).then((response) => {
        console.log(response.data);
      if (response.status === 200) {
        setProd(response.data);
      }
    });
  }, []);
  return (
    <div>
      <Row>
        <Col className="col-5">
          <img src={img} id="img-prod" alt="" />
        </Col>
        <Col>
          <div className="info-prod">
            <h2 className="name-prod">{prod.name}</h2>
            <h1 className="price-prod">
              {prod.price}
              <p className="currency-prod">VNĐ</p>
            </h1>
            <h5>MODEL :: {prod.id} </h5>
            <h5>Remain :: {prod.quantity} </h5>
            <h5>BRAND :: {prod.brand}</h5>
            {/* <h5>TYPE :: {prod.categories.name}</h5> */}
            <Button outline color="primary">Add to Cart</Button>
          </div>
        </Col>
      </Row>
      <br/>
      <h4 className="descrip-prod">{prod.description}</h4>
    </div>
  );
}
