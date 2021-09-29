import React, { useState, useEffect } from "react";
import "./ModalRestore.css";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { MdRestore } from "react-icons/md";
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
    console.log(`clicked ${choice}`);
    props.onChoice(choice);
  }
  return (
    <div>
      <Button color="primary" onClick={toggle}>
        <MdRestore />
      </Button>
      <Modal isOpen={modal} toggle={toggle}>
        <ModalHeader toggle={toggle}>
          <MdRestore /> Restore data
        </ModalHeader>
        <ModalBody>
          Are you sure to recovery data back to day {props.id}?
          <p className="attentionNote-ModalRestore mb-0">
            All data will be lost. Remember to backup
            data before restore
          </p>
        </ModalBody>
        <ModalFooter>
          <Button color="primary" onClick={() => btnClick("OK")}>
            Yes
          </Button>{" "}
          <Button color="secondary" onClick={toggle}>
            No
          </Button>
        </ModalFooter>
      </Modal>
    </div>
  );
};

export default ModalConfirm;
