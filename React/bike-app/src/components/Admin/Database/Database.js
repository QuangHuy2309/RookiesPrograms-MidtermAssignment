import React, { useState, useEffect, useRef } from "react";
import { getWithAuth, del, post } from "../../../Utils/httpHelper";
import { FcDataBackup } from "react-icons/fc";
import "./Database.css";
import { toast } from "react-toastify";
import Page from "../../Pagination";
import ModalDeleteConfirm from "../ModalDeleteConfirm";
import ModalRestore from "./ModalRestore";
import { Table, Button } from "reactstrap";

toast.configure();
export default function Database() {
  const [fileNameList, setFileNameList] = useState([]);
  const [pagenum, setPageNum] = useState(0);
  const [totalPage, setTotalPage] = useState(0);
  const size = 4;

  useEffect(() => {
    getListFileName();
  }, []);



  function getListFileName() {
    getWithAuth("/db/getBackupFileName").then((response) => {
      if (response.status === 200) {
        setFileNameList([...response.data.sort((a1, a2) => (a1.assetId < a2.assetId ? 1 : -1))]);
        setTotalPage(response.data.length);
      }
    });
  }
  function btnBackUpClick() {
    getWithAuth("/db/export").then((response) => {
      if (response.status === 200) {
        toast.success("Backup successfully!!!", {
          position: toast.POSITION.TOP_RIGHT,
          autoClose: 3000,
        });
        getListFileName();
      }
    });
  }
  function dateFormat(name) {
    if (name.trim().length === 13) {
      let dateList = name.split("_");
      let dateFormat = `${dateList[0].slice(6, 8)}-${dateList[0].slice(
        4,
        6
      )}-${dateList[0].slice(0, 4)} ${dateList[1].slice(
        0,
        2
      )}:${dateList[1].slice(2, 4)}`;
      return dateFormat;
    } else return name;
  }
  function handleDelete(e, name) {
    if (e === "OK") {
      del(`/db/${name}`)
        .then((response) => {
          if (response.status === 200) {
            toast.success("Delete successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
            getListFileName();
          }
        })
        .catch((error) => {
          if (error.response.status === 400)
            toast.error("Delete failed!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
        });
    }
  }
  function handleRestoreClick(e, fileName) {
    if (e) {
      post(`/db/import/${fileName}`)
        .then((response) => {
          console.log(response.data);
          if (response.status === 200)
            toast.success("Restore successfully!!!", {
              position: toast.POSITION.TOP_RIGHT,
              autoClose: 3000,
            });
        })
        .catch((error) => {
          toast.error("Restore failed!", {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          });
          console.log(error);
        });
    }
  }
  function pagingTable() {
    let lastIndex = (pagenum+1) * size;
    let startIndex = lastIndex - size;
    let fileNamePaging = fileNameList.slice(startIndex,lastIndex);
    return fileNamePaging.map((fileName, index) => (
      <tr key={index}>
        <td>{dateFormat(fileName)}</td>
        <td style={{ display: "flex", "justify-content": "center" }}>
          <ModalRestore
            id={dateFormat(fileName)}
            onChoice={(e) => handleRestoreClick(e, fileName)}
          />
          <ModalDeleteConfirm onChoice={(e) => handleDelete(e, fileName)} />
        </td>
      </tr>
    ));
  }
  return (
    <div>
      <h2 className="title-database">BACKUP AND RESTORE</h2>
      <br />
      <div className="btnBackUp-DB mb-3">
        <Button color="info" outline onClick={() => btnBackUpClick()}>
          <FcDataBackup /> Backup Data
        </Button>
      </div>
      <Table bordered className="ms-3">
        <thead>
          <tr>
            <th className="titleTable-Database">TIME BACKUP</th>
            <th className="titleTable-Database">ACTION</th>
          </tr>
        </thead>
        <tbody>
          {/* {fileNameList.map((fileName, index) => (
            <tr key={index}>
              <td>{dateFormat(fileName)}</td>
              <td style={{ display: "flex", "justify-content": "center" }}>
                <ModalRestore
                  id={dateFormat(fileName)}
                  onChoice={(e) => handleRestoreClick(e,fileName)}
                />
                <ModalDeleteConfirm
                  onChoice={(e) => handleDelete(e, fileName)}
                />
              </td>
            </tr>
          ))} */}
          {pagingTable()}
        </tbody>
      </Table>
      <Page
        total={Math.ceil(totalPage / size)}
        onPageChange={(e) => setPageNum(e)}
      />
    </div>
  );
}
