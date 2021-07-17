import React, { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Link, Redirect } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import img from "../../assets/img/test.jpg";
import "./ProductByType.css";
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


export default function Index() {
  let { id, pagenum } = useParams();
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);

  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    get(`/public/product/page?pagenum=${pagenum}&size=10&type=${id}`).then(
      (response) => {
        if (response.status === 200) {
          setProdList([...response.data]);
        }
      }
    );
  }, []);
  useEffect(()=> {
    get(`/public/product/page?pagenum=${pagenum}&size=10&type=${id}`).then(
      (response) => {
        if (response.status === 200) {
          setProdList([...response.data]);
        }
      }
    );
  }, [id,pagenum]);
  return (
    <Row>
      <Col className="col-3 ">
        <div className="bike-type">
        <h2>{id}</h2>
        {cateList.map((cate) => (
          <Link to={`/Bike/${cate.id}/0`} style={{ textDecoration: "none" }}>
            <div>{cate.name}</div>
          </Link>
        ))}
        </div>
      </Col>
      <Col>
        <h3>{pagenum}</h3>
        <Row>
          {prodList.map((prod) => (
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
                    <Link to="/prodDetail" className="card-btn">
                      Buy Now
                    </Link>
                  </div>
                </CardBody>
              </Card>
            </Col>
          ))}
        </Row>
      </Col>
    </Row>
  );
}
