import React, { useState, useEffect } from "react";
import { getWithAuth, post } from "../../../../Utils/httpHelper";
import "./ModalAddCate.css";
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



  function handleSubmit(e) {
    e.preventDefault();
    const body = JSON.stringify({
      name: e.target.name.value,
      description: e.target.description.value,
    });
    console.log(body);
    // console.log(e.target.dob.value);

    post("/categories", body)
      .then((response) => {
        console.log(response.data);
        alert("ADDING SUCCESS");
      })
      .catch((error) => console.log(error));
    // props.onEdit(e);
  }
  return (
    <div>
      <div className="btn-modal">
      <Button outline color="info" onClick={toggle} className="btn-modal">
        ADD
      </Button>
      </div>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>Cate Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            
            <FormGroup>
              <Label for="exampleFullName">Name</Label>
              <Input
                type="text"
                name="name"
                id="exampleFullName"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleText">Description</Label>
              <Input
                type="textarea"
                name="description"
                id="exampleText"
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
