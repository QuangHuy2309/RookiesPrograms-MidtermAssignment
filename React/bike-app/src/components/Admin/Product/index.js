import React, { useState, useEffect, useRef } from "react";
import { get, del } from "../../../Utils/httpHelper";
import Page from "../../Pagination";
import ModalEdt from "./ModalEdtProd";
import ModalAdd from "./ModalAddProd";
import ModalReview from "./ModalReview";
import { format } from "date-fns";
import { IoReloadSharp } from "react-icons/io5";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import {toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { AiOutlineAppstore } from "react-icons/ai";
import "./Product.css";
import {
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  Table,
  Button,
} from "reactstrap";


toast.configure()
export default function Index() {
  const [choice, setChoice] = useState("1");
  const [pagenum, setPageNum] = useState(0);
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [dropdownOpen, setOpen] = useState(false);

  const size = 4;
  let totalPage = useRef(0);

  const toggle = () => setOpen(!dropdownOpen);

  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    
  }, []);
  useEffect(() => {
    get(`/public/product/numTotal/${choice}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
  },[choice])
  useEffect(() => {
    getListProd();
  }, [choice, pagenum]);
  function getUpdated(e) {
    getListProd();
  }
  function getListProd() {
    get(
      `/public/product/page?pagenum=${pagenum}&size=${size}&type=${choice}`
    ).then((response) => {
      if (response.status === 200) {
        setProdList([...response.data]);
      }
    });
  }
  function handleDelete(e, id) {
    if (e === "OK") {
      del(`/product/${id}`)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Delete successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            getListProd();
          }
        })
        .catch((error) => {
          toast.error(`Error: ${error}`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
        });
    }
  }

  return (
    <>
      <h2 className="title-user">PRODUCT MANAGER</h2>
      <div className="btn-list">
        {" "}
        <ButtonDropdown isOpen={dropdownOpen} toggle={toggle}>
          <DropdownToggle caret><AiOutlineAppstore/>Categories</DropdownToggle>
          <DropdownMenu>
            {cateList.map((cate) => (
              <div key={cate.id}>
                <DropdownItem onClick={() => setChoice(cate.id)}>
                  {cate.name}
                </DropdownItem>
                <DropdownItem divider />
              </div>
            ))}
          </DropdownMenu>
        </ButtonDropdown>{" "}
        <ModalAdd />
        <Button outline color="link" onClick={() => getListProd()}>
          <IoReloadSharp />
        </Button>
      </div>
      <Table bordered className="tableProd">
        <thead>
          <tr>
            <th>ID</th>
            <th>NAME</th>
            <th>QUANTITY</th>
            <th>PRICE</th>
            <th>CREATE DAY</th>
            <th>LAST UPDATE DAY</th>
            <th>Review</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {prodList.map((prod) => (
            <tr key={prod.id}>
              <th scope="row">{prod.id}</th>
              <td>{prod.name}</td>
              <td>{prod.quantity}</td>
              <td>{numberFormat(prod.price)}</td>
              <td>
                {format(new Date(prod.createDate), "dd/MM/yyyy HH:mm:ss")}
              </td>
              <td>
                {" "}
                {format(new Date(prod.updateDate), "dd/MM/yyyy HH:mm:ss")}
              </td>
              <td>
                {" "}
                <ModalReview id={prod.id} />
              </td>
              <td>
                <ModalDeleteConfirm
                  onChoice={(e) => handleDelete(e, prod.id)}
                />
                {/* <Button color="warning" onClick={console.log("clicked")}>
                  Edit
                </Button> */}
                <ModalEdt id={prod.id} onEdit={(e) => getUpdated(e)}></ModalEdt>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Page
        total={Math.ceil(totalPage.current / size)}
        onPageChange={(e) => setPageNum(e)}
      />
    </>
  );
}
