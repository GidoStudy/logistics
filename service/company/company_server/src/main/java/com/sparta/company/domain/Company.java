package com.sparta.company.domain;

import com.sparta.commons.domain.jpa.BaseEntity;
import com.sparta.company.application.dto.company.CompanyUpdateRequest;
import com.sparta.company.exception.AlreadyDeletedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "p_companies")
@SQLRestriction("is_delete is false")
public class Company extends BaseEntity {

  @Id
  @GeneratedValue
  @Column(name = "company_id")
  private UUID companyId;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private CompanyType companyType;

  @Column(length = 100, nullable = false)
  private String companyName;

  @Column(nullable = false)
  private String companyAddress;
  private boolean isDelete = false;
  private UUID hubId;
  private Long userId;
  @OneToMany(mappedBy = "company")
  private List<Product> products;


  public void update(CompanyUpdateRequest request) {
    companyName = request.getCompanyName();
    companyAddress = request.getCompanyAddress();
    companyType = request.getCompanyType();
    hubId = request.getHubId();
    userId = request.getUserId();
    super.updatedAt = LocalDateTime.now();
  }


  public void delete(String username) {
    this.isDelete = true;
    deletedAt = LocalDateTime.now();
    deletedBy = username;
  }
}
