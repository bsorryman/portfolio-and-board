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

    <h1 class="h3">JSP & API 게시판</h1>
    <br /><br />
    <div class="float-end mb-2">
      <button type="button" class="btn btn-sm btn-outline-primary" onclick="javascript:location.href='/jsp-board/write'">
      	글쓰기
      </button>
    </div>
    
    <!-- 날짜 필터링 -->
    <!-- 
    <form action="/order-list" method="get">
    <div class="mb-1 row">
        <div class="col-sm-4" style="width:250px;">
          <input type="date" id="startDate" name="startDate" class="form-control" value="<c:if test="${startDate ne '1970-01-01'}">${startDate}</c:if>">
        </div>
        ~
        <div class="col-sm-4" style="width:250px">
          <input type="date" id="endDate" name="endDate" class="form-control" value="<c:if test="${endDate ne '2038-01-19'}">${endDate}</c:if>">
        </div>
        <input type="hidden" name="service" value="${param.service}" />
        <input type="hidden" name="keyword" value="${param.keyword}" />
        <input type="hidden" name="status" value="${param.status}" />
        <div class="col-sm-4" style="width:120px">        
          <input type="submit" class="form-control" value="기간설정" style="background-color: #f5f5f5">
        </div>
    </div>    
    </form>
    -->
    
    <!-- 검색 Form -->
    <!-- 
	<div class="float-end mb-2 tooltip" style="float:right;">
	<span class="tooltiptext tooltip-left" style="font-size: small">검색 가능 키워드: E-mail, Invoice.no, ChemInfo-id, mol-id, 거래 ID, 상품 타입,<br/>상품 코드, 물성 이름, 비고 </span>
	  <form action="/order-list" method="get" >
	    <input class="form-control ipt_search" type="search" placeholder="Search" aria-label="Search" name="keyword" id="searchKeyword" value="${param.keyword}">
	    <input type=hidden value="${param.service}" name="service"/>
	    <input type=hidden value="${param.status}" name="satus" />
	    <input type=hidden value="${startDate}" name="startDate"/>
	    <input type=hidden value="${endDate}" name="endDate"/>
	    <button class="btn btn-search" type="submit"><svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" role="img" viewBox="0 0 24 24" focusable="false"><title>Search</title><circle cx="10.5" cy="10.5" r="7.5"></circle><path d="M21 21l-5.2-5.2"></path></svg></button>
	  </form>
	</div>
    -->
    <!-- 
    <div>
        <h5 style="float:left;">언어  &nbsp;</h5>
        <select onChange="selectLang(this.value)" class="form-select" style="width:200px; float:left;">
            <option value="" <c:if test="${lang==''}">selected="selected"</c:if>>전체</option>
            <option value="en" <c:if test="${lang=='en' }">selected="selected"</c:if>>영문</option>
            <option value="ko" <c:if test="${lang=='ko'}">selected="selected"</c:if>>국문</option>
        </select>
    </div>    
    <br />
    <br />
    -->
    <table class="table table-bordered table-hover align-middle" id="jspBoardTable" style="display: none">
      <colgroup>
        <col width="3%"><!-- NO. -->
        <col width="10%"><!-- 작성자 -->
        <col><!-- 제목  -->
        <col width="15%"><!-- 날짜 -->
        <col width="6%"><!-- 조회수 -->
      </colgroup>
      <thead>
        <tr>
          <th scope="col">NO.</th><!-- product - orderNo -->
          <th scope="col">작성자</th>
          <th scope="col">제목</th>
          <th scope="col" id="created_at" style="cursor:pointer">날짜</th> <!-- Order no(invoice, 메일 등에서 쓰이는 변형된 orderNo) -->
          <th scope="col">조회수</th>
          <!-- 필드 필터링 -->
          <!--
          <th scope="col">
          <select onChange="selectService(this.value)" style="width:60px; float:left; font-size: 13px; font-weight: bold">
            <option value="" <c:if test="${param.service==null}">selected="selected"</c:if> disabled hidden>&nbsp;서비스</option>
            <option value="" <c:if test="${param.service==''}">selected="selected"</c:if>>All</option>
            <option value="www.mol" <c:if test="${param.service=='www.mol'}">selected="selected"</c:if>>www.mol (SEO)</option>
            <option value="search.mol" <c:if test="${param.service=='search.mol'}">selected="selected"</c:if>>search.mol</option>
            <option value="chemRTP" <c:if test="${param.service=='chemRTP'}">selected="selected"</c:if>>chemRTP</option>
          </select>
          </th>
          -->          
        </tr>
      </thead>
      <tbody>
      
      </tbody>      
    </table>
    <!-- pagination start //-->
    <div id="pagingSection" class="row mt-4" aria-label="Page navigation example">
    
    </div>
    <!-- //pagination End -->       
  </div>
  <!-- //contents -->
</div>
  <script>
  $(function(){
	  //파라미터로 api 함수 실행
	  getJspBoardList('${page}', '${size}', '${bsize}', '${field}', '${keyword}');
	  
	  /*
      if ('${orderType}' == 'DESC') {
          $('#created_at').append('▲');
      } else {
          $('#created_at').append('▼');
      } 
	  */
  });
  
  /*
  function orderByDate() {
      
      if ('${orderType}'=='DESC') {
          orderType = "ASC";
      } else {
          orderType = "DESC";
      }
      
      location.href="/newsletter-list?lang=${lang}&orderType="+orderType;
  }
  
  function selectLang(lang) {
      location.href="/newsletter-list?lang="+lang+"&orderType=${orderType}";
  }  
  */
  
  function getJspBoardList(page, size, bsize, field, keyword) {
	  $.ajax({
		  url: '/api/v1.0/jsp-board/list',
		  type: 'GET',
		  dataType: 'json',
		  data: {
			  page: page,
			  size: size,
			  bszie: bsize,
			  field: field,
			  keyword: keyword
		  },
		  success: function(result) {
			  displayJspBoardBoard(result);
		  },
		  error: function(request, status, error) {
			  alert('목록을 불러오는데 실패했습니다.');
		  }
	  })
  }
  
  function displayJspBoardBoard(json) {
	  var jspBoardPostList = json.jspBoardPostList;
	  var pager = json.pager;
	  $('#jspBoardTable').show();
	  
	  var tbodyContents;
	  
	  jspBoardPostList.forEach((jspBoardPost) => {
		  tbodyContents += '<tr>'+
			  '<td style="text-align: center">'+jspBoardPost.idx+'</td>'+
	          '<td style="text-align: center">'+jspBoardPost.writer+'</td>'+
	          '<td onclick="viewDetail('+jspBoardPost.idx+')" style="cursor:pointer">'+jspBoardPost.title+'</a></td>'+
	          '<td style="text-align: center">'+jspBoardPost.createdAt+'</td>'+
	          '<td style="text-align: right">'+jspBoardPost.hits+'</td>'+
          '</tr>';
	  });

	  $('#jspBoardTable>tbody').append(tbodyContents);
	  
	  displayJspBoardPage(pager);
  }
  
  function viewDetail(idx) {
	  location.href="/jsp-board?idx="+idx;
  }
  
  function displayJspBoardPage(pager) {
	  console.log(pager);
	  //page parameter default set
	  var target = "list";
	  var home = "page=1";
	  var prev = "page="+(pager.bsPage-1);
	  var next = "page="+(pager.bePage+1);
	  var last = "page="+(pager.totalPage)
// 	  var sizePath = "size="+pager.size+"&bsize="+pager.bsize;
	  
	  //make pagination section
	  var pagination = '<div class="col-8">'
	  +'<ul class="pagination justify-content-center">';
	  
	  if (pager.rows==0) {
		  pagination += '<li class="active page-item"><a class="page-link" href="#">1</a></li>';
	  
	  } else if (pager.rows > 0) {
		  if (pager.bsPage > pager.bsize) {
			  pagination += '<li class=""><a class="page-link" href="'+target+'?'+home+'">First</a></li>'+
                  '<li class="page-item"><a class="page-link" href="'+target+'?'+prev+'">&laquo;</a></li>'
		  }
		  
		  //page number
		  for (var i = pager.bsPage; i <= pager.bePage; i++ ) {
			  var page = 'page='+i;
			  //active page
			  if (i == pager.page) {
				  pagination += '<li class="page-item active"><a class="page-link" href="#">'+i+'</a></li>';
			  } else {
				  if (i <= pager.totalPage) {
					  pagination += '<li class="page-item"><a class="page-link" href="'+target+'?'+page+'">'+i+'</a></li>';
				  }
			  }
		  }
		 	
		  if (pager.bePage < pager.totalPage) {
			  pagination += '<li class="page-item"><a class="page-link" href="'+target+'?'+next+'">&raquo;</a></li>'+
              '<li class="page-item"><a class="page-link" href="'+target+'?'+last+'">Last</a></li>'
		  }
	  }
	  
	  pagination += "</ul>" +
		  		"</div>" +
	  		"</div>";
	  		
	  $('#pagingSection').append(pagination);
  }
  
  </script>
</body>
</html>
