import React, { useState, useEffect } from "react";
import { postAuth } from "../../../../Utils/httpHelper";
import { IoPersonAddSharp } from "react-icons/io5";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./ModalAddUser.css"
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

  useEffect(() => {
  }, [modal]);

  function handleSubmit(e) {
    e.preventDefault();
    const body = JSON.stringify({
      id: e.target.id.value,
      fullname: e.target.fullname.value,
      email: e.target.email.value,
      password: e.target.password.value,
      gender: e.target.radio.value,
      dob: e.target.dob.value,
      phonenumber: e.target.phonenumber.value,
      address: e.target.address.value,
      role: "ADMIN"
    });
    console.log(body);
    // console.log(e.target.dob.value);

    postAuth("/auth/signup", body)
      .then((response) => {
        if(response.status === 200)  toast("Add successfully!!!", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
      })
      .catch((error) => {
        toast.error("Add failed, please check again", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
        console.log(error)});
  }
  return (
    <div>
      <Button color="info" onClick={toggle}>
      <IoPersonAddSharp/> Add admin account
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}><IoPersonAddSharp/> User Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="examplePrice">Email</Label>
              <Input
                type="email"
                name="email"
                id="examplePrice"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePasswrod">Password</Label>
              <Input type="password" name="password" id="exampleEmail"  required />
            </FormGroup>
            <FormGroup>
              <Label for="exampleFullName">Name</Label>
              <Input
                type="text"
                name="fullname"
                id="exampleFullName"
                required
              />
            </FormGroup>
            
            <FormGroup tag="fieldset" className="radioGr-user">
            <Label for="exampleQuantity">Gender</Label>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio" value="false" required/> MALE
                </Label>
              </FormGroup>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio" value="true"/> FEMALE
                </Label>
              </FormGroup>
              
            </FormGroup>
            <FormGroup>
              <Label for="exampleBrand" >Day of Birth</Label>
              <Input
                type="date"
                name="dob"
                id="exampleBrand"
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePhone">Phonenumber</Label>
              <Input
                type="number"
                name="phonenumber"
                id="examplePhone"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleAddress">Address</Label>
              <Input
                type="text"
                name="address"
                id="exampleAddress"
                required
              />
            </FormGroup>
            <br />
            <Button color="primary" type="submit">
            <IoPersonAddSharp/> ADD
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
