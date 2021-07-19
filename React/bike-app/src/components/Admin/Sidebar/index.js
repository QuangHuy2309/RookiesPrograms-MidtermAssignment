import React from "react";

import "./Sidebar.css";
import { Link } from "react-router-dom";
import { ProSidebar, Menu, MenuItem, SubMenu } from "react-pro-sidebar";
import "react-pro-sidebar/dist/css/styles.css";
import { GiDutchBike } from "react-icons/gi";
import { BiUserPin } from "react-icons/bi";


export default function index(props) {
  return (
    
        <ProSidebar>
          <Menu iconShape="square">
            <MenuItem icon={<GiDutchBike />}><p onClick={() => props.onChoice("PRODUCT")}>
          PRODUCT MANAGER
        </p></MenuItem> 
            <MenuItem icon={<BiUserPin />}><p onClick={() => props.onChoice("USER")}>
          USER MANAGER
        </p></MenuItem> 
            <SubMenu title="Account">
              <MenuItem>Edit Information</MenuItem>
              <MenuItem>Log Out</MenuItem>
            </SubMenu>
          </Menu>
        </ProSidebar>
        
        
      
  );
}
