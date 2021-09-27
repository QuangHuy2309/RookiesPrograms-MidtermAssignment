import React, { useState, useEffect, useRef } from "react";
import {
  Table,
  Label,
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
  Row,
  Col,
} from "reactstrap";
import { getWithAuth, del } from "../../../Utils/httpHelper";
import { format } from "date-fns";
import "./StaffPage.css";
import ModalEdtAdmin from "./ModalEdtAdmin";
import ModalAdd from "./ModalAddEmployee";
import Page from "../../Pagination";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import { getCookie } from "../../../Utils/Cookie";

toast.configure();

export default function Index() {
  const [choice, setChoice] = useState("EMPLOYEE");
  const [pagenum, setPageNum] = useState(0);
  const [userList, setUserList] = useState([]);
  const [dropdownOpen, setOpen] = useState(false);
  const [totalPage, setTotalPage] = useState(0);
  const role = getCookie("role");
  const size = 4;

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

  const toggle = () => setOpen(!dropdownOpen);

  function getTotalUser() {
    getWithAuth(`/persons/countByRole/${choice}`).then((response) => {
      if (response.status === 200) {
        setTotalPage(response.data);
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

  return (
    <div>
      <h2 className="title-UserAdmin">USER MANAGER</h2>
      <Row className="">
        <Col className="col-7 btn-list-StaffPage">
        <ModalAdd onAdd={(e) => handleAdd(e)} />
        </Col>
        <Col className="selectRole-StaffPage">
          {<Label className={`statusChoiceText-Order `}>Role: </Label>}
          <ButtonDropdown
            isOpen={dropdownOpen}
            toggle={toggle}
            className="ms-3"
          >
            <DropdownToggle caret>{choice}</DropdownToggle>
            <DropdownMenu>
              <DropdownItem onClick={() => setChoice("EMPLOYEE")}>
                All
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem onClick={() => setChoice("ADMIN")}>
                Admin
              </DropdownItem>
              <DropdownItem divider />
              <DropdownItem onClick={() => setChoice("STAFF")}>
                Staff
              </DropdownItem>
            </DropdownMenu>
          </ButtonDropdown>{" "}
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
            <th className="titleTable-UserAdmin">Role</th>
            <th className="titleTable-UserAdmin"></th>
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
              <td>{user.role}</td>
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

                <ModalEdtAdmin
                  id={user.id}
                  onEdit={(e) => handleUpdate(e)}
                ></ModalEdtAdmin>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
      <Page
        total={Math.ceil(totalPage / size)}
        onPageChange={(e) => setPageNum(e)}
      />
    </div>
  );
}
