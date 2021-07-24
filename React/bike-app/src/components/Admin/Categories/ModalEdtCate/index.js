import React, { useState, useEffect } from "react";
import { getWithAuth, post, put } from "../../../../Utils/httpHelper";
import "./ModalEdtCate.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
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

toast.configure();
const ModalEdt = (props) => {
  const { id } = props;
  const [modal, setModal] = useState(false);
  const [cate, setCate] = useState(Object);
  const [checkName, setCheckName] = useState(true);
  const [nameError, setNameError] = useState("");
  const toggle = () => setModal(!modal);

  useEffect(() => {
    if (modal) {
      getWithAuth(`/categories/${id}`).then((response) => {
        if (response.status === 200) {
          // console.log(response.data);
          setCate(response.data);

        }
      });
      setNameError("");
    }
  }, [modal]);
  function handleFieldChange(e, key) {
    setCate({ [key]: e.target.value });
    if (key === "name") {
      if (e.target.value.trim() == "") {
        setNameError("Categories name must not blank");
      }
      else {
        setNameError("");
        setCheckName(true);
      }
    }
  }

  function checkNameCate(name, id) {
    getWithAuth(`/categories/checkName?name=${name}&id=${id}`).then(
      (response) => {
        if (response.status === 200) {
          if (response.data) setNameError("");
          else {
            setNameError("Categories name is duplicated. Choose another name");
            setCheckName(false);
          }
        }
      }
    );
  }
  function handleSubmit(e) {
    e.preventDefault();
    const name = e.target.name.value.trim();
    const id = e.target.id.value;
    checkNameCate(name, id);
    if (checkName) {
      const body = JSON.stringify({
        id: e.target.id.value,
        name: e.target.name.value,
        description: e.target.description.value,
      });
      put(`/categories/${id}`, body)
        .then((response) => {
          console.log(response.data);
          if(response.data === "SUCCESS")  toast("Edit successfully!!!", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
      })
      .catch((error) => console.log(error));
    }
  }
  return (
    <div>
      <Button color="warning" onClick={toggle}>
        Edit
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>Categories Information</ModalHeader>
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
                disabled
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
              <div style={{ color: "red" }}>{nameError}</div>
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

export default ModalEdt;
