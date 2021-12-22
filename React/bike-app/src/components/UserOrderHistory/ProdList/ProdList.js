import React, { useEffect } from 'react'
import { Link } from "react-router-dom";
import { getWithAuth, get } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { useState } from 'react/cjs/react.development';
import { Button, Form, FormGroup, Label, Input, Row, Col } from "reactstrap";


export default function ProdList(props) {
    const [prodList, setProdList] = useState([]);

    useEffect(() => {
        getProdList();
    },[]);
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
      async function getProdList(){
          setProdList([]);
          props.orderDetail.map((detail) => getProd(detail.id.productId, detail.ammount));
      }
    return (
        <div>
            {prodList.map((prod) => (
              <div key={prod.id}>
              <Link
              to={`/prodDetail/${prod.id}`}
              style={{ textDecoration: "none",color: "black" }}
            >
            <Row className="mb-3" key={prod.id}>
              <Col className="col-3">
                <img
                  src={`data:image/jpeg;base64,${prod.photo}`}
                  className="img-order"
                />
              </Col>
              <Col className="info-prod-order">
                <h4>
                  {prod.name}
                </h4>
                <h6>
                    Quantity: {prod.quantity}
                </h6>
                <h5>
                    Price: {numberFormat(prod.quantity * prod.price)}
                </h5>
                </Col>
                </Row>
                </Link>
                </div>))
        }
        </div>
    )
}
