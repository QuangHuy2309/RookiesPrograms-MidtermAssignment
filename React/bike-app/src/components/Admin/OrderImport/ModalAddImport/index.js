import React, { useState, useEffect } from "react";
import { get, post } from "../../../../Utils/httpHelper";
import { getCookie } from "../../../../Utils/Cookie";
import { numberFormat } from "../../../../Utils/ConvertToCurrency";
import "./ModalAddImport.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { IoAddOutline } from "react-icons/io5";
import { GrBike } from "react-icons/gr";
import { AiOutlineAppstore, AiOutlineClose } from "react-icons/ai";
import * as XLSX from "xlsx";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  Form,
  FormGroup,
  Label,
  Input,
  Table,
  Row,
  Col,
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
} from "reactstrap";

toast.configure();
const ModalExample = (props) => {
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [totalProdList, setTotalProdList] = useState([]);
  const [prodPickedList, setProdPickedList] = useState([]);
  const [modal, setModal] = useState(false);
  const [base64, setBase64] = useState("");
  const [choice, setChoice] = useState("1");
  const [dropdownOpen, setOpen] = useState(false);
  const [total, setTotal] = useState(0);
  const toggle = () => setModal(!modal);
  const toggleDropdown = () => setOpen(!dropdownOpen);

  useEffect(() => {
    if (modal) {
      setChoice(1);
      setProdPickedList([]);
      setTotal(0);
      get("/public/categories").then((response) => {
        if (response.status === 200) {
          setCateList([...response.data]);
        }
      });
      getProductList();
    }
  }, [modal]);

  useEffect(() => {
    getTotalProductList();
  }, [cateList]);

  useEffect(() => {
    getProductList();
  }, [choice]);

  useEffect(() => {
    getTotalPrice(prodPickedList);
  }, [prodPickedList]);

  async function getTotalProductList() {
    setProdList([]);
    cateList.map((cate) => {
      get(`/public/product`).then((response) => {
        if (response.status === 200) {
          setTotalProdList([...response.data]);
        }
      });
    });
  }
  async function getProductList() {
    setProdList([]);
    get(`/public/product/${choice}`).then((response) => {
      if (response.status === 200) {
        setProdList([...response.data]);
      }
    });
  }
  async function handleProdFieldChange(e, key, index) {
    let list = [...prodPickedList];
    let prod = { ...list[index] };
    if (key === "quantity") prod.quantity = e.target.value;
    else if (key === "price") prod.price = e.target.value;
    list[index] = prod;
    setProdPickedList(list);
  }
  function toArr() {
    let arr = [];
    prodPickedList.forEach((prod) => {
      let item = {
        productId: prod.id,
        amount: prod.quantity,
        unitprice: prod.price,
      };
      arr = [...arr, item];
    });
    return arr;
  }
  function handleSubmit(e) {
    e.preventDefault();
    if (prodPickedList.length < 1) {
      toast.error("You need add at least one product!", {
        position: toast.POSITION.TOP_RIGHT,
        autoClose: 3000,
      });
    } else {
      const emailUser = getCookie("email");
      const body = JSON.stringify({
        employeeEmail: emailUser,
        // totalCost: total,
        status: true, //e.target.status.value,
        orderImportDetails: toArr(),
      });
      post("/imports", body)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Add successfully!!!", {
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
          console.log(error);
        });
    }
  }

  async function getTotalPrice(list) {
    let totalCost = 0;
    list.map((prod) => {
      totalCost += prod.price * prod.quantity;
    });
    setTotal(totalCost);
  }
  async function handleProdPick(id) {
    let check = prodPickedList.some((prod) => prod.id === id);
    if (!check)
      prodList
        .filter((prod) => prod.id === id)
        .map((filtered) => {
          filtered.quantity = 1;
          filtered.price = 0;
          setProdPickedList((oldArr) => [...oldArr, filtered]);
        });
  }
  async function handleProdPickRemove(index) {
    let list = [...prodPickedList];
    list.splice(index, 1);
    setProdPickedList(list);
  }
  function handleSearchChange(e) {
    setProdList([]);
    get(
      `/public/product/search?keyword=${e.target.value.trim()}&type=${choice}`
    ).then((response) => {
      if (response.status === 200) {
        setProdList([...response.data]);
      }
    });
  }
  function findDuplicateProd(id) {
    return prodPickedList.findIndex((prod) => prod.id === id);
  }
  function findExistProd(id) {
    return totalProdList.findIndex((prod) => prod.id === id);
  }

  function uploadProd(e) {
    const file = e.target.files[0];
    const promise = new Promise((resolve, reject) => {
      const fileReader = new FileReader();
      fileReader.readAsArrayBuffer(file);
      fileReader.onload = (evt) => {
        const bufferArray = evt.target.result;
        const wb = XLSX.read(bufferArray, { type: "buffer" });
        /* Get first worksheet */
        const wsname = wb.SheetNames[0];
        const ws = wb.Sheets[wsname];
        /* Convert array of arrays */
        const data = XLSX.utils.sheet_to_json(ws);
        /* Update state */
        resolve(data);
      };
      fileReader.onerror = (error) => {
        reject(error);
      };
    });
    promise.then((data) => {
      // console.log(data);
      data.forEach((prod_data) => {
        let checkExist = findExistProd(prod_data.id);
        if (checkExist >= 0) {
          // let prod = findDuplicateProd(prod_data.id);
          const check = findDuplicateProd(prod_data.id);
          if (check >= 0) {
            let list = [...prodPickedList];
            let prod = { ...list[check] };
            prod.quantity = prod_data.amount;
            prod.price = prod_data.price;
            list[check] = prod;
            setProdPickedList(list);
          } else {
            let prod = {
              id: prod_data.id,
              name: prod_data.name,
              quantity: prod_data.amount,
              price: prod_data.price,
            };
            setProdPickedList((oldArr) => [...oldArr, prod]);
          }
        } else {
          toast.error(
            `Product with ID:"${prod_data.id}" not exist. \nPlease create it first!`,
            {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            }
          );
        }
      });
    });
  }

  return (
    <div>
      <div className="btn-modal-AddImport">
        <Button color="info" onClick={toggle} className="btn-modal">
          <IoAddOutline /> Add new Import Order
        </Button>
      </div>
      <Modal isOpen={modal} toggle={toggle} size="lg">
        <ModalHeader toggle={toggle} className="title-OrderImport">
          <GrBike /> Import Order Information
        </ModalHeader>
        <ModalBody>
          <Row className="mb-2">
            <Col className="col-7">
              <ButtonDropdown
                isOpen={dropdownOpen}
                toggle={toggleDropdown}
                className="mb-3"
              >
                <DropdownToggle caret>
                  <AiOutlineAppstore />
                  {cateList
                    .filter((cate) => cate.id === choice)
                    .map((filtered) => (
                      <> {filtered.name}</>
                    ))}
                </DropdownToggle>
                <DropdownMenu>
                  {cateList.map((cate, index) => (
                    <div key={cate.id}>
                      <DropdownItem onClick={() => setChoice(cate.id)}>
                        {cate.name}
                      </DropdownItem>
                      {index < cateList.length - 1 ? (
                        <DropdownItem divider />
                      ) : null}
                    </div>
                  ))}
                </DropdownMenu>
              </ButtonDropdown>{" "}
            </Col>

            <Col className="searchField-AddImport mx-3">
              <Input
                type="email"
                name="email"
                id="exampleEmail"
                required="required"
                placeholder="Search Product by name"
                onChange={(e) => handleSearchChange(e)}
              />
            </Col>
          </Row>
          <Label>Import: </Label>
          <Input
            type="file"
            name="file"
            id="exampleFile"
            onChange={(e) => {
              uploadProd(e);
            }}
            className="ms-3 mb-3"
          />
          <div className="scrollable">
            {prodList.map((prod, index) => (
              <Row className="mb-3">
                <Col className="col-9">
                  <Label>
                    <img
                      src={`data:image/jpeg;base64,${prod.photo}`}
                      className="img-cart"
                    />
                    {prod.name}
                  </Label>
                </Col>
                <Col className="btn-addProd">
                  <Button
                    outline
                    color="info"
                    onClick={() => handleProdPick(prod.id)}
                  >
                    {" "}
                    <IoAddOutline /> Add
                  </Button>
                </Col>
              </Row>
            ))}
          </div>
          <Form onSubmit={(e) => handleSubmit(e)}>
            <Table bordered>
              <thead>
                <tr>
                  <th className="titleTable-OrderImportAdmin">ID</th>
                  <th className="titleTable-OrderImportAdmin">PRODUCT NAME</th>
                  <th className="titleTable-OrderImportAdmin">QUANTITY</th>
                  <th className="titleTable-OrderImportAdmin">UNIT COST</th>
                  <th className="titleTable-OrderImportAdmin">ACTION</th>
                </tr>
              </thead>
              <tbody>
                {prodPickedList.map((prod, index) => (
                  <tr key={prod.id}>
                    <td>{prod.id}</td>
                    <td className="prodName-title">{prod.name}</td>
                    <td>
                      <FormGroup>
                        <Input
                          type="number"
                          name="quantity"
                          id="examplePrice"
                          required="required"
                          min="1"
                          value={prod.quantity}
                          onChange={(e) =>
                            handleProdFieldChange(e, "quantity", index)
                          }
                        />
                      </FormGroup>
                    </td>
                    <td>
                      <FormGroup>
                        <Input
                          type="number"
                          name="price"
                          id="examplePrice"
                          required="required"
                          step="1"
                          min="50"
                          value={prod.price}
                          onChange={(e) =>
                            handleProdFieldChange(e, "price", index)
                          }
                        />
                      </FormGroup>
                    </td>
                    <td>
                      <Button
                        color="danger"
                        className="btn-removeProd"
                        onClick={() => handleProdPickRemove(index)}
                      >
                        <AiOutlineClose />
                      </Button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
            {/* <FormGroup tag="fieldset" className="radioGr-user">
              <Label for="exampleQuantity">Status</Label>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="status" value="false" required />{" "}
                  Draft
                </Label>
              </FormGroup>
              <FormGroup check className="radioBtn-user">
                <Label check>
                  <Input type="radio" name="status" value="true" /> Completed
                </Label>
              </FormGroup>
            </FormGroup> */}
            <Row>
              <Col className="priceTotal-OrderAddImport">
                <h4 className="priceTitle-OrderAddImport">Total: </h4>
                <h4 className="priceNum-OrderAddImport">
                  {numberFormat(total)}
                </h4>
              </Col>
            </Row>
            <hr />
            <Button color="primary" type="submit">
              <IoAddOutline /> ADD
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
