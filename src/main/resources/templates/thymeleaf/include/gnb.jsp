<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<style>
  /* 사이드바 래퍼 스타일 */
  
  #page-wrapper {
    padding-left: 250px;
  }
  
  #sidebar-wrapper {
    position: fixed;
    width: 250px;
    height: 100%;
    margin-left: -250px;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: #1472ce;
    
  }
  
  #page-content-wrapper {
    width: 100%;
    padding: 25px;
  }
  /* 사이드바 스타일 */
  
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
<div id="sidebar-wrapper">
  <header class="navbar bd-navbar">
      <ul class="sidebar-nav nav nav-pills m-1">
        <li class="nav-item">
          <a id="free-menu" class="nav-link" href="/free/list">JSP & API 게시판</a>
        </li>     
        <li class="nav-item">
          <a id="" class="nav-link" href="/free2/list">자유 게시판2</a>
        </li>                  
        <li class="nav-item">
          <a id="" class="nav-link" href="/free3/list">자유 게시판3</a>
        </li>        
      </ul>
  </header>
</div>
  <link rel="stylesheet" type="text/css" href="../assets/select_and_hover.css">
<script src="../assets/jquery.min.js"></script>
<script src="../assets/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.10/clipboard.min.js"></script>  
<script>
	var curPath = window.location.pathname;
	curPath = curPath.substr(1);
	
	if (curPath.indexOf('free')!=-1) {
		$('#free-menu').attr('class', 'nav-link active');
	}
</script>
  