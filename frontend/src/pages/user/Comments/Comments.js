import React, { useState, useEffect } from "react";
import { useCookies } from "react-cookie";
import { toast } from "react-toastify";
import { ConfigProvider, Popconfirm } from "antd";
import { API, MESSAGE, POPCONFIRM, PROFILE_PAGE } from "@Const";
import EditComment from "./Editcomment";

function CommentList() {
  const [cookies] = useCookies(['access_token']);
  const [comments, setComments] = useState([]);
  const [editingComment, setEditingCommentId] = useState(null);

  const accessToken = cookies.access_token;

  const fetchComments = async () => {
    try {
      const url = API.PUBLIC.GET_COMMENT + window.location.search
      const response = await fetch(url, {
        method: "GET",
        headers: {
          "Authorization": `Bearer ${accessToken}`,
        },
      });

      if (response.ok) {
        const data = await response.json();
        setComments(data);
      } else {
        const data = await response.json();
        toast.error(data.message);
      }
    } catch (error) {
      toast.error(MESSAGE.DB_CONNECTION_ERROR);
    }
  };
  const handleDelete = async (commentID) => {
    try {
      const formData = new FormData();
      formData.append("commentID", commentID);

      const response = await fetch(API.PUBLIC.DELETE_COMMENT, {
        method: "POST",
        headers: {
          "Authorization": `Bearer ${accessToken}`,
        },
        body: formData,
      });

      if (response.ok) {
        fetchComments().then(() => {
          toast.success("Comment deleted successfully");
        });
      } else {
        const data = await response.json();
        toast.error(data.message || "Error deleting comment");
      }
    } catch (error) {
      console.error("Error:", error);
      toast.error("An error occurred while deleting the comment");
    }
  };
  const handleEdit = (comment) => {
    setEditingCommentId(comment);
  };
  useEffect(() => {
    fetchComments().then(r => { });
  }, []);

  return (
    <div>
      {comments.map((comment, index) => (
        <div className="comment">
          <img className="avatar comment-avatar" src={comment.userID.avatarPath} alt="Avatar" />
          <div className="comment-content">
            <div>
              <div className="content">{comment.userID.fullName}</div>
              <div className="author">{comment.content}</div>
              <div className="content">{comment.createdAt}</div>
            </div>
            {editingComment === comment.commentID && (
              <EditComment
                comment={editingComment}
                onCancel={() => setEditingCommentId(null)}
              />
            )}
            <div className="action-comment">
              <div><a className="edit-comment" onClick={() => handleEdit(comment.commentID)}>Edit</a></div>
              <div><a className="delete-comment" onClick={() => handleDelete(comment.commentID)}>Delete</a></div>
            </div>
          </div>
        </div>
      ))}
    </div>
  );
}

export default CommentList;

// import React, { useState, useEffect } from "react";
// import { useCookies } from "react-cookie";
// import { toast } from "react-toastify";
// import { API, MESSAGE } from "@Const";
// import EditComment from "./Editcomment"; // Import EditComment component

// function CommentList() {
//   const [cookies] = useCookies(['access_token']);
//   const [comments, setComments] = useState([]);
//   const [editingComment, setEditingComment] = useState(null); // State để lưu comment đang chỉnh sửa
//   const [accessToken] = cookies.access_token;

//   const fetchComments = async () => {
//     try {
//       const url = API.PUBLIC.GET_COMMENT + window.location.search;
//       const response = await fetch(url, {
//         method: "GET",
//         headers: {
//           "Authorization": `Bearer ${accessToken}`,
//         },
//       });

//       if (response.ok) {
//         const data = await response.json();
//         setComments(data);
//       } else {
//         const data = await response.json();
//         toast.error(data.message);
//       }
//     } catch (error) {
//       toast.error(MESSAGE.DB_CONNECTION_ERROR);
//     }
//   };

//   const handleDelete = async (commentID) => {
//     try {
//       const formData = new FormData();
//       formData.append("commentID", commentID);

//       const response = await fetch(API.PUBLIC.DELETE_COMMENT, {
//         method: "POST",
//         headers: {
//           "Authorization": `Bearer ${accessToken}`,
//         },
//         body: formData,
//       });

//       if (response.ok) {
//         fetchComments().then(() => {
//           toast.success("Comment deleted successfully");
//         });
//       } else {
//         const data = await response.json();
//         toast.error(data.message || "Error deleting comment");
//       }
//     } catch (error) {
//       console.error("Error:", error);
//       toast.error("An error occurred while deleting the comment");
//     }
//   };

//   const handleEdit = (comment) => {
//     setEditingComment(comment); // Lưu comment đang chỉnh sửa vào state
//   };

//   useEffect(() => {
//     fetchComments();
//   }, []);

//   return (
//     <div>
//       {comments.map((comment, index) => (
//         <div className="comment" key={comment.commentID}>
//           <img className="avatar comment-avatar" src={comment.userID.avatarPath} alt="Avatar" />
//           <div className="comment-content">
//             <div>
//               <div className="content">{comment.userID.fullName}</div>
//               <div className="author">{comment.content}</div>
//               <div className="content">{comment.createdAt}</div>
//             </div>
//             <div className="action-comment">
//               <div><a className="edit-comment" onClick={() => handleEdit(comment)}>Edit</a></div>
//               <div><a className="delete-comment" onClick={() => handleDelete(comment.commentID)}>Delete</a></div>
//             </div>
//           </div>
//         </div>
//       ))}
//       {/* Hiển thị EditComment component khi có comment đang được chỉnh sửa */}
//       {editingComment && (
//         <EditComment
//           comment={editingComment}
//           onCancel={() => setEditingComment(null)} // Hủy bỏ chỉnh sửa
//         />
//       )}
//     </div>
//   );
// }

// export default CommentList;
