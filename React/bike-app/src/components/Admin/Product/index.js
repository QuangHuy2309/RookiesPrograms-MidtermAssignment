import React, { useState, useEffect, useRef } from "react";
import { get } from "../../../Utils/httpHelper";
import {
  ButtonDropdown,
  DropdownToggle,
  DropdownMenu,
  DropdownItem,
} from "reactstrap";

export default function Index() {
  const [choice, setChoice] = useState("1");
  const [pagenum, setPageNum] = useState(0);
  const [cateList, setCateList] = useState([]);
  const [dropdownOpen, setOpen] = useState(false);
  const size = 4;
  let totalPage = useRef(0);

  const toggle = () => setOpen(!dropdownOpen);

  useEffect(() => {
    get("/public/categories").then((response) => {
      if (response.status === 200) {
        setCateList([...response.data]);
      }
    });
  }, []);
  useEffect(() => {
      console.log(choice);
    get(`/public/product/numTotal/${choice}`).then((response) => {
      if (response.status === 200) {
        totalPage.current = response.data;
      }
    });
  }, [choice, pagenum]);
  return (
    <div>
      <h2 className="title-user">PRODUCT MANAGER</h2>
      <div className="btn-list">
      <ButtonDropdown  isOpen={dropdownOpen} toggle={toggle}>
        <DropdownToggle caret>Categories</DropdownToggle>
        <DropdownMenu>
          {cateList.map((cate) => (
            <div key={cate.id}>
              <DropdownItem onClick={() => setChoice(cate.id)}>{cate.name}</DropdownItem>
              <DropdownItem divider />
            </div>
          ))}
        </DropdownMenu>
      </ButtonDropdown>
      </div>
    </div>
  );
}
