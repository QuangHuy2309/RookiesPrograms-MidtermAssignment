import React, { useState, useEffect } from "react";
import "./Navbar.css";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import logo from "../../assets/img/logo.png";
import {
  Collapse,
  Navbar,
  NavbarToggler,
  NavbarBrand,
  Nav,
  NavItem,
  NavLink,
  UncontrolledDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  NavbarText,
} from "reactstrap";
import { TiShoppingCart } from "react-icons/ti";

export default function Index(props) {
  const [isOpen, setIsOpen] = useState(false);
  const [cateList, setCateList] = useState([]);
  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
  }, []);
  useEffect(() => {}, [props.status, props.username]);
  function handleLogOut(e) {
    console.log("LOG OUT PRESS");

    props.onLogOut(e);
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
        {props.status === false ? (
          <Link to={`/Login`} style={{ textDecoration: "none" }}>
            Login
          </Link>
        ) : (
          <>
            {/* <Collapse isOpen={isOpen} navbar> */}
            <Nav className="mr-auto" navbar>
              <UncontrolledDropdown nav inNavbar>
                <DropdownToggle nav caret>
                  Hello, {props.username}
                </DropdownToggle>
                <DropdownMenu>
                  <DropdownItem divider />
                  <DropdownItem>
                    <Link to={`/Info/${props.email}`} style={{ textDecoration: "none" }}>
                      Information
                    </Link>
                  </DropdownItem>
                  <DropdownItem divider />
                  <DropdownItem>
                    <p onClick={(e) => handleLogOut(e)}>LogOut</p>
                  </DropdownItem>
                </DropdownMenu>
              </UncontrolledDropdown>
            </Nav>
            {/* </Collapse> */}
          </>
        )}
        <Link to="/" style={{ textDecoration: "none" }}>
          <TiShoppingCart size={50} />
        </Link>
      </Navbar>
    </>
  );
}
