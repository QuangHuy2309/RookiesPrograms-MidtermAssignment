import React, { useState, useEffect } from "react";
import "./ModalConfirm.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { BiLogOut } from "react-icons/bi";
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


  function btnClick(choice){
    toggle();
    props.onChoice(choice);
  }
  return (
    <div>
      <p onClick={toggle} className={`${props.onUserSide} logout-text`}>Log Out <BiLogOut/></p>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}><BiLogOut/> LogOut</ModalHeader>
        <ModalBody>
          Do you want to logout?
        </ModalBody>
        <ModalFooter>
          <Button color="danger" onClick={()=> btnClick("OK")}>LOGOUT</Button>{' '}
          <Button color="secondary" onClick={() => btnClick("Cancel")}>Cancel</Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default ModalConfirm;
