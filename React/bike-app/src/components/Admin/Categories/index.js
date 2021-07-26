import React, { useState, useEffect } from "react";
import { get, del } from "../../../Utils/httpHelper";
import ModalEdt from "./ModalEdtCate"
import ModalAdd from "./ModalAddCate"
import {toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import {
    Table,
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

  function handleDelete(e,id){
      // console.log("DELETE CLICKED");
      // notify();
      if (e === "OK") {
      del(`/categories/${id}`)
      .then((response) => {
        if (response.status === 200) {
          toast.success("Delete successfully!!!", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          getListCate();
        }
      })
      .catch((error) => {
        if (error.response.status === 409)
        toast.error("Please delete product with this Categories first", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
      });
    }
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
              <td style={{display:"flex", "text-align":"center"}}>
                <ModalDeleteConfirm onChoice={(e) => handleDelete(e,cate.id)} />
                {/* <Button color="danger" onClick={() => handleDelete(cate.id)}>
                  Delete
                </Button> */}
                <ModalEdt id={cate.id} onEdit={(e) => getUpdated(e)}></ModalEdt>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}
