import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Row, Col } from "reactstrap";
import { Link } from "react-router-dom";
import img from "../../assets/img/test.jpg";
import { get } from "../../Utils/httpHelper";
import { FaCartPlus } from "react-icons/fa";
import Review from "./Review/"
import "./ProductDetail.css";

export default function Index() {
  let { id } = useParams();
  const [prod, setProd] = useState(Object);
  
  useEffect(() => {
      async function getProduct(){
        await get(`/public/product/search/${id}`).then((response) => {
        if (response.status === 200) {
          console.log(response.data);
          setProd(response.data);
        }
      });
      } 
      getProduct()
  }, [id]);
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
            <div className="prod-btn">
            <Link to={`/prodDetail/${prod.id}`} className="addtoCard-btn">
                    <FaCartPlus/>  Add to Cart
                    </Link>
            <Link to={`/Ordering/${prod.id}`} className="buyNow-btn">
                    Buy Now
                    </Link>
                    </div>
          </div>
        </Col>
      </Row>
      <br/>
      <h4 className="descrip-prod">{prod.description}</h4>
      <div>
        <Review id={prod.id}/>
      </div>
    </div>
  );
}
