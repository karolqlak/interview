package org.ing.interview.integration;

import org.ing.interview.integration.model.PagedResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "reportIntegrator", url = "${realestate.api.client.url}")
public interface RealEstateApiClient {

    @RequestMapping(method = RequestMethod.GET, value = "/api/real-estates/{regionId}?page={pageNo}")
    PagedResponse getPageForRegion(@PathVariable String regionId, @PathVariable long pageNo);
}
