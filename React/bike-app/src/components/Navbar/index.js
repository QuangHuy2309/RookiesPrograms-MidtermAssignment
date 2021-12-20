import React, { useState, useEffect } from "react";
import "./Navbar.css";
import { Link, useHistory } from "react-router-dom";
import { get, getWithAuth } from "../../Utils/httpHelper";
import { getCookie } from "../../Utils/Cookie";
import { logOut } from "../../Utils/Auth";
import ModalConfirm from "../ModalConfirm";
import ModalChangePass from "../ModalChangePass";
import ModalEdtUser from "../Admin/UserPage/ModalEdtUser";
import ModalEdtAdmin from "../Admin/StaffPage/ModalEdtAdmin";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import Cart from "../ModalCart";
import {
  Collapse,
  Row,
  Col,
  Popover,
  PopoverBody,
  Label,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  Button,
  Input,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
} from "reactstrap";

toast.configure();
export default function Index(props) {
  const history = useHistory();
  const [isOpen, setIsOpen] = useState(false);
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [status, setStatus] = useState([getCookie("status")]);
  const [popoverOpen, setPopoverOpen] = useState(false);
  const id = getCookie("id");
  const role = getCookie("role");
  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    // setStatus(getCookie("status"));
  }, []);
  useEffect(() => {
    if (status) {
      // username = getCookie("username");
      // email = getCookie("email");
      // role = getCookie("role");
    }
  }, [status]);

  function handleLogOut(e) {
    if (e === "OK") {
      getWithAuth("/auth/logout")
        .then((response) => {
          if (response.status === 200) {
            console.log("User logged out successfully!");
            setStatus(false);
            history.push("/");
            logOut();
          }
        })
        .catch((error) => {
          console.log(error);
        });
    }
  }
  function handleOrder() {
    let cartCookie = getCookie("cart");
    if (cartCookie.trim().length !== 0) {
      history.push(`/Ordering`);
    } else {
      toast.info("🦄 Cart is empty. Fill it in righ away", {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
      });
    }
  }
  function handleUpdate(e) {
    // if (e) getListUser();
  }
  function isLogging() {
    const name = getCookie("username");
    // const email = getCookie("email");
    if (status && name !== "") {
      return (
        <Nav className="mr-auto" navbar>
          <UncontrolledDropdown nav inNavbar>
            <DropdownToggle
              nav
              caret
              style={{
                textDecoration: "none",
                color: "white",
                "font-family": "'Barlow Condensed', sans-serif;",
                "font-size": "1.45rem",
                "-webkit-text-stroke": "0.125px #FF5C58",
              }}
            >
              Hi, {name}
            </DropdownToggle>
            <DropdownMenu className="dropdownMenu-Nav">
              <DropdownItem>
              {(role.includes('USER')) ?
                <ModalEdtUser
                  isUser="true"
                  id={id}
                  onUserSide="onUserSide"
                  onEdit={(e) => handleUpdate(e)}
                /> :
                <ModalEdtAdmin
                  isUser="true"
                  id={id}
                  onUserSide="onUserSide"
                  onEdit={(e) => handleUpdate(e)}
                />
            }
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem>
                <ModalChangePass onUserSide="onUserSide" />
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem>
                {(role.includes('USER')) ?
                <Link
                  to={`/OrderHistory`}
                  style={{
                    textDecoration: "none",
                    color: "#1DB9C3",
                    "font-weight": "600",
                  }}
                >
                  Order History
                </Link> : 
                <Link
                to={`/Admin`}
                style={{
                  textDecoration: "none",
                  color: "#1DB9C3",
                  "font-weight": "600",
                }}
              >
                Page Management
              </Link>
                }
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem>
                <ModalConfirm
                  onUserSide="onUserSide"
                  onChoice={(e) => handleLogOut(e)}
                />
              </DropdownItem>
            </DropdownMenu>
          </UncontrolledDropdown>
        </Nav>
      );
    }
    return (
      <Link
        to={`/Login`}
        style={{
          textDecoration: "none",
          color: "white",
          "font-family": "'Barlow Condensed', sans-serif;",
          "font-size": "1.45rem",
          "-webkit-text-stroke": "0.125px #FF5C58",
        }}
        className="Nav-HomeBtn"
      >
        Login
      </Link>
    );
  }
  async function handleSearchChange(e) {
    let keyword = e.target.value.trim().split(/ +/).join(' ');
    if (keyword != "") {
      setProdList([]);
      get(`/public/product/search?keyword=${keyword.replace(/%20/g, " ")}`).then(
        (response) => {
          if (response.status === 200) {
            setProdList([...response.data]);
          }
        }
      );
      setPopoverOpen(true);
    }
    if (keyword == "") {
      setPopoverOpen(false);
      setProdList([]);
    }
  }
  const toggle = () => setIsOpen(!isOpen);
  const toggle_Search = () => {
    if (prodList.length > 1) setPopoverOpen(!popoverOpen);
  };
  return (
    <>
      <Navbar expand="md" className="fixed-nav">
        <NavbarBrand
          href="/"
          style={{
            textDecoration: "none",
            color: "white",
            "font-family": "'Barlow Condensed', sans-serif;",
            "font-size": "1.45rem",
            "font-weight": "600",
            "-webkit-text-stroke": "0.125px #FF5C58",
          }}
          className="me-5 ms-3 Nav-HomeBtn"
        >
          Home
        </NavbarBrand>
        <NavbarToggler onClick={toggle} />
        <Collapse isOpen={isOpen} navbar>
          <Nav className="mr-auto" navbar>
            <UncontrolledDropdown nav inNavbar>
              <DropdownToggle
                nav
                caret
                style={{
                  textDecoration: "none",
                  color: "white",
                  "font-family": "'Barlow Condensed', sans-serif;",
                  "font-size": "1.4rem",
                  "font-weight": "500",
                  "-webkit-text-stroke": "0.125px #FF5C58",
                }}
                className="Nav-HomeBtn"
              >
                Bicycles
              </DropdownToggle>
              <DropdownMenu>
                {cateList.map((cate, index) => (
                  <div key={cate.id}>
                    <DropdownItem>
                      <Link
                        to={`/Bike/${cate.id}`}
                        style={{
                          textDecoration: "none",
                          color: "#1DB9C3",
                          "font-weight": "600",
                        }}
                      >
                        {cate.name}
                      </Link>
                    </DropdownItem>
                    {index != cateList.length - 1 ? (
                      <DropdownItem divider />
                    ) : null}
                  </div>
                ))}
              </DropdownMenu>
            </UncontrolledDropdown>
            <div className="searchField-Navbar">
              <Input
                type="search"
                name="search"
                id="searchProd"
                required="required"
                placeholder="Search Product by name"
                onChange={(e) => handleSearchChange(e)}
              />
              <Popover
                placement="bottom-end"
                isOpen={popoverOpen}
                target="searchProd"
                toggle={toggle_Search}
                className="popover-Nav"
              >
                <PopoverBody className="popoverCart-Nav">
                  {prodList.map((prod, index) => {
                    if (index >= 0 && index <= 3) {
                      return (
                        <Link
                          to={`/prodDetail/${prod.id}`}
                          style={{ textDecoration: "none", color: "black" }}
                          key={index}
                        >
                          <Row className="">
                            <Col className="col-3">
                              <img
                                src={`data:image/jpeg;base64,${prod.photo}`}
                                className="img-prodSearch"
                              />
                            </Col>
                            <Col className="">
                              <h6>{prod.name}</h6>
                              <Label>Price </Label>
                              <Label for="exampleQuantity" className="priceNum">
                                {numberFormat(prod.price)}
                              </Label>
                            </Col>
                          </Row>
                          <hr />
                        </Link>
                      );
                    }
                  })}
                </PopoverBody>
              </Popover>
            </div>
          </Nav>
        </Collapse>

        {isLogging()}
        {/* <Button color="link" onClick={() => handleOrder()}>
          <TiShoppingCart size={50} />
        </Button> */}
        <Cart />
      </Navbar>
    </>
  );
}
