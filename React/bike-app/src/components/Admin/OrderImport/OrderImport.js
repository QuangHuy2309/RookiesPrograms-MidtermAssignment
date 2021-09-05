import React, { useState, useEffect, useRef } from "react";
import Page from "../../Pagination";
import { getWithAuth, put, get, del } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { format } from "date-fns";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { Row, Col, Table, Button } from "reactstrap";
import "./OrderImport.css";
import ModalAdd from "./ModalAddImport";

toast.configure();
export default function OrderImport() {
  const [pagenum, setPageNum] = useState(0);
  const [orderList, setOrderList] = useState([]);
  const size = 6;
  let totalPage = useRef(0);
  useEffect(() => {

  }, []);

  return (
  <>
   <h2 className="title-user">ORDER MANAGER</h2>
   <ModalAdd />
      <Table bordered className="tableImport">
        <thead>
          <tr>
            <th>ID</th>
            <th>EMPLOYEE EMAIL</th>
            <th>EMPLOYEE NAME</th>
            <th>TIME IMPORT</th>
            <th>TOTAL PRICE</th>
            <th>PRODUCT</th>
            <th>STATUS</th>
            <th></th>
          </tr>
        </thead>
        <tbody>

        </tbody>
        </Table>
        </>
        );
}
