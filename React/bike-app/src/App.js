import "./App.css";
import Navbar from "./components/Navbar";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Login from "./components/Login";
import ProductByType from "./components/ProductByType";
import ProductDetail from "./components/ProductDetail";
import Home1 from "./components/Home1/Home1";
import { read_cookie } from 'sfcookies';
import { BrowserRouter, Route } from "react-router-dom";
import React, { useState, useEffect } from "react";

function App() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [loginStatus, setLoginStatus] = useState(false);
  function handleStatusChange(e) {
    setLoginStatus(true);
    setEmail(e.email);
    setUsername(e.username);
  }
  async function handleLogOut(e) {
    console.log(`STATUS: ${loginStatus}`);
    console.log("LOG OUT IN APP");
    await setLoginStatus(false);
  }
  useEffect(() => {
    console.log(`STATUS CHANE: ${loginStatus}`);
    console.log(read_cookie("token"));
  }, [loginStatus]);

  return (
    <BrowserRouter>
      <div className="App">
        <Header />
        <Navbar
          status={loginStatus}
          username={username}
          email={email}
          onLogOut={(e) => handleLogOut(e)}
        />
        <Route exact path="/">
          <Home1 />
        </Route>
        <Route exact path="/Bike/:id">
          <ProductByType />
        </Route>
        <Route exact path="/prodDetail/:id">
          <ProductDetail />
        </Route>
        <Route exact path="/Login">
          <Login onStatus={(e) => handleStatusChange(e)} />
        </Route>

        <Footer />
      </div>
    </BrowserRouter>
  );
}

export default App;
