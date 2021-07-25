import React, { useState, useEffect } from "react";
import { getWithAuth, put } from "../../../../Utils/httpHelper";
import "./ModalEdtUser.css";
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
    const body = JSON.stringify({
      id: e.target.id.value,
      fullname: e.target.fullname.value,
      email: e.target.email.value,
      gender: e.target.radio.value,
      dob: e.target.dob.value,
      phonenumber: e.target.phonenumber.value,
      address: e.target.address.value,
    });
    console.log(body);
    // console.log(e.target.dob.value);

    put("/persons", body)
      .then((response) => {
        console.log(response.data);
        if(response.data === "SUCCESS")  toast.success("Edit successfully!!!", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
      })
      .catch((error) => {
        toast.error("Add failed, please check again", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
        console.log(error);
      });
    props.onEdit(e);
  }
  return (
    <div>
      <Button color="warning" onClick={toggle}>
        <MdModeEdit/>
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}><MdModeEdit/> User Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail">ID</Label>
              <Input type="text" name="id" id="exampleEmail" value={user.id} required />
            </FormGroup>
            <FormGroup>
              <Label for="exampleFullName">Name</Label>
              <Input
                type="text"
                name="fullname"
                id="exampleFullName"
                value={user.fullname}
                required
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
                required
                onChange={(e) => handleFieldChange(e, "email")}
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
                value={user.dob}//{format(new Date(user.dob), "YYYY-MM-DD")}
                //{user.dob.toISOString().substr(0,10)}
                onChange={(e) => handleFieldChange(e, "dob")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleEmail">Phonenumber</Label>
              <Input
                type="number"
                name="phonenumber"
                id="exampleEmail"
                required
                value={user.phonenumber}
                onChange={(e) => handleFieldChange(e, "phonenumber")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword">Address</Label>
              <Input
                type="text"
                name="address"
                id="examplePassword"
                value={user.address}
                required
                onChange={(e) => handleFieldChange(e, "address")}
              />
            </FormGroup>
            <br />
            <Button outline color="warning" type="submit">
            <MdModeEdit/> Edit
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
