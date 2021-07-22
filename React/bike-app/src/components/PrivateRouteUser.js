import React from "react";
import { Route, Redirect } from "react-router-dom";
import { isCustomerLogin } from "../Utils/Auth";

const PrivateRouteUser = ({ component: Component, ...rest }) => {
  return (
    // Show the component only when the user is logged in
    // Otherwise, redirect the user to /signin page
    <Route
      {...rest}
      render={(props) =>
        isCustomerLogin() ? <Component {...props} /> : <Redirect to="/Login" />
      }
    />
  );
};

export default PrivateRouteUser;
