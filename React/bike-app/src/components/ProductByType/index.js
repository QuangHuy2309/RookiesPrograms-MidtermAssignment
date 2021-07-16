import React from "react";
import { useParams } from "react-router-dom";
export default function Index() {
    let {id,pagenum} = useParams();
  return <div>
      <h2>{id}</h2>
      <h3>{pagenum}</h3>
  </div>;
}
