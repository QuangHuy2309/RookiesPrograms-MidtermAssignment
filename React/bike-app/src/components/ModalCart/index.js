import React, { useState, useEffect } from "react";
import "./ModalCart.css";
import { useHistory, Link } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { getCookie } from "../../Utils/Cookie";
import { get } from "../../Utils/httpHelper";
import { TiShoppingCart } from "react-icons/ti";
import ModalDelte from "../Admin/ModalDeleteConfirm";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import {
  Button,
  Popover,
  PopoverBody,
  Col,
  Row,
  Label,
  Input,
  Badge,
} from "reactstrap";

toast.configure();
const ModalCart = (props) => {
  const history = useHistory();
  const [popoverOpen, setPopoverOpen] = useState(false);
  const [prodList, setProdList] = useState([]);
  const [total, setTotal] = useState(0);
  const [cartSize, setCartSize] = useState(0);
  // let total = useRef(0);

  const toggle = () => {
    let status = getCookie("status");
    if (status === "true") setPopoverOpen(!popoverOpen);
  };

  useEffect(async () => {
    if (popoverOpen) {
      await getProdList();
    }
  }, [popoverOpen]);

  useEffect(() => {
    getTotalPrice(prodList);
  }, [prodList]);

  async function setCartCookie(list) {
    let strListProd = "";

    list.map((prod) => {
      strListProd = strListProd.concat(`${prod.id}#$${prod.quantity} `);
    });
    document.cookie = `cart=${strListProd}; max-age=86400; path=/;`;
  }
  async function getTotalPrice(list) {
    let totalCost = 0;
    list.map((prod) => {
      totalCost += prod.price * prod.quantity;
    });
    setTotal(totalCost);
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
  async function getProdList() {
    let cartCookie = getCookie("cart");
    if (cartCookie.trim().length !== 0) {
      // history.push(`/Ordering`);
      setProdList([]);
      const listprod = getCookie("cart").split(" ");
      await listprod.map((prod) => getProd(prod));
    } else {
      toast.info("🦄 Cart is empty. Fill it in righ away", {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
      });
    }
  }
  async function getCartSize() {
    let cartCookie = getCookie("cart");
    if (cartCookie.trim().length !== 0) {
      const list = getCookie("cart").split(" ");
      setCartSize(list.length);
    }
  }
  async function handleProdFieldChange(e, key, index) {
    // if (e.target.value > 0 && e.target.value <= prodList[index].description ) {
      let list = [...prodList];
      let prod = { ...list[index] };
      prod.quantity = e.target.value;
      list[index] = prod;

      await setProdList(list);
      setCartCookie(list);
      getTotalPrice(list);
    // }
  }
  async function handleDelete(e, index) {
    if (e === "OK") {
      let list = [...prodList];
      list.splice(index, 1);
      await setProdList(list);
      setCartCookie(list);
    }
  }
  function handleOrder() {
    let cartCookie = getCookie("cart");
    if (cartCookie.trim() != "") history.push(`/Ordering`);
  }
  return (
    <div>
      <Button id="Popover1" type="button" color="link">
        <TiShoppingCart size={40} className="cartModal-Btn" />
      </Button>
      <Popover
        placement="left-end"
        isOpen={popoverOpen}
        target="Popover1"
        toggle={toggle}
      >
        <PopoverBody className="popoverCart">
          {prodList.map((prod, index) => (
            <div key={index}>
              <Row className="cart-form" key={prod.id}>
                <Col className="col-4">
                  <Link
                    to={`/prodDetail/${prod.id}`}
                    style={{
                      textDecoration: "none",
                      color: "black",
                    }}
                  >
                    <img
                      src={`data:image/jpeg;base64,${prod.photo}`}
                      className="img-cart"
                    />
                  </Link>
                </Col>
                <Col className="infoCart">
                  <Link
                    to={`/prodDetail/${prod.id}`}
                    style={{
                      textDecoration: "none",
                      color: "black",
                    }}
                  >
                    <h6>{prod.name}</h6>
                  </Link>
                  <Row>
                    <Col className="col-7">
                      <Label for="exampleQuantity">Qty</Label>
                    </Col>
                    <Row>
                      <Col className="col-7">
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
                      <Col>
                        <ModalDelte onChoice={(e) => handleDelete(e, index)} />
                      </Col>
                      <Row className="priceCart">
                        <Col className="col-3">
                          <Label>Price </Label>
                        </Col>
                        <Col>
                          <Label for="exampleQuantity" className="priceNum">
                            {" "}
                            {numberFormat(prod.quantity * prod.price)}
                          </Label>
                        </Col>
                      </Row>
                    </Row>
                  </Row>
                </Col>
              </Row>
              <hr />
            </div>
          ))}
          <Row>
            <Col>
              <Button outline color="primary" onClick={() => handleOrder()}>
                Order
              </Button>
            </Col>
            <Col class="totalCost">
              <p>{numberFormat(total)}</p>
            </Col>
          </Row>
        </PopoverBody>
      </Popover>
    </div>
  );
};

export default ModalCart;
