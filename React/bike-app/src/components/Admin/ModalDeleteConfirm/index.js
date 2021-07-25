import React, { useState, useEffect } from "react";
import "./ModalDeleteConfirm.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { RiDeleteBin2Fill } from "react-icons/ri";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  ModalFooter,
} from "reactstrap";

toast.configure();
const ModalConfirm = (props) => {
  const [modal, setModal] = useState(false);
  const toggle = () => setModal(!modal);

  // useEffect(() => {
  //   if (modal) {
  //   }
  // }, [modal]);

  function btnClick(choice){
    toggle();
    props.onChoice(choice);
  }
  return (
    <div>
      <Button color="danger" onClick={toggle}><RiDeleteBin2Fill/></Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}><RiDeleteBin2Fill/>Delete</ModalHeader>
        <ModalBody>
          Are you sure you want to delete?
        </ModalBody>
        <ModalFooter>
          <Button color="danger" onClick={()=> btnClick("OK")}>Delete</Button>{' '}
          <Button color="secondary" onClick={() => btnClick("Cancel")}>Cancel</Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default ModalConfirm;
