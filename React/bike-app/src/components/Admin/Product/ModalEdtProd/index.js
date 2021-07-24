import React, { useState, useEffect } from "react";
import { get, put } from "../../../../Utils/httpHelper";
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
  const [cateList, setCateList] = useState([]);
  const [modal, setModal] = useState(false);
  const [prod, setProd] = useState(Object);
  const [base64, setBase64] = useState("");
  const toggle = () => setModal(!modal);
  function handleFieldChange(e, key) {
    setProd({ [key]: e.target.value });
  }
  useEffect(() => {
    if (modal) {
      getProd();
      get("/public/categories").then((response) => {
        if (response.status === 200) {
          setCateList([...response.data]);
        }
      });
    }
  }, [modal]);

  function getProd() {
    get(`/public/product/search/${id}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        setProd(response.data);
        setBase64(`data:image/jpeg;base64,${response.data.photo}`);
      }
    });
  }

  const uploadImage = async (e) => {
    const file = e.target.files[0];
    const base64 = await convertBase64(file);
    setBase64(base64);
  };
  const convertBase64 = (file) => {
    return new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsDataURL(file);

      fileReader.onload = () => {
        resolve(fileReader.result);
      };

      fileReader.onerror = (error) => {
        reject(error);
      };
    });
  };

  function handleSubmit(e) {
    e.preventDefault();

    const byteArr = base64.split(",");
    let photo = byteArr[1];

    console.log("SUBMIT");
    // const body = JSON.stringify({
    //   id: e.target.id.value,
    //   name: e.target.name.value,
    //   price: e.target.price.value,
    //   quantity: e.target.quantity.value,
    //   description: e.target.description.value,
    //   brand: e.target.brand.value,
    //   createDate: e.target.createDate.value,
    //   categoriesId: e.target.select.value,
    //   photo: photo,
    // });
    // console.log(body);

    // put("/product", body)
    //   .then((response) => {
    //     console.log(response.data);
    //     alert("EDIT SUCCESS");
    //   })
    //   .catch((error) => console.log(error));
  }
  return (
    <div>
      <Button color="warning" onClick={toggle}>
        EDIT
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>Product Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail">ID</Label>
              <Input type="text" name="id" id="exampleEmail" value={prod.id} disabled />
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword">Name</Label>
              <Input
                type="text"
                name="name"
                id="examplePassword"
                value={prod.name}
                required="required"
                onChange={(e) => handleFieldChange(e, "name")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePrice">Price</Label>
              <Input
                type="number"
                name="price"
                id="examplePrice"
                value={prod.price}
                required="required"
                min="0"
                onChange={(e) => handleFieldChange(e, "price")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleQuantity">Quantity</Label>
              <Input
                type="number"
                name="quantity"
                id="exampleQuantity"
                value={prod.quantity}
                required="required"
                min="0"
                onChange={(e) => handleFieldChange(e, "quantity")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleCreateDay">Create Day</Label>
              <Input
                type="text"
                name="createDate"
                id="exampleCreateDay"
                value={prod.createDate}
                disabled
                required="required"
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleSelect">Type</Label>
              <Input type="select" name="select" id="exampleSelect" required>
                {cateList.map((cate) => (
                  <option
                    key={cate.id}
                    value={cate.id}
                    label={cate.name}
                  ></option>
                ))}
              </Input>
            </FormGroup>
            <FormGroup>
              <Label for="exampleBrand">Brand</Label>
              <Input
                type="text"
                name="brand"
                id="exampleBrand"
                value={prod.brand}
                required="required"
                onChange={(e) => handleFieldChange(e, "brand")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleText">Description</Label>
              <Input
                type="textarea"
                name="description"
                id="exampleText"
                value={prod.description}
                required
                onChange={(e) => handleFieldChange(e, "description")}
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleFile">File</Label>
              <br />
              <Input
                type="file"
                name="file"
                id="exampleFile"
                accept=".jpeg, .png, .jpg"
                onChange={(e) => {
                  uploadImage(e);
                }}
              />
              <img src={base64} height="250px"></img>
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
