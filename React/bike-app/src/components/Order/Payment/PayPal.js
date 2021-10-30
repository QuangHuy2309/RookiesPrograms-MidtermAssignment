import React, { useEffect, useRef } from "react";
import { put } from "../../../Utils/httpHelper";
import { useHistory, Link } from "react-router-dom";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./PayPal.css";

toast.configure();
export default function PayPal(props) {
  const history = useHistory();
  function changeIsPay() {
    put(`/order/payment/${props.order_id}`, "").then((response) => {
      if (response.status === 200) {
        toast.success(
          `Thanks for your payment!!!`,
          {
            position: toast.POSITION.TOP_RIGHT,
            autoClose: 3000,
          }
        );
        if (window.location.pathname.includes("Ordering")) history.push("/");
        else props.onPaymentChange(true);
      }
    });
  }
  const paypal = useRef();
  useEffect(() => {
    window.paypal
      .Buttons({
        createOrder: (data, actions, err) => {
          console.log(`total: ${props.total}`);
          return actions.order.create({
            intent: "CAPTURE",
            purchase_units: [
              {
                description: "Payment for MyBikeShop",
                amount: {
                  value: props.total,
                },
              },
            ],
          });
        },
        onApprove: async (data, actions) => {
          const order = await actions.order.capture();
          changeIsPay();
        },
        onError: (error) => {
          console.log(error);
        },
      })
      .render(paypal.current);
    console.log(props.order_id);
  }, []);
  return (
    <div>
      <div ref={paypal}></div>
    </div>
  );
}
