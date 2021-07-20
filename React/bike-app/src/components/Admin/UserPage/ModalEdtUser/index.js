import React, { useState, useEffect } from "react";
import { getWithAuth, put } from "../../../../Utils/httpHelper";
import { format } from "date-fns";
import "./ModalEdtUser.css"
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
  const { buttonLabel, id } = props;
  const [modal, setModal] = useState(false);
  const [user, setUser] = useState(Object);
  const toggle = () => setModal(!modal);
  function handleFieldChange(e, key) {
    setUser({ [key]: e.target.value });
  }
  useEffect(() => {
    if (modal) {
      getWithAuth(`/persons/search/${id}`).then((response) => {
        if (response.status === 200) {
          // console.log(response.data);
          setUser(response.data);
        }
      });
    }
  }, [modal]);

  function handleSubmit(e) {
    e.preventDefault();
    // console.log(photo);
    const body = JSON.stringify({
      id: e.target.id.value,
      name: e.target.name.value,
      price: e.target.price.value,
      quantity: e.target.quantity.value,
      description: e.target.description.value,
      brand: e.target.brand.value,
    });
    console.log(body);

    put("/product", body)
      .then((response) => {
        console.log(response.data);
        alert("EDIT SUCCESS");
      })
      .catch((error) => console.log(error));
    props.onEdit(e);
  }
  return (
    <div>
      <Button color="warning" onClick={toggle}>
        EDIT
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>User Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail">ID</Label>
              <Input type="text" name="id" id="exampleEmail" value={user.id} />
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword">Name</Label>
              <Input
                type="text"
                name="fullname"
                id="examplePassword"
                value={user.fullname}
                onChange={(e) => handleFieldChange(e, "fullname")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePrice">Email</Label>
              <Input
                type="email"
                name="email"
                id="examplePrice"
                value={user.email}
                onChange={(e) => handleFieldChange(e, "email")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleQuantity">Gender</Label>
              <Input
                type="number"
                name="quantity"
                id="exampleQuantity"
                value={user.gender}
                onChange={(e) => handleFieldChange(e, "gender")}
              />
            </FormGroup>
            <FormGroup tag="fieldset" className="radioGr-user">
            <Label for="exampleQuantity">Gender</Label>
              <FormGroup check class="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio1" value="false"/> MALE
                </Label>
              </FormGroup>
              <FormGroup check class="radioBtn-user">
                <Label check>
                  <Input type="radio" name="radio1" value="true"/> FEMALE
                </Label>
              </FormGroup>
              
            </FormGroup>
            <FormGroup>
              <Label for="exampleBrand">Day of Birth</Label>
              <Input
                type="date"
                name="dob"
                id="exampleBrand"
                value={user.dob}
                onChange={(e) => handleFieldChange(e, "dob")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleEmail">Phonenumber</Label>
              <Input
                type="number"
                name="phonenumber"
                id="exampleEmail"
                value={user.phonenumber}
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword">Address</Label>
              <Input
                type="text"
                name="address"
                id="examplePassword"
                value={user.address}
                onChange={(e) => handleFieldChange(e, "address")}
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
