package org.ing.interview.schedule;

import org.ing.interview.model.Region;
import org.ing.interview.schedule.model.RealEstateImportJob;
import org.ing.interview.schedule.repository.RealEstateJobRepository;
import org.ing.interview.service.TimeProvider;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

@SpringBootTest
class RealEstateDataSchedulerTest {

    @Autowired
    private RealEstateDataScheduler underTest;
    @MockBean
    private RealEstateDataSchedulerExecutor executor;
    @MockBean
    private RealEstateJobRepository jobRepository;
    @MockBean
    private TimeProvider timeProvider;

    @Test
    void shouldExecuteImportsForAllRegion() throws Exception {
        //given
        LocalDate fakeNow = LocalDate.of(2022, Month.JANUARY, 1);
        Mockito.when(timeProvider.now()).thenReturn(fakeNow);
        Mockito.when(jobRepository.findByDate(fakeNow)).thenReturn(Arrays.asList());
        //when
        underTest.loadRealEstateData();
        //then
        Mockito.verify(executor, Mockito.times(Region.values().length)).runImportForRegion(Mockito.any());
    }

    @Test
    void shouldNotExecuteImportsForAllRegionLUBL() throws Exception {
        //given
        RealEstateImportJob lublJob = new RealEstateImportJob();
        lublJob.setRegion(Region.LUBL);
        lublJob.setStatus(RealEstateImportJob.Status.FAILED);

        LocalDate fakeNow = LocalDate.of(2022, Month.JANUARY, 1);
        Mockito.when(timeProvider.now()).thenReturn(fakeNow);
        //there is a failed job for lubl that day
        Mockito.when(jobRepository.findByDate(fakeNow)).thenReturn(Arrays.asList(lublJob));
        //when
        underTest.loadRealEstateData();
        //then
        Mockito.verify(executor, Mockito.times(Region.values().length - 1)).runImportForRegion(Mockito.any());
        Mockito.verify(executor, Mockito.times(0)).runImportForRegion(Region.LUBL);
    }
}