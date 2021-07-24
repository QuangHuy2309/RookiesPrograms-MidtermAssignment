import React, { useState, useEffect } from "react";
import { getWithAuth, post } from "../../../../Utils/httpHelper";
import "./ModalAddCate.css";
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
const ModalAdd = (props) => {
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);
  const [checkName, setCheckName] = useState(true);
  const [nameError, setNameError] = useState("");

  useEffect(() => {
    if (modal) {
      setNameError("");
    }
  }, [modal]);

  function handleSubmit(e) {
    e.preventDefault();
    checkNameCate(e.target.name.value);
    const body = JSON.stringify({
      name: e.target.name.value,
      description: e.target.description.value,
    });
    console.log(body);
    // console.log(e.target.dob.value);

    post("/categories", body)
      .then((response) => {
        console.log(response.data);
        if (response.data === "SUCCESS")
          toast("Add successfully!!!", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
      })
      .catch((error) => console.log(error));
  }

  function checkNameCate(name, id) {
    getWithAuth(`/categories/checkName?name=${name}&id=0`).then((response) => {
      if (response.status === 200) {
        if (response.data) {
          setNameError("");
          setCheckName(true);
        } else {
          setNameError("Categories name is duplicated. Choose another name");
          setCheckName(false);
        }
      }
    });
  }
  function handleFieldChange(e) {
    if (e.target.value.trim() == "") {
      setNameError("Categories name must not blank");
    } else {
      setNameError("");
    }
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
                onChange={(e) => handleFieldChange(e)}
              />
              <div style={{ color: "red" }}>{nameError}</div>
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
