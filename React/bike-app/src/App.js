import "./App.css";
import Navbar from "./components/Navbar";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Login from "./components/Login";
import FNFPage from "./components/404Page/404.js";
import ForgetPassword from "./components/Login/ModalForgotPass";
import Admin from "./components/Admin";
import Order from "./components/Order";
import UserOrderHistory from "./components/UserOrderHistory/UserOrderHistory.js";
import ProductByType from "./components/ProductByType";
import ProductDetail from "./components/ProductDetail";
import Home1 from "./components/Home1/Home1";
import { BrowserRouter, Route } from "react-router-dom";
import React from "react";
import PrivateRouteAdmin from "./components/PrivateRouteAdmin";
import PrivateRouteUser from "./components/PrivateRouteUser";
import { useEffect } from "react/cjs/react.development";

function App() {
  useEffect(() => {
  },[]);
  return (
    <BrowserRouter>
      <div className="App">
        <Route exact path="/">
          <Header />
          <Navbar />
          <Home1 />
          <Footer />
        </Route>
        <Route exact path="/Bike/:id">
          <Header />
          <Navbar />
          <ProductByType />
          <Footer />
        </Route>
        <Route exact path="/prodDetail/:id">
          <Header />
          <ProductDetail />
          <Footer />
        </Route>
        <Route exact path="/Login">
          <Header />
          <Login />
          <Footer />
        </Route>
        <Route exact path="/ForgetPassword">
          <Header />
          <ForgetPassword />
          <Footer />
        </Route>
        <Route exact path="/404">
          <Header />
          <Navbar />
          <FNFPage />
          <Footer />
        </Route>
        <PrivateRouteAdmin exact path="/Admin" component={Admin} />
        <PrivateRouteUser exact path="/Ordering" component={Order} />
        <PrivateRouteUser exact path="/OrderHistory" component={UserOrderHistory} />
      </div>
    </BrowserRouter>
  );
}

export default App;
