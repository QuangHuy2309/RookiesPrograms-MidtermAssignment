import React, { useState, useEffect } from "react";
import { Link } from "react-router-dom";
import { get } from "../../Utils/httpHelper";
import Product from "./Product";
import "./Home.css";

export default function Home1() {
  const [cateList, setCateList] = useState([]);
  //   const [prodList, setProdList] = useState([]);
  // const prodList = [];
  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
  }, []);

  return (
    <div>
      {
        cateList.map((cate) => {
          let prodList = [];
          console.log(prodList);
          return (
            <div key={cate.id}>
              <div className="title-bike-type">
                <Link
                  to={`/Bike/${cate.id}`}
                  style={{ textDecoration: "none" }}
                >
                  <h2>{cate.name}</h2>
                </Link>
              </div>
              <Product id={cate.id} />
            </div>
          );
        })
      }
    </div>
  );
}
