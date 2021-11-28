import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import Product from "./Product";
import "./Home.css";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import Carousel from "react-elastic-carousel";
import {
  Card,
  CardImg,
  CardBody,
  CardTitle,
  CardSubtitle,
  Button,
  Row,
  Col,
} from "reactstrap";

export default function Home1() {
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const size = 4;
  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    get(`/public/product/hotProd/${size}`).then((response) => {
      if (response.status === 200) {
        setProdList([...response.data]);
      }
    });
  }, []);

  return (
    <div className="mt-4">
      {/* <div className="title-bike-type">
        <h2 className="hotProd">
          🔥
          <h2 className="cateName-Home Nav-HomeBtn"> HOT PRODUCT</h2>
          🔥
        </h2>
      </div>
      <Row className="mb-4">
        {prodList.map((prod) => (
          <Col key={prod.id} className="col-3">
            <Link
              to={`/prodDetail/${prod.id}`}
              style={{ textDecoration: "none" }}
            >
              <Card>
                <CardImg
                  top
                  width="100%"
                  src={`data:image/jpeg;base64,${prod.photo}`}
                  alt="Card image cap"
                />
                <CardBody>
                  <CardTitle tag="h5" className="card-name-prodHome">
                    {prod.name}
                  </CardTitle>
                  <div className="card-info">
                    <CardSubtitle tag="h4" className="mb-2 card-price">
                      {numberFormat(prod.price)}
                    </CardSubtitle>
                    <Link to={`/prodDetail/${prod.id}`} className="card-btn">
                      Buy Now
                    </Link>
                  </div>
                </CardBody>
              </Card>
            </Link>
          </Col>
        ))}
      </Row> */}
      <Carousel>
      {prodList.map((prod) => (
        <img
        src={`data:image/jpeg;base64,${prod.photo}`}
        className="img-cart"
        alt="Hot product"
      />
         ))}
      </Carousel>
      {cateList.map((cate) => {
        return (
          <div key={cate.id}>
            <div className="title-bike-type">
              <Link
                to={`/Bike/${cate.id}`}
                style={{ textDecoration: "none", color: "black" }}
              >
                <h2 className="cateName-Home Nav-HomeBtn">{cate.name}</h2>
              </Link>
            </div>
            <Product id={cate.id} />
          </div>
        );
      })}
    </div>
  );
}
