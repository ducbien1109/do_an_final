import React from "react";
import { AiFillMessage } from "react-icons/ai";

const Mess = () => {
  const iconStyle = {
    position: "fixed",
    bottom: "200px",
    right: "20px",
    fontSize: "40px",
    zIndex: 1000,
    animation: "shake 0.5s ease-in-out infinite",
  };

  const shakeKeyframes = `
    @keyframes shake {
      0% { transform: rotate(0deg); }
      25% { transform: rotate(-15deg); }
      50% { transform: rotate(0deg); }
      75% { transform: rotate(15deg); }
      100% { transform: rotate(0deg); }
    }
  `;

  return (
    <>
      <style>{shakeKeyframes}</style>
      <div style={iconStyle}>
        <a href="https://www.facebook.com/profile.php?id=100033968265083">
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/thumb/b/be/Facebook_Messenger_logo_2020.svg/1024px-Facebook_Messenger_logo_2020.svg.png"
            style={{ width: "50px" }}
          />
        </a>
      </div>
    </>
  );
};

export default Mess;
