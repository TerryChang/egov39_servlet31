package egovframework.mybatis.service;

public interface BoardServiceWithoutFile<T, S, C, K> extends BoardService<T, S, C, K> {
	public void insert(T t) throws Exception;
	public void update(T t) throws Exception;
}
