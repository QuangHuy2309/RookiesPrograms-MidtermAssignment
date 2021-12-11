import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import Product from "./Product";
import "./Home.css";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import Carousel from "react-multi-carousel";
import "react-multi-carousel/lib/styles.css";
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
  const size = 7;
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
  const responsive = {
    superLargeDesktop: {
      // the naming can be any, depends on you.
      breakpoint: { max: 4000, min: 3000 },
      items: 5,
    },
    desktop: {
      breakpoint: { max: 3000, min: 1024 },
      items: 5,
    },
    tablet: {
      breakpoint: { max: 1024, min: 464 },
      items: 2,
    },
    mobile: {
      breakpoint: { max: 464, min: 0 },
      items: 1,
    },
  };
  return (
    <div className="">
      <h2 className="hotProd">
        🔥
        <h2 className="cateName-Home Nav-HomeBtn"> HOT PRODUCT</h2>
        🔥
      </h2>
      <Carousel
        draggable={true}
        infinite={true}
        showDots={true}
        responsive={responsive}
        autoPlay={true}
        autoPlaySpeed={2000}
        className="carouselHome"
      >
        {prodList.map((prod) => (
          <Link
            to={`/prodDetail/${prod.id}`}
            style={{ textDecoration: "none" }}
          >
            <img
              src={`data:image/jpeg;base64,${prod.photo}`}
              className="imgHotProd"
              alt="Hot product"
            />
          </Link>
        ))}
      </Carousel>
      <hr />
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
