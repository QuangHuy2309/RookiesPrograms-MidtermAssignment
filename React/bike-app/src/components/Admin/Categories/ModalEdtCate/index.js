import React, { useState, useEffect } from "react";
import { getWithAuth, post } from "../../../../Utils/httpHelper";
import "./ModalEdtCate.css";
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
  const { id } = props;
  const [modal, setModal] = useState(false);
  const [cate, setCate] = useState(Object);
  const toggle = () => setModal(!modal);

  function handleFieldChange(e, key) {
    setCate({ [key]: e.target.value });
  }
  useEffect(() => {
    if (modal) {
      getWithAuth(`/categories/${id}`).then((response) => {
        if (response.status === 200) {
          // console.log(response.data);
          setCate(response.data);
        }
      });
    }
  }, [modal]);

  function handleSubmit(e) {
    e.preventDefault();
    const body = JSON.stringify({
      id: e.target.id.value,
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
      <Button  color="warning" onClick={toggle} >
        Edit
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>Cate Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail">ID</Label>
              <Input
                type="text"
                name="id"
                id="exampleEmail"
                value={cate.id}
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleFullName">Name</Label>
              <Input
                type="text"
                name="name"
                id="exampleFullName"
                value={cate.name}
                required
                onChange={(e) => handleFieldChange(e, "name")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleText">Description</Label>
              <Input
                type="textarea"
                name="description"
                id="exampleText"
                value={cate.description}
                required
                onChange={(e) => handleFieldChange(e, "description")}
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
