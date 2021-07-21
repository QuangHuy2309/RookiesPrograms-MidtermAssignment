import React, { useState, useEffect } from "react";
import "./Navbar.css";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { getCookie } from "../../Utils/Cookie";
import { logOut } from "../../Utils/Auth";
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
  const [status, setStatus] = useState([getCookie("status")]);
  let username, email, role;
  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    // setStatus(getCookie("status"));
  }, []);
  useEffect(() => {
    if (status){
      username = getCookie("username");
      email = getCookie("email");
      role = getCookie("role");
    }
  }, [status]);

  function handleLogOut(e) {
    console.log("LOG OUT PRESS");
    setStatus(false);
    logOut();
    props.onLogOut(e);
  }
  function isLogging() {
    console.log(`IS LOGGING STATUS ${status}`)
    const name = getCookie("username");
    if (status && name !== "") {
      return (
        <Nav className="mr-auto" navbar>
          <UncontrolledDropdown nav inNavbar>
            <DropdownToggle nav caret>
              Hello, {name}
            </DropdownToggle>
            <DropdownMenu>
              <DropdownItem divider />
              <DropdownItem>
                <Link
                  to={`/Info/${email}`}
                  style={{ textDecoration: "none" }}
                >
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
        {
          isLogging()
          // props.status ? (
          // <>
          // {/* <Collapse isOpen={isOpen} navbar> */}
          // <Nav className="mr-auto" navbar>
          //   <UncontrolledDropdown nav inNavbar>
          //     <DropdownToggle nav caret>
          //       Hello, {props.username}
          //     </DropdownToggle>
          //     <DropdownMenu>
          //       <DropdownItem divider />
          //       <DropdownItem>
          //         <Link
          //           to={`/Info/${props.email}`}
          //           style={{ textDecoration: "none" }}
          //         >
          //           Information
          //         </Link>
          //       </DropdownItem>
          //       <DropdownItem divider />
          //       <DropdownItem>
          //         <p onClick={(e) => handleLogOut(e)}>LogOut</p>
          //       </DropdownItem>
          //     </DropdownMenu>
          //   </UncontrolledDropdown>
          // </Nav>
          // {/* </Collapse> */}
          // </>
          // ) : (
          //   <Link to={`/Login`} style={{ textDecoration: "none" }}>
          //     Login
          //   </Link>
          // )
        }
        <Link to="/" style={{ textDecoration: "none" }}>
          <TiShoppingCart size={50} />
        </Link>
      </Navbar>
    </>
  );
}
