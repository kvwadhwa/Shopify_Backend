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
@RequestMapping(path = "/warehouse")
public class WarehouseController {
    @Autowired
    private WarehouseRepository warehouseRepository;

    @PostMapping(path = "/create")
    public @ResponseBody
    Warehouse create(@RequestParam long warehouseId, @RequestParam String warehouseLocation) throws EntityAlreadyExistsException {
        if (warehouseRepository.existsById(warehouseId))
            throw new EntityAlreadyExistsException("Warehouse already exists");
        Warehouse w = new Warehouse(warehouseId, warehouseLocation);
        warehouseRepository.save(w);
        return w;
    }

    @PostMapping(path = "/delete")
    public @ResponseBody
    Warehouse delete(@RequestParam long warehouseId) throws EntityNotFoundException {
        Warehouse w = get(warehouseId);
        warehouseRepository.deleteById(warehouseId);
        return w;
    }

    @PostMapping(path = "/update")
    public @ResponseBody
    Warehouse update(@RequestParam long warehouseId, @RequestParam String warehouseLocation) throws EntityNotFoundException {
        Warehouse w = get(warehouseId);
        w.setWarehouseId(warehouseId);
        if (warehouseLocation != null) w.setWarehouseLocation(warehouseLocation);
        warehouseRepository.save(w);
        return w;
    }

    @PostMapping(path = "/get")
    public @ResponseBody
    Warehouse get(@RequestParam long warehouseId) throws EntityNotFoundException {
        Optional<Warehouse> w = warehouseRepository.findById(warehouseId);
        if (w.isPresent()) {
            return w.get();
        } else {
            throw new EntityNotFoundException("Warehouse ID not found.");
        }
    }

    @PostMapping(path = "/list")
    public @ResponseBody
    List<Warehouse> list() {
        List<Warehouse> warehouses = new ArrayList<>();
        warehouseRepository.findAll().forEach(warehouses::add);
        return warehouses;
    }
}
