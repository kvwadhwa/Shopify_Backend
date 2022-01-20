package com.kunal.inventory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/inventory")
public class InventoryController {
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private WarehouseRepository warehouseRepository;

    @PostMapping(path = "/create")
    public @ResponseBody
    Inventory create(@RequestParam long inventoryId, @RequestParam String itemName, @RequestParam long itemQuantity, @RequestParam long warehouseId) throws EntityAlreadyExistsException {
        if (inventoryRepository.existsById(inventoryId))
            throw new EntityAlreadyExistsException("Inventory already exists");


        Inventory i = new Inventory(inventoryId, itemName, itemQuantity, getWarehouse(warehouseId));
        inventoryRepository.save(i);
        return i;
    }

    private Warehouse getWarehouse(long warehouseId) {
        Optional<Warehouse> warehouse = warehouseRepository.findById(warehouseId);
        if (warehouse.isEmpty()) {
            throw new EntityNotFoundException("Warehouse ID not found.");
        }
        return warehouse.get();
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    Inventory delete(@RequestParam long inventoryId) throws EntityNotFoundException {
        Inventory i = get(inventoryId);
        inventoryRepository.deleteById(inventoryId);
        return i;
    }

    @PostMapping(path = "/update")
    public @ResponseBody
    Inventory update(@RequestParam long inventoryId, @RequestParam(required = false) String itemName, @RequestParam(required = false) Long itemQuantity, @RequestParam(required = false) Long warehouseId) throws EntityNotFoundException {
        Inventory i = get(inventoryId);
        if (itemName != null) i.setItemName(itemName);
        if (itemQuantity != null) i.setItemQuantity(itemQuantity);
        if (warehouseId != null) i.setWarehouse(getWarehouse(warehouseId));
        inventoryRepository.save(i);
        return i;


    }

    @PostMapping(path = "/get")
    public @ResponseBody
    Inventory get(@RequestParam long inventoryId) throws EntityNotFoundException {
        Optional<Inventory> i = inventoryRepository.findById(inventoryId);
        if (i.isPresent()) {
            return i.get();
        } else {
            throw new EntityNotFoundException("Item ID not found.");
        }
    }

    @PostMapping(path = "/list")
    public @ResponseBody
    List<Inventory> list() {
        List<Inventory> items = new ArrayList<>();
        inventoryRepository.findAll().forEach(items::add);
        return items;
    }


}

