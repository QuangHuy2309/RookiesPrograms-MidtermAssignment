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
        document.cookie = `token=${response.data.accessToken}; max-age=86400; path=/;`;
        document.cookie = `username=${response.data.username}; max-age=86400; path=/;`;
        document.cookie = `email=${response.data.email}; max-age=86400; path=/;`;
        document.cookie = `role=${response.data.roles[0]}; max-age=86400; path=/;`;
        document.cookie = `status=true; max-age=86400; path=/;`;
        alert("Loggin Success!");
        props.onStatus(response.data);
        if (response.data.roles[0] === "ROLE_USER"){
          history.goBack();
        }
        else if (response.data.roles[0] === "ROLE_ADMIN")
        {
          history.push("/Admin")
        }
        
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
