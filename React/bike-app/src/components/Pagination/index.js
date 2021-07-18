import React, { useState, useEffect } from "react";
import "./Pagination.css"
import { Pagination, PaginationItem, PaginationLink } from "reactstrap";

export default function Index(props) {
  const listPage = [];
  for (let i = 1; i <= props.total; i++) {
    listPage.push(i);
  }
  //   function createListPage(total) {
  //     console.log(`Page TOTAL IS ${total}`);
  //     let list =[];
  //     for (let i = 1; i <= total; i++) {
  //         list.push(i);
  //     }
  //     setPageList([...listPage,list]);
  //   }
  function setFirstPage() {
    props.onPageChange(0);
    // setPageNum(0);
  }
  function setPreviousPage() {
    // if (pagenum !== 0) setPageNum(pagenum - 1);
  }
  function setPage(number) {
    props.onPageChange(number-1);
  }
  
  //   useEffect(() => {
  //       console.log(`TOTAL PAGE IS ::::: ${props.total}`)
  //     if (props.total !== 0 )
  //         {createListPage(props.total);}
  //   }, [props.total]);

  return (

      <Pagination aria-label="Page navigation example" key="Page" className="pagination justify-content-center ">
        <PaginationItem key="first">
          <PaginationLink first onClick={() => setFirstPage()} />
        </PaginationItem>
        {/* <PaginationItem key="before">
          <PaginationLink previous onClick={() => setPreviousPage()} />
        </PaginationItem> */}
        {listPage.map((number) => (
          <PaginationItem key={`page${number}`}>
            <PaginationLink onClick={() => setPage(number)}>
              {number}
            </PaginationLink>
          </PaginationItem>
        ))}
        {/* 
        <PaginationItem>
          <PaginationLink next href="#" />
        </PaginationItem> */}
        <PaginationItem>
          <PaginationLink
            last
            onClick={() => props.onPageChange(props.total-1)}
          />
        </PaginationItem>
      </Pagination>

  );
}
