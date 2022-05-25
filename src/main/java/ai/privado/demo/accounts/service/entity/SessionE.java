package ai.privado.demo.accounts.service.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SESSIONS")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Data
public class SessionE extends BaseE {

	private static final long serialVersionUID = 1L;

	@Column(name = "USER_ID", length = 200)
	private String userId;
}
