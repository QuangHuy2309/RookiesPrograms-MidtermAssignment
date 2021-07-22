import React, { useState, useRef, useEffect } from "react";
import { RatingView } from "react-simple-star-rating";
import { get, delWithBody ,post } from "../../../../Utils/httpHelper";
import Page from "../../../Pagination";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Row,
  Col,
} from "reactstrap";
import "./ModalReview.css";

export default function Index(props) {
  const [modal, setModal] = useState(false);
  const [pagenum, setPageNum] = useState(0);
  const [reviewList, setReviewList] = useState([]);
  let totalReview = useRef(0);
  const size = 4;
  const toggle = () => setModal(!modal);
  useEffect(() => {
    if (modal) {
      get(`/public/product/rateTotal/${props.id}`).then((response) => {
        if (response.status === 200) {
          totalReview.current = response.data;
        }
      });
      get(
        `/public/product/rate?pagenum=${pagenum}&size=${size}&id=${props.id}`
      ).then((response) => {
        if (response.status === 200) {
          setReviewList([...response.data]);
        }
      });
    }
  }, [modal]);
  useEffect(() => {
    if (modal) {
      get(`/public/product/rateTotal/${props.id}`).then((response) => {
        if (response.status === 200) {
          totalReview.current = response.data;
        }
      });
      get(
        `/public/product/rate?pagenum=${pagenum}&size=${size}&id=${props.id}`
      ).then((response) => {
        if (response.status === 200) {
          setReviewList([...response.data]);
        }
      });
    }
  }, [pagenum, props.id]);
  function handlePageChange(e) {
    console.log(`Page press is ${e}`);
    setPageNum(e);
  }
  function handleDelete(customerId){
    const body = JSON.stringify({
      customerId : customerId,
      productId :props.id,
    });
    console.log(body);
    post("/product/rate/delete", body)
      .then((response) => {
        if (response.status === 200) {
          alert("DELETE SUCCES");
        }
      })
      .catch((error) => {
        alert(error);
      });
  }
  return (
    <div>
      <Button color="link" onClick={toggle}>
        Review
      </Button>
      <Modal isOpen={modal} toggle={toggle} className="">
        <ModalHeader toggle={toggle}>Product Review</ModalHeader>
        <ModalBody>
          {reviewList.map((review) => (
            <div class="reviewForm">
              <Row>
                <Col>
                  <h5>{review.customer.fullname}</h5>
                  <RatingView
                    ratingValue={review.rateNum}
                    size={30}
                    className=""
                  />
                </Col>
                <Col className="btnCol-ModalRv">
                  <Button color="danger" onClick={() => handleDelete(review.id.customerId)} >Delete</Button>
                </Col>
              </Row>
              <p>{review.rateText}</p>
            </div>
          ))}
        </ModalBody>
        <ModalFooter>
          <div className="Paging">
            <Page
              total={Math.ceil(totalReview.current / size)}
              onPageChange={(e) => handlePageChange(e)}
              
            />
          </div>
            <Button color="secondary" onClick={toggle} className="btnCancel">
            Cancel
            </Button>
          
        </ModalFooter>
      </Modal>
    </div>
  );
}
