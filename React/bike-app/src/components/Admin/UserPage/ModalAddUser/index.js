import React, { useState, useEffect } from "react";
import { postAuth } from "../../../../Utils/httpHelper";
import { format } from "date-fns";
import DatePicker from "react-datepicker";
import "./ModalAddUser.css"
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Form,
  FormGroup,
  Label,
  Input,
  FormText,
} from "reactstrap";

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
        console.log(response.data);
        alert("ADD ADMIN ACCOUNT SUCCESS");
      })
      .catch((error) => console.log(error));
    props.onEdit(e);
  }
  return (
    <div>
      <Button color="info" onClick={toggle}>
        Add admin account
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>User Information</ModalHeader>
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
              <FormGroup check class="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio" value="false" required/> MALE
                </Label>
              </FormGroup>
              <FormGroup check class="radioBtn-user">
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
              Submit
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
