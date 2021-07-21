import React, { useState, useEffect } from "react";
import { Row, Col } from "reactstrap";
import Sidebar from "./Sidebar";
import Product from "./Product";
import User from "./UserPage";
import Cate from "./Categories";
import { Route } from "react-router-dom";


export default function Index() {
  const [choice, setChoice] = useState("");
  function ProductRoute(){
      return(<Product/>);
  }
  function UserRoute(){
      return(<User/>);
  }
  function showRoute(name){
    if (choice === "PRODUCT") {return (<Product/>)}
    else if (choice === "USER") {return (<User/>)}
    else if (choice === "CATE") {return (<Cate/>)}
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
        <Col>
        {showRoute()}
        </Col>
      </Row>
    </div>
  );
}
