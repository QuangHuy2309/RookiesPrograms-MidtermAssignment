import React, { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import Page from "../Pagination";
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
  const { id } = useParams();
  const [pagenum, setPageNum] = useState(0);
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const size = 4;
  let totalPage = useRef(0);
  useEffect(() => {
    get(`/public/product/numTotal/${id}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
    get(`/public/product/page?pagenum=${pagenum}&size=${size}&type=${id}`).then(
      (response) => {
        if (response.status === 200) {
          setProdList([...response.data]);
        }
      }
    );
  }, [id, pagenum]);
  // useEffect(()=> {
  //   get(`/public/product/page?pagenum=${pagenum}&size=10&type=${id}`).then(
  //     (response) => {
  //       if (response.status === 200) {
  //         setProdList([...response.data]);
  //       }
  //     }
  //   );
  // }, [id,pagenum]);

  function handlePageChange(e) {
    console.log(`Page press is ${e}`);
    setPageNum(e);
  }

  return (
    <Row>
      <Col className="col-3 ">
        <div className="bike-type">
          {cateList.map((cate) => (
            <div key={cate.id}>
              <Link to={`/Bike/${cate.id}`} style={{ textDecoration: "none" }}>
                <div>{cate.name}</div>
              </Link>
            </div>
          ))}
        </div>
      </Col>
      <Col>
        <Row>
          {prodList.map((prod) => (
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
                    <CardTitle tag="h3" className="card-name">
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
        <Page
          total={Math.ceil(totalPage.current / size)}
          onPageChange={(e) => handlePageChange(e)}
        />
      </Col>
    </Row>
  );
}
