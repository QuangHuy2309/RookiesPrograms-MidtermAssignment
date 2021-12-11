import React, { useState, useEffect } from "react";
import { postAuth, get } from "../../../Utils/httpHelper";
import "./ModalSignUp.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Recaptcha from "react-recaptcha";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  Form,
  FormGroup,
  Label,
  Input,
} from "reactstrap";

toast.configure();
const ModalAdd = (props) => {
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);
  const [emailError, setEmailError] = useState("");
  const [checkEmail, setCheckEmail] = useState(false);
  const [checkName, setCheckName] = useState(false);
  const [nameError, setNameError] = useState("");
  const [checkAddress, setCheckAddress] = useState(false);
  const [addressError, setAddressError] = useState("");
  const [phone, setPhonenumber] = useState("");
  const [rePass, setRePass] = useState("");
  const [rePassError, setRePassError] = useState("");
  const [today, setToday] = useState("");
  const [verify, setVerify] = useState(false);
  const recaptchaRef = React.createRef();
  
  useEffect(() => {
    if (modal) {
      setDate();
      setEmailError("");
      setNameError("");
      setPhonenumber("");
      setRePassError("");
      setCheckEmail(false);
      setRePass("");
      setVerify(false);
    }
  }, [modal]);
  async function checkRePass(pass) {
    if (pass !== rePass) {
      setRePassError("Please enter the same password as above");
    }
  }
  function captchaCallback(){
    console.log("Load ReCaptcha success");
  }
  function verifyCallback(response){
    if (response){
      setVerify(true);
    }
  }
  async function handleSubmit(e) {
    e.preventDefault();
    const email = e.target.email.value.trim().toLowerCase();
    const pass = e.target.password.value;
    checkRePass(pass);
    await checkExistEmail(email);
    const check =
      checkEmail &&
      checkName &&
      rePassError == "" &&
      pass === rePass &&
      checkAddress &&
      verify;
    if (check) {
      const body = JSON.stringify({
        fullname: e.target.fullname.value.trim(),
        email: e.target.email.value.trim().toLowerCase(),
        password: e.target.password.value,
        gender: e.target.radio.value,
        dob: e.target.dob.value,
        phonenumber: e.target.phonenumber.value,
        address: e.target.address.value.trim(),
        role: "USER",
        status: true,
      });
      console.log(body);

      // console.log(e.target.dob.value);

      postAuth("/auth/signup", body)
        .then((response) => {
          if (response.status === 200)
            toast.success("Sign up success!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
          toggle();
        })
        .catch((error) => {
          toast.error("Sign up failed!", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          console.log(error);
        });
    }
  }
  async function handleFieldChange(e, key) {
    if (key === "fullname") {
      if (e.target.value.trim() == "") {
        setNameError("Name must not blank");
        setCheckName(false);
      } else {
        setNameError("");
        setCheckName(true);
      }
    } else if (key === "email") {
      setEmailError("");
    } else if (key === "phonenumber") {
      setPhonenumber(e.target.value.replace(/\D/, ""));
    } else if (key === "rePass") {
      setRePass(e.target.value);
      setRePassError("");
    } else if (key === "address") {
      if (e.target.value.trim() == "") {
        setAddressError("Address must not blank");
        setCheckAddress(false);
      } else {
        setAddressError("");
        setCheckAddress(true);
      }
    } 
  }

  function checkExistEmail(email) {
    get(`/auth/checkEmail/${email}`).then((response) => {
      if (response.status === 200) {
        if (response.data) {
          setCheckEmail(true);
        } else {
          setCheckEmail(false);
          setEmailError("Email already used. Choose another email");
        }
      }
    });
  }
  function setDate() {
    let today = new Date();
    let dd = today.getDate();
    let mm = today.getMonth() + 1; //January is 0!
    let yyyy = today.getFullYear() - 12;
    if (dd < 10) {
      dd = "0" + dd;
    }
    if (mm < 10) {
      mm = "0" + mm;
    }

    today = yyyy + "-" + mm + "-" + dd;
    setToday(today);
  }

  return (
    <>
      <Button color="link" onClick={toggle} className="btnModalSignUp-login">
        Create an Account
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader
          toggle={toggle}
          className="titleModalSignUp-login titleTextSignUp-login"
        >
          User Information
        </ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail" className="titleModalSignUp-login">
                Email
              </Label>
              <Input
                type="email"
                name="email"
                id="exampleEmail"
                required="required"
                onChange={(e) => handleFieldChange(e, "email")}
              />
              <div style={{ color: "red", "text-align": "left" }}>
                {emailError}
              </div>
            </FormGroup>
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
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleRePassword" className="titleModalSignUp-login">
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
            <FormGroup>
              <Label for="exampleFullname" className="titleModalSignUp-login">
                Name
              </Label>
              <Input
                type="text"
                name="fullname"
                id="exampleFullname"
                required="required"
                onChange={(e) => handleFieldChange(e, "fullname")}
              />
              <div style={{ color: "red", "text-align": "left" }}>
                {nameError}
              </div>
            </FormGroup>
            <FormGroup tag="fieldset" className="radioGr-user">
              <Label for="exampleQuantity" className="titleModalSignUp-login">
                Gender
              </Label>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio" value="false" required />{" "}
                  Male
                </Label>
              </FormGroup>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio" value="true" /> Female
                </Label>
              </FormGroup>
            </FormGroup>
            <FormGroup>
              <Label for="exampleBrand" className="titleModalSignUp-login">
                Day of Birth
              </Label>
              <Input
                type="date"
                name="dob"
                id="exampleBrand"
                max={today}
                required="required"
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePhone" className="titleModalSignUp-login">
                Phonenumber
              </Label>
              <Input
                type="text"
                name="phonenumber"
                id="examplePhone"
                required="required"
                value={phone}
                minLength="10"
                maxLength="10"
                onChange={(e) => handleFieldChange(e, "phonenumber")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleAddress" className="titleModalSignUp-login">
                Address
              </Label>
              <Input
                type="text"
                name="address"
                id="exampleAddress"
                required="required"
                onChange={(e) => handleFieldChange(e, "address")}
              />
              <div style={{ color: "red", "text-align": "left" }}>
                {addressError}
              </div>
            </FormGroup>
            <br />
            <Recaptcha
              sitekey="6LdgzbocAAAAAO_58KW6-5BbN0DKNj4uY89_CvWq"
              render="explicit"
              verifyCallback={verifyCallback}
              onloadCallback={captchaCallback}
              ref={recaptchaRef}
              onExpired={() => {
                recaptchaRef.current.reset(); // here
              }}
            />
            <br />
            <Button color="primary" type="submit" disabled={!verify}>
              Sign Up
            </Button>{" "}
            <Button color="secondary" onClick={toggle}>
              Cancel
            </Button>
          </Form>
        </ModalBody>
      </Modal>
    </>
  );
};

export default ModalAdd;
