package com.sparta.user.domain.model;

import com.sparta.commons.domain.jpa.BaseEntity;
import com.sparta.user.domain.model.vo.ShippingManagerType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.UUID;

@Slf4j
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_shipping_managers")
@SQLDelete(sql = "UPDATE p_shipping_managers SET is_delete = true where id=?")
@SQLRestriction(value = "is_delete is false")
public class ShippingManager extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "shipping_manager_id")
  private UUID id;

  @Column(unique = true, nullable = false)
  private String slackId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private ShippingManagerType type;

  @Column(nullable = false)
  private Boolean isDelete;

  @Column(nullable = false)
  private UUID hubId; // DB 분할로 인한 연관관계가 아닌 정보 저장용

  @Builder(access = AccessLevel.PROTECTED)
  public ShippingManager(UUID hubId, String slackId, ShippingManagerType type) {
    this.hubId = hubId;
    this.isDelete = false;
    this.slackId = slackId;
    this.type = type;
  }

  public static ShippingManager create(String slackId, ShippingManagerType type, UUID hubId) {
    return ShippingManager.builder()
        .slackId(slackId)
        .type(type)
        .hubId(hubId)
        .build();
  }

  public void update(String slackId, ShippingManagerType type, UUID hubId) {
    this.slackId = slackId;
    this.type = type;
    this.hubId = hubId;
  }

  public void softDelete(String username) {
    this.isDelete = true;
    this.deletedAt = LocalDateTime.now();
    this.deletedBy = username;
  }
}
