<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<% pageContext.setAttribute("LF", "\n"); %>
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<head>
    <meta charset="UTF-8">
    <title>게시판</title>
    
    <link rel="stylesheet" type="text/css" href="<c:url value="/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css" />" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/webjars/bootstrap/3.3.7-1/css/bootstrap-theme.min.css" />" />
    <script src="<c:url value="/webjars/jquery/1.11.1/jquery.min.js" />" ></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js" />" ></script>
    
    <style>
        tr.text-center th {
            text-align: center;
        }

        tr.text-center td {
            text-align: center;
        }
    </style>
    <script type="text/javascript" th:inline="javascript">
        /* <![CDATA[ */
        $(document).ready(function () {
            // ajax 작업시 캐쉬를 사용하지 않도록 한다
            $.ajaxSetup({cache: false, async: false});

            $("#delbtn").click(function () {
                if (confirm("게시물을 삭제하시겠습니까?")) {
                    // deleteAjax();
                    deleteForm();
                }
            });
            
            function deleteAjax() {
            	const idxs = [];
                idxs.push(<c:out value="${board.idx}"/>);
                $.ajax({
                    // data: JSON.stringify(idxs),
                    data: {delchk: idxs},
                    type: "POST",
                    url: "<c:url value="/board/delete.do" />",              // json으로 return 받을때 확장자로 html을 쓰면 안된다. html을 쓰면 return 을 html 문서로 생각하고 그에 맞춰 return 하기 때문에 json으로 받질 못한다
                    dataType: "json",
                    success: function (data, status, xhr) {
                        var result = data.result;
                        if (result == "OK") {
                            alert("글이 삭제되었습니다");
                            location.href = "<c:url value="/board/list.do" />";
                        }
                    },
                    error: function (error) {
                        alert("삭제에 실패했습니다");
                        console.log(error);
                    }
                });
            }

            function deleteForm() {
                $("#contentsForm").prop("action", "/board/delete.do");
                $("#contentsForm")[0].submit();
            }
        });
        /* ]]> */
    </script>
</head>
<body>
<div class="container">
    <h1><span>게시판 조회</span></h1>
    <form id="contentsForm" name="contentsForm" method="post" action="/board/delete.do">
        <table class="table">
            <tr>
                <td class="text-center col-md-2">제목</td>
                <td class="col-md-10"><c:out value="${board.title}"/></td>
            </tr>
            <tr>
                <c:set var="contents" value="${board.contents}" />
                <td class="text-center">내용</td>
                <td>${fn:replace(contents, LF, "<br/>")}</td>
            </tr>
            <tr>
                <td class="text-center">첨부파일</td>
                <td>
                    <c:choose>
                        <c:when test="${!empty board.listUploadFileVO}">
                            <c:forEach var="uploadFile" items="${board.listUploadFileVO}" varStatus="status">
                                <span><c:out value="${uploadFile.orgFileName }" /> (<c:out value="${uploadFile.fileSize}" />)bytes</span>
                                <span class="glyphicon glyphicon-remove"></span>
                                <c:if test="${status.last == false}">
                                    <br/>
                                </c:if>
                            </c:forEach>    
                        </c:when>
                        <c:otherwise>
                            업로드된 파일이 없습니다
                        </c:otherwise>
                    </c:choose>
                    
                </td>
            </tr>
        </table>
        <div class="text-center">
            <a href="update.do?idx=<c:out value="${board.idx}"/>&pageNo=<c:out value="${searchVO.pageNo}"/>&pageSize=<c:out value="${searchVO.pageSize}"/>&searchType=<c:out value="${searchVO.searchType}"/>&searchWord=<c:out value="${searchVO.searchWord}"/>" class="btn btn-default">수정</a>
            <a href="list.do?&pageNo=<c:out value="${searchVO.pageNo}"/>&pageSize=<c:out value="${searchVO.pageSize}"/>&searchType=<c:out value="${searchVO.searchType}"/>&searchWord=<c:out value="${searchVO.searchWord}"/>" class="btn btn-default">목록</a>
            <a href="#" id="delbtn" class="btn btn-default">삭제</a>
        </div>
        <input type="hidden" id="delchk" name="delchk" value="<c:out value="${board.idx}"/>" />
    </form>
</div>
</body>
</html>