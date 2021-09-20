import React from "react";

import "./Sidebar.css";
import { useHistory } from "react-router-dom";
import { ProSidebar, Menu, MenuItem, SubMenu } from "react-pro-sidebar";
import "react-pro-sidebar/dist/css/styles.css";
import { GiDutchBike } from "react-icons/gi";
import { GiTruck } from "react-icons/gi";
import { BiUserPin } from "react-icons/bi";
import { logOut } from "../../../Utils/Auth";
import { AiFillDatabase, AiOutlineLineChart } from "react-icons/ai";
import { MdBorderColor } from "react-icons/md";
import ModalConfirm from "../../ModalConfirm";
import ModalChangePass from "../../ModalChangePass";
import ModalEdtUser from "../UserPage/ModalEdtUser";
import { getCookie } from "../../../Utils/Cookie";

export default function Index(props) {
  const history = useHistory();
  const id = getCookie("id");
  function handleLogOut(e) {
    if (e === "OK") {
      logOut();
      history.push("/");
    }
  }
  function handleUpdate(e){}
  return (
    <ProSidebar>
      <Menu iconShape="square">
        <MenuItem
          icon={<AiFillDatabase />}
          onClick={() => props.onChoice("CATE")}
        >
          CATEGORIES
        </MenuItem>
        <MenuItem
          icon={<GiDutchBike />}
          onClick={() => props.onChoice("PRODUCT")}
        >
          PRODUCT MANAGER
        </MenuItem>
        <MenuItem
          icon={<MdBorderColor />}
          onClick={() => props.onChoice("ORDER")}
        >
          ORDER MANAGER
        </MenuItem>
        <MenuItem
          icon={<GiTruck />}
          onClick={() => props.onChoice("ORDER IMPORT")}
        >
          ORDER IMPORT
        </MenuItem>
        <MenuItem icon={<BiUserPin />} onClick={() => props.onChoice("USER")}>
          USER MANAGER
        </MenuItem>
        <MenuItem
          icon={<AiOutlineLineChart />}
          onClick={() => props.onChoice("REPORT")}
        >
          REPORT
        </MenuItem>
        <SubMenu title="Account">
          {/* <MenuItem>Edit Information</MenuItem> */}
          {/* <MenuItem onClick={() => props.onChoice("CHANGEPASS")}> 
          Change Password</MenuItem> */}
          <MenuItem>
            <ModalEdtUser isUser = "true" id={id} onEdit={(e) => handleUpdate(e)} />
          </MenuItem>
          <MenuItem>
            <ModalChangePass />
          </MenuItem>
          <MenuItem>
            <ModalConfirm onChoice={(e) => handleLogOut(e)} />
          </MenuItem>
        </SubMenu>
      </Menu>
    </ProSidebar>
  );
}
