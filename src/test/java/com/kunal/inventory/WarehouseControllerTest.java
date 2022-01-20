package com.kunal.inventory;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WarehouseController.class)
class WarehouseControllerTest {

    private static final Warehouse TEST_WAREHOUSE = new Warehouse(1, "Toronto");
    private static final String TEST_WAREHOUSE_STRING = "{\"warehouseId\":1,\"warehouseLocation\":\"Toronto\"}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WarehouseRepository warehouseRepository;

    @Test
    public void canCreateSuccessfully() throws Exception {
        when(warehouseRepository.save(any())).thenReturn(TEST_WAREHOUSE);
        mockMvc.perform(post("/warehouse/create?warehouseId=1&warehouseLocation=Toronto")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_WAREHOUSE_STRING));
    }

    @Test
    public void duplicateCreateFails() throws Exception {
        when(warehouseRepository.existsById(any())).thenReturn(true);

        mockMvc.perform(post("/warehouse/create?warehouseId=1&warehouseLocation=Toronto")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void canGetSuccessfully() throws Exception {
        when(warehouseRepository.findById(any())).thenReturn(Optional.of(TEST_WAREHOUSE));

        mockMvc.perform(post("/warehouse/get?warehouseId=1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_WAREHOUSE_STRING));
    }

    @Test
    public void missingGetFails() throws Exception {
        when(warehouseRepository.existsById(any())).thenReturn(false);

        mockMvc.perform(post("/warehouse/get?warehouseId=2")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void canUpdateSuccessfully() throws Exception {
        Warehouse UPDATED_WAREHOUSE = new Warehouse(TEST_WAREHOUSE.getWarehouseId(), "Miami");
        String UPDATED_WAREHOUSE_STRING = "{\"warehouseId\":1,\"warehouseLocation\":\"Miami\"}";

        when(warehouseRepository.findById(any())).thenReturn(Optional.of(TEST_WAREHOUSE));
        when(warehouseRepository.save(any())).thenReturn(UPDATED_WAREHOUSE);

        mockMvc.perform(post("/warehouse/update?warehouseId=1&warehouseLocation=Miami")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(UPDATED_WAREHOUSE_STRING));
    }

    @Test
    public void canDeleteSuccessfully() throws Exception {
        when(warehouseRepository.findById(any())).thenReturn(Optional.of(TEST_WAREHOUSE));

        mockMvc.perform(post("/warehouse/delete?warehouseId=1")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_WAREHOUSE_STRING));

        when(warehouseRepository.findById(any())).thenReturn(Optional.empty());

        mockMvc.perform(post("/warehouse/delete?warehouseId=1")).andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void canListSuccessfully() throws Exception {
        when(warehouseRepository.findAll()).thenReturn(List.of(TEST_WAREHOUSE,
                new Warehouse(2, "Miami"),
                new Warehouse(3, "Seattle")));

        String TEST_WAREHOUSES_STRING = "[{\"warehouseId\":1,\"warehouseLocation\":\"Toronto\"}," +
                "{\"warehouseId\":2,\"warehouseLocation\":\"Miami\"}," +
                "{\"warehouseId\":3,\"warehouseLocation\":\"Seattle\"}]";

        mockMvc.perform(post("/warehouse/list")).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(TEST_WAREHOUSES_STRING));

    }
}