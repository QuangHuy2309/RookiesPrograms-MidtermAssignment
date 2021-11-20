import React, { useState, useEffect } from "react";
import { getWithAuth, post } from "../../../../Utils/httpHelper";
import "./ModalAddCate.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AiOutlineAppstoreAdd } from "react-icons/ai";
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
  const [checkName, setCheckName] = useState(false);
  const [nameError, setNameError] = useState("");

  useEffect(() => {
    if (modal) {
      setNameError("");
      setCheckName(false);
    }
  }, [modal]);

  async function handleSubmit(e) {
    e.preventDefault();
    const name = e.target.name.value.trim();
    const des = e.target.description.value.trim();
    if (nameError === "") checkNameCate(name, des);
    // const check = (nameError === "") && checkName;
    // if (check) {
     
    // }
  }

  async function checkNameCate(name,des) {
    getWithAuth(`/categories/checkName?name=${name}&id=0`).then((response) => {
      if (response.status === 200) {
        if (response.data) {
          const body = JSON.stringify({
            name: name,
            description: des,
          });
          post("/categories", body)
            .then((response) => {
              console.log(response.data);
              if (response.data === "SUCCESS")
                toast.success("Add successfully!!!", {
                  position: toast.POSITION.TOP_RIGHT,
                  autoClose: 3000,
                });
                props.onAdd(true);
                toggle();
            })
            .catch((error) => {
            toast.error("Add category failed!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            console.log(error)
          }
            );
        } else {
          setNameError("Categories name is duplicated. Choose another name");
        }
      }
    });
  }
  async function handleFieldChange(e) {
    if (e.target.value.trim() == "") {
      setNameError("Categories name must not blank");
    } else {
      setNameError("");
    }
  }
  return (
    <div>
      <div className="btn-modal">
        <Button color="info" onClick={toggle} className="btn-modal">
          <AiOutlineAppstoreAdd /> ADD
        </Button>
      </div>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle} className="title-AddCategoryAdmin">
          <AiOutlineAppstoreAdd /> Cate Information
        </ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleFullName" className="titleTable-AdminCategory">Name</Label>
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
              <Label for="exampleText" className="titleTable-AdminCategory">Description</Label>
              <Input
                type="textarea"
                name="description"
                id="exampleText"
                required
              />
            </FormGroup>
            <br />
            <Button color="primary" type="submit">
              <AiOutlineAppstoreAdd /> ADD
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
