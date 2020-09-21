<%@ page contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="ui" uri="http://egovframework.gov/ctl/ui"%>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>게시판</title>
    <style>
        tr.text-center th {
            text-align: center;
        }

        tr.text-center td {
            text-align: center;
        }
        
        .table.no-border tr td, .table.no-border tr th {
            border-width: 0;
        }
    </style>
    <link rel="stylesheet" type="text/css" href="<c:url value="/webjars/bootstrap/3.3.7-1/css/bootstrap.min.css" />" />
    <link rel="stylesheet" type="text/css" href="<c:url value="/webjars/bootstrap/3.3.7-1/css/bootstrap-theme.min.css" />" />
    <link rel="stylesheet" type="text/css" href="/css/progressbar.css" />
    <script src="<c:url value="/webjars/jquery/1.11.1/jquery.min.js" />" ></script>
    <script src="<c:url value="/webjars/bootstrap/3.3.7-1/js/bootstrap.min.js" />" ></script>
    <script type="text/javascript">
        /* <![CDATA[ */
        $(document).ready(function () {
            // ajax 작업시 캐쉬를 사용하지 않도록 한다
            $.ajaxSetup({cache: false});

            $("#regbtn").click(function () {
            	if ($("#title").val() == "") {
                    alert("제목을 작성해주세요");
                    $("#title").focus();
                } else if ($("#contents").val() == "") {
                    alert("내용을 작성해주세요");
                    $("#contents").focus();
                } else {
                    // $("#form")[0].submit();
                    regForm();
                }
            });
            
            $("#addFile").on("click", function() {
                // 태그 이름이 uploadFile인 것이 없을경우(등록하고자 하는 파일이 없습니다 란 문구를 보여주는 경우) 안내문구 행을 지운다(즉 테이블에 행이 없게 된다) 
                if($("input[name='uploadFile']").length == 0) {
                    $("#fileTable tbody tr").remove();
                }
                let trHtml = "";
                trHtml += "<tr>\n";
                trHtml += " <td class=\"col-md-9\">\n";
                trHtml += "     <input type=\"file\" name=\"uploadFile\">\n";
                trHtml += " </td>\n";
                trHtml += " <td class=\"col-md-1\">\n";
                trHtml += "    <button type=\"button\" class=\"glyphicon glyphicon-remove removeFileBtn\"></button>\n";
                trHtml += " </td>\n";
                trHtml += "</tr>\n";
                
                // 파일 태그가 있는 행을 추가한다
                $("#fileTable tbody").append(trHtml);
            });
            
            // 기존의 live 메소드로 동적 요소에 이벤트거는 것을 대체하는 방법
            $("#fileTable").on("click", ".removeFileBtn", function() {
                $(this).parent().parent().remove();
                // 태그 이름이 uploadFile인 것이 없을경우(행이 아예 없는 경우) 기존의 안내 문구가 들어간 행을 넣어준다
                if($("input[name='uploadFile']").length == 0){
                    let trHtml = "<tr>\n";
                    trHtml += "<td class=\"col-md-10\">\n";
                    trHtml += "    등록하고자 하는 파일이 없습니다\n";
                    trHtml += "</td>\n";
                    trHtml += "</tr>\n";
                    $("#fileTable tbody").append(trHtml);
                }
            });

            $("#resetbtn").click(function () {
                $("#form")[0].reset();
            });

            $("#listbtn").click(function () {
                location.href="list.do?&pageNo=<c:out value="${searchVO.pageNo}"/>&pageSize=<c:out value="${searchVO.pageSize}"/>&searchType=<c:out value="${searchVO.searchType}"/>&searchWord=<c:out value="${searchVO.searchWord}"/>";
            });
            
            // 첨부파일 삭제 아이콘에 마우스를 올렸을때 손모양 아이콘으로 바꾸기
            $("span.glyphicon-remove").on("mouseover", function(){
            	$(this).css("cursor", "pointer");
            });
            
            // 첨부파일 삭제 아이콘에서 마우스가 벗어났을때 기본 마우스 아이콘으로 바꾸기 
            $("span.glyphicon-remove").on("mouseout", function(){
                $(this).css("cursor", "default");
            });
            
            // 첨부파일 삭제 아이콘 클릭
            $("span.glyphicon-remove").on("click", function(){
                let idx = $(this).data("idx");
                let orgfilename = $(this).data("orgfilename");
                
                if(confirm("클릭하신 " + orgfilename + "을 삭제하시겠습니까?")) {
                	deleteFile(this, idx, orgfilename);
                }
            });
            
            
        });
        
        // 업로드 파일 존재 여부를 체크하는 함수(파일 태그만 추가하고 파일 설정을 하지 않을 경우도 있기 때문에 실제 파일이 설정되어 있는지도 같이 체크해야 한다)
        function existUploadFile(fileTagName) {
            let result = false;
            let fileExistCount = 0;
            if($("input[name='" + fileTagName + "']").length != 0) {
                $("input[name='" + fileTagName + "']").each(function(index, item) {
                    if($(item).val() != "") {
                        fileExistCount++;
                    }
                });
            }
            if(fileExistCount > 0) {
                result = true;
            }
            return result;
        }
     
        function regForm(){
            
            let formData = new FormData($("#form")[0]);
            
            let bar = $(".bar");
            let percent = $(".percent");
            let status = $("#status");
            let existFile = existUploadFile("uploadFile");
            
            $.ajax({
                xhr : function() {
                    let xhr = $.ajaxSettings.xhr();
                    if(existFile) {
                        xhr.upload.onprogress = function(e) {
                            if(e.lengthComputable) {
                                let percentComplete = Math.floor((e.loaded / e.total) * 100);
                                
                                let percentVal = percentComplete + "%";
                                bar.width(percentVal);
                                percent.html(percentVal);
                            }
                        }
                    }
                    return xhr;
                },
                type: "POST",
                encType: "multipart/form-data",
                url: "/board/update.do",
                dataType: "json",
                data: formData,
                processData: false,
                contentType: false,
                beforeSend: function(xhr) {
                    if(existFile) {
                        // progress Modal 열기
                        $("#pleaseWaitDialog").modal("show");
                        
                        status.empty();
                        let percentVal = "0%";
                        bar.width(percentVal);
                        percent.html(percentVal);
                    }
                },
                success: function(result) {
                    alert("등록되었습니다");
                    location.href="list.do";
                },
                error: function(xhr, ajaxSettings, throwError) {
                    alert("등록에 실패했습니다");
                },
                complete: function() {
                    $("#pleaseWaitDialog").modal("hide");
                }
            });
        }
        
        function deleteFile(delFileIcon, idx, orgFileName){
            // let formData = new FormData();
            // formData.append("idx", idx);
            
            let formData = {"idx" : idx};
            
            $.ajax({
                type: "POST",
                url: "/board/deleteFile.do",
                dataType: "json",
                data: formData,
                success: function(result) {
                    alert(orgFileName + " 파일이 삭제되었습니다");
                    $(delFileIcon).parent().remove();
                    
                    if($("#currentFileTd div").length == 0) {
                    	$("#currentFileTd").html("업로드된 파일이 없습니다.")
                    }
                },
                error: function(xhr, ajaxSettings, throwError) {
                	alert(orgFileName + " 파일 삭제에 실패했습니다");
                }
            });
        }
        /* ]]> */
    </script>
</head>
<body>
<div class="container">
    <h1><span>게시판 수정</span></h1>
    
    <form id="form" method="post" action="/board/update.do">
        <table class="table table-bordered" style="table-layout: fixed">
            <tr>
                <td class="text-center col-md-2">제목</td>
                <td class="col-md-10">
                    <input type="text" id="title" name="title" class="form-control" placeholder="제목을 작성해주세요" value="<c:out value="${board.title}"/>"/>
                </td>
            </tr>
            <tr>
                <td class="text-center">내용</td>
                <td>
                    <textarea class="form-control" id="contents" name="contents" rows="20" cols="80" placeholder="내용을 작성해주세요"><c:out value="${board.contents}"/></textarea>
                </td>
            </tr>
            <tr>
                <td class="text-center" rowspan="2">
                첨부파일
                <button type="button" id="addFile" class="btn btn-default">추가</button>
                </td>
                <td id="currentFileTd">
                    <c:choose>
                        <c:when test="${!empty board.listUploadFileVO}">
                            <c:forEach var="uploadFile" items="${board.listUploadFileVO}" varStatus="status">
                                <div>
                                    <span><c:out value="${uploadFile.orgFileName }" /> (<c:out value="${uploadFile.fileSize}" />)bytes</span>
                                    <span class="glyphicon glyphicon-remove" data-idx="<c:out value="${uploadFile.idx}"/>" data-orgfilename="<c:out value="${uploadFile.orgFileName}"/>"></span>
                                </div>
                            </c:forEach>    
                        </c:when>
                        <c:otherwise>
                            업로드된 파일이 없습니다
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
            <tr>
                <td>
                    <table id="fileTable" class="table no-border">
                        <tbody>
                        <tr>
                            <td class="col-md-10">
                                등록하고자 하는 파일이 없습니다
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </td>
            </tr>
        </table>
        <input type="hidden" id="idx" name="idx" value="<c:out value="${board.idx}"/>" />
        <input type="hidden" id="pageNo" name="pageNo" value="<c:out value="${searchVO.pageNo}"/>" />
        <input type="hidden" id="pageSize" name="pageSize" value="<c:out value="${searchVO.pageSize}"/>" />
        <input type="hidden" id="searchType" name="searchType" value="<c:out value="${searchVO.searchType}"/>" />
        <input type="hidden" id="searchWord" name="searchWord" value="<c:out value="${searchVO.searchWord}"/>" />
    </form>
    <div class="text-center">
        <a id="regbtn" class="btn btn-default">수정</a>
        <a id="resetbtn" class="btn btn-default">재작성</a>
        <a id="listbtn" class="btn btn-default">목록</a>
    </div>
    <!-- Progress Bar Div (https://deonggi.tistory.com/57 참고)-->
    <div class="modal fade" id="pleaseWaitDialog" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" data-backdrop="static">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h3>업로드 중입니다..</h3>
                </div>
                <div class="modal-body">
                    <!-- progress, bar, percent를 표시할 div 생성 -->
                    <div class="bar"></div>
                    <div class="percent">0%</div>
                </div>
                <div id="status"></div>
            </div>
        </div>
    </div>
</div>
</body>
</html>