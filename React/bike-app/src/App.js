import "./App.css";
import Navbar from "./components/Navbar";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Login from "./components/Login";
import Admin from "./components/Admin";
import ProductByType from "./components/ProductByType";
import ProductDetail from "./components/ProductDetail";
import Home1 from "./components/Home1/Home1";
import { BrowserRouter, Route } from "react-router-dom";
import React, { useState, useEffect } from "react";
import PrivateRoute from "./components/PrivateRoute";
import { getCookie } from "./Utils/Cookie";

function App() {
  const [username, setUsername] = useState("");
  const [email, setEmail] = useState("");
  const [loginStatus, setLoginStatus] = useState(getCookie("status"));

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
  }, [loginStatus]);
  useEffect(() => {
    console.log(loginStatus);
    if (loginStatus) {
      setUsername(getCookie("username"));
      setEmail(getCookie("email"));
      console.log(`${email} + ${username}`)
    }
  },[])
  return (
    <BrowserRouter>
      <div className="App">
        <Route exact path="/">
        <Header />
        <Navbar
          status={loginStatus}
          username={username}
          email={email}
          onLogOut={(e) => handleLogOut(e)}
        />
          <Home1 />
          <Footer />
        </Route>
        <Route exact path="/Bike/:id">
        <Header />
        <Navbar
          status={loginStatus}
          username={username}
          email={email}
          onLogOut={(e) => handleLogOut(e)}
        />
          <ProductByType />
          <Footer />
        </Route>
        <Route exact path="/prodDetail/:id">
        <Header />
        <Navbar
          status={loginStatus}
          username={username}
          email={email}
          onLogOut={(e) => handleLogOut(e)}
        />
          <ProductDetail />
          <Footer />
        </Route>
        <Route exact path="/Login">
        <Header />
        <Navbar
          status={loginStatus}
          username={username}
          email={email}
          onLogOut={(e) => handleLogOut(e)}
        />
          <Login onStatus={(e) => handleStatusChange(e)} />
          <Footer />
        </Route>
        <PrivateRoute exact path="/Admin" component={Admin}/>
      </div>
    </BrowserRouter>
  );
}

export default App;
