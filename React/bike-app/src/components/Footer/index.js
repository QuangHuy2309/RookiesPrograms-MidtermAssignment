import React from "react";
import './Footer.css';
import { FaFacebook } from "react-icons/fa";
import { FaInstagram } from "react-icons/fa";
import { FaGithub } from "react-icons/fa";
export default function index() {
  return (
    <footer>
      <h2>ABOUT ME</h2>
      <br/>
      <a href="https://www.facebook.com/supbored/" style={{ textDecoration: 'none' }}>
        <h4>
          <FaFacebook/> Facebook
        </h4>
      </a>
      <a href="https://www.instagram.com/hi_immal/" style={{ textDecoration: 'none' }}>
        <h4>
          <FaInstagram/> Instagram
        </h4>
      </a>
      <a href="https://github.com/QuangHuy239/RookiesPrograms-MidtermAssignment" style={{ textDecoration: 'none' }}>
        <h4>
          <FaGithub/> Github
        </h4>
      </a>
    </footer>
  );
}
