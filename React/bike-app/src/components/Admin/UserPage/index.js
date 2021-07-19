import React, { useState, useEffect, useRef } from "react";
import { Table, Button } from "reactstrap";
import { getWithAuth } from "../../../Utils/httpHelper";
import "./UserPage.css";
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
  }, [choice, pagenum]);

  return (
    <div>
      <h2 className="title-user">USER MANAGER</h2>
      <div className="btn-list">
        <Button outline color="primary">
          Customer List
        </Button>
        <Button outline color="primary">
          Employee List
        </Button>
      </div>
      <Table bordered>
        <thead>
          <tr>
            <th>#</th>
            <th>First Name</th>
            <th>Last Name</th>
            <th>Username</th>
          </tr>
        </thead>
        <tbody>
          <tr>
            <th scope="row">1</th>
            <td>Mark</td>
            <td>Otto</td>
            <td>@mdo</td>
          </tr>
          <tr>
            <th scope="row">2</th>
            <td>Jacob</td>
            <td>Thornton</td>
            <td>@fat</td>
          </tr>
          <tr>
            <th scope="row">3</th>
            <td>Larry</td>
            <td>the Bird</td>
            <td>@twitter</td>
          </tr>
        </tbody>
      </Table>
    </div>
  );
}
