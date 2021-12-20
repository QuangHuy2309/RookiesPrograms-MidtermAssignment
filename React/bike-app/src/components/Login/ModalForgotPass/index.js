import React, { useState, useEffect } from "react";
import { postAuth, get, put } from "../../../Utils/httpHelper";
import { useHistory } from "react-router-dom";
import "./ModalForgotPass.css";
import { toast } from "react-toastify";
import Navbar from "../../Navbar";
import "react-toastify/dist/ReactToastify.css";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  Row,
  Col,
  Form,
  FormGroup,
  Label,
  Input,
} from "reactstrap";

toast.configure();
const ModalAdd = (props) => {
  const history = useHistory();
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);
  const [emailError, setEmailError] = useState("");
  const [checkEmail, setCheckEmail] = useState(false);
  const [email, setEmail] = useState("");
  const [otp, setOTP] = useState("");
  const [newPass, setNewPass] = useState("");
  const [rePass, setRePass] = useState("");
  const [rePassError, setRePassError] = useState("");
  const [show, setShow] = useState(false);

  useEffect(() => {
    setNewPass("");
    setOTP("");
    setEmailError("");
    setRePassError("");
    setCheckEmail(false);
    setRePass("");
    setShow(false);
  }, []);
  useEffect(() => {
    if (modal) {
      setRePassError("");
      setRePass("");
      setNewPass("");
    }
  }, [modal]);
  useEffect(() => {
    console.log(otp);
  }, [otp]);
  async function checkRePass(pass) {
    if (pass !== rePass) {
      setRePassError("Please enter the same password as above");
      return false;
    }
  }
  function handleEmailChange(e) {
    setEmailError("");
    setEmail(e.target.value.trim().toLowerCase());
  }
  async function handleFieldChange(e, key) {
    if (key === "rePass") {
      setRePass(e.target.value);
      setRePassError("");
    } else if (key === "newPass") {
      setNewPass(e.target.value);
    }
  }
  async function sendOTP(e) {
    e.preventDefault();
    let email_trim = email.trim().toLowerCase();
    const check = await checkExistEmail(email_trim);
    // await console.log(check);
    // if (check) {
    //   console.log("TRUE");
    //   get(`/auth/sendOTP/${email_trim}`)
    //     .then((response) => {
    //       if (response.status === 200)
    //         toast.success("Verification code had send to your email", {
    //           position: toast.POSITION.TOP_RIGHT,
    //           autoClose: 3000,
    //         });
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //     });
    // }
  }
  async function sendMail(email_trim){
    get(`/auth/sendOTP/${email_trim}`)
        .then((response) => {
          if (response.status === 200)
            toast.success("Verification code had send to your email", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
        })
        .catch((error) => {
          console.log(error);
        });
  }
  function checkOTP(e) {
    e.preventDefault();
    if (email.trim() !== ""){
    get(`/auth/checkOTP?email=${email}&otp=${e.target.otp.value}`)
      .then((response) => {
        if (response.data === true) toggle();
        else {
          toast.error("Wrong verification code! Please send OTP again", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
        }
      })
      .catch((error) => {
        console.log(error);
      });
    }
  }
  async function handleSubmit(e) {
    e.preventDefault();
    const check = checkRePass(newPass) && (rePassError == "") && (newPass === rePass);
    if (check && show) {
      const body = JSON.stringify({
        email: email,
        password: newPass
      });
      put(`/auth/forgotPassword/${email}`, body)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Change password successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            toggle();
            history.push("/");
          }
        })
        .catch((error) => {
          if (error.response.status === 400) {
            toast.error(
              `Change password failed. Please try again!`,
              {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 3000,
              }
            );
          }
        });
    }
  }

  async function checkExistEmail(email) {
    get(`/auth/checkEmail/${email}`).then((response) => {
      if (response.status === 200) {
        if (response.data) {
          setEmailError("Email does not exist!!!");
        } else {
          sendMail(email);
          setShow(true);
        }
      }
    });
  }
  return (
    <div>
      <Navbar />
      <div className="login-form-Login mt-5">
        <h2 className="head-login-Login">FORGOT PASSWORD</h2>
        <hr className="hrLoginForm" />
        <Form
          onSubmit={(e) => sendOTP(e)}
          className="inputEmail_ForgotPass"
        >
          <Row>
            <Col>
              <FormGroup className="emailForgotPass">
                <Label
                  for="exampleEmail"
                  className="titleModalForgotPass-login"
                >
                  Email
                </Label>
                <Input
                  type="email"
                  name="email"
                  id="exampleEmail"
                  required="required"
                  className="ms-3"
                  onChange={(e) => handleEmailChange(e)}
                  value={email}
                />
              </FormGroup>
            </Col>
            <Col className="col-4 divBtnSendOTP-ForgotPass">
              <Button
                color="info"
                outline
                type="submit"
                className="btnSendOTP-ForgotPass"
              >
                Send OTP
              </Button>
            </Col>
          </Row>
        </Form>
        <div className="error-ForgotPass">{emailError}</div>
        <Form onSubmit={(e) => checkOTP(e)} className="inputOTP_ForgotPass mt-3">
          <FormGroup className="otpForgotPass">
            <Label for="exampleOTP" className="titleModalForgotPass-login">
              OTP code
            </Label>
            <Input
              type="text"
              name="otp"
              id="exampleOTP"
              required="required"
              minLength="5"
              className="ms-3 inputOTP-ForgotPassword"
            />
          </FormGroup>
          <Button
            color="primary"
            type="submit"
            className="mt-3"
            disabled={!show}
          >
            Submit
          </Button>
        </Form>
        {/*  */}

        <Modal isOpen={modal} toggle={toggle}>
          <ModalHeader toggle={toggle}>Forgot Password</ModalHeader>
          <ModalBody>
            <Form onSubmit={(e) => handleSubmit(e)}>
              <FormGroup>
                <Label for="examplePassword" className="titleModalSignUp-login">
                  Password
                </Label>
                <Input
                  type="password"
                  name="password"
                  id="examplePassword"
                  required="required"
                  minLength="6"
                  value={newPass}
                  value={newPass}
                  onChange={(e) => handleFieldChange(e, "newPass")}
                />
              </FormGroup>
              <FormGroup>
                <Label
                  for="exampleRePassword"
                  className="titleModalSignUp-login"
                >
                  Confirm Password
                </Label>
                <Input
                  type="password"
                  name="rePass"
                  id="exampleRePassword"
                  required="required"
                  minLength="6"
                  value={rePass}
                  onChange={(e) => handleFieldChange(e, "rePass")}
                />
              </FormGroup>
              <div style={{ color: "red", "text-align": "left" }}>
                {rePassError}
              </div>
              <br />
              <Button color="primary" type="submit">
                Change
              </Button>{" "}
              <Button color="secondary" onClick={toggle}>
                Cancel
              </Button>
            </Form>
          </ModalBody>
        </Modal>
      </div>
    </div>
  );
};

export default ModalAdd;
