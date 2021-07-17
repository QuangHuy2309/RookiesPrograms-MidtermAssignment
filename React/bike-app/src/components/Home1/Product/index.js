import React, { Component } from "react";
import { get } from "../../../Utils/httpHelper";
import img from "../../../assets/img/test.jpg";
import "./Product.css";
import { Link } from "react-router-dom";
import {
  Card,
  CardImg,
  CardText,
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

  render() {
    return (
      <Row>
        {this.state.prodList.map((prod) => (
          <Col key={prod.id} className="col-3">
            <Card>
              <CardImg top width="100%" src={img} alt="Card image cap" />
              <CardBody>
                <CardTitle tag="h3" className="card-name">
                  {prod.name}
                </CardTitle>
                <div className="card-info">
                  <CardSubtitle tag="h4" className="mb-2 card-price">
                    {prod.price}
                  </CardSubtitle>
                  <Link to="/prodDetail" className="card-btn">Buy Now</Link>
                </div>
              </CardBody>
            </Card>
          </Col>
        ))}
      </Row>
    );
  }
}
