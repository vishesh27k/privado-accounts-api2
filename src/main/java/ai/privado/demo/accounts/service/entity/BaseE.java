package ai.privado.demo.accounts.service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.Version;

import lombok.Data;
import lombok.NoArgsConstructor;

@MappedSuperclass
@Data
@NoArgsConstructor
public class BaseE implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "ID")
	private String id;

	@Column(name = "CREATED_BY", length = 200)
	private String createdBy;

	@Column(name = "UPDATED_BY", length = 200)
	private String updatedBy;

	@Column(name = "CREATED_DATE")
	private LocalDateTime createdDate;

	@Column(name = "UPDATED_DATE")
	private LocalDateTime updatedDate;

	@Column(name = "DELETED", nullable = false, columnDefinition = "TINYINT(1) NULL DEFAULT '0'")
	private boolean deleted;

	@Column(name = "VERSION")
	@Version
	private Long version = 0L;

	public BaseE(String createdBy, String updatedBy, LocalDateTime createdDate, LocalDateTime updatedDate,
			boolean deleted) {
		this.createdBy = createdBy;
		this.updatedBy = updatedBy;
		this.createdDate = createdDate;
		this.updatedDate = updatedDate;
		this.deleted = deleted;
	}

	@PrePersist
	public void prePersist() {
		if (id == null || id.isBlank()) {
			this.id = UUID.randomUUID().toString();
		}
	}

}
