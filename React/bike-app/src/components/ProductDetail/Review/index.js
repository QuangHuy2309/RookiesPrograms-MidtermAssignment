import React, { useState, useEffect, useRef } from "react";
import { get, post, getWithAuth } from "../../../Utils/httpHelper";
import { useHistory } from "react-router-dom";
import { Row, Col, Button, Form, FormGroup, Input } from "reactstrap";
import Avatar from "react-avatar";
import "./Review.css";
import { Rating, RatingView } from "react-simple-star-rating";
import avaImg from "../../../assets/img/smile.jpg";
import { format } from "date-fns";
import Page from "../../Pagination";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { getCookie } from "../../../Utils/Cookie";

toast.configure();
export default function Index(props) {
  const [pagenum, setPageNum] = useState(0);
  const [reviewList, setReviewList] = useState([]);
  const [rating, setRating] = useState(3);
  const [user, setUser] = useState(Object);
  const [check, setCheck] = useState(false);
  const history = useHistory();
  let totalPage = useRef(0);
  const size = 2;

  useEffect(() => {}, [props.id]);

  useEffect(() => {
    getRate();
    getUserDetail();
    checkReview();
  }, [pagenum, props.id]);

  function getRate() {
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
  }

  function getUserDetail() {
    const emailUser = getCookie("email");
    getWithAuth(`/persons/search/email/${emailUser}`).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        setUser(response.data);
      }
    });
  }
  function checkReview() {
    const body = JSON.stringify({
      productId: props.id,
      customerId: user.id,
    });
    post(`/product/rate/checkExist`, body).then((response) => {
      if (response.status === 200) {
        // console.log(response.data);
        setCheck(response.data);
      }
    });
  }
  function handlePageChange(e) {
    setPageNum(e);
  }
  function handleSubmitReview(e) {
    e.preventDefault();
    const body = JSON.stringify({
      productId: props.id,
      customerId: user.id,
      rateNum: rating,
      rateText: e.target.rateText.value,
    });
    post(`/product/rate/`, body)
      .then((response) => {
        if (response.status === 200) {
          // console.log(response.data);
          toast.success(`Thank for your Review`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          getRate();
          props.onReviewChange(true);
        }
      })
      .catch((error) => {
        if (error.response.status === 409)
          toast.error(`You had review this product before`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
      });
  }
  function customerReview() {
    const name = getCookie("username");
    const status = getCookie("status");
    checkReview();
    const open = check && status === "true";
    if (open) {
      return (
        <div className="reviewInput-card">
          <Row>
            <Col className="col-1">
              <Avatar src={avaImg} size="100" round={true} />
            </Col>
            <Col className="col-review">
              <Form onSubmit={(e) => handleSubmitReview(e)}>
                <FormGroup className="col-review">
                  <p className="name-review">{name}</p>
                  <div className="rateNum">
                    <Rating
                      onClick={(e) => setRating(e, console.log(`rate is ${e}`))}
                      ratingValue={rating}
                      transition
                      size={40}
                      stars={5}
                    />
                  </div>
                  <Input
                    type="textarea"
                    name="rateText"
                    id="exampleText"
                    requires
                    className="inputForm"
                  />
                </FormGroup>
                <div className="btnInput-card">
                  <Button outline color="info" type="submit">
                    Submit
                  </Button>
                </div>
              </Form>
            </Col>
          </Row>
        </div>
      );
    }
  }

  return (
    <>
      {customerReview()}
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
              <RatingView ratingValue={review.rateNum} size={30} className="" />
              <p>{review.rateText}</p>
            </Col>
          </Row>
        </div>
      ))}
      {reviewList.length < 1 ? null : (
        <Page
          total={Math.ceil(totalPage.current / size)}
          onPageChange={(e) => handlePageChange(e)}
        />
      )}
    </>
  );
}
