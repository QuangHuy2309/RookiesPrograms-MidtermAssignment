import React, { useState } from 'react';
import "./Navbar.css";
import logo from '../../assets/img/logo.png';
import { Navbar} from 'reactstrap';
import {Button} from 'reactstrap';
import { FaShoppingCart } from "react-icons/fa";

export default function index(props) {
    return (
        <Navbar color="faded" className="fixed-nav" light>
        <img src={logo} id="logo-img" alt="Logo" width="170"/>
        <Button id="cart-button"color="info">
            <FaShoppingCart/> CART
        </Button>
      </Navbar>
    );
}
