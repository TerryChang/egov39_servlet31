<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<%@ taglib prefix="javatime" uri="http://sargue.net/jsptags/time" %>
<!DOCTYPE html>
<html lang="ko">
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
    <script type="text/javascript">
        $(document).ready(function () {
        	// ajax 작업시 캐쉬를 사용하지 않도록 한다
            $.ajaxSetup({cache: false, async: false});
        	
            $("#searchBtn").on("click", function () {
            	$("#pageNo").val(1);
                $("#searchForm").submit();
            });

            $("#allchk").on("click", function () {
                $("input[name='delchk']").prop("checked", $("#allchk").is(":checked"));
            });

            $("#btndelete").on("click", function () {
                const size = $("input[name='delchk']:checked").length;
                if (size === 0) {
                    alert("삭제할 글을 선택해주세요");
                } else {
                    // deleteAjax();
                    deleteForm();
                }

            });
            
            function deleteAjax() {
            	const delchk = [];
                $("input[name='delchk']:checked").each(function () {
                    delchk.push($(this).val());
                });
                $.ajax({
                    url: "<c:url value="/board/delete.do" />",
                    type: "POST",
                    // data : JSON.stringify({"idx" : idx}),
                    data: {delchk: delchk},
                    // contentType: "application/json", // contentType으로 지정하면 Request Body로 전달되기 때문에 Spring에서 받을때 다르게 접근해야 한다
                    dataType: "json",
                    success: function (data, textStatus, jqXHR) {
                        alert("삭제되었습니다");
                        location.href = "<c:url value="/board/list.do" />";
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("삭제에 실패했습니다");
                        let responseJSON = jqXHR.responseJSON;
                        let message = responseJSON.message;
                        console.log(message);
                    }
                });
            }

            function deleteForm() {
                $("#contentsForm").prop("action", "/board/delete.do");
                $("#contentsForm")[0].submit();
            }
        });
        
        function go_page(pageno) {
        	// alert(pageno);
        	$("#pagingPageNo").val(pageno);
        	$("#pagingForm")[0].submit();
        }
    </script>

</head>
<body>
<div class="container">
    <h1>게시판</h1>
    <%-- Spring 4.3부터는 기존의 commandName 속성은 deprecated 되어서 modelAttribute 속성으로 바꿔서 사용한다 --%>
    <form:form id="searchForm" name="searchForm" method="get" modelAttribute="searchVO">
        <div class="row align-items-center justify-content-center">
            <div class="col-md-2">
                <div class="form-group ">
                    <form:select id="searchType" name="searchType" cssClass="form-control" path="searchType">
                        <form:option value="">선택해주세요</form:option>
                        <form:option value="title">제목</form:option>
                        <form:option value="contents">내용</form:option>
                    </form:select>
                </div>
            </div>
            <div class="col-md-8">
                <div class="form-group ">
                    <form:input cssClass="form-control" name="searchWord" path="searchWord" placeholder="검색어를 입력해주세요" />
                </div>
            </div>
            <div class="col-md-2">
                <button type="button" id="searchBtn" class="btn btn-primary btn-block">검색</button>
            </div>
        </div>
        <form:hidden id="pageNo" name="pageNo" path="pageNo"/>
        <form:hidden id="pageSize" name="pageSize" path="pageSize"/>
    </form:form>
    <form id="contentsForm" name="contentsForm" method="post" action="/board/delete.do">
    <table class="table table-hover">
        <thead>
        <tr class="text-center">
            <th class="col-md-1"><input type="checkbox" id="allchk" value="Y"></th>
            <th class="col-md-1">번호</th>
            <th class="col-md-8">제목</th>
            <th class="col-md-2">등록일자</th>
        </tr>
        </thead>
        <c:choose>
            <c:when test="${paginationInfo.totalRecordCount == 0}">
                <td class="text-center" colspan="4"><spring:message code="common.nodata.msg" /></td>
            </c:when>
            <c:otherwise>
                <c:set var="idx" value="${paginationInfo.totalRecordCount - ((paginationInfo.currentPageNo - 1) * paginationInfo.recordCountPerPage)}"/>
                <c:forEach var="board" items="${resultList}" varStatus="status">
                    <tr>
                        <td class="text-center"><input type="checkbox" name="delchk" value="<c:out value="${board.idx}" />"></td>
                        <td class="text-center">${idx-status.index}</td>
                        <td>
                            <c:url value="/board/view.do" var="url">
                                <c:param name="idx" value="${board.idx}" />
                                <c:param name="pageNo" value="${paginationInfo.currentPageNo}" />
                                <c:param name="pageSize" value="${paginationInfo.recordCountPerPage}" />
                                <c:param name="searchType" value="${searchVO.searchType}" />
                                <c:param name="searchWord" value="${searchVO.searchWord}" />
                            </c:url>
                            <a href="${url}"><c:out value="${board.title}" /></a>
                        </td>
                        <td class="text-center"><javatime:format value="${board.regdate}" pattern="yyyy-MM-dd" /></td>
                    </tr>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </table>
    </form>
    <a id="btndelete" href="#" class="btn btn-default pull-right">삭제</a>
    <a id="btnwrite" href="<c:url value="/board/insert.do" />" class="btn btn-default pull-right">글쓰기</a>
    <c:if test="${paginationInfo.totalRecordCount > 0}">
        <div class="text-center">
            <ui:pagination paginationInfo="${paginationInfo}" type="image" jsFunction="go_page" />    
        </div>
    </c:if>
</div>
<%-- paging 태그에서 페이지 번호를 클릭했을때 자바스크립트 함수로 실행하게될 form을 작성한다(읽기전용 form임) --%>
<form:form id="pagingForm" name="pagingForm" method="get" modelAttribute="searchVO">
    <form:hidden id="pagingSearchType" name="searchType" path="searchType"/>
    <form:hidden id="pagingSearchWord" name="searchWord" path="searchWord"/>
    <form:hidden id="pagingPageNo" name="pageNo" path="pageNo"/>
    <form:hidden id="pagingPageSize" name="pageSize" path="pageSize"/>
</form:form>
</body>
</html>