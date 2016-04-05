package io.uve.mybatis.output;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

public class ReportTargetDAO {
	
	private SqlSessionFactory sqlSessionFactory = null;
	
	public ReportTargetDAO(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
	}

	public void replace(ReportTarget reportTarget) {

		SqlSession session = sqlSessionFactory.openSession();

		try {
			session.insert("FeedReport.replace", reportTarget);
		} finally {
			session.commit();
			session.close();
		}
	}
}
