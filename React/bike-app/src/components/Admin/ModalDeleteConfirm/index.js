import React, { useState, useEffect } from "react";
import "./ModalDeleteConfirm.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { RiDeleteBin2Fill } from "react-icons/ri";
import { Button, Modal, ModalHeader, ModalBody, ModalFooter } from "reactstrap";

toast.configure();
const ModalConfirm = (props) => {
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);

  // useEffect(() => {
  //   if (modal) {
  //   }
  // }, [modal]);

  function btnClick(choice) {
    toggle();
    props.onChoice(choice);
  }
  return (
    <div>
      <Button color="danger" disabled={props.disable} onClick={toggle}>
      {props.cancel == null
            ? (<><RiDeleteBin2Fill /></>)
            : "Cancel this order"}
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>
          
          {props.cancel == null
            ? (<><RiDeleteBin2Fill /> Delete</>)
            : "Cancel this order"}
        </ModalHeader>
        <ModalBody>
          {props.cancel == null
            ? "Are you sure you want to remove this?"
            : "Are you sure you want to cancel this?"}
        </ModalBody>
        <ModalFooter>
          <Button color="danger" onClick={() => btnClick("OK")}>
          {props.cancel == null
            ? "Delete"
            : "Yes"}
          </Button>{" "}
          <Button color="secondary" onClick={() => btnClick("Cancel")}>
          {props.cancel == null
            ? "Cancel"
            : "No"} 
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default ModalConfirm;
