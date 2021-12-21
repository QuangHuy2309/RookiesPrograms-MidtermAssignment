import React, { useState, useEffect } from "react";
import { put } from "../../../../Utils/httpHelper";
import { toast } from "react-toastify";
import { getCookie } from "../../../../Utils/Cookie";
import {
  Button,
  Modal,
  ModalHeader,
  ModalBody,
  Label,
  Input,
  Form,
  FormGroup,
} from "reactstrap";

toast.configure();
export default function ModalAddNote(props) {
  const [modal, setModal] = useState(props.modal);
  
  function toggleNote() {
    props.onChangeShowNote(false);
    // setModal(!modal);
  }

  useEffect(() => {
    setModal(props.modal);
  }, [props.modal]);

  function handleSubmitNote(e) {
    e.preventDefault();
    const id = getCookie("id");
    const username = getCookie("username");
    let note = `Cancel by: ${id}-${username}. Reason: ${e.target.note.value.trim()}`;

    put(
      `/order/note/${props.id}?status=${
        props.status
      }&note=${note}`,
      ""
    )
      .then((response) => {
        if (response.status === 200) {
          toast.success(`Update order note success`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          props.onAddNote();
          toggleNote();
        }
      })
      .catch((error) => {
        if (error.response.status === 400 || error.response.status === 404) {
          toast.error(`Failed: ${error.response.data.message}`, {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
        }
      });
  }
  return (
    <div>
      <Modal isOpen={modal} toggle={toggleNote}>
        <ModalHeader toggle={toggleNote} className="titleTable-OrderAdmin">
          Order note
        </ModalHeader>
        <ModalBody>
          <Form onSubmit={(e) => handleSubmitNote(e)}>
            <FormGroup>
              <Label for="exampleText" >
              {(props.status === "4") ? "Cancel reason" : "Note"}
              </Label>
              <Input id="exampleText" name="note" type="textarea" required />
            </FormGroup>
            <br />
            <Button color="primary" type="submit">
              Submit
            </Button>{" "}
            <Button color="secondary" onClick={toggleNote}>
              Cancel
            </Button>
          </Form>
        </ModalBody>
      </Modal>
    </div>
  );
}
