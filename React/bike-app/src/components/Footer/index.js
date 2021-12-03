import React from "react";
import "./Footer.css";
import { FaFacebook } from "react-icons/fa";
import { FaInstagram } from "react-icons/fa";
import { FaGithub } from "react-icons/fa";
export default function index() {
  return (
    <footer className="mt-3">
      {/* <h2>ABOUT ME</h2>
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
      </a> */}
      <div class="content">
        <div class="footer-grids">
          <div class="footer one">
            <h3>More About Company</h3>
            <p>
              {" "}
              Since 2021 BikeCo.com has been the web incarnation of our brick
              and mortar located in Lake Forest, California. We provide sport to
              pro race MTB riders access to unmatched expertise, the best
              products from leading manufacturers as well as unique content and
              insight.
            </p>
            <p class="adam">- BikeCo.com – Race Proven!</p>
            <div class="clear"></div>
          </div>
          <div class="footer two">
            <h3>Keep Connected</h3>
            <ul>
              <li>
                <a class="fb" href="https://www.facebook.com/supbored/">
                  <i></i>Like us on Facebook
                </a>
              </li>
              <li>
                <a class="fb1" href="#">
                  <i></i>Follow us on Twitter
                </a>
              </li>
              <li>
                <a class="fb2" href="#">
                  <i></i>Add us on Google Plus
                </a>
              </li>
              <li>
                <a class="fb4" href="#">
                  <i></i>Follow us on Pinterest
                </a>
              </li>
            </ul>
          </div>
          <div class="footer three">
            <h3>Contact Information</h3>
            <ul>
              <li>21098 Bake Parkway #112 Lake Forest, CA 92630</li>
              <li> (+84) 967 141 557 </li>
              <li>
                <a href="mailto:info@example.com">lqhuy2309@gmail.com</a>{" "}
              </li>
            </ul>
          </div>
          <div class="clear"></div>
        </div>
        <div class="copy-right-grids">
          <div class="copy-left">
            <p class="footer-gd"></p>
          </div>
          <div class="copy-right">
            <ul>
              <li>
                <a href="#">Company Information</a>
              </li>
              <li>
                <a href="#">Privacy Policy</a>
              </li>
              <li>
                <a href="#">Terms & Conditions</a>
              </li>
            </ul>
          </div>
          <div class="clear"></div>
        </div>
      </div>
    </footer>
  );
}
