import React, { useState, useRef, useEffect } from "react";
import { get} from "../../../../Utils/httpHelper";
import { format } from "date-fns";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./ModalSelectCateExport.css";
import ReactExport from "react-data-export";
import { HiOutlineDownload } from "react-icons/hi";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Row,
  Col,
  Label,
} from "reactstrap";

toast.configure();
const ExcelFile = ReactExport.ExcelFile;
const ExcelSheet = ExcelFile.ExcelSheet;
export default function Index(props) {
  const [modal, setModal] = useState(false);
  const [choice, setChoice] = useState(0);
  const [prodReportList, setProdReport] = useState([]);
  const [cateList, setCateList] = useState([]);
  const DataSet = [
    {
      columns: [
        { title: "ID" },
        { title: "Name" },
        { title: "Price" },
        { title: "Quantity" },
        { title: "Description" },
        { title: "Brand" },
        // { title: "Photo" },
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
        // { value: convertBase64ToFile(report.photo, report.id) },
        { value: format(new Date(report.createDate), "dd/MM/yyyy HH:mm:ss") },
        { value: format(new Date(report.updateDate), "dd/MM/yyyy HH:mm:ss") },
        { value: report.employeeUpdate.fullname },
        { value: report.categories.name },
      ]),
    },
  ];

  const toggle = () => setModal(!modal);
  useEffect(() => {
    if (modal) {
      getCateList();
      getListProdReport();
    }
  }, [modal]);
  useEffect(() => {
    if (choice == 0) {
      getListProdReport();
    } else {
      getListProdReportByType();
    }
  }, [choice]);
  function getCateList() {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
  }
  function getListProdReport() {
    get(`/public/product`).then((response) => {
      if (response.status === 200) {
        setProdReport([...response.data]);
      }
    });
  }
  function getListProdReportByType() {
    get(`/public/product/${choice}`).then((response) => {
      if (response.status === 200) {
        setProdReport([...response.data]);
      }
    });
  }

  return (
    <div>
      <Button onClick={toggle} outline color="success">
        Export <HiOutlineDownload/>
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}><HiOutlineDownload/>Product XLSS Export</ModalHeader>
        <ModalBody>
          <Label>Category: </Label>
          <select
            value={choice}
            className="selectCate-ExportProd"
            onChange={(e) => setChoice(e.target.value)}
          >
            <option value={0}>All</option>
            {cateList.map((cate, index) => (
              <>
                <option value={cate.id}>{cate.name}</option>
              </>
            ))}
          </select>
        </ModalBody>
        <ModalFooter>
          <ExcelFile
            filename={`ProductList`}
            element={
              <Button outline color="success">
                Export
              </Button>
            }
          >
            <ExcelSheet dataSet={DataSet} name="Report_Sheet" />
          </ExcelFile>
          <Button color="secondary" onClick={toggle}>
            Cancel
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
}
