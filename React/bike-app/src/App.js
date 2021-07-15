import logo from "./logo.svg";
import "./App.css";
import Navbar from "./components/Navbar";
import Header from "./components/Header";
import { BrowserRouter, Route } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        <Navbar/>
        <Header/>
      </div>
    </BrowserRouter>
  );
}

export default App;
