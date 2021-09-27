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
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AiOutlineAppstore } from "react-icons/ai";
import "./Product.css";
import {
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  Table,
  Button,
  Row,
  Col,
  Input,
} from "reactstrap";

toast.configure();
export default function Index() {
  const [choice, setChoice] = useState("1");
  const [pagenum, setPageNum] = useState(0);
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [dropdownOpen, setOpen] = useState(false);
  const [showPagination, setShowPage] = useState(true);
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
  }, [choice]);
  useEffect(() => {
    getListProd();
  }, [choice, pagenum]);

  function getUpdated(e) {
    if (e) getListProd();
  }

  function getListProd() {
    get(
      `/public/productDTO/page?pagenum=${pagenum}&size=${size}&type=${choice}`
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
  function handleAdd(e) {
    if (e) getListProd();
  }
  function handleSearchChange(e) {
    if (e.target.value.trim().length > 0) {
      setProdList([]);
      get(
        `/public/product/search?keyword=${e.target.value.trim()}&type=${choice}`
      ).then((response) => {
        if (response.status === 200) {
          setProdList([...response.data]);
          setShowPage(false);
          
        }
      });
    } else {
      getListProd();
      setShowPage(true);
    }
  }
  return (
    <>
      <h2 className="title-ProductAdmin">PRODUCT MANAGER</h2>
      <div>
        <Row>
          <Col className="col-7 btn-list">
            <ButtonDropdown isOpen={dropdownOpen} toggle={toggle}>
              <DropdownToggle caret>
                <AiOutlineAppstore />
                Categories
              </DropdownToggle>
              <DropdownMenu>
                {cateList.map((cate, index) => (
                  <div key={cate.id}>
                    <DropdownItem onClick={() => setChoice(cate.id)}>
                      {cate.name}
                    </DropdownItem>
                    {index != cateList.length - 1 ? (
                      <DropdownItem divider />
                    ) : null}
                  </div>
                ))}
              </DropdownMenu>
            </ButtonDropdown>{" "}
            <ModalAdd onAdd={(e) => handleAdd(e)} />
          </Col>
          <Col>
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
        {/* <Button outline color="link" onClick={() => getListProd()}>
          <IoReloadSharp />
        </Button> */}
      </div>
      <Table bordered className="tableProd">
        <thead>
          <tr>
            <th className="titleTale-ProductAdmin">ID</th>
            <th className="titleTale-ProductAdmin">NAME</th>
            <th className="titleTale-ProductAdmin">QUANTITY</th>
            <th className="titleTale-ProductAdmin">PRICE</th>
            <th className="titleTale-ProductAdmin">CREATE DAY</th>
            <th className="titleTale-ProductAdmin">LAST UPDATE DAY</th>
            <th className="titleTale-ProductAdmin">UPDATE BY</th>
            <th className="titleTale-ProductAdmin">REVIEW</th>
            <th className="titleTale-ProductAdmin">ACTION</th>
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
              <td>{prod.nameEmployeeUpdate}</td>
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
      {showPagination ? (
        <Page
          total={Math.ceil(totalPage.current / size)}
          onPageChange={(e) => setPageNum(e)}
        />
      ) : null}
    </>
  );
}
