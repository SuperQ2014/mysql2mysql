package io.uve.mybatis.input;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class RequestsTargetDAO {
	
	private SqlSessionFactory sqlSessionFactory = null;
	
	public RequestsTargetDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public Integer select_pv(RequestsTarget requestsTarget) {

		SqlSession session = sqlSessionFactory.openSession();
		Integer requests = 0;

		try {
			 requests = session.selectOne("FeedReport.select_pv", requestsTarget);
		} finally {
			session.commit();
			session.close();
		}
		return requests;
	}
}
