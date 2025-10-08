package ru.practicum.shareit.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.controller.item.ItemController;
import ru.practicum.shareit.dto.item.ItemDto;
import ru.practicum.shareit.model.item.Item;
import ru.practicum.shareit.service.item.ItemService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private ItemService itemService;

    @Test
    void createItem_shouldReturnItem() throws Exception {
        long userId = 1L;
        ItemDto itemToCreate = new ItemDto();
        itemToCreate.setName("Test Item");
        itemToCreate.setDescription("Description");
        itemToCreate.setAvailable(true);

        Item createdItem = new Item();
        createdItem.setId(1L);
        createdItem.setName("Test Item");
        createdItem.setDescription("Description");
        createdItem.setAvailable(true);

        when(itemService.create(anyLong(), any(Item.class), any())).thenReturn(createdItem);

        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemToCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }
}
