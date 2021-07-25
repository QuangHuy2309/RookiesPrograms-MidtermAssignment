import React, { useState} from "react";
import { useHistory   } from "react-router-dom";
import "./Login.css";
import ModalAdd from "./ModalAddUser"
import { postAuth } from "../../Utils/httpHelper";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Navbar from "../Navbar";
import {
  Button,
  Form,
  FormGroup,
  Label,
  Input,
  Row,
  Col,
} from "reactstrap";

toast.configure();
export default function Index(props) {
  const history = useHistory();
  const [loginError, setLoginError] = useState("");

  
  function handleSubmitLogin(e) {
    e.preventDefault();
    const body = JSON.stringify({
      email: e.target.email.value.trim(),
      password: e.target.password.value,
    });
    console.log(body);
    postAuth("/auth/signin",body).then((response) => {
      if (response.status === 200) {
        document.cookie = `token=${response.data.accessToken}; max-age=86400; path=/;`;
        document.cookie = `username=${response.data.username}; max-age=86400; path=/;`;
        document.cookie = `email=${response.data.email}; max-age=86400; path=/;`;
        document.cookie = `role=${response.data.roles[0]}; max-age=86400; path=/;`; 
        document.cookie = `status=true; max-age=86400; path=/;`;
        document.cookie = `cart=; max-age=86400; path=/;`;
        // props.onStatus(response.data);
        toast.info(`Welcome back, ${response.data.username}`, {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
        if (response.data.roles[0] === "ROLE_USER"){
          history.goBack();
        }
        else if (response.data.roles[0] === "ROLE_ADMIN")
        {
          history.push("/Admin")
        }
        
      }
    }).catch(error => {
      setLoginError("Login Failed! Please check email and password again");
    });
      
  }
  function handleFieldChange(e) {
      setLoginError(""); 
  }

  return (
    <>
    <Navbar />
    <div className="login-form">
      <h2 className="head-login">SIGN-IN</h2>
      <Row >
            <Col className="col-1"></Col>
      <Col><div style={{ color: "red", "text-align": "left" }}>{loginError}</div></Col>
      </Row>
      <Form onSubmit={(e) => handleSubmitLogin(e)}>
        <FormGroup>
          <Row className="login">
            <Col className="col-1">
              <Label for="exampleEmail">Email</Label>
            </Col>
            <Col>
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
          <Row className="login">
            <Col className="col-1">
              <Label for="examplePassword">Password</Label>
            </Col>
            <Col>
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
        <Button outline color="info" type="submit">SIGN IN</Button>
      </Form>
      <ModalAdd />
    </div>
    </>
  );
}
