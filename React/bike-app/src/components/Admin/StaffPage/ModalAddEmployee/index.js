import React, { useState, useEffect } from "react";
import { postAuth, get } from "../../../../Utils/httpHelper";
import { IoPersonAddSharp } from "react-icons/io5";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./ModalAddUser.css";
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
  const [today, setToday] = useState("");
  const [checkName, setCheckName] = useState(true);
  const [nameError, setNameError] = useState("");
  const [checkEmail, setCheckEmail] = useState(false);
  const [emailError, setEmailError] = useState("");
  const [phone, setPhonenumber] = useState("");
  const [rePass, setRePass] = useState("");
  const [rePassError, setRePassError] = useState("");

  const toggle = () => setModal(!modal);

  useEffect(() => {
    if (modal) {
      setDate();
      setRePassError("");
      setRePass("");
      setNameError("");
      setEmailError("");
      setPhonenumber("");
    }
  }, [modal]);

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
    }
  }

  async function checkRePass(pass) {
    if (pass !== rePass) {
      setRePassError("Please enter the same password as above");
    }
  }

  async function handleSubmit(e) {
    e.preventDefault();
    const email = e.target.email.value.trim().toLowerCase();
    const pass = e.target.password.value;
    checkRePass(pass);
    checkExistEmail(email);
    const check =
      checkEmail && checkName && rePassError == "" && pass === rePass;
    if (check) {
      const body = JSON.stringify({
        id: e.target.id.value,
        fullname: e.target.fullname.value.trim(),
        email: e.target.email.value.trim().toLowerCase(),
        password: e.target.password.value,
        gender: e.target.radio.value,
        dob: e.target.dob.value,
        phonenumber: e.target.phonenumber.value,
        address: e.target.address.value.trim(),
        role: e.target.role.value,
        status: true,
      });
      console.log(body);
      // console.log(e.target.dob.value);

      postAuth("/auth/signup", body)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Add successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            props.onAdd(true);
            toggle();
          }
        })
        .catch((error) => {
          toast.error("Add failed, please check again", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          console.log(error);
        });
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
    let yyyy = today.getFullYear() - 18;
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
    <div>
      <Button color="success" onClick={toggle}>
        <IoPersonAddSharp /> Add staff account
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle} className="title-UserAddAdmin">
          <IoPersonAddSharp /> User Information
        </ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="examplePrice" className="titleTable-UserAdmin">
                Email
              </Label>
              <Input
                type="email"
                name="email"
                id="examplePrice"
                required="required"
                onChange={(e) => handleFieldChange(e, "email")}
              />
              <div style={{ color: "red", "text-align": "left" }}>
                {emailError}
              </div>
            </FormGroup>
            <FormGroup>
              <Label for="examplePasswrod" className="titleTable-UserAdmin">
                Password
              </Label>
              <Input
                type="password"
                name="password"
                id="exampleEmail"
                required
                minLength="6"
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleRePassword" className="titleTable-UserAdmin">
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
              <Label for="exampleFullName" className="titleTable-UserAdmin">
                Name
              </Label>
              <Input
                type="text"
                name="fullname"
                id="exampleFullName"
                required="required"
                onChange={(e) => handleFieldChange(e, "fullname")}
              />
              <div style={{ color: "red", "text-align": "left" }}>
                {nameError}
              </div>
            </FormGroup>
            <FormGroup tag="fieldset" className="radioGr-user mb-2">
              <Label for="exampleQuantity" className="titleTable-UserAdmin">
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
              <Label
                for="exampleBrand"
                required="required"
                className="titleTable-UserAdmin"
              >
                Day of Birth
              </Label>
              <Input
                type="date"
                name="dob"
                id="exampleBrand"
                required="required"
                max={today}
              />
            </FormGroup>
            <FormGroup tag="fieldset" className="radioGr-user mb-2">
              <Label for="exampleQuantity" className="titleTable-UserAdmin">
                Role
              </Label>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="role" value="ADMIN" required />{" "}
                  Admin
                </Label>
              </FormGroup>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="role" value="STAFF" /> Staff
                </Label>
              </FormGroup>
            </FormGroup>
            <FormGroup>
              <Label for="examplePhone" className="titleTable-UserAdmin">
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
              <Label for="exampleAddress" className="titleTable-UserAdmin">
                Address
              </Label>
              <Input
                type="text"
                name="address"
                id="exampleAddress"
                required="required"
              />
            </FormGroup>
            <br />
            <Button color="success" type="submit">
              <IoPersonAddSharp /> ADD
            </Button>{" "}
            <Button color="secondary" onClick={toggle}>
              Cancel
            </Button>
          </Form>
        </ModalBody>
      </Modal>
    </div>
  );
};

export default ModalAdd;
