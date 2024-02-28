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
  <link href="../assets/bootstrap_short.css" rel="stylesheet">
</head>

<body>
<div id="page-wrapper">
  <!-- side menu -->
  <jsp:include page="../include/gnb.jsp" />
  <!-- //side menu -->

  <!-- contents -->
  <div id="page-content-wrapper">

    <h1 class="h3">자유게시판</h1>
    <h3>글쓰기</h3>
    <br />
	<form id="jspBoardPostForm" method="post">
		<div class="form-group" style="width:100%">
			<input type="text" name="writer" id="writer" class="form-control mb-2"
				style="display:inline; width:20%;" placeholder="작성자" required
			>
			<input type="text" name="password" id="password" class="form-control mb-2"
				style="display:inline; width:20%" placeholder="비밀번호" required
			>		
			<input type="text" name="title" id="title" class="form-control mb-2"
				placeholder="제목을 입력해주세요." required
			>					
		</div>
		<div class="form-group">
			<textarea class="form-control" rows="10" name="contents" id="contents"
				placeholder="내용을 입력해주세요" required
			></textarea>
		</div><br />
		<button type="button" onclick="writePost()" class="btn btn-secondary mb-3">등록</button>
	</form>
  </div>
  <!-- //contents -->
</div>
  <script src="../assets/jquery.min.js"></script>
  <script src="../assets/bootstrap.min.js"></script>
  <script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.10/clipboard.min.js"></script>
  <script>
  	function writePost() {
		var params = {
				title: $('#title').val(),
				writer: $('#writer').val(),
				password: $('#password').val(),
				contents: $('#contents').val(),
		}
		
		if (params.writer == "") {
			alert("작성자 이름을 입력하세요.");
			return false;
		}
		if (params.password == "") {
			alert("비밀번호를 입력하세요.");
			return false;
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
			type:"POST",
			url: "/api/v1.0/jsp-board",
			data: params,
			success: function(result) {
				if (result) {
					alert("완료되었습니다.")
					location.href="/jsp-board/list";
				} else {
					alert("글쓰기에 실패하였습니다.");
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
