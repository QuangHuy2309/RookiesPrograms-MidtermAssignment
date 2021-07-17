import logo from "./logo.svg";
import "./App.css";
import Navbar from "./components/Navbar";
import Header from "./components/Header";
import Footer from "./components/Footer";
import Home from "./components/Home";
import ProductByType from "./components/ProductByType";
import ProductDetail from "./components/ProductDetail";
import Home1 from "./components/Home1/Home1";
import { BrowserRouter, Route } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar/>
        <Header/>
        <Route exact path="/Home">
          <Home1/>
        </Route>
        <Route exact path="/Bike/:id/:pagenum">
          <ProductByType/>
        </Route>
        <Route exact path="/prodDetail/:id">
          <ProductDetail/>
        </Route>


        <Footer/>
      </div>
    </BrowserRouter>
  );
}

export default App;
