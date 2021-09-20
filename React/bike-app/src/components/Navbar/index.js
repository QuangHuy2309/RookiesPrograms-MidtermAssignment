import React, { useState, useEffect } from "react";
import "./Navbar.css";
import { Link, useHistory } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { getCookie } from "../../Utils/Cookie";
import { logOut } from "../../Utils/Auth";
import ModalConfirm from "../ModalConfirm";
import ModalChangePass from "../ModalChangePass";
import ModalEdtUser from "../Admin/UserPage/ModalEdtUser";
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
    console.log("LOG OUT PRESS");
    if (e === "OK") {
      setStatus(false);
      history.push("/");
      logOut();
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
  function handleUpdate(e){
    
  }
  function isLogging() {
    const name = getCookie("username");
    // const email = getCookie("email");
    if (status && name !== "") {
      return (
        <Nav className="mr-auto" navbar>
          <UncontrolledDropdown nav inNavbar>
            <DropdownToggle nav caret>
              Hi, {name}
            </DropdownToggle>
            <DropdownMenu>
            <DropdownItem divider />
              <DropdownItem>
                <ModalEdtUser isUser = "true" id={id} onEdit={(e) => handleUpdate(e)}/>
              </DropdownItem>
            <DropdownItem divider />
              <DropdownItem>
                <ModalChangePass />
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem>
                <Link
                  to={`/OrderHistory`}
                  style={{ textDecoration: "none", color: "black" }}
                >
                  Order History
                </Link>
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem>
                <ModalConfirm onChoice={(e) => handleLogOut(e)} />
              </DropdownItem>
            </DropdownMenu>
          </UncontrolledDropdown>
        </Nav>
      );
    }
    return (
      <Link to={`/Login`} style={{ textDecoration: "none" }}>
        Login
      </Link>
    );
  }
  async function handleSearchChange(e) {
    if (e.target.value.trim() != "") {
      setProdList([]);
      get(`/public/product/search?keyword=${e.target.value}`).then(
        (response) => {
          if (response.status === 200) {
            setProdList([...response.data]);
          }
        }
      );
      setPopoverOpen(true);
    }
    if (e.target.value == "") {
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
        <NavbarBrand href="/">Home</NavbarBrand>
        <NavbarToggler onClick={toggle} />
        <Collapse isOpen={isOpen} navbar>
          <Nav className="mr-auto" navbar>
            <UncontrolledDropdown nav inNavbar>
              <DropdownToggle nav caret>
                Bicycles
              </DropdownToggle>
              <DropdownMenu>
                {cateList.map((cate) => (
                  <div key={cate.id}>
                    <DropdownItem>
                      <Link
                        to={`/Bike/${cate.id}`}
                        style={{ textDecoration: "none" }}
                      >
                        {cate.name}
                      </Link>
                    </DropdownItem>
                    <DropdownItem divider />
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
                    if (index >= 1 && index <= 4) {
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
