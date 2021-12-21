import React, { useState, useRef, useEffect } from "react";
import { RatingView } from "react-simple-star-rating";
import { get, delWithBody, del } from "../../../../Utils/httpHelper";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Page from "../../../Pagination";
import "./ModalReview.css";
import ModalDeleteConfirm from "../../ModalDeleteConfirm";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
  Row,
  Col,
} from "reactstrap";

toast.configure();
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
      getRate();
    }
  }, [pagenum, props.id]);

  function getRate(){
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

  function handlePageChange(e) {
    console.log(`Page press is ${e}`);
    setPageNum(e);
  }

  function handleDelete(e, id) {
    if (e === "OK") {
      // const body = JSON.stringify({
      //   customerId: customerId,
      //   productId: props.id,
      // });
      del(`/product/rate/delete/${id}`)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Delete review success", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            getRate();
          }
        })
        .catch((error) => {
          toast.error(`Error: ${error}`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
        });
    }
  }
  return (
    <div>
      <Button color="link" onClick={toggle}>
        Review
      </Button>
      <Modal isOpen={modal} toggle={toggle} className="">
        <ModalHeader toggle={toggle} className="title-AddProductAdmin">Product Review</ModalHeader>
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
                  {/* <Button color="danger" onClick={() => handleDelete(review.id.customerId)} >Delete</Button> */}
                  <ModalDeleteConfirm
                    onChoice={(e) => handleDelete(e, review.id)}
                  />
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
