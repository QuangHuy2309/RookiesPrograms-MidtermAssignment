import { getCookie } from "./Cookie";
import { getWithAuth } from "../Utils/httpHelper";

export function isAdminLogin() {
  const status = getCookie("status");
  const role = getCookie("role");
  if (status === "true" && (role === "ROLE_ADMIN" || role === "ROLE_STAFF")) {
    return true;
  } else {
    console.log("isLogin FALSE");
    return false;
  }
}
export function isCustomerLogin() {
  const status = getCookie("status");
  const role = getCookie("role");
  if (status === "true" && role === "ROLE_USER") {
    return true;
  } else {
    console.log("isLogin FALSE");
    return false;
  }
}
export function isLogin() {
  const status = getCookie("status");
  const role = getCookie("role");
  if (status === "true" && role !== "") {
    return true;
  } else {
    return false;
  }
}

export function logOut() {
  document.cookie = `token=; max-age=86400; path=/;`;
  document.cookie = `username=; max-age=86400; path=/;`;
  document.cookie = `email=; max-age=86400; path=/;`;
  document.cookie = `role=; max-age=86400; path=/;`;
  document.cookie = `cart=; max-age=86400; path=/;`;
  document.cookie = `status=false; max-age=86400; path=/;`;
 
}
