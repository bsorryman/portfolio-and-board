<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<link rel="stylesheet" type="text/css" href="../assets/select_and_hover.css">
<script src="../assets/jquery.min.js"></script>
<script src="/assets/jquery.cookie.js"></script>
<script src="../assets/bootstrap.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/clipboard.js/2.0.10/clipboard.min.js"></script>  
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
    margin-top: -73px;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: #495057;
    
  }
  
  #page-content-wrapper {
    width: 100%;
    padding: 25px;
    background-color: #fff; 
  }
  
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
<div class="modal fade" id="exampleModal" tabindex="1" aria-labelledby="exampleModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="exampleModalLabel">안내</h5>
      </div>
      <div class="modal-body">
        <h4>쿠키 사용 동의 안내</h4>
        <p>쿠키 사용에 동의하시면 동의 버튼을 눌러주세요.</p>
      </div>
      <div class="modal-footer">
<!--         <div style="margin-right: 280;"> -->
<!--           <input type="checkbox"> 오늘 그만보기 -->
<!--         </div> -->
        <button id="acceptCookie" type="button" class="btn btn-secondary" data-bs-dismiss="modal">동의</button>
      </div>
    </div>
  </div>
</div>

<div class="navbar navbar-expand-md bd-navbar" style="height: 70px; background-color:#495057">
  <nav class="container-fluid justify-content-end">
    <ul class="nav nav-pills m-1">
      <button id="signUpButtonRedirect" type="button" class="btn btn-primary" style="font-weight: bold; margin-right: 7px;">회원가입</button>
      <button id="logoutButton" type="button" class="btn btn-primary" style="font-weight: bold; margin-right: 7px;">로그아웃</button>
      <button id="loginButton" type="button" class="btn btn-primary" style="font-weight: bold; margin-right: 7px;">로그인</button>    
    </ul>
  </nav>
</div>
<div id="sidebar-wrapper">
  <div class="navbar bd-navbar">
      <ul class="sidebar-nav nav nav-pills m-1">
        <li class="nav-item">
          <a class="nav-link">Home</a>
        </li>         
        <li class="nav-item">
          <a id="jsp-board-menu" class="nav-link" href="/jsp-board/list">JSP & API 게시판</a>
        </li>     
        <li class="nav-item">
          <a id="" class="nav-link" href="/thyme-board/list">Thymeleaf 게시판</a>
        </li>                  
      </ul>
  </div>
</div>
</header>
<script>
    $(function(){
        var curPath = window.location.pathname;
        curPath = curPath.substr(1);
        
        if (curPath.indexOf('jsp-board')!=-1) {
            $('#jsp-board-menu').attr('class', 'nav-link active');
        }
        
        if ($.cookie('userInfo')!=null) {
            $('#logoutButton').show();
            $('#loginButton').hide();
        } else {
            $('#logoutButton').hide();
            $('#loginButton').show();
        }
        
        if ($.cookie('accept_cookie') != 'true') {
            $("#exampleModal").modal("show");
        }
        
        $('#acceptCookie').on("click", function(){
            acceptCookie();
            $("#exampleModal").modal("hide");
        });

        $('#signUpButtonPopup').on("click", function(){
            window.location.href = "/signup/popup";
        });
        
        $('#signUpButtonRedirect').on("click", function(){
            window.location.href = "/signup";
        });    
    })	
    
    //쿠키 팝업 동의 - 쿠키 생성
    function acceptCookie() {
        $.cookie('accept_cookie', 'true', { expires: 3650, path: '/' });
    }       
</script>
  