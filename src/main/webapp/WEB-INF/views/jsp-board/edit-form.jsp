<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <meta http-equiv="X-UA-Compatible" content="ie=edge">
  <title>My Board - Jsp Board</title>
  <link href="../assets/bootstrap.css" rel="stylesheet">
</head>

<body>
<div id="page-wrapper">
  <!-- side menu -->
  <jsp:include page="../include/gnb.jsp" />
  <!-- //side menu -->

  <!-- contents -->
  <div id="page-content-wrapper">

    <h1 class="h3">자유게시판</h1>
    <h3>글수정</h3>
    <br />
	<form id="jspBoardPostForm" method="post">
		<div class="form-group" style="width:100%">
			<input type="text" name="title" id="title" class="form-control mb-2"
				placeholder="제목을 입력해주세요." required
			>					
		</div>
		<div class="form-group">
			<textarea class="form-control" rows="10" name="contents" id="contents"
				placeholder="내용을 입력해주세요" required
			></textarea>
		</div><br />
		<button type="button" class="btn btn-secondary mb-3" data-toggle="modal" data-target="#Modal_edit">등록</button>
	</form>
  </div>
  <!-- //contents -->
  
  <!-- Edit Post -->
  <div class="modal fade" id="Modal_edit" tabindex="-1" aria-labelledby="ModalLabel_edit" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="ModalLabel_edit">글 수정</h5>
          <button type="button" class="btn-close" id="edit_close" data-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3 row">
            <label for="edit_name" class="col-sm-4 col-form-label text-end">비밀번호 입력</label><br/>
            <div class="col-sm-8">
              <input type="password" class="form-control" id="editPassword" name="password" value="">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="editPost()">확인</button>
        </div>
      </div>
    </div>
  </div>    
</div>
  <script src="../assets/jquery.min.js"></script>
  <script src="../assets/bootstrap.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.10/clipboard.min.js"></script>
  <script>
	$(function(){
		getJspBoardPostDetail(${idx});
 	});
  	
 	function getJspBoardPostDetail(idx) {
		$.ajax({
			url: '/api/v1.0/jsp-board',
			type: 'GET',
			dataType: 'json',
			data: {
				idx: idx
			},
			success: function(result) {
				displayEditForm(result);
			},
			error: function(request, status, error) {
				alert('게시물을 불러오는데 실패했습니다.');
			}
		}); 		
 	}
 	
 	function displayEditForm(jspBoardPost) {
 		$('#title').val(jspBoardPost.title);
 		$('#contents').val(jspBoardPost.contents);
 	}
 	
  	function editPost() {
		var params = {
				idx: ${idx},
				title: $('#title').val(),
				contents: $('#contents').val(),
				password: $('#editPassword').val()
		}
		
		if (params.title == "") {
			alert("제목을 입력하세요.");
			return false;
		}
		if (params.contents == "") {
			alert("내용을 입력하세요.");
			return false;
		}		
		
		$.ajax({
			type:"PUT",
			url: "/api/v1.0/jsp-board",
			data: params,
			success: function(result) {
				if (result) {
					alert("완료되었습니다.")
					location.href="/jsp-board?idx=${idx}";
				} else {
					alert("글 수정에 실패하였습니다.");
				}
				
			},
			error: function (request, status, error) {
				alert("에러가 발생했습니다.");
			}
		});
  	}  	
  </script>
</body>
</html>
