import React, { useState, useEffect } from "react";
import { Row, Col } from "reactstrap";
import Sidebar from "./Sidebar";
import Product from "./Product";
import User from "./UserPage";
import Staff from "./StaffPage";
import Cate from "./Categories";
import Order from "./Order";
import OrderImport from "./OrderImport/OrderImport.js";
import Database from "./Database/Database.js";
import Report_RaE from "./Report_RaE/Report_RaE.js";
import Report_ProfitMonth from "./Report_ProfitMonth/Report_ProfitMonth.js";
import REPORT_TopProd from "./Report_TopProd/Report_TopProd.js";
import REPORT_ProdProccess from "./ReportProductProccess/ReportProductProcess.js";
import "./Admin.css"


export default function Index() {
  const [choice, setChoice] = useState("ORDER");
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
      case 'EMPLOYEE':
      return (<Staff/>);
      case 'ORDER':
      return (<Order/>);
      case 'ORDER IMPORT':
      return (<OrderImport/>);
      case 'DATABASE':
      return (<Database/>);
      case 'REPORT_RaE':
      return (<Report_RaE/>);
      case 'REPORT_ProfitMonth':
      return (<Report_ProfitMonth/>);
      case 'REPORT_TopProd':
      return (<REPORT_TopProd/>);
      case 'REPORT_ProdProccess':
      return (<REPORT_ProdProccess/>);
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
