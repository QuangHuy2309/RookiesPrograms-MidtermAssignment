import React, { Component } from "react";
import { get } from "../../../Utils/httpHelper";
import { numberFormat } from "../../../Utils/ConvertToCurrency";
import { Link } from "react-router-dom";

import "./Product.css";
import {
  Card,
  CardImg,
  CardBody,
  CardTitle,
  CardSubtitle,
  Button,
  Row,
  Col,
} from "reactstrap";

export default class index extends Component {
  constructor(props) {
    super(props);
  }
  state = {
    prodList: [],
  };
  componentDidMount() {
    let id = this.props.id;
    get(`/public/product/categories/${id}`).then((response) => {
      if (response.status === 200) {
        this.setState({ prodList: response.data });
      }
    });
  }
  // numberFormat = (value) =>
  // new Intl.NumberFormat('en-IN', {
  //   style: 'currency',
  //   currency: 'VND'
  // }).format(value);

  render() {
    return (
      <Row className="mb-4">
        {this.state.prodList.map((prod) => (
          <Col key={prod.id} className="col-3">
            <Link
              to={`/prodDetail/${prod.id}`}
              style={{ textDecoration: "none" }}
            >
              <Card>
                <CardImg
                  top
                  width="100%"
                  src={`data:image/jpeg;base64,${prod.photo}`}
                  alt="Card image cap"
                />
                <CardBody>
                  <CardTitle tag="h5" className="card-name-prodHome">
                    {prod.name}
                  </CardTitle>
                  <div className="card-info">
                    <CardSubtitle tag="h4" className="mb-2 card-price">
                      {numberFormat(prod.price)}
                    </CardSubtitle>
                    <Link to={`/prodDetail/${prod.id}`} className="card-btn">
                      Buy Now
                    </Link>
                  </div>
                </CardBody>
              </Card>
            </Link>
          </Col>
        ))}
      </Row>
    );
  }
}
