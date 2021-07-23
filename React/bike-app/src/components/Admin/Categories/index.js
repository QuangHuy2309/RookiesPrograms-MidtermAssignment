import React, { useState, useEffect } from "react";
import { get, del } from "../../../Utils/httpHelper";
import ModalEdt from "./ModalEdtCate"
import ModalAdd from "./ModalAddCate"
import {toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { MdDelete } from "react-icons/md";
import {
    ButtonDropdown,
    DropdownToggle,
    DropdownMenu,
    DropdownItem,
    Table,
    Button,
  } from "reactstrap";

  toast.configure()
export default function Index() {
  const [cateList, setCateList] = useState([]);

  useEffect(() => {
    getListCate();
  }, []);
  function getListCate(){
    get("/public/categories").then((response) => {
        if (response.status === 200) {
          setCateList([...response.data]);
        }
      });
  }
  const notify = () =>{
    toast('Delete successfully!!!', {position: toast.POSITION.TOP_RIGHT});
  }
  function handleDelete(id){
      // console.log("DELETE CLICKED");
      // notify();
     
      del(`/categories/${id}`)
      .then((response) => {
        if (response.status === 200) {
          alert("DELETE SUCCES");
          getListCate();
        }
      })
      .catch((error) => {
        if (error.response.status === 409)
        alert("This category had at least a product. Delete Product have this category first!");
      });
  }
  function getUpdated(e){
    getListCate();
  }
  return (
    <div>
      <h2 className="title-user">CATEGORIES MANAGER</h2>
        <br/>
        <ModalAdd/>
      <Table bordered>
        <thead>
          <tr>
            <th>ID</th>
            <th>NAME</th>
            <th>DESCRIPTION</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          {cateList.map((cate) => (
            <tr key={cate.id}>
              <th scope="row">{cate.id}</th>
              <td>{cate.name}</td>
              <td>{cate.description}</td>
              <td>
                <Button color="danger" onClick={() => handleDelete(cate.id)}>
                  Delete
                </Button>
                <ModalEdt id={cate.id} onEdit={(e) => getUpdated(e)}></ModalEdt>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
