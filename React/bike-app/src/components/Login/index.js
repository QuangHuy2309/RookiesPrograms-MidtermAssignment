import React from "react";
import {
  Button,
  Form,
  FormGroup,
  Label,
  Input,
  FormText,
  Row,
  Col,
} from "reactstrap";
import { Redirect, useHistory   } from "react-router-dom";
import { bake_cookie } from 'sfcookies';
import "./Login.css";
import { postAuth } from "../../Utils/httpHelper";

export default function Index(props) {
  const history = useHistory();
  function handleSubmitLogin(e) {
    e.preventDefault();
    const body = JSON.stringify({
      email: e.target.email.value,
      password: e.target.password.value,
    });
    postAuth("/auth/signin",body).then((response) => {
      if (response.status === 200) {
        bake_cookie("token", response.data.accessToken);
        alert("Loggin Success!");
        props.onStatus(response.data);
        history.goBack();
      }
    }).catch(error => {alert("Login Failed! Please check email and password again")});
      
  }

  return (
    <div className="login-form">
      <h2 className="head-login">SIGN-IN</h2>
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
              />
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
              />
            </Col>
          </Row>
        </FormGroup>
        <Button type="submit">Submit</Button>
      </Form>
    </div>
  );
}
