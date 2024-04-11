package ai.privado.demo.accounts.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SESSIONS")
public class SessionE extends BaseE {

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID", length = 200)
	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
