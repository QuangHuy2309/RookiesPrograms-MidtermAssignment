import React, { Component } from "react";
import "./Home.css";
import { get } from "../../Utils/httpHelper";
import { Link } from "react-router-dom";
export default class Index extends Component {
  state = {
    cateList: [],
    prodList: [],
  };
  componentDidMount() {
      
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        this.setState({ cateList: response.data });
      }
    });
    // this.state.cateList.map(cate => {
    //     console.log("AV")
    //     console.log(`/public/product/categories/${cate.id}`);
        // get(`/public/product/categories/${cate.id}`).then((response) => {
        //     if (response.status === 200) {
        //       this.setState({
        //           prodList: [...this.state.prodList,response.data],
        //       });
        //     }
        //   });
    // });
  }
  getProductByType(id){
    get(`/public/product/categories/${id}`).then((response) => {
          if (response.status === 200) {
            this.setState({
                prodList: response.data,
            });
          }
        });
  }
  render() {
    return (
      <div>
        {
        // this.state.cateList.map((cate) => (
        //   <div key={cate.id} class="title-bike-type">
        //     <Link
        //       to={`product/type/${cate.id}`}
        //       style={{ textDecoration: "none" }}>
        //       <h2>{cate.name}</h2>
        //     </Link>
        //   </div>
        // ))
        this.state.cateList.map((cate) => (
          <div>
            <div key={cate.id} class="title-bike-type">
              <Link
                to={`product/type/${cate.id}`}
                style={{ textDecoration: "none" }}>
                <h2>{cate.name}</h2>
              </Link>
            </div>
            {this.getProductByType(cate.id)}
            </div>
          ))
        }
      </div>
    );
  }
}
