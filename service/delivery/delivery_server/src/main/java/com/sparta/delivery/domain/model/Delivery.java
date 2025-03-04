package com.sparta.delivery.domain.model;

import com.sparta.commons.domain.jpa.BaseEntity;
import com.sparta.delivery.domain.model.State.DeliveryState;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "P_DELIVERIES",
    indexes = {
        @Index(name = "idx_shipping_manager_id", columnList = "shipping_manager_id")
    })
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLRestriction("is_delete is false")
@SQLDelete(sql = "UPDATE p_deliveries SET deleted_at = NOW() where delivery_id = ?")
@Getter
public class Delivery extends BaseEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(name = "delivery_id")
  private UUID deliveryId;

  @Column(nullable = false)
  private UUID orderId;

  @Column(nullable = false)
  private UUID departureHubId;

  @Column(nullable = false)
  private UUID arrivalHubId;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private DeliveryState deliveryState = DeliveryState.PENDING;

  @Column(nullable = false)
  private String shippingAddress;

  @Column(name = "shipping_manager_id", nullable = false)
  private UUID shippingManagerId;

  @Column(name = "shipping_manager_slack_id", nullable = false)
  private String shippingManagerSlackId;

  @OneToMany(mappedBy = "delivery", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DeliveryRoute> routes = new ArrayList<>();

  private long estimatedElapsedTime;
  private double estimatedDistance;

  @Column(name = "shipping_start_date")
  private LocalDateTime shippingStartDate; //TODO :: 배송대기에서 배송요청으로 변경되었을때 초기화

  @Column(name = "shipping_end_date")
  private LocalDateTime shippingEndDate; //TODO :: 업체배송중에서 업체배송완료로 변경되었을때 초기화

  private boolean isDelete = false;

  @Builder
  private Delivery(
      UUID orderId,
      UUID departureHubId,
      UUID arrivalHubId,
      String shippingAddress,
      UUID shippingManagerId,
      String shippingManagerSlackId) {
    this.orderId = orderId;
    this.departureHubId = departureHubId;
    this.arrivalHubId = arrivalHubId;
    this.shippingAddress = shippingAddress;
    this.shippingManagerId = shippingManagerId;
    this.shippingManagerSlackId = shippingManagerSlackId;
  }
  public void setEstimatedDeliveryData(long estimatedElapsedTime, double estimatedDistance){
    this.estimatedElapsedTime = estimatedElapsedTime;
    this.estimatedDistance = estimatedDistance;
    this.shippingStartDate = LocalDateTime.now();
  }

  public void setDeliveryRoutes(List<DeliveryRoute> routes){
    this.routes = routes;
  }

  public void updateDeliveryState(DeliveryState state){
    this.deliveryState = state;
  }

}
