package com.example.shoppingmall.user.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeliveryAddressDto {
    private Long deliveryAddressId;
    private Long userId;
    private String recipientName;
    private String recipientPhoneNumber;
    private String recipientEmail;
    private String postalCode;
    private String mainAddress;
    private String detailedAddress;
    @JsonProperty("isDefault")
    private boolean isDefault;
    private boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public DeliveryAddressDto() {}

    public DeliveryAddressDto(Long userId, String recipientName, String recipientPhoneNumber, String recipientEmail, String postalCode, String mainAddress, String detailedAddress, boolean isDefault, boolean isActive) {
        this.userId = userId;
        this.recipientName = recipientName;
        this.recipientPhoneNumber = recipientPhoneNumber;
        this.recipientEmail = recipientEmail;
        this.postalCode = postalCode;
        this.mainAddress = mainAddress;
        this.detailedAddress = detailedAddress;
        this.isDefault = isDefault;
        this.isActive = isActive;
    }

    public Long getDeliveryAddressId() {
        return deliveryAddressId;
    }

    public void setDeliveryAddressId(Long deliveryAddressId) {
        this.deliveryAddressId = deliveryAddressId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhoneNumber() {
        return recipientPhoneNumber;
    }

    public void setRecipientPhoneNumber(String recipientPhoneNumber) {
        this.recipientPhoneNumber = recipientPhoneNumber;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getMainAddress() {
        return mainAddress;
    }

    public void setMainAddress(String mainAddress) {
        this.mainAddress = mainAddress;
    }

    public String getDetailedAddress() {
        return detailedAddress;
    }

    public void setDetailedAddress(String detailedAddress) {
        this.detailedAddress = detailedAddress;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "DeliveryAddress{" +
                "deliveryAddressId=" + deliveryAddressId +
                ", userId='" + userId + '\'' +
                ", recipientName='" + recipientName + '\'' +
                ", recipientPhoneNumber='" + recipientPhoneNumber + '\'' +
                ", recipientEmail='" + recipientEmail + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", mainAddress='" + mainAddress + '\'' +
                ", detailedAddress='" + detailedAddress + '\'' +
                ", isDefault=" + isDefault +
                ", isActive=" + isActive +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DeliveryAddressDto that = (DeliveryAddressDto) o;
        return Objects.equals(userId, that.userId) && Objects.equals(postalCode, that.postalCode) && Objects.equals(mainAddress, that.mainAddress) && Objects.equals(detailedAddress, that.detailedAddress);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postalCode, mainAddress, detailedAddress);
    }
}
