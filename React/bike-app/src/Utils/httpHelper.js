import axios from "axios";
import { getCookie } from "./Cookie";
const endpoint = "http://localhost:8080/api/v1";


export function get(url) {
  // const token = getCookie("token");
  return axios.get(endpoint + url, {
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  });
}
export function getWithAuth(url) {
  const token = getCookie("token");
  return axios.get(endpoint + url, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function post(url, body) {
  const token = getCookie("token");
  return axios.post(endpoint + url, body, {
    "Access-Control-Allow-Origin": "*",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function postAuth(url, body) {
  // const token = getCookie("token");
  return axios.post(endpoint + url, body, {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function put(url, body) {
  const token = getCookie("token");
  return axios.put(endpoint + url, body, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function putNoBody(url) {
  const token = getCookie("token");
  return axios.put(endpoint + url, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function del(url) {
  const token = getCookie("token");
  return axios.delete(endpoint + url, {
    "Access-Control-Allow-Origin": "*",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function delWithBody(url, body) {
  const token = getCookie("token");
  return axios.delete(endpoint + url, body, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
