package org.ing.interview.web;

import org.ing.interview.integration.RealEstateApiClient;
import org.ing.interview.model.Region;
import org.ing.interview.model.entity.RealEstateInfoEntity;
import org.ing.interview.repository.RealEstateEntityRepository;
import org.ing.interview.service.TimeProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class RealEstateStatsControllerTest {

    @MockBean
    private TimeProvider timeProvider;

    @MockBean
    private RealEstateApiClient mockApiClient;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RealEstateEntityRepository entityRepository;


    @Test
    void shouldCalculateAveragePrice() throws Exception {
        //given
        LocalDate fakeNow = LocalDate.of(2022, Month.JANUARY, 1);
        Mockito.when(timeProvider.now()).thenReturn(fakeNow);

        entityRepository.saveAndFlush(createEntity(BigDecimal.valueOf(10)));
        entityRepository.saveAndFlush(createEntity(BigDecimal.valueOf(20)));
        entityRepository.saveAndFlush(createEntity(BigDecimal.valueOf(30)));
        //when,then
        mockMvc.perform(get("/api/real-estates-stats/LUBL?size=S&types=apartment&dateSince=20220101&dateUntil=20220102"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.avgValue").value(20.0));
        //clean
        entityRepository.deleteAll();
    }

    @Test
    void shouldGetInvalidRequest() throws Exception {
        //given
        LocalDate fakeNow = LocalDate.of(2022, Month.JANUARY, 1);
        Mockito.when(timeProvider.now()).thenReturn(fakeNow);

        //when,then
        mockMvc.perform(get("/api/real-estates-stats/CZERESNIOWO"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.[0]").value("UNKNOWN_REGION"))
                .andExpect(jsonPath("$.[1]").value("UNKNOWN_SIZE"))
                .andExpect(jsonPath("$.[2]").value("MISSING_TYPES"))
                .andExpect(jsonPath("$.[3]").value("INVALID_DATE_SINCE"))
                .andExpect(jsonPath("$.[4]").value("INVALID_DATE_UNTIL"));
    }

    private RealEstateInfoEntity createEntity(BigDecimal price) {
        RealEstateInfoEntity entity = new RealEstateInfoEntity();
        entity.setRegion(Region.LUBL);
        entity.setArea(BigDecimal.valueOf(30));
        entity.setPrice(price);
        entity.setRooms(4);
        entity.setDate(LocalDate.of(2022, Month.JANUARY, 1));
        entity.setType("apartment");
        return entity;
    }
}