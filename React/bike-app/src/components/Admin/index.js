import React, { useState, useEffect } from "react";
import { Row, Col } from "reactstrap";
import Sidebar from "./Sidebar";
import Product from "./Product";
import User from "./UserPage";
import Cate from "./Categories";
import Order from "./Order";
import ChangePass from "./ChangePassword";
import "./Admin.css"


export default function Index() {
  const [choice, setChoice] = useState("");
  function ProductRoute(){
      return(<Product/>);
  }
  function UserRoute(){
      return(<User/>);
  }
  function showRoute(){
    switch(choice){
      case 'PRODUCT':
      return (<Product/>);
      case 'CATE':
      return (<Cate/>);
      case 'USER':
      return (<User/>);
      case 'ORDER':
      return (<Order/>);
      case 'CHANGEPASS':
      return (<ChangePass/>);
    }
    // if (choice === "PRODUCT") {return (<Product/>)}
    // else if (choice === "USER") {return (<User/>)}
    // else if (choice === "CATE") {return (<Cate/>)}
    // else if (choice === "ORDER") {return (<Order/>)}
    // else if (choice === "CHANGEPASS") {return (<ChangePass/>)}
  }
  function handleChoiceChange(e){
      console.log(e);
      setChoice(e);
  }
  useEffect(()=>{

  },[choice])
  return (
    <div>
      <Row className="mainRow-admin">
        <Col className="col-2">
            <Sidebar onChoice={(e) => handleChoiceChange(e)} />
        </Col>
        <Col className="col">
        {showRoute()}
        </Col>
      </Row>
    </div>
  );
}
