package egovframework.mybatis.config.web.paging;

import java.text.MessageFormat;

import javax.servlet.ServletContext;

import org.springframework.web.context.ServletContextAware;

import egovframework.rte.ptl.mvc.tags.ui.pagination.AbstractPaginationRenderer;
import egovframework.rte.ptl.mvc.tags.ui.pagination.PaginationInfo;
import lombok.NoArgsConstructor;

/**
 * MybatisImagePaginationRenderer 클래스
 * 
 * 전자정부프레임워크에서 페이징 UI를 그리는 방법을 분석하기 위해
 * 전자정부프레임워크 simple homepage 템플릿에 있는 egovframework.cmm.ImagePaginationRenderer 클래스를 기반으로 제작
 * 
 * @author Terry Chang
 *
 */
@NoArgsConstructor
public class MybatisImagePaginationRenderer extends AbstractPaginationRenderer implements ServletContextAware {

	private ServletContext servletContext;

	private void initVariables() {
		
        // diableClass, javascript function, pageno
        firstPageLabel = "<li class={0}><a href=\"#\" onclick=\"{1}({2})\"><span class=\"glyphicon glyphicon-fast-backward\"></span></a></li>";
        previousPageLabel = "<li class={0}><a href=\"#\" onclick=\"{1}({2})\"><span class=\"glyphicon glyphicon-backward\"></span></a></li>";
        currentPageLabel = "<li class=\"active\"><span>{0}</span></li>";
        otherPageLabel = "<li><a href=\"#\" onclick=\"{0}({1})\">{1}</a></li>";
        nextPageLabel = "<li class={0}><a href=\"#\" onclick=\"{1}({2})\"><span class=\"glyphicon glyphicon-forward\"></span></a></li>";
        lastPageLabel = "<li class={0}><a href=\"#\" onclick=\"{1}({2})\"><span class=\"glyphicon glyphicon-fast-forward\"></span></a></li>";
	}
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		// TODO Auto-generated method stub
		this.servletContext = servletContext;
		initVariables();

	}

	@Override
	public String renderPagination(PaginationInfo paginationInfo, String jsFunction) {
		// TODO Auto-generated method stub
		// return super.renderPagination(paginationInfo, jsFunction);
		StringBuffer strBuff = new StringBuffer();
		
		int firstPageNo = paginationInfo.getFirstPageNo();
		int firstPageNoOnPageList = paginationInfo.getFirstPageNoOnPageList(); // 페이지 목록에서 보여지는 페이지 중 첫번째 페이지 번호
		int totalPageCount = paginationInfo.getTotalPageCount();
		int pageSize = paginationInfo.getPageSize();	// 페이징 목록에서 보여줘야 할 페이지 갯수
		int lastPageNoOnPageList = paginationInfo.getLastPageNoOnPageList(); // 페이지 목록에서 보여지는 페이지 중 마지막 페이지 번호
		int currentPageNo = paginationInfo.getCurrentPageNo();
		int lastPageNo = paginationInfo.getLastPageNo();		// 전체 페이지 중 마지막 페이지 번호( == 총 페이지 갯수(totalPageCount))
		
		strBuff.append("<nav aria-label=\"Page Navigation\">\n");
		strBuff.append("<ul class=\"pagination\">\n");
		
		if(currentPageNo == firstPageNo) {
			strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { "disabled", jsFunction, Integer.toString(firstPageNo) }));
		} else {
			strBuff.append(MessageFormat.format(firstPageLabel, new Object[] { "", jsFunction, Integer.toString(firstPageNo) }));
		}
		
		if(currentPageNo == firstPageNo) {
			strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { "disabled", jsFunction, Integer.toString(currentPageNo) }));
		} else {
			strBuff.append(MessageFormat.format(previousPageLabel, new Object[] { "", jsFunction, Integer.toString(currentPageNo - 1) }));
		}
		
		for (int i = firstPageNoOnPageList; i <= lastPageNoOnPageList; i++) {
			if (i == currentPageNo) {
				strBuff.append(MessageFormat.format(currentPageLabel, new Object[] { Integer.toString(i) }));
			} else {
				strBuff.append(MessageFormat.format(otherPageLabel, new Object[] { jsFunction, Integer.toString(i) }));
			}
		}
		
		if(currentPageNo == lastPageNo) {
			strBuff.append(MessageFormat.format(nextPageLabel, new Object[] { "disabled", jsFunction, Integer.toString(currentPageNo) }));
		} else {
			strBuff.append(MessageFormat.format(nextPageLabel, new Object[] { "", jsFunction, Integer.toString(currentPageNo + 1) }));
		}
		
		if(currentPageNo == lastPageNo) {
			strBuff.append(MessageFormat.format(lastPageLabel, new Object[] { "disabled", jsFunction, Integer.toString(lastPageNo) }));
		} else {
			strBuff.append(MessageFormat.format(lastPageLabel, new Object[] { "", jsFunction, Integer.toString(lastPageNo) }));
		}
		
		
		strBuff.append("</ul>\n");
		strBuff.append("</nav>\n");
		
		return strBuff.toString();
	}
	
/*
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
 */
	
	
	

}
