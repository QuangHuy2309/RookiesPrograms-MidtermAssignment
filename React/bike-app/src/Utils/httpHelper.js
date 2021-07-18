import axios from "axios";

const endpoint = "http://localhost:8080/api";
const token = localStorage.getItem("token");
export function get(url) {
  return axios.get(endpoint + url, {
    headers: {
      "Access-Control-Allow-Origin": "*",
    },
  });
}
export function post(url, body) {
  return axios.post(endpoint + url, body, {
    "Access-Control-Allow-Origin": "*",
    headers: { 
      Authorization: `Bearer ${token}`, 
      'Content-Type': 'application/json; charset=utf-8',
    },
  });
}
export function postAuth(url, body) {
  return axios.post(endpoint + url, body, {
    headers: { 
      "Access-Control-Allow-Origin": "*",
      'Content-Type': 'application/json; charset=utf-8',
    },
  });
}
export function put(url, body) {
  return axios.put(endpoint + url, body, {
    "Access-Control-Allow-Origin": "*",
    headers: { 
      Authorization: `Bearer ${token}` ,
      'Content-Type': 'application/json; charset=utf-8',
    },
  });
}
export function del(url) {
  return axios.delete(endpoint + url, {
    "Access-Control-Allow-Origin": "*",
    headers: { 
      Authorization: `Bearer ${token}` ,
      'Content-Type': 'application/json; charset=utf-8',
    },
  });
}
