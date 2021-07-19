import { getCookie } from "./Cookie";

export function isLogin() {
  const status = getCookie("status");
  const role = getCookie("role");
  if ((status === "true") && (role === "ROLE_ADMIN")) {
    console.log("isLogin TRUE");  
    return true;
}
  else {
    console.log("isLogin FALSE");  
      return false;
    }
}
