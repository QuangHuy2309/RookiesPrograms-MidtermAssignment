import React, { useState, useEffect } from "react";
import { getWithAuth } from "../../../../Utils/httpHelper";
import ProdList from "../../../UserOrderHistory/ProdList/ProdList.js";
import { numberFormat } from "../../../../Utils/ConvertToCurrency";
import { AiFillPrinter } from "react-icons/ai";
import { format } from "date-fns";
import "./PrintOrder.css";
import ImageLogo from "../../../../assets/img/logo.jpg";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  Table,
  Row,
  Col,
  ModalFooter,
} from "reactstrap";

export default function PrintOrder(props) {
  const { id } = props;
  const [modal, setModal] = useState(false);
  const [order, setOrder] = useState(Object);
  const [prodList, setProdList] = useState([]);

  const toggle = () => setModal(!modal);

  useEffect(() => {
    setProdList([]);
    if (modal) {
      getWithAuth(`/order/${id}`).then((response) => {
        if (response.status === 200) {
          setOrder(response.data);
        }
      });
    }
  }, [modal]);

  useEffect(() => {
    if (Object.keys(order).length != 0) getProdList();
  }, [order]);

  function getTotal(list) {
    let totalCost = 0;
    list.forEach((detail) => {
      totalCost += detail.ammount * detail.unitPrice;
    });
    return totalCost;
  }

  function getProd(id, amount) {
    getWithAuth(`/product/search/${id}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        let prod = response.data;
        prod.quantity = amount;
        setProdList((oldArr) => [...oldArr, prod]);
      }
    });
  }
  async function getProdList() {
    setProdList([]);
    order.orderDetails.map((detail) =>
      getProd(detail.id.productId, detail.ammount)
    );
  }

  return (
    <div>
      <div>
        <Button color="warning" onClick={toggle} className="btnModal mx-3">
          <AiFillPrinter />
        </Button>
      </div>
      <Modal isOpen={modal} toggle={toggle} size="lg">
        {/* <ModalHeader toggle={toggle}>
          <AiFillPrinter /> Order Information
        </ModalHeader> */}
        <ModalBody>
          <div className="">
            {Object.keys(order).length == 0 ? null : (
              <div id="orderBill">
                <Row className="printOrderFrom">
                  <Row className="mb-3">
                    <Col className="col-6">
                      <img src={ImageLogo} width="50%" />
                    </Col>
                    <Col className="idOrder_PrintOrder">
                      <p className="textAddressBox_PrintOrder">
                        Order code: {order.id}
                      </p>
                    </Col>
                  </Row>
                  <br />
                  <Row className="addressBox_PrintOrder mb-3">
                    <Col className="col-6 leftAddressBox_PrintOrder">
                      <p className="textAddressBox_PrintOrder">FROM:</p>
                      <p className="textAddressBox_PrintOrder ms-3">
                        Bike Shop
                      </p>
                      <p className="textAddressBox_PrintOrder ms-3">
                        Phonenumber: (+84) 967 141 557
                      </p>
                      <p className="textAddressBox_PrintOrder ms-3">
                        Address: Bike Shop, 21098 Bake Parkway #112 Lake Forest,
                        CA 92630
                      </p>
                    </Col>
                    <Col className="rightAddressBox_PrintOrder">
                      <p className="textAddressBox_PrintOrder">TO:</p>
                      <p className="textAddressBox_PrintOrder ms-3">
                        {order.customers.fullname}
                      </p>
                      <p className="textAddressBox_PrintOrder ms-3">
                        Phonenumber: (+84) 903 671 456
                      </p>
                      <p className="textAddressBox_PrintOrder ms-3">
                        Address: 97 Man Thiện, P. Hiệp Phú, Quận 9
                      </p>
                    </Col>
                  </Row>
                  <Row>
                    <Col className="col-2">
                      <p className="textAddressBox_PrintOrder">Order date:</p>
                    </Col>
                    <Col>
                      <p className="textAddressBox_PrintOrder">
                        {format(
                          new Date(order.timebought),
                          "dd/MM/yyyy HH:mm:ss"
                        )}
                      </p>
                    </Col>
                  </Row>
                  <Row>
                    <Col className="col-2">
                      <p className="textAddressBox_PrintOrder">Payment:</p>
                    </Col>
                    <Col>
                      <p className="textAddressBox_PrintOrder">
                        {order.payment ? "Paid" : "Pay on delivery"}
                      </p>
                    </Col>
                  </Row>
                  <Table bordered className="tableProduct_PrintOrder mt-3">
                    <thead>
                      <tr>
                        <th className="total_PrintOrder">Quantity</th>
                        <th className="total_PrintOrder">Product Details</th>
                        <th className="total_PrintOrder">PRICE</th>
                        <th className="total_PrintOrder">Total</th>
                      </tr>
                    </thead>
                    <tbody>
                      {prodList.map((prod) => (
                        <tr key={prod.id}>
                          <td>{prod.quantity}</td>
                          <td className="tableProductTextLeft_PrintOrder">
                            {prod.name}
                          </td>
                          <td className="tableProductTextLeft_PrintOrder">{numberFormat(prod.price)}</td>
                          <td className="tableProductTextLeft_PrintOrder">{numberFormat(prod.price * prod.quantity)}</td>
                        </tr>
                      ))}
                    </tbody>
                    <tfoot>
                      <tr>
                        <th className="tableProductTextRight_PrintOrder total_PrintOrder" colspan="3">
                          ORDER TOTAL 
                        </th>
                        <td className="tableProductTextLeft_PrintOrder total_PrintOrder">{numberFormat(getTotal(order.orderDetails))}</td>
                      </tr>
                    </tfoot>
                  </Table>
                </Row>
              </div>
            )}
          </div>
        </ModalBody>
        <ModalFooter>
          <Button
            className="dontPrint"
            color="primary"
            outline
            onClick={() => window.print()}
          >
            {" "}
            Capture as PDF{" "}
          </Button>
          <Button className="dontPrint" color="secondary" onClick={toggle}>
            Cancel
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
}
