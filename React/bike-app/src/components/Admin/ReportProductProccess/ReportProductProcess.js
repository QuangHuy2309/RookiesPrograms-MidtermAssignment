import React, { useState, useEffect } from "react";
import { getWithAuth, get } from "../../../Utils/httpHelper";
import "./ReportProductProcess.css";
import Page from "../../Pagination";
import { AiOutlineAppstore } from "react-icons/ai";
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

export default function ReportProductProcess() {
  const [choice, setChoice] = useState("1");
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [dropdownOpen, setOpen] = useState(false);
  const [pagenum, setPageNum] = useState(0);
  const [totalPage, setTotalPage] = useState(0);
  const size = 6;
  const toggle = () => setOpen(!dropdownOpen);

  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    getProdReport();
  }, []);

  useEffect(() => {
    getProdReport();
  }, [choice]);

  function getProdReport() {
    getWithAuth(`/report/productProccess/${choice}`).then((response) => {
      if (response.status === 200) {
        setProdList([...response.data]);
        setTotalPage(response.data.length);
      }
    });
  }
  function pagingTable() {
    let lastIndex = (pagenum+1) * size;
    let startIndex = lastIndex - size;
    let prodListPaging = prodList.slice(startIndex,lastIndex);
    return prodListPaging.map((prod) => (
      <tr key={prod.id}>
        <th scope="row">{prod.id}</th>
        <td className="nameProd-ReportProdProccess">{prod.name}</td>
        <td>{prod.quantity}</td>
        <td>{prod.inProcess}</td>
        <td>{prod.delivery}</td>
        <td>{prod.completed}</td>
        {/* <td>{prod.cancel}</td> */}
      </tr>
    ));
  }
  return (
    <>
      <h2 className="title-ProductProccess">PRODUCT REPORT</h2>
      <div className="category_ReportProdProccess">
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
                {index != cateList.length - 1 ? <DropdownItem divider /> : null}
              </div>
            ))}
          </DropdownMenu>
        </ButtonDropdown>
      </div>
      <br/>
      <Table bordered className="tableProd">
        <thead>
          <tr>
            <th className="titleTale-ProductAdmin">ID</th>
            <th className="titleTale-ProductAdmin">NAME</th>
            <th className="titleTale-ProductAdmin">IN STOCK</th>
            <th className="titleTale-ProductAdmin">IN PROCCESS</th>
            <th className="titleTale-ProductAdmin">DELIVERING</th>
            <th className="titleTale-ProductAdmin">SOLD</th>
            {/* <th className="titleTale-ProductAdmin">CANCELED</th> */}
          </tr>
        </thead>
        <tbody>
          {/* {prodList.map((prod) => (
            <tr key={prod.id}>
              <th scope="row">{prod.id}</th>
              <td className="nameProd-ReportProdProccess">{prod.name}</td>
              <td>{prod.quantity}</td>
              <td>{prod.inProcess}</td>
              <td>{prod.delivery}</td>
              <td>{prod.completed}</td>
            </tr>
          ))}
           */}
           {pagingTable()}
        </tbody>
      </Table>
      <Page
        total={Math.ceil(totalPage / size)}
        onPageChange={(e) => setPageNum(e)}
      />
    </>
  );
}
