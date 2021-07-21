import React, { useState, useEffect, useRef } from "react";
import { Table, Button } from "reactstrap";
import { getWithAuth, del } from "../../../Utils/httpHelper";
import { format } from "date-fns";
import "./UserPage.css";
import ModalEdt from "./ModalEdtUser";
import ModalAdd from "./ModalAddUser";
import Page from "../../Pagination";


export default function Index() {
  const [choice, setChoice] = useState("USER");
  const [pagenum, setPageNum] = useState(0);
  const [userList, setUserList] = useState([]);

  const size = 4;
  let totalPage = useRef(0);

  useEffect(() => {
    getWithAuth(`/persons/countByRole/${choice}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
    getListUser();
  }, [choice, pagenum]);

  function getListUser() {
    getWithAuth(`/persons?pagenum=${pagenum}&size=${size}&role=${choice}`).then(
      (response) => {
        if (response.status === 200) {
          setUserList([...response.data]);
        }
      }
    );
  }

  function handleDelete(id) {
    del(`/persons/${id}`)
      .then((response) => {
        if (response.status === 200) {
          alert("DELETE SUCCES");
          getListUser();
        }
      })
      .catch((error) => {
        alert("FAILED! This user is having a RATE/ORDER. Please delete it first");
      });
  }

  function getUpdated(e){
    getListUser();
  }


  return (
    <div>
      <h2 className="title-user">USER MANAGER</h2>
      <div className="btn-list">
        <Button outline color="primary" onClick={() => setChoice("USER")}>
          Customer List
        </Button>
        <Button outline color="primary" onClick={() => setChoice("ADMIN")}>
          Employee List
        </Button>
        <ModalAdd onEdit={(e) => getUpdated(e)}/>
      </div>
      <Table bordered>
        <thead>
          <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Day of Birth</th>
            <th>Gender</th>
            <th>Address</th>
            <th>Phonenumber</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {userList.map((user) => (
            <tr>
              <td>{user.fullname}</td>
              <td>{user.email}</td>
              <td>{format(new Date(user.dob), "dd/MM/yyyy")}</td>
              <td>{user.gender ? "FEMALE" : "MALE"}</td>
              <td>{user.address}</td>
              <td>{user.phonenumber}</td>
              <td>
                <Button color="danger" onClick={() => handleDelete(user.id)}>
                  Delete
                </Button>
                {/* <Button color="warning" onClick={console.log("clicked")}>
                  Edit
                </Button> */}
                <ModalEdt id={user.id} onEdit={(e) => getUpdated(e)}></ModalEdt>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Page
        total={Math.ceil(totalPage.current / size)}
        onPageChange={(e) => setPageNum(e)}
      />
    </div>
  );
}
