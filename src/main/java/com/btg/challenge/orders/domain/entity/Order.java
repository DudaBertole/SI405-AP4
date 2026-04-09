package com.btg.challenge.orders.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Order {

    private Long orderId;
    private Long customerId;
    private BigDecimal totalAmount;
    private Integer itemsCount;
    private LocalDateTime createdAt;
    private List<OrderItem> items;

    // Constructors
    public Order() {
    }

    public Order(Long orderId, Long customerId, List<OrderItem> items) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.items = items;
        updateTotals();
    }


    public BigDecimal calculateTotalAmount() {
        if (items == null || items.isEmpty()) {
            return BigDecimal.ZERO;
        }

        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void updateTotals() {
        this.itemsCount = items != null ? items.size() : 0;
        this.totalAmount = calculateTotalAmount();

        // Atualiza o total de cada item também
        if (items != null) {
            items.forEach(OrderItem::updateTotalPrice);
        }
    }

    public boolean isValid() {
        return orderId != null
                && customerId != null
                && items != null
                && !items.isEmpty()
                && items.stream().allMatch(OrderItem::isValid);
    }

    public void addItem(OrderItem item) {
        if (items != null) {
            items.add(item);
            updateTotals();
        }
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
// TODO
// O Problema: A entidade de domínio expõe setters públicos para atributos que deveriam ser restritos à lógica interna de cálculo, como totalAmount ou totalPrice.
// Por que é um Code Smell: Qualquer camada da aplicação (um controller ou um mapper) pode arbitrariamente chamar order.setTotalAmount(new BigDecimal("99999")), quebrando completamente a consistência matemática do pedido, já que a soma dos itens não baterá com este valor manual.
// Relação Clean Code / SOLID: Viola o pilar de Encapsulamento da Programação Orientada a Objetos e fere indiretamente o Open-Closed Principle (OCP) (as regras de cálculo do domínio perdem seu poder). Entidades ricas de domínio devem expor ações e comportamentos (calculateTotals()), enquanto propriedades geradas não deveriam ter "setters" públicos arbitrários.

    public Integer getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(Integer itemsCount) {
        this.itemsCount = itemsCount;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
        updateTotals();
    }
}
