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
@Table(name = "USERS")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@Data
public class UserE extends BaseE {

	private static final long serialVersionUID = 1L;

	@Column(name = "FIRST_NAME", length = 200)
	private String firstName;

	@Column(name = "LAST_NAME", length = 200)
	private String lastName;

	@Column(name = "EMAIL", length = 200)
	private String email;

	@Column(name = "PHONE", length = 200)
	private String phone;

	@Column(name = "PASSWORD", length = 200)
	private String password;

	@Column(name = "DOB", length = 200)
	private String dob;
}
