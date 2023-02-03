<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.2/dist/js/bootstrap.bundle.min.js"></script>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.1/font/bootstrap-icons.css">
    <script src="../resources/jquery/jquery-3.6.3.min.js"></script>
<title>Insert title here</title>
</head>
<body>
<div class="container">
	<h2>트랜잭션(Transaction)</h2>
	
	<h3>티켓 구매 결과 보기</h3>
	
	<ul>
		<li>고객아이디 : ${ticketInfo.customerId }</li>
		<li>티켓구매수 : ${ticketInfo.amount }</li>
	</ul>
	<p> 
		<a href="./buyTicketMain.do">
			티켓구매페이지 바로가기
		</a>
	</p>
</div>
</body>
</html>