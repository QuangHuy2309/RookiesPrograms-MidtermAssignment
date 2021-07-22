import React from "react";
import { Route, Redirect } from "react-router-dom";
import { isAdminLogin } from "../Utils/Auth";

const PrivateRouteAdmin = ({ component: Component, ...rest }) => {
  return (
    // Show the component only when the user is logged in
    // Otherwise, redirect the user to /signin page
    <Route
      {...rest}
      render={(props) =>
        isAdminLogin() ? <Component {...props} /> : <Redirect to="/Login" />
      }
    />
  );
};

export default PrivateRouteAdmin;
