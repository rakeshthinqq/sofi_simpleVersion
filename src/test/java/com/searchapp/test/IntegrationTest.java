package com.searchapp.test;

import com.searchapp.dto.SearchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.web.client.RestTemplate;
import org.testng.Assert;
import org.testng.annotations.Test;

@SpringBootTest( webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class IntegrationTest extends AbstractTestNGSpringContextTests {


    @Autowired
    RestTemplate template;


    private final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    @Test
    public void testSearch() {
//        String query = "happy dog";
        String query = "lion king";

        SearchDto response = template.getForObject("http://localhost:8686/search/"+ query, SearchDto.class);
        Assert.assertNotNull(response);
        Assert.assertNotNull(response.getData());
        Assert.assertEquals(response.getData().size(),  5, "No of records should be 5");
    }
}
