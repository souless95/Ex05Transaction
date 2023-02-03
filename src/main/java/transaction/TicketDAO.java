package transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

public class TicketDAO {
	
	/*
	멤버변수선언 : Spring-JDBC를 사용하기 위한 멤버변수와 setter를 정의한다.
		또한 트랜잭션 처리를 위한 매니저 클래스의 멤버변수와 setter를 정의한다.
	*/
	JdbcTemplate template;
	PlatformTransactionManager transactionManager;
	/*
	servlet-context.xml에서 빈 생성시 아래의 setter를 통해
	멤버변수를 초기화한다. 멤버변수를 지정시 <property> 엘리먼트를 사용한다.
	*/
	public void setTemplate(JdbcTemplate template) {
		this.template = template;
	}

	public void setTransactionManager(PlatformTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}
	
	public TicketDAO() {
		System.out.println("TicketDAO생성자호출:"+template);
	}
	
	//티켓구매와 결제를 위한 메서드로 트랜잭션 처리를 한다.
	public void buyTicket(final TicketDTO dto) {
		
		System.out.println("buyTicket()메소드 호출");
		System.out.println(dto.getCustomerId()+"님이 "
				+"티켓 "+ dto.getAmount() + "장을 "
				+"구매합니다.");
		
		//DAO에서 트랜잭션 처리를 위해 아래 객체를 생성한다.
		TransactionDefinition def = new DefaultTransactionDefinition();
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			/*
			티켓 구매 금액에 대한 DB처리를 한다. insert계열의 쿼리문이므로
			update() 메서드를 사용한다.
			PreparedStatementCreator를 익명클래스로 선언하여 쿼리문의
			인파라미터를 설정한 후 실행한다.
			*/
			template.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					
					String query = "INSERT INTO "
							+ "transaction_pay (customerId, amount) "
							+ "VALUES (?, ?)";
					PreparedStatement psmt =
							con.prepareStatement(query);
					psmt.setString(1, dto.getCustomerId());
					psmt.setInt(2, dto.getAmount()*10000);
					
					return psmt;
				}
			});
			
			/*
			티켓 구매 매수에 대한 DB처리
			: check 제약조건에 의해 매수가 5장을 초과하는 경우에는
			제약조건 위배로 쿼리 에러가 발생하게 된다.
			*/
			template.update(new PreparedStatementCreator() {
				
				@Override
				public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
					
					String query = "INSERT INTO "
							+ "transaction_ticket (customerId, countNum) "
							+ "VALUES (?, ?)";
					PreparedStatement psmt =
							con.prepareStatement(query);
					psmt.setString(1, dto.getCustomerId());
					psmt.setInt(2, dto.getAmount());
					
					return psmt;
				}
			});
			
			//티켓매수가 5장 이하라면 정상처리되어 모든 작업이 실제 테이블에 commit 된다.
			System.out.println("카드결제와 티켓구매 모두 정상처리되었습니다.");
			transactionManager.commit(status);
		} 
		catch (Exception e) {
			System.out.println("제약조건을 위배하여 카드결제와 티켓구매 모두가 취소되었습니다.");
			transactionManager.rollback(status);
		}
	}
}
