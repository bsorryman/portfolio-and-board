<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
  /* 사이드바 래퍼 스타일 */
  
  #page-wrapper {
    padding-left: 250px;
    background-color: #495057;
  }
  
  #sidebar-wrapper {
    position: fixed;
    width: 250px;
    height: 100%;
    margin-left: -250px;
    margin-top: -73px;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: #495057;
    
  }
  
  #page-content-wrapper {
    width: 100%;
    padding: 25px;
    background-color: #fff; 
    border-radius: 30px 0px 0px 0px;    
  }
<<<<<<< HEAD:src/main/resources/templates/thymeleaf/include/gnb.html
=======
  /* 사이드바 스타일 */
>>>>>>> parent of 4af3fd5 (thymeleaf로 구성한 게시판 적용):src/main/resources/templates/thymeleaf/include/gnb.jsp
  
  .sidebar-nav {
    width: 250px;
    margin: 0;
    padding: 0;
    list-style: none;
  }
  
  .sidebar-nav li {
    text-indent: 1.5em;
    line-height: 2.8em;
  }
  
  .sidebar-nav li a {
    display: block;
    text-decoration: none;
  }
  
  .sidebar-nav > .sidebar-brand {
    font-size: 1.3em;
    line-height: 3em;
  }
</style>
<header>
<div class="navbar navbar-expand-md bd-navbar" style="height: 70px; background-color:#495057">
  <nav class="container-fluid justify-content-end">
    <ul class="nav nav-pills m-1">
      <button id="signUpButton" type="button" class="btn btn-primary" style="font-weight: bold; margin-right: 7px;">회원가입</button>
      <button id="logoutButton" type="button" class="btn btn-primary" onclick="logout();" style="font-weight: bold; margin-right: 7px;">로그아웃</button>
      <button id="loginButton" type="button" class="btn btn-primary" onclick="javascript:location.href='/login';" style="font-weight: bold; margin-right: 7px; display: none;">로그인</button>    
    </ul>
  </nav>
</div>
<div id="sidebar-wrapper">
<<<<<<< HEAD:src/main/resources/templates/thymeleaf/include/gnb.html
  <div class="navbar bd-navbar" >
      <ul class="sidebar-nav nav nav-pills m-1">
        <li class="nav-item">
          <a class="nav-link">Home</a>
        </li>      
        <li class="nav-item">
          <a id="jsp-board" class="nav-link" href="/jsp-board/list">JSP & API 게시판</a>
=======
  <header class="navbar bd-navbar">
      <ul class="sidebar-nav nav nav-pills m-1">
        <li class="nav-item">
          <a id="free-menu" class="nav-link" href="/free/list">JSP & API 게시판</a>
>>>>>>> parent of 4af3fd5 (thymeleaf로 구성한 게시판 적용):src/main/resources/templates/thymeleaf/include/gnb.jsp
        </li>     
        <li class="nav-item">
          <a id="" class="nav-link" href="/free2/list">자유 게시판2</a>
        </li>                  
        <li class="nav-item">
          <a id="" class="nav-link" href="/free3/list">자유 게시판3</a>
        </li>        
      </ul>
  </div>
</div>
<<<<<<< HEAD:src/main/resources/templates/thymeleaf/include/gnb.html
</header>
<!-- fragment parameter value-->
<input type="hidden" id="curBoard" th:value="${curBoard}" />

<script>
	$(function(){
	    $('#signUpButton').on("click", function(){
	        window.location.href = "/signup";
	    })
	})
	//frament parameter value
	var curBoard = $('#curBoard').val();
=======
  <link rel="stylesheet" type="text/css" href="../assets/select_and_hover.css">
<script src="../assets/jquery.min.js"></script>
<script src="../assets/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.10/clipboard.min.js"></script>  
<script>
	var curPath = window.location.pathname;
	curPath = curPath.substr(1);
>>>>>>> parent of 4af3fd5 (thymeleaf로 구성한 게시판 적용):src/main/resources/templates/thymeleaf/include/gnb.jsp
	
	if (curPath.indexOf('free')!=-1) {
		$('#free-menu').attr('class', 'nav-link active');
	}
</script>
  