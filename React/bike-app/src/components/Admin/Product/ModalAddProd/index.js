import React, { useState, useEffect } from "react";
import { get, post } from "../../../../Utils/httpHelper";
import "./ModalAddProd.css";
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

const ModalExample = (props) => {
  const { buttonLabel, id } = props;
  const [cateList, setCateList] = useState([]);
  const [modal, setModal] = useState(false);
  const [base64, setBase64] = useState("");
  const toggle = () => setModal(!modal);

  useEffect(() => {
    if (modal) {
      get("/public/categories").then((response) => {
        if (response.status === 200) {
          setCateList([...response.data]);
        }
      });
    }
  }, [modal]);
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

  // function getBase64(file) {
  //   return new Promise((resolve) => {
  //     let fileInfo;
  //     let baseURL = "";
  //     // Make new FileReader
  //     let reader = new FileReader();
  //     // Convert the file to base64 text
  //     reader.readAsDataURL(file);
  //     // on reader load somthing...
  //     reader.onload = (readerEvt) => {
  //       // Make a fileInfo Object
  //       let binaryString = readerEvt.target.result;
  //       setBase64(btoa(binaryString));
  //       baseURL = reader.result;
  //       resolve(baseURL);
  //     };
  //     console.log(fileInfo);
  //   });
  // }
  // function changeFile(e){
  //   if (e.target.files.length !== 0) {
  //     console.log("CO FILE");
  //     let file = e.target.files[0];
  //     getBase64(file)
  //       .then((result) => {
  //         file["base64"] = result;
  //       })
  //       .catch((err) => {
  //         console.log(err);
  //       });
  //   }
  //   else{ setBase64(prod.photo)}
  // }

  function handleSubmit(e) {
    e.preventDefault();
    let photo;
    if (e.target.file.files.length !== 0) {
      const byteArr = base64.split(",");
      photo = byteArr[1];
    } 
    // console.log(photo);
    const body = JSON.stringify({
      id: e.target.id.value,
      name: e.target.name.value,
      price: e.target.price.value,
      quantity: e.target.quantity.value,
      description: e.target.description.value,
      brand: e.target.brand.value,
      createDate: new Date(),
      categoriesId: e.target.select.value,
      photo: photo,
    });
    console.log(body);

    post("/product", body)
      .then((response) => {
        console.log(response.data);
        alert("ADD NEW PRODUCT SUCCESS");
      })
      .catch((error) => console.log(error));
  }
  return (
    <div>
      <Button color="primary" onClick={toggle} className="open-prod">
        Add new Product
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>Product Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail">ID</Label>
              <Input type="text" name="id" id="exampleEmail" required/>
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword">Name</Label>
              <Input
                type="text"
                name="name"
                id="examplePassword"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="examplePrice">Price</Label>
              <Input
                type="number"
                name="price"
                id="examplePrice"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleQuantity">Quantity</Label>
              <Input
                type="number"
                name="quantity"
                id="exampleQuantity"
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleSelect">Type</Label>
              <Input
                type="select"
                name="select"
                id="exampleSelect"
                required
              >
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
                required
              />
            </FormGroup>
            <FormGroup>
              <Label for="exampleText">Description</Label>
              <Input
                type="textarea"
                name="description"
                id="exampleText"
                
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

export default ModalExample;
