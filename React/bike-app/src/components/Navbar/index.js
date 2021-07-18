import React, { useState } from 'react';
import "./Navbar.css";
import { Link } from "react-router-dom";
import logo from '../../assets/img/logo.png';
import { Navbar} from 'reactstrap';
import {Button} from 'reactstrap';
import { FaShoppingCart } from "react-icons/fa";

export default function index(props) {
    return (
        <Navbar color="faded" className="fixed-nav" light>
            <Link to="/" style={{ textDecoration: 'none' }}>
                <img src={logo} id="logo-img" alt="Logo" width="170"/>
            </Link>
        
        <Button id="cart-button"color="info">
            <FaShoppingCart/> CART
        </Button>
      </Navbar>
    );
}
