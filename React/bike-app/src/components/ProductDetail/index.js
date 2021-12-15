import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Row, Col, Button } from "reactstrap";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import { useHistory } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { FaCartPlus } from "react-icons/fa";
import Review from "./Review/";
import "./ProductDetail.css";
import { getCookie } from "../../Utils/Cookie";
import { isLogin } from "../../Utils/Auth";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Navbar from "../Navbar/";
import { RatingView } from "react-simple-star-rating";

toast.configure();
export default function Index() {
  const history = useHistory();
  let { id } = useParams();
  const [prod, setProd] = useState(Object);
  const [rateAvg, setRateAvg] = useState(0);

  useEffect(() => {
    getProduct();
    getAvgRate();
  }, [id]);

  async function getProduct() {
    await get(`/public/product/search/${id}`)
      .then((response) => {
        if (response.status === 200) {
          setProd(response.data);
        }
      })
      .catch((error) => {
        if (error.response.status === 404) {
          history.push("/404");
        }
      });
  }
  function addProductIdToCookie(type) {
    let cartCookie = getCookie("cart");
    const roleCookie = getCookie("role");
    let checkRole = roleCookie.includes("ROLE_USER");
    let checkCart = cartCookie.trim().includes(id);
    if (checkRole) {
      if (!checkCart) {
        cartCookie = cartCookie.concat(` ${id}`);
        cartCookie = cartCookie.trim();
        document.cookie = `cart=${cartCookie}#$1; max-age=86400; path=/;`;
        toast.info("Product have been add to cart", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
      } else {
        if (type === "Add") {
          toast("This product is already in your cart", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
        }
      }
    }
  }
  function handleAddCart() {
    if (isLogin()) addProductIdToCookie("Add");
    else {
      toast.warning("You need to login first!!!", {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
      });
    }
  }
  async function getAvgRate() {
    get(`/public/product/rateAvgProd/${id}`).then((response) => {
      if (response.status === 200) {
        setRateAvg(Math.round(response.data * 10) / 10);
      }
    });
  }
  function handleReviewChange(e) {
    if (e) {
      getAvgRate();
    }
  }
  function handleOrder() {
    if (isLogin()) {
      const roleCookie = getCookie("role");
      let checkRole = roleCookie.includes("ROLE_USER");
      if (checkRole) {
        addProductIdToCookie("Order");
        history.push(`/Ordering`);
      }
    } else {
      toast.warning("You need to login first!!!", {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
      });
    }
  }

  return (
    <>
      <Navbar />
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
            <h3 className="name-prodDetail">{prod.name}</h3>
            <h5>
              {rateAvg}{" "}
              <RatingView
                ratingValue={Math.round(rateAvg)}
                size={20}
                className=""
              />
            </h5>
            <h2 className="price-prod">{numberFormat(prod.price)}</h2>
            <h5>Model :: {prod.id} </h5>
            <h5>Qty :: {prod.quantity} </h5>
            <h5>Brand :: {prod.brand}</h5>
            <div className="prod-btn">
              <Button
                outline
                color="info"
                className="addtoCard-btn"
                onClick={() => handleAddCart()}
                disabled={prod.quantity === 0 ? "true" : ""}
              >
                <FaCartPlus />{" "}
                {prod.quantity === 0 ? "Out of stock " : "Add to Cart"}
              </Button>
              <Button
                color="info"
                disabled={prod.quantity === 0 ? "true" : ""}
                onClick={() => handleOrder()}
                className="buyNow-btn"
              >
                {prod.quantity === 0 ? "Out of stock " : "Buy Now"}
              </Button>
            </div>
          </div>
        </Col>
      </Row>
      <br />
      <h4 className="descrip-prod">{prod.description}</h4>
      <div>
        <Review id={prod.id} onReviewChange={(e) => handleReviewChange(e)} />
      </div>
    </>
  );
}
