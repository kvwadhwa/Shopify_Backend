package com.kunal.inventory;

import javax.persistence.*;

@Entity
public class Inventory {
    @Id
    private long inventoryId;
    private String itemName;
    private long itemQuantity;
    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    protected Inventory() {
    }

    public Inventory(long inventoryId, String itemName, long itemQuantity, Warehouse warehouse) {
        this.inventoryId = inventoryId;
        this.itemName = itemName;
        this.itemQuantity = itemQuantity;
        this.warehouse = warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public long getInventoryId() {
        return inventoryId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public long getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(long itemQuantity) {
        this.itemQuantity = itemQuantity;
    }
}

