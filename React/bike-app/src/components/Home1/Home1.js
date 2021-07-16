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

  function getProductByType(id) {
    get(`/public/product/categories/${id}`).then((response) => {
      if (response.status === 200) {
        return response.data;
      }
    });
  }

  return (
    <div>
      {
        cateList.map((cate) => {
          let prodList = [];
          prodList = getProductByType(cate.id);
          console.log(prodList);
          return (
              <>
            <div key={cate.id} className="title-bike-type">
              <Link
                to={`/Bike/${cate.id}/0`}
                style={{ textDecoration: "none" }}
              >
                <h2>{cate.name}</h2>
              </Link>
            </div>
            <Product id = {cate.id}/>
            </>
          );
        })
        //   cateList.map((cate) => (
        //     <div key={cate.id} class="title-bike-type">
        //       <Link
        //         to={`product/type/${cate.id}`}
        //         style={{ textDecoration: "none" }}
        //       >
        //         <h2>{cate.name}</h2>

        //       </Link>
        //     </div>
        //   ))
      }
    </div>
  );
}
