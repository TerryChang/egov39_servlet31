<!-- 페이징과 관련된 include 대상들이 모인 파일 -->
<!DOCTYPE html>
<html lang="ko" xmlns:th="http://www.thymeleaf.org"
      xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity4">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<th:block th:fragment="pagination(page, pageSize, pageCnt, pageBlockSize, url)">
    <!--
    page : [(${page})]
    pageSize : [(${pageSize})]
    pageCnt : [(${pageCnt})]
    pageBlockSize : [(${pageBlockSize})]
    url : [(${url})]
    -->
    <nav aria-label="Page Navigation" th:with="pageStartNumber=(${T(Math).floor(page / pageSize)}*${pageSize})+1
                   , pageEndNumber=${pageStartNumber}+${pageBlockSize}-1 > ${pageCnt} ? ${pageCnt} : ${pageStartNumber}+${pageBlockSize}-1">
        <ul class="pagination">
            <li th:class="${page == 1} ? 'disabled' : ''">
                <a th:queryStringLink="@{${url}(pageNo=1,pageSize=${pageSize})}">
                    <span class="glyphicon glyphicon-fast-backward"></span>
                </a>
            </li>
            <li th:class="${page == 1} ? disabled : ''">
                <a th:queryStringLink="@{${url}(pageNo=${page == 1} ? 1 : ${page}-1,pageSize=${pageSize})}">
                    <span class="glyphicon glyphicon-backward"></span>
                </a>
            </li>
            <th:block th:each="i : ${#numbers.sequence(pageStartNumber, pageEndNumber)}">
                <li th:class="${page == i} ? 'active'">
                    <th:block th:if="${page == i}">
                        <span th:text="${i}">1</span>
                    </th:block>
                    <th:block th:unless="${page == i}">
                        <a th:queryStringLink="@{${url}(pageNo=${i},pageSize=${pageSize})}" th:text="${i}">1</a>
                    </th:block>
                </li>

            </th:block>
            <li th:class="${page == pageCnt} ? disabled : ''">
                <a th:queryStringLink="@{${url}(pageNo=${page == pageCnt} ? ${pageCnt} : ${page}+1,pageSize=${pageSize})}">
                    <span class="glyphicon glyphicon-forward"></span>
                </a>
            </li>
            <li th:class="${page == pageCnt} ? disabled : ''">
                <a th:queryStringLink="@{${url}(pageNo=${pageCnt},pageSize=${pageSize})}">
                    <span class="glyphicon glyphicon-fast-forward"></span>
                </a>
            </li>
        </ul>
    </nav>
</th:block>
</body>
</html>