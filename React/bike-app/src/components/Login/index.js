import React, { useState } from "react";
import { useHistory, Link } from "react-router-dom";
import "./Login.css";
import ModalAdd from "./ModalSignUp";
import ModalForget from "./ModalForgotPass";
import { postAuth } from "../../Utils/httpHelper";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Navbar from "../Navbar";
import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";

toast.configure();
export default function Index(props) {
  const history = useHistory();
  const [loginError, setLoginError] = useState("");

  function handleSubmitLogin(e) {
    e.preventDefault();
    const body = JSON.stringify({
      email: e.target.email.value.trim().toLowerCase(),
      password: e.target.password.value,
    });
    postAuth("/auth/signin", body)
      .then((response) => {
        if (response.status === 200) {
          document.cookie = `token=${response.data.accessToken}; max-age=86400; path=/;`;
          document.cookie = `username=${response.data.username}; max-age=86400; path=/;`;
          document.cookie = `id=${response.data.id}; max-age=86400; path=/;`;
          document.cookie = `email=${response.data.email}; max-age=86400; path=/;`;
          document.cookie = `role=${response.data.roles[0]}; max-age=86400; path=/;`;
          document.cookie = `status=true; max-age=86400; path=/;`;
          document.cookie = `cart=; max-age=86400; path=/;`;
          // props.onStatus(response.data);
          toast.info(`Welcome back, ${response.data.username}`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          if (response.data.roles[0] === "ROLE_USER") {
            history.goBack();
          } else if (
            response.data.roles[0] === "ROLE_ADMIN" ||
            response.data.roles[0] === "ROLE_STAFF"
          ) {
            history.push("/Admin");
          }
        }
      })
      .catch((error) => {
        setLoginError("Login Failed! Please check email and password again");
      });
  }
  function handleFieldChange(e) {
    setLoginError("");
  }

  return (
    <>
      <Navbar />
      <div className="login-form-Login mt-5">
        <h2 className="head-login-Login">SIGN-IN</h2>
        <hr className="hrLoginForm" />
        <Row>
          <Col className="col-1"></Col>
          <Col>
            <div style={{ color: "red", "text-align": "left" }}>
              {loginError}
            </div>
          </Col>
        </Row>
        <Form onSubmit={(e) => handleSubmitLogin(e)}>
          <FormGroup>
            <Row className="login">
              <Col className="col-2 labelText-Login">
                <Label for="exampleEmail" className="labelText-Login">
                  Email
                </Label>
              </Col>
              <Col className="me-3">
                <Input
                  type="email"
                  name="email"
                  id="emailExample"
                  required="required"
                  placeholder="something@nomail.com"
                  className="input-login"
                  maxLength="50"
                  onChange={(e) => handleFieldChange(e)}
                />
                {/* <div style={{ color: "red", "text-align": "left" }}>{emailError}</div> */}
              </Col>
            </Row>
          </FormGroup>
          <FormGroup>
            <Row className="displayflex mb-3">
              <Col className="col-2 labelText-Login">
                <Label for="examplePassword " className="labelText-Login">
                  Password
                </Label>
              </Col>
              <Col className="me-3">
                <Input
                  type="password"
                  name="password"
                  id="passwordExample"
                  required="required"
                  placeholder="password"
                  className="input-login"
                  minLength="6"
                  onChange={(e) => handleFieldChange(e)}
                />
              </Col>
            </Row>
          </FormGroup>

          <Button
            outline
            color="info"
            type="submit"
            className="btnSignIn-Login"
          >
            SIGN IN
          </Button>
        </Form>
        <Row>
          <Col className="forgotPass-Login">
            <Link
              to={`/ForgetPassword`}
              style={{
                textDecoration: "none",
              }}
            >
              Forget Password
            </Link>
          </Col>
          <Col className="createAcc-Login">
            <ModalAdd />
          </Col>
        </Row>
      </div>
    </>
  );
}
