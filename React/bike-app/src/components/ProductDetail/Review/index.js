import React, { useState, useEffect, useRef } from "react";
import { get } from "../../../Utils/httpHelper";
import {
  Row,
  Col,
  Pagination,
  PaginationItem,
  PaginationLink,
} from "reactstrap";
import Avatar from "react-avatar";
import "./Review.css";
import { Rating, RatingView } from "react-simple-star-rating";
import avaImg from "../../../assets/img/smile.jpg";
import { format } from "date-fns";
import Page from "../../Pagination";


export default function Index(props) {
  const [pagenum, setPageNum] = useState(0);
  const [reviewList, setReviewList] = useState([]);

  let totalPage =  useRef(0) ;
  const size = 2;

  useEffect(() => {}, [props.id]);

  useEffect(() => {
    get(`/public/product/rateTotal/${props.id}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
    get(
      `/public/product/rate?pagenum=${pagenum}&size=${size}&id=${props.id}`
    ).then((response) => {
      if (response.status === 200) {
        setReviewList([...response.data]);
      }
    });
    
  }, [pagenum, props.id]);
  function handlePageChange(e){
    console.log(`Page press is ${e}`);
    setPageNum(e);
  }
  return (
    <>
      {reviewList.map((review) => (
          <div key={review.id.customerId} className="review-card">
            <Row>
              <Col className="col-1">
                <Avatar src={avaImg} size="100" round={true} />
              </Col>
              <Col className="col-review">
                <p className="name-review">{review.customer.fullname}</p>
                <p className="date-review">
                  {format(new Date(review.dateReview), "dd/MM/yyyy")}
                </p>
                <RatingView
                  ratingValue={review.rateNum}
                  size={30}
                  className=""
                />
                <p>{review.rateText}</p>
              </Col>
            </Row>
          </div>
      ))}
     <Page total={Math.ceil(totalPage.current/size)}  onPageChange={(e) => handlePageChange(e)}/>
    </>
  );
}
