import React from "react";
import Header from "../Header";
import Navbar from "../Navbar";
import Footer from "../Footer";
import OrderForm from "./OrderForm";

export default function Index(props) {
  function handleLogOut(e) {}

  return (
    <>
      <Header />
      <Navbar onLogOut={(e) => handleLogOut(e)} />
      <OrderForm />
      <Footer />
    </>
  );
}
