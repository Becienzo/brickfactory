package com.caci.brickfactory;

import java.util.ArrayList;

import java.util.List;
import java.util.logging.Logger;

import org.junit.jupiter.api.DisplayName;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;

import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.hamcrest.Matchers.*;

import com.caci.brickfactory.model.Ord;

import com.caci.brickfactory.service.OrdService;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class BrickfactoryApplicationTests {
	static Logger logger = Logger.getLogger(BrickfactoryApplicationTests.class.getName());

	@MockBean
	private OrdService ordService;

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("POST /orders success")
	@Rollback(value = false)
	public void saveOrderTest() throws Exception {
		
        Ord ordToPost = new Ord(1L,5, "not processed");
        Ord ordToReturn = new Ord(2L, 10, "not processed");

        doReturn(ordToReturn).when(ordService).save(any());


        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(ordToPost)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/orders/2"))
                .andExpect(jsonPath("$.ordRef", is(2)))
                .andExpect(jsonPath("$.amountOfBrick", is(10)))
                .andExpect(jsonPath("$.status", is("not processed")));
	}

	@Test
	@DisplayName("GET /orders/single success")

	public void getOrderTest() throws Exception {
        Ord ord = new Ord(1L,5, "not processed");

        doReturn(Optional.of(ord)).when(ordService).findById(1l);

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/orders/1"))
                .andExpect(jsonPath("$.ordRef", is(1)))
                .andExpect(jsonPath("$.amountOfBrick", is(5)))
                .andExpect(jsonPath("$.status", is("not processed")));;
	}

	@Test
    @DisplayName("GET /orders/1 - Not Found")
    void testGetOrdByIdNotFound() throws Exception {

        doReturn(Optional.empty()).when(ordService).findById(1l);

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isNotFound());
    }

	@Test
	@DisplayName("GET /orders/all success")

	public void getListOfOrdersTest() throws Exception{
        Ord ord1 = new Ord(1L, 5, "not processed");
        Ord ord2 = new Ord(2l, 10, "not processed");

		List<Ord> lists = new ArrayList<>();
		lists.add(ord1);
		lists.add(ord2);

        doReturn(lists).when(ordService).findAll();

        mockMvc.perform(get("/orders"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(header().string(HttpHeaders.LOCATION, "/orders"))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].ordRef", is(1)))
                .andExpect(jsonPath("$[0].amountOfBrick", is(5)))
                .andExpect(jsonPath("$[0].status", is("not processed")))
                .andExpect(jsonPath("$[1].ordRef", is(2)))
                .andExpect(jsonPath("$[1].amountOfBrick", is(10)))
                .andExpect(jsonPath("$[1].status", is("not processed")));
	}

	static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
