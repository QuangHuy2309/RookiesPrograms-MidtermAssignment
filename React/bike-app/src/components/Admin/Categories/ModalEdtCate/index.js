import React, { useState, useEffect } from "react";
import { getWithAuth, put } from "../../../../Utils/httpHelper";
import "./ModalEdtCate.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { MdModeEdit } from "react-icons/md";
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
const ModalEdt = (props) => {
  const { id } = props;
  const [modal, setModal] = useState(false);
  const [cate, setCate] = useState(Object);
  const [checkName, setCheckName] = useState(false);
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
      setCheckName(false);
    }
  }, [modal]);
  function handleFieldChange(e, key) {
    setCate({ [key]: e.target.value });
    if (key === "name") {
      if (e.target.value.trim() == "") {
        setNameError("Categories name must not blank");
      } else {
        setNameError("");
      }
    }
  }

  function checkNameCate(name, id, des) {
    getWithAuth(`/categories/checkName?name=${name}&id=${id}`).then(
      (response) => {
        if (response.status === 200) {
          if (response.data) {
            const body = JSON.stringify({
              id: id,
              name: name,
              description: des,
            });
            put(`/categories/${id}`, body)
              .then((response) => {
                console.log(response.data);
                if (response.data === "SUCCESS")
                  toast.success("Edit successfully!!!", {
                    position: toast.POSITION.TOP_RIGHT,
                    autoClose: 3000,
                  });
                props.onEdit("true");
                toggle();
              })
              .catch((error) => {
                console.log(error);
                toast.error(`Error: ${error}`, {
                  position: toast.POSITION.TOP_RIGHT,
                  autoClose: 3000,
                });
              });
          } else {
            setNameError("Categories name is duplicated. Choose another name");
            setCheckName(false);
          }
        }
      }
    );
  }
  async function handleSubmit(e) {
    e.preventDefault();
    const name = e.target.name.value.trim();
    const id = e.target.id.value;
    const des = e.target.description.value.trim();
    if (nameError === "") checkNameCate(name, id, des);
    // const check = nameError === "" && checkName;
    // if (check) {
    //   const body = JSON.stringify({
    //     id: e.target.id.value,
    //     name: name,
    //     description: e.target.description.value,
    //   });
    //   put(`/categories/${id}`, body)
    //     .then((response) => {
    //       console.log(response.data);
    //       if (response.data === "SUCCESS")
    //         toast.success("Edit successfully!!!", {
    //           position: toast.POSITION.TOP_RIGHT,
    //           autoClose: 3000,
    //         });
    //       props.onEdit("true");
    //       toggle();
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //       toast.error(`Error: ${error}`, {
    //         position: toast.POSITION.TOP_RIGHT,
    //         autoClose: 3000,
    //       });
    //     });
    // }
  }
  return (
    <div>
      <Button color="warning" onClick={toggle}>
        <MdModeEdit />
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle} className="title-AddCategoryAdmin">
          <MdModeEdit /> Categories Information
        </ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail" className="titleTable-AdminCategory">
                ID
              </Label>
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
              <Label for="exampleFullName" className="titleTable-AdminCategory">
                Name
              </Label>
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
              <Label for="exampleText" className="titleTable-AdminCategory">
                Description
              </Label>
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
            <Button outline color="warning" type="submit">
              <MdModeEdit />
              Edit
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
