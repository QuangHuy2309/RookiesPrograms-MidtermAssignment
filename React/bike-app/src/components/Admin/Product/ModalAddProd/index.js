import React, { useState, useEffect } from "react";
import { get, post, getWithAuth } from "../../../../Utils/httpHelper";
import "./ModalAddProd.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { IoAddOutline } from "react-icons/io5";
import { GrBike } from "react-icons/gr";
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
const ModalExample = (props) => {
  const [cateList, setCateList] = useState([]);
  const [modal, setModal] = useState(false);
  const [base64, setBase64] = useState("");
  const [checkId, setCheckId] = useState(false);
  const [idError, setIdError] = useState("");
  const [checkName, setCheckName] = useState(false);
  const [nameError, setNameError] = useState("");
  const [brandError, setBrandError] = useState("");
  const [checkBrand, setCheckBrand] = useState(true);
  const [id, setId] = useState("");

  const toggle = () => setModal(!modal);

  useEffect(() => {
    if (modal) {
      get("/public/categories").then((response) => {
        if (response.status === 200) {
          setCateList([...response.data]);
        }
      });
      setId("");
      setNameError("");
      setIdError("");
      setBrandError("");
      setBase64("");
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
  async function handleFieldChange(e, key) {
    if (key === "id") {
      if (e.target.value.trim() == "") {
        setIdError("Product ID must not blank");
        setId(e.target.value);
        // setCheckId(false);
      } 
      else if (e.target.value.includes('#')) {
        // e.target.value.replace(/\D/, "")
      }
      else {
        setIdError("");
        setId(e.target.value);
        // setCheckId(true);
      }
    } else if (key === "name") {
      if (e.target.value.trim() == "") {
        setNameError("Product name must not blank");
        // setCheckName(false);
      } else {
        setNameError("");
        // setCheckName(true);
      }
    } else if (key === "brand") {
      if (e.target.value.trim() == "") {
        setBrandError("Product brand must not blank");
        // setCheckBrand(false);
      } else {
        setBrandError("");
        // setCheckBrand(true);
      }
    }
  }
  // const checkExistProdId = async (id) => {
  async function checkExistProdId(id) {
      getWithAuth(`/product/checkExistId/${id}`).then((response) => {
        if (response.status === 200) {
          if (response.data) {
            setCheckId(true);
          } else {
            setIdError("Product id is duplicated");
          }
        }
      });
    
  }

  async function checkExistProdName(name) {
      getWithAuth(`/product/checkExistName/${name}`).then((response) => {
        if (response.status === 200) {
          if (response.data) {
            setNameError("");
            setCheckName(true);
          } else {
            setCheckName(false);
            setNameError("Product name is duplicated");
          }
        }
      });
    
  }
  function handleSubmit(e) {
    e.preventDefault();
    const id = e.target.id.value.trim();
    const name = e.target.name.value.trim();
    checkExistProdId(id);
    checkExistProdName(name);
    const check = checkId && checkName && checkBrand;
    if (check) {
      let photo;
      if (e.target.file.files.length !== 0) {
        const byteArr = base64.split(",");
        photo = byteArr[1];
      }
      const body = JSON.stringify({
        id: e.target.id.value,
        name: e.target.name.value,
        price: e.target.price.value,
        quantity: 0,
        description: e.target.description.value,
        brand: e.target.brand.value,
        createDate: new Date(),
        categoriesId: e.target.select.value,
        photo: photo,
      });
      // console.log(body);

      post("/product", body)
        .then((response) => {
          if(response.status === 200) {toast.success("Add successfully!!!", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          props.onAdd(true);
          toggle();
        } 
        })
        .catch((error) => {
          toast.error("Add failed, please check again", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          console.log(error)});
    }
  }
  return (
    <>
      <Button color="info" onClick={toggle} className="ms-3">
       <IoAddOutline/> Add new Product
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle} className="title-AddProductAdmin"><GrBike/> Product Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleID" className="titleTale-ProductAdmin">ID</Label>
              <Input
                type="text"
                name="id"
                id="exampleID"
                required="required"
                value={id}
                onChange={(e) => handleFieldChange(e, "id")}
              />
              <div style={{ color: "red" }}>{idError}</div>
            </FormGroup>
            <FormGroup>
              <Label for="examplePassword" className="titleTale-ProductAdmin">Name</Label>
              <Input
                type="text"
                name="name"
                id="examplePassword"
                required="required"
                onChange={(e) => handleFieldChange(e, "name")}
              />
              <div style={{ color: "red" }}>{nameError}</div>
            </FormGroup>
            <FormGroup>
              <Label for="examplePrice" className="titleTale-ProductAdmin">Price</Label>
              <Input
                type="number"
                name="price"
                id="examplePrice"
                required="required"
                min="50"
                step="1"
              />
            </FormGroup>
            {/* <FormGroup>
              <Label for="exampleQuantity" className="titleTale-ProductAdmin">Quantity</Label>
              <Input
                type="number"
                name="quantity"
                id="exampleQuantity"
                required="required"
                min="0"
              />
            </FormGroup> */}
            <FormGroup>
              <Label for="exampleSelect" className="titleTale-ProductAdmin">Type</Label>
              <Input
                type="select"
                name="select"
                id="exampleSelect"
                required="required"
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
              <Label for="exampleBrand" className="titleTale-ProductAdmin">Brand</Label>
              <Input
                type="text"
                name="brand"
                id="exampleBrand"
                required="required"
                onChange={(e) => handleFieldChange(e, "brand")}
              />
              <div style={{ color: "red" }}>{brandError}</div>
            </FormGroup>
            <FormGroup>
              <Label for="exampleText" className="titleTale-ProductAdmin">Description</Label>
              <Input type="textarea" name="description" id="exampleText" required="required" />
            </FormGroup>
            <FormGroup>
              <Label for="exampleFile" className="titleTale-ProductAdmin">File</Label>
              <br />
              <Input
                type="file"
                name="file"
                id="exampleFile"
                accept=".jpeg, .png, .jpg"
                required="required"
                onChange={(e) => {
                  uploadImage(e);
                }}
              />
              <img src={base64} height="250px"></img>
            </FormGroup>
            <br />
            
            <Button color="primary" type="submit">
            <IoAddOutline/> ADD
            </Button>{" "}
            <Button color="secondary" onClick={toggle}>
              Cancel
            </Button>
          </Form>
        </ModalBody>
      </Modal>
    </>
  );
};

export default ModalExample;
