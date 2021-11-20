import React, { useState, useEffect } from "react";
import "./ModalChangePass.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BiKey } from "react-icons/bi";
import { getCookie } from "../../Utils/Cookie";
import {  put } from "../../Utils/httpHelper";
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
const ModalConfirm = (props) => {
  const [modal, setModal] = useState(false);
  const [oldPass, setOldPass] = useState("");
  const [newPass, setNewPass] = useState("");
  const [rePass, setRePass] = useState("");
  const [rePassError, setRePassError] = useState("");
  const [newPassError, setNewPassError] = useState("");
  const toggle = () => setModal(!modal);

  useEffect(() => {
    if (modal) {
      setRePassError("");
      setNewPassError("");
      setRePass("");
      setNewPass("");
      setOldPass("");

    }
  }, [modal]);
  async function checkRePass(pass){
    if (pass !== rePass){
      setRePassError("Please enter the same password as above")
    }
  }

  function handleSubmit(e) {
    e.preventDefault();
    const email = getCookie("email");
    const pass = e.target.password.value;
    checkRePass(pass);
    const check = (newPassError == "") && (rePassError == "") && (pass === rePass);
    if (check) {
      const body = JSON.stringify({
        oldPassword: e.target.oldpassword.value,
        newPassword: e.target.password.value
      });
      put(`/persons/updatePassword/${email}`, body)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Change password successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            toggle();
          }
        })
        .catch((error) => {
          if (error.response.status === 400) {
            toast.error(
              `Old password incorrect. Please try again`,
              {
                position: toast.POSITION.TOP_RIGHT,
                autoClose: 3000,
              }
            );
          }
        });
    }
  }
  async function handleFieldChange(e,key) {
    
    if (key === "oldPass") {
      setOldPass(e.target.value);
      if (newPass === e.target.value){
        setNewPassError("New password cannot be the same as Old password");
      }
    }
    else if (key === "newPass"){
      const newPass = e.target.value;
      if (newPass === oldPass){
        setNewPassError("New password cannot be the same as Old password");
      }
      else if (newPass !== rePass){
        setNewPassError("");
        setRePassError("Please enter the same password as above");
      }
      else {
        setNewPassError("");
      }
      setNewPass(newPass);
    }
    else if (key === "rePass"){
      const rePass = e.target.value;
      if (rePass !== newPass){
        setRePassError("Please enter the same password as above");
      }
      else {
        setRePassError("");
      }
      setRePass(rePass);
    }
  }
  return (
    <div>
      <p onClick={toggle} className={`${props.onUserSide} changepass-text`}>
        Change Password <BiKey />
      </p>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>
          <BiKey /> Change Password
        </ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)} >
            <FormGroup>
              <Label for="examplePassword">Old Password</Label>
              <Input
                type="password"
                name="oldpassword"
                id="oldpassword"
                required="required"
                minLength="6"
                value={oldPass}
                onChange={(e) => handleFieldChange(e, "oldPass")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword">Password</Label>
              <Input
                type="password"
                name="password"
                id="examplePassword"
                required="required"
                minLength="6"
                value={newPass}
                onChange={(e) => handleFieldChange(e, "newPass")}
              />
            </FormGroup>
            <div style={{ color: "red", "text-align": "left" }}>
              {newPassError}
            </div>
            <FormGroup>
              <Label for="exampleRePassword">Confirm Password</Label>
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
              Confirm
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

export default ModalConfirm;
