import React, { useState, useEffect } from "react";
import { get, put, getWithAuth } from "../../../../Utils/httpHelper";
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
  const { id } = props;
  const [cateList, setCateList] = useState([]);
  const [modal, setModal] = useState(false);
  const [prod, setProd] = useState(Object);
  const [base64, setBase64] = useState("");
  const toggle = () => setModal(!modal);
  const [checkName, setCheckName] = useState(true);
  const [nameError, setNameError] = useState("");
  const [brandError, setBrandError] = useState("");
  const [checkBrand, setCheckBrand] = useState(true);

  useEffect(() => {
    if (modal) {
      getProd();
      get("/public/categories").then((response) => {
        if (response.status === 200) {
          setCateList([...response.data]);
        }
      });
      setNameError("");
      setBrandError("");
    }
  }, [modal]);
  function handleFieldChange(e, key) {
    setProd({ [key]: e.target.value });
    if (key === "name") {
      if (e.target.value.trim() == "") {
        setNameError("Product name must not blank");
        setCheckName(false);
      } else {
        setNameError("");
        setCheckName(true);
      }
    } else if (key === "brand") {
      if (e.target.value.trim() == "") {
        setBrandError("Product brand must not blank");
        setCheckBrand(false);
      } else {
        setBrandError("");
        setCheckBrand(true);
      }
    }
  }
  function checkNameProd(name, id) {
    if (checkName && checkBrand)
    getWithAuth(`/product/checkExistNameUpdate?name=${name}&id=${id}`).then(
      (response) => {
        if (response.status === 200) {
          if (response.data) {
            setNameError("");
            setCheckName(true);
          } else {
            setNameError("Product name is duplicated");
            setCheckName(false);
          }
        }
      }
    );
  }
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
    checkNameProd(e.target.name.value.trim(), e.target.id.value);
    const check = checkBrand && checkName;
    if (check) {
      const byteArr = base64.split(",");
      let photo = byteArr[1];

      console.log("SUBMIT");
      const body = JSON.stringify({
        id: e.target.id.value,
        name: e.target.name.value.trim(),
        price: e.target.price.value,
        quantity: e.target.quantity.value,
        description: e.target.description.value.trim(),
        brand: e.target.brand.value.trim(),
        createDate: e.target.createDate.value,
        categoriesId: e.target.select.value,
        photo: photo,
      });
      // console.log(body);

      put(`/product/${props.id}`, body)
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
    }
  }
  return (
    <div>
      <Button color="warning" onClick={toggle}>
        <MdModeEdit/>
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}><MdModeEdit/> Product Information</ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <FormGroup>
              <Label for="exampleEmail">ID</Label>
              <Input
                type="text"
                name="id"
                id="exampleEmail"
                value={prod.id}
                disabled
              />
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
              <div style={{ color: "red" }}>{nameError}</div>
            </FormGroup>
            <FormGroup>
              <Label for="examplePrice">Price</Label>
              <Input
                type="number"
                name="price"
                id="examplePrice"
                value={prod.price}
                required="required"
                min="1000"
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
              <div style={{ color: "red" }}>{brandError}</div>
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
