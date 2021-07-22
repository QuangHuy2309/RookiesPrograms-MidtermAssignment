import axios from "axios";
import { getCookie } from "./Cookie";
const endpoint = "http://localhost:8080/api";
const token = getCookie("token");

export function get(url) {
  return axios.get(endpoint + url, {
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  });
}
export function getWithAuth(url, body) {
  return axios.get(endpoint + url, {
    "Access-Control-Allow-Origin": "*",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function post(url, body) {
  return axios.post(endpoint + url, body, {
    "Access-Control-Allow-Origin": "*",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function postAuth(url, body) {
  return axios.post(endpoint + url, body, {
    headers: {
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function put(url, body) {
  return axios.put(endpoint + url, body, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function putNoBody(url) {
  return axios.put(endpoint + url, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function del(url) {
  return axios.delete(endpoint + url, {
    "Access-Control-Allow-Origin": "*",
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
export function delWithBody(url, body) {
  return axios.delete(endpoint + url, body, {
    headers: {
      Authorization: `Bearer ${token}`,
      "Access-Control-Allow-Origin": "*",
      "Content-Type": "application/json; charset=utf-8",
    },
  });
}
