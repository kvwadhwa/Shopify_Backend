package com.kunal.inventory;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Warehouse {
    @Id
    private long warehouseId;
    private String warehouseLocation;

    public Warehouse() {
    }

    public Warehouse(long warehouseId, String warehouseLocation) {
        this.warehouseId = warehouseId;
        this.warehouseLocation = warehouseLocation;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getWarehouseLocation() {
        return warehouseLocation;
    }

    public void setWarehouseLocation(String warehouseLocation) {
        this.warehouseLocation = warehouseLocation;
    }
}
