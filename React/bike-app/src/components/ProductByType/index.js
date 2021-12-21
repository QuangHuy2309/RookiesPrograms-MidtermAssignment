import React, { useState, useEffect, useRef } from "react";
import { useParams } from "react-router-dom";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import { numberFormat } from "../../Utils/ConvertToCurrency";
import Page from "../Pagination";
import "./ProductByType.css";
import { ImSortNumbericDesc, ImSortNumericAsc } from "react-icons/im";
import {
  Card,
  CardImg,
  CardBody,
  CardTitle,
  CardSubtitle,
  Row,
  Col,
  Label,
  Input,
  FormGroup,
} from "reactstrap";

export default function Index() {
  const { id } = useParams();
  const [pagenum, setPageNum] = useState(0);
  const [cateList, setCateList] = useState([]);
  const [prodList, setProdList] = useState([]);
  const [checkRadioAsc, setCheckRadioAsc] = useState(false);
  const [checkRadioDes, setCheckRadioDes] = useState(false);
  const [search, setSearch] = useState("");
  const size = 8;
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
    if (checkRadioAsc) {
      getSortListProd("ASC");
    } else if (checkRadioDes) {
      getSortListProd("DES");
    } else getProductList();
  }, [id, pagenum]);

  async function getProductList() {
    get(`/public/product/page?pagenum=${pagenum}&size=${size}&type=${id}`).then(
      (response) => {
        if (response.status === 200) {
          setProdList([...response.data]);
        }
      }
    );
  }

  function handlePageChange(e) {
    setPageNum(e);
  }
  async function handleSortClick(e) {
    setSearch("");
    if (e === "ASC") {
      if (checkRadioAsc) {
        setCheckRadioAsc(false);
        getProductList();
      } else {
        setCheckRadioAsc(true);
        setCheckRadioDes(false);
        getSortListProd(e);
      }
    } else if (e === "DES") {
      if (checkRadioDes) {
        setCheckRadioDes(false);
        getProductList();
      } else {
        setCheckRadioDes(true);
        setCheckRadioAsc(false);
        getSortListProd(e);
      }
    }
  }
  async function getSortListProd(typeSort) {
    let sort;
    if (typeSort === "ASC") sort = "ASC";
    else sort = "DES";
    get(
      `/public/productSort/page?pagenum=${pagenum}&size=${size}&type=${id}&sort=${sort}`
    ).then((response) => {
      if (response.status === 200) {
        setProdList([...response.data]);
      }
    });
  }
  function handleSearchChange(e) {  
    let keyword = e.target.value.trim().split(/ +/).join(' ');
    setSearch(e.target.value);
    setCheckRadioAsc("");
    setProdList([]);
    get(`/public/product/search?keyword=${keyword}&type=${id}`).then(
      (response) => {
        if (response.status === 200) {
          setProdList([...response.data]);
        }
      }
    );
  }
  return (
    <Row className="mt-3">
      <Col className="col-3 ">
        <div className="bike-type">
          {cateList.map((cate) => (
            <div key={cate.id} className="cateType-PBT">
              <Link
                to={`/Bike/${cate.id}`}
                style={{
                  textDecoration: "none",
                  // color: "black",
                  // "font-family": "'Barlow Condensed', sans-serif;",
                  // "font-size": "1.45rem",
                  // "-webkit-text-stroke": "0.125px #FF5C58",
                }}
                className="cateText-PBT"
              >
                <div>{cate.name}</div>
              </Link>
            </div>
          ))}
          <hr />
          <FormGroup tag="fieldset" className="sortRadio priceSortText-PBT">
            <legend>Price Sort: </legend>
            <FormGroup check className="sortRadio">
              <Label check>
                <Input
                  type="radio"
                  name="radio2"
                  checked={checkRadioAsc}
                  onClick={() => handleSortClick("ASC")}
                />{" "}
                Ascending <ImSortNumericAsc/>
              </Label>
            </FormGroup>
            <FormGroup check className="sortRadio">
              <Label check>
                <Input
                  type="radio"
                  name="radio2"
                  checked={checkRadioDes}
                  onClick={() => handleSortClick("DES")}
                />{" "}
                Descending <ImSortNumbericDesc/>
              </Label>
            </FormGroup>
          </FormGroup>
        </div>
        <div className="searchField-ProductType mx-3">
          <Label>Search: </Label>
          <Input
            type="email"
            name="email"
            id="exampleEmail"
            required="required"
            placeholder="Search Product by name"
            value={search}
            onChange={(e) => handleSearchChange(e)}
          />
        </div>
      </Col>

      <Col>
        {cateList
          .filter((cate) => cate.id == id)
          .map((filterCate) => (
            <>
              <h2 className="titleCart-PBT">{filterCate.name}</h2>
              <br />
            </>
          ))}
        <Row className="mb-4">
          {prodList.map((prod, index) => (
            <Col key={prod.id} className="col-3 mb-4 cardProd">
              <Link
                to={`/prodDetail/${prod.id}`}
                style={{ textDecoration: "none" }}
              >
                <Card className="cardProd">
                  <CardImg
                    top
                    width="100%"
                    src={`data:image/jpeg;base64,${prod.photo}`}
                    alt="Card image cap"
                  />
                  <CardBody>
                    <CardTitle tag="h5" className="card-name">
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
        {(search === "")?
        <Page
          total={Math.ceil(totalPage.current / size)}
          onPageChange={(e) => handlePageChange(e)}
        /> : null}
      </Col>
    </Row>
  );
}
