package com.btg.challenge.orders.domain.entity;

import java.math.BigDecimal;

public class OrderItem {

    private Long itemId;
    private String product;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal totalPrice;

    public OrderItem() {
    }

    public OrderItem(String product, Integer quantity, BigDecimal price) {
        this.product = product;
        this.quantity = quantity;
        this.price = price;
        updateTotalPrice();
    }

    public void updateTotalPrice() {
        if (price != null && quantity != null) {
            this.totalPrice = price.multiply(BigDecimal.valueOf(quantity));
        } else {
            this.totalPrice = BigDecimal.ZERO;
        }
    }

    public boolean isValid() {
        return product != null && !product.trim().isEmpty()
                && quantity != null && quantity > 0
                && price != null && price.compareTo(BigDecimal.ZERO) >= 0;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
        updateTotalPrice();
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
        updateTotalPrice();
    }

    public BigDecimal getTotalPrice() {
        if (totalPrice == null) {
            updateTotalPrice();
        }
        return totalPrice;
    }
// TODO
// O Problema: O método getTotalPrice(), que aparenta ser apenas um acessor (getter) para resgatar uma informação, altera o estado interno da classe ao disparar a rotina de atualização updateTotalPrice().
// Por que é um Code Smell: No Clean Code, métodos com o prefixo get instintivamente indicam ausência de efeitos colaterais. Quem chama um get() não espera mutação no objeto.
// Relação Clean Code / SOLID: Isso fere frontalmente o Command Query Separation (CQS), que estipula que um método deve ser um Comando (que muda o estado) ou uma Consulta (que retorna dados), nunca ambos ao mesmo tempo.

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
