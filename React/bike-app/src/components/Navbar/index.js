import React, { useState, useEffect } from "react";
import "./Navbar.css";
import { Link, useHistory } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { getCookie } from "../../Utils/Cookie";
import { logOut } from "../../Utils/Auth";
import ModalConfirm from "../ModalConfirm";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Cart from "../ModalCart";
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  Button,
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
  const [status, setStatus] = useState([getCookie("status")]);
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
    }
    else {
      toast.info("🦄 Cart is empty. Fill it in righ away", {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
      });
    }
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
                <Link to={`/OrderHistory`} style={{ textDecoration: "none" }}>
                  Order History
                </Link>
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem>
                {/* <p onClick={(e) => handleLogOut(e)}>LogOut</p> */}
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
  const toggle = () => setIsOpen(!isOpen);
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
          </Nav>
        </Collapse>
        {isLogging()}
        {/* <Button color="link" onClick={() => handleOrder()}>
          <TiShoppingCart size={50} />
        </Button> */}
        <Cart/>
      </Navbar>
    </>
  );
}
