package com.kunal.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
class InventoryControllerTest {

    private static final Warehouse TEST_WAREHOUSE = new Warehouse(1, "Toronto");

    private static final Inventory TEST_INVENTORY = new Inventory(1, "Pen", 10, TEST_WAREHOUSE);
    private static final String TEST_INVENTORY_STRING = "{\"inventoryId\":1,\"itemName\":\"Pen\",\"itemQuantity\":10,\"warehouse\":{\"warehouseId\":1,\"warehouseLocation\":\"Toronto\"}}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @MockBean
    private InventoryRepository inventoryRepository;

    @Test
    public void canCreateSuccessfully() throws Exception {
        when(warehouseRepository.findById(any())).thenReturn(Optional.of(TEST_WAREHOUSE));
        when(inventoryRepository.save(any())).thenReturn(TEST_INVENTORY);

        mockMvc.perform(post("/inventory/create?inventoryId=1&itemName=Pen&itemQuantity=10&warehouseId=1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_INVENTORY_STRING));
    }

    @Test
    public void duplicateCreateFails() throws Exception {
        when(inventoryRepository.existsById(any())).thenReturn(true);

        mockMvc.perform(post("/inventory/create?inventoryId=1&itemName=Pen&itemQuantity=10&warehouseId=1")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void canGetSuccessfully() throws Exception {
        when(inventoryRepository.findById(any())).thenReturn(Optional.of(TEST_INVENTORY));

        mockMvc.perform(post("/inventory/get?inventoryId=1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_INVENTORY_STRING));
    }

    @Test
    public void missingGetFails() throws Exception {
        when(inventoryRepository.existsById(any())).thenReturn(false);

        mockMvc.perform(post("/inventory/get?inventoryId=2")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void canUpdateSuccessfully() throws Exception {
        Warehouse NEW_WAREHOUSE = new Warehouse(2, "Miami");

        Inventory UPDATED_INVENTORY = new Inventory(TEST_INVENTORY.getInventoryId(), TEST_INVENTORY.getItemName(), TEST_INVENTORY.getItemQuantity(), NEW_WAREHOUSE);
        String UPDATED_INVENTORY_STRING = "{\"inventoryId\":1,\"itemName\":\"Pen\",\"itemQuantity\":10,\"warehouse\":{\"warehouseId\":2,\"warehouseLocation\":\"Miami\"}}";

        when(warehouseRepository.findById(eq(1L))).thenReturn(Optional.of(TEST_WAREHOUSE));
        when(warehouseRepository.findById(eq(2L))).thenReturn(Optional.of(NEW_WAREHOUSE));
        when(inventoryRepository.findById(any())).thenReturn(Optional.of(TEST_INVENTORY));
        when(inventoryRepository.save(any())).thenReturn(UPDATED_INVENTORY);

        mockMvc.perform(post("/inventory/update?inventoryId=1&warehouseId=2")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(UPDATED_INVENTORY_STRING));
    }

    @Test
    public void canDeleteSuccessfully() throws Exception {
        when(inventoryRepository.findById(any())).thenReturn(Optional.of(TEST_INVENTORY));

        mockMvc.perform(post("/inventory/delete?inventoryId=1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_INVENTORY_STRING));

        when(inventoryRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/inventor/delete?inventoryId=1")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void canListSuccessfully() throws Exception {
        when(inventoryRepository.findAll()).thenReturn(List.of(TEST_INVENTORY,
                new Inventory(2, "Paper", 100, TEST_WAREHOUSE),
                new Inventory(3, "Pencil", 1000, TEST_WAREHOUSE)));

        String TEST_INVENTORIES_STRING = "[{\"inventoryId\":1,\"itemName\":\"Pen\",\"itemQuantity\":10,\"warehouse\":{\"warehouseId\":1,\"warehouseLocation\":\"Toronto\"}}," +
                "{\"inventoryId\":2,\"itemName\":\"Paper\",\"itemQuantity\":100,\"warehouse\":{\"warehouseId\":1,\"warehouseLocation\":\"Toronto\"}}," +
                "{\"inventoryId\":3,\"itemName\":\"Pencil\",\"itemQuantity\":1000,\"warehouse\":{\"warehouseId\":1,\"warehouseLocation\":\"Toronto\"}}]";

        mockMvc.perform(post("/inventory/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_INVENTORIES_STRING));

    }
}