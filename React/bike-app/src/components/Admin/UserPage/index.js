import React, { useState, useEffect, useRef } from "react";
import { Table, Button, Row, Col, Input } from "reactstrap";
import { getWithAuth, del } from "../../../Utils/httpHelper";
import { format } from "date-fns";
import "./UserPage.css";
import ModalEdt from "./ModalEdtUserInPage";
import Page from "../../Pagination";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import { getCookie } from "../../../Utils/Cookie";

toast.configure();

export default function Index() {
  const [choice, setChoice] = useState("USER");
  const [pagenum, setPageNum] = useState(0);
  const [userList, setUserList] = useState([]);
  const role = getCookie("role");
  const [showPagination, setShowPage] = useState(true);
  const size = 4;
  let totalPage = useRef(0);

  useEffect(() => {
    // getWithAuth(`/persons/countByRole/${choice}`).then((response) => {
    //   if (response.status === 200) {
    //     totalPage.current = response.data;
    //   }
    // });
    getListUser();
  }, [choice, pagenum]);
  useEffect(() => {
    getTotalUser();
  }, [userList]);
  function getTotalUser() {
    getWithAuth(`/persons/countByRole/${choice}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
  }

  async function getListUser() {
    getWithAuth(`/persons?pagenum=${pagenum}&size=${size}&role=${choice}`).then(
      (response) => {
        if (response.status === 200) {
          setUserList([...response.data]);
        }
      }
    );
  }

  function handleDelete(e, id) {
    if (e === "OK") {
      del(`/persons/${id}`)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Delete successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            getListUser();
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

  function handleUpdate(e) {
    if (e) getListUser();
  }
  function handleAdd(e) {
    if (e) getListUser();
  }
  async function handleSearchChange(e) {
    if (e.target.value.trim().length > 0) {
      setUserList([]);
      let keyword = e.target.value.trim().split(/ +/).join(' ');
      getWithAuth(
        `/persons/search?keyword=${keyword}&role=USER`
      ).then((response) => {
        if (response.status === 200) {
          setUserList([...response.data]);
          setShowPage(false);
        }
      });
    } else {
      setShowPage(true);
      getListUser();
    }
  }
  return (
    <div>
      <h2 className="title-UserAdmin">CUSTOMER MANAGER</h2>
      <Row className="mb-3">
        <Col className="col-7">
          <div className="btn-list"> </div>
        </Col>
        <Col>
          <Input
            type="text"
            name="name"
            id="name"
            className="search-OrderImport"
            placeholder="Search Customer by name"
            onChange={(e) => handleSearchChange(e)}
          />
        </Col>
      </Row>

      <Table bordered className="tableUser">
        <thead>
          <tr>
            <th className="titleTable-UserAdmin">ID</th>
            <th className="titleTable-UserAdmin">Name</th>
            <th className="titleTable-UserAdmin">Email</th>
            <th className="titleTable-UserAdmin">Day of Birth</th>
            <th className="titleTable-UserAdmin">Gender</th>
            <th className="titleTable-UserAdmin">Address</th>
            <th className="titleTable-UserAdmin">Phonenumber</th>
            <th className="titleTable-UserAdmin">Action</th>
          </tr>
        </thead>
        <tbody>
          {userList.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.fullname}</td>
              <td>{user.email}</td>
              <td>{format(new Date(user.dob), "dd/MM/yyyy")}</td>
              <td>{user.gender ? "Female" : "Male"}</td>
              <td>{user.address}</td>
              <td>{user.phonenumber}</td>
              <td>
                {/* <Button color="danger" onClick={() => handleDelete(user.id)}>
                  Delete
                </Button> */}
                <ModalDeleteConfirm
                  onChoice={(e) => handleDelete(e, user.id)}
                />
                {/* <Button color="warning" onClick={console.log("clicked")}>
                  Edit
                </Button> */}
                {/* {choice.includes("ADMIN") ? (
                  <ModalEdtAdmin
                    id={user.id}
                    onEdit={(e) => handleUpdate(e)}
                  ></ModalEdtAdmin>
                ) :  */}
                <ModalEdt
                  id={user.id}
                  onEdit={(e) => handleUpdate(e)}
                ></ModalEdt>
                {/* } */}
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      {showPagination ? (
        <Page
          total={Math.ceil(totalPage.current / size)}
          onPageChange={(e) => setPageNum(e)}
        />
      ) : null}
    </div>
  );
}
