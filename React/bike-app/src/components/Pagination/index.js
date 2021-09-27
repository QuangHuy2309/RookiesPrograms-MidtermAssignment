import React from "react";
import "./Pagination.css"
import { Pagination, PaginationItem, PaginationLink } from "reactstrap";

export default function Index(props) {
  const listPage = [];
  for (let i = 1; i <= props.total; i++) {
    listPage.push(i);
  }
 
  function setFirstPage() {
    props.onPageChange(0);
 
  }

  function setPage(number) {
    props.onPageChange(number-1);
  }

  return (

      <Pagination aria-label="Page navigation example" key="Page" className="pagination justify-content-center mb-3">
        <PaginationItem key="first">
          <PaginationLink first onClick={() => setFirstPage()} />
        </PaginationItem>
        {listPage.map((number) => (
          <PaginationItem key={`page${number}`}>
            <PaginationLink onClick={() => setPage(number)}>
              {number}
            </PaginationLink>
          </PaginationItem>
        ))}
        <PaginationItem>
          <PaginationLink
            last
            onClick={() => props.onPageChange(props.total-1)}
          />
        </PaginationItem>
      </Pagination>

  );
}
