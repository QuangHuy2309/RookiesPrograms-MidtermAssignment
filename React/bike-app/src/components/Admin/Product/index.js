import React, { useState, useEffect, useRef } from "react";
import { get, del } from "../../../Utils/httpHelper";
import Page from "../../Pagination";
import ModalEdt from "./ModalEdtProd";
import ModalAdd from "./ModalAddProd";
import ModalReview from "./ModalReview";
import ModalSelectCateExport from "./ModalSelectCateExport";
import { format } from "date-fns";
import { IoReloadSharp } from "react-icons/io5";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { convertBase64ToFile } from "../../../Utils/ConvertBase64ToFile";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { AiOutlineAppstore } from "react-icons/ai";
import { getCookie } from "../../../Utils/Cookie";
import ReactExport from "react-data-export";
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
const ExcelFile = ReactExport.ExcelFile;
const ExcelSheet = ExcelFile.ExcelSheet;
export default function Index() {
  const [choice, setChoice] = useState("1");
  const [pagenum, setPageNum] = useState(0);
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [dropdownOpen, setOpen] = useState(false);
  const [showPagination, setShowPage] = useState(true);
  const [totalPage, setTotalPage] = useState(0);
  const [prodReportList, setProdReport] = useState([]);
  const size = 4;
  const role = getCookie("role");
  const DataSet = [
    {
      columns: [
        { title: "ID" },
        { title: "Name" },
        { title: "Price" },
        { title: "Quantity" },
        { title: "Description" },
        { title: "Brand" },
        { title: "Create Date" },
        { title: "Update Date" },
        { title: "Update By" },
        { title: "Category" },
      ],
      data: prodReportList.map((report) => [
        { value: report.id },
        { value: report.name },
        { value: report.price },
        { value: report.quantity },
        { value: report.description },
        { value: report.brand },
        { value: format(new Date(report.createDate), "dd/MM/yyyy HH:mm:ss") },
        { value: format(new Date(report.updateDate), "dd/MM/yyyy HH:mm:ss") },
        { value: report.employeeUpdate.fullname },
        { value: report.categories.name },
      ]),
    },
  ];

  const toggle = () => setOpen(!dropdownOpen);

  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    getListProdReport();
  }, []);
  useEffect(() => {
    getTotalProd();
  }, [choice]);
  useEffect(() => {
    getListProd();
  }, [choice, pagenum]);

  function getTotalProd() {
    get(`/public/product/numTotal/${choice}`).then((response) => {
      if (response.status === 200) {
        setTotalPage(response.data);
      }
    });
  }

  function getUpdated(e) {
    if (e) getListProd();
  }
  function getListProdReport() {
    get(`/public/product`).then((response) => {
      if (response.status === 200) {
        setProdReport([...response.data]);
      }
    });
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
            getTotalProd();
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
      let keyword = e.target.value.trim().split(/ +/).join(' ');
      setProdList([]);
      get(
        `/public/product/search?keyword=${keyword}&type=${choice}`
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
        <Row className="mb-3">
          <Col className="col-6 ms-5 divTextAlignLeft">
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
          <Col className="col-4 divTextAlign">
            <Input
              type="text"
              name="email"
              id="exampleEmail"
              required="required"
              className="titleControl-ProdAdmin"
              placeholder="Search Product by name"
              onChange={(e) => handleSearchChange(e)}
            />
          </Col>
          <Col>
            {role.includes("ADMIN") ? (
              <ModalSelectCateExport/>
            ) : null}
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
          total={Math.ceil(totalPage / size)}
          onPageChange={(e) => setPageNum(e)}
        />
      ) : null}
    </>
  );
}
