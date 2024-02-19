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
  <title>My Board - Free Board</title>
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
	    <br /><br />
	    <div id="detailSection">
	    
		</div>
	</div>
  <!-- //contents -->
</div>

<!-- Delete Post -->
  <div class="modal fade" id="Modal_del" tabindex="-1" aria-labelledby="ModalLabel_del" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
      <div class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="ModalLabel_del">글 삭제</h5>
          <button type="button" class="btn-close" id="del_close" data-dismiss="modal" aria-label="Close"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3 row">
            <label for="del_name" class="col-sm-4 col-form-label text-end">비밀번호 입력</label><br/>
            <div class="col-sm-8">
              <input type="password" class="form-control" id="delPassword" name="password" value="">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-primary" onclick="deleteFreePost()">확인</button>
        </div>
      </div>
    </div>
  </div>
  
<script src="../assets/jquery.min.js"></script>
<script src="../assets/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.10/clipboard.min.js"></script>
<script>
	$(function(){
		getFreePostDetail(${idx});
	});

	function getFreePostDetail(idx) {
		
		$.ajax({
			url: '/api/v1.0/free',
			type: 'GET',
			dataType: 'json',
			data: {
				idx: idx
			},
			success: function(result) {
				displayFreePostDetail(result);
			},
			error: function(request, status, error) {
				alert('게시물을 불러오는데 실패했습니다.');
			}
		});
	}
	
	function displayFreePostDetail(freePost) {
		var createdAt = freePost.createdAt;
		var contents = freePost.contents;
		
		var detailHtml = '<h2>'+freePost.title+'</h2>'+
			'<div class="mb-3">'+
				'<div class="mb-3">'+
					'<label class="form-label">'+freePost.writer+'</label><br />'+
					'<label class="form-label" style="font-size: small;">'+freePost.createdAt+'</label><br />'+
					'<label class="form-label" style="font-size: small;">Hits: '+freePost.hits+'</label>'+
				'</div>'+
			'</div>'+
			'<hr size="5px;">'+
			'<div class="mb-3" style="white-space: pre;">'+
			freePost.contents+
			'</div>'+
			'<hr size="5px">'+
			'<div class="float-end mb-2" style="margin-left:5px;">'+
      			'<button type="button" class="btn btn-sm btn-outline-primary" data-toggle="modal" data-target="#Modal_del">'+
      		'삭제'+
      		'</button>'+
    		'</div>'+
			'<div class="float-end mb-2">'+
      			'<button type="button" class="btn btn-sm btn-outline-primary" onclick="location.href=\'/free/edit-form?idx=${idx}\'">'+
	      		'수정'+
	      		'</button>'+
    		'</div>';    		
			
		$('#detailSection').append(detailHtml);

	}
	
	function deleteFreePost() {
		var idx = ${idx};
		var password = $('#delPassword').val();
		
		$.ajax({
			url: '/api/v1.0/free',
			type: 'DELETE',
			dataType: 'json',
			data: {
				idx: idx,
				password: password
			},
			success: function(result) {
				if (result) {
					alert("삭제가 완료되었습니다.");
					location.href="/free/list";
				} else {
					alert("비밀번호가 틀립니다.");
				}
			},
			error: function(request, status, error) {
				alert('에러가 발생했습니다.');
			}
		});		
	}

</script>
</body>
</html>
