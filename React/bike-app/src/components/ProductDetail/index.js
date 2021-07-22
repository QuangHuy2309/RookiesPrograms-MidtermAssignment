import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Row, Col, Button } from "reactstrap";
import { Link } from "react-router-dom";
import { useHistory } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { FaCartPlus } from "react-icons/fa";
import Review from "./Review/";
import "./ProductDetail.css";
import { getCookie } from "../../Utils/Cookie";

export default function Index() {
  const history = useHistory();
  let { id } = useParams();
  const [prod, setProd] = useState(Object);

  useEffect(() => {
    async function getProduct() {
      await get(`/public/product/search/${id}`).then((response) => {
        if (response.status === 200) {
          console.log(response.data);
          setProd(response.data);
        }
      });
    }
    getProduct();
  }, [id]);
  function addProductIdToCookie() {
    let cartCookie = getCookie("cart");
    let check = cartCookie.trim().includes(id);
    if (!check) {
      cartCookie = cartCookie.concat(` ${id}`);
      cartCookie = cartCookie.trim();
      document.cookie = `cart=${cartCookie}; max-age=86400; path=/;`;
    }
  }
  function handleAddCart() {
    addProductIdToCookie();
    history.push(`/`);
  }
  function handleOrder() {
    addProductIdToCookie();
    history.push(`/Ordering`);
  }
  
  return (
    <div>
      <Row>
        <Col className="col-5">
          <img
            src={`data:image/jpeg;base64,${prod.photo}`}
            id="img-prod"
            alt=""
          />
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
            <div className="prod-btn">
              <Button
                outline
                color="info"
                className="addtoCard-btn"
                onClick={() => handleAddCart()}
                disabled={prod.quantity === 0 ? "true" : ""}
              >
                <FaCartPlus /> {prod.quantity === 0 ? ("Out of stock ") :("Add to Cart")}
              </Button>
              <Button
                color="info"
                disabled={prod.quantity === 0 ? "true" : ""}
                onClick={() => handleOrder()}
                className="buyNow-btn"
              >
                {prod.quantity === 0 ? ("Out of stock ") :("Buy Now")} 
              </Button>
            </div>
          </div>
        </Col>
      </Row>
      <br />
      <h4 className="descrip-prod">{prod.description}</h4>
      <div>
        <Review id={prod.id} />
      </div>
    </div>
  );
}
