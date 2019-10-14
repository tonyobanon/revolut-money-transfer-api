

package com.revolut.transferservice.impl;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.classes.TransactionPayload;
import com.revolut.transferservice.impl.utils.JsonUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SparkServerTest {

    @BeforeAll
    static void setUp() {
        SparkServer.start();
    }

    @AfterAll
    static void tearDown() {
        SparkServer.stop();
    }

    @Test
    void getAllPartiesDefaultPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties?limit=10");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"hasMore\": false,\n" +
                        "  \"pageNumber\": 1,\n" +
                        "  \"recordsPerPage\": 10,\n" +
                        "  \"content\": [\n" +
                        "    {\n" +
                        "      \"name\": \"Revolut LLC\",\n" +
                        "      \"id\": 1,\n" +
                        "      \"partyType\": \"LEGAL_PERSON\",\n" +
                        "      \"taxIdentificationNumber\": \"7703408188\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "      \"name\": \"Google6\",\n" +
                        "      \"id\": 2,\n" +
                        "      \"partyType\": \"LEGAL_PERSON\",\n" +
                        "      \"taxIdentificationNumber\": \"0000000023\"\n" +
                        "    }\n" +
                        "  ]\n" +
                        "}", json);
            }
        }
    }
    
    @Test
    void createNewAccount() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPut httpPut = new HttpPut("http://localhost:9999/accounts");
            
            		StringEntity stringEntity = new StringEntity("{\r\n" + 
                  		  "  \"partyId\": 2,\r\n" + 
                		  "  \"number\": \"00000000000004725638\",\r\n" + 
                		  "  \"balance\": \"0\",\r\n" + 
                		  "  \"active\": true,\r\n" + 
                		  "  \"currency\": \"GBP\"\n" +
                		  "}");
            		httpPut.setEntity(stringEntity);
            
            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"id\": 2,\n" +
                        "  \"currency\": {\n" +
                        "    \"isoCode\": \"GBP\",\n" +
                        "    \"country\": \"GBR\"\n" +
                        "  },\n" +
                        "  \"number\": \"00000000000004725638\",\n" +
                        "  \"holder\": {\n" +
                        "    \"name\": \"Google6\",\n" +
                        "    \"id\": 2,\n" +
                        "    \"partyType\": \"LEGAL_PERSON\",\n" +
                        "    \"taxIdentificationNumber\": \"0000000023\"\n" +
                        "  },\n" +
                        "  \"active\": true,\n" +
                        "  \"balance\": 0\n" +
                        "}", json);
            }
        }
    }

    @Test
    void createNewParty() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPut httpPut = new HttpPut("http://localhost:9999/parties");
            
            		StringEntity stringEntity = new StringEntity("{\r\n" + 
                  		  "  \"name\": \"Google6\",\r\n" + 
                		  "  \"type\": \"LEGAL_PERSON\",\r\n" + 
                		  "  \"taxIdentificationNumber\": \"0000000023\"\n" +
                		  "}");
            		httpPut.setEntity(stringEntity);
            
            try (CloseableHttpResponse response = httpClient.execute(httpPut)) {
                assertEquals(HttpStatus.SC_CREATED, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" + 
                	  "  \"name\": \"Google6\",\n" + 
                	  "  \"id\": 2,\n" + 
              		  "  \"partyType\": \"LEGAL_PERSON\",\n" + 
              		  "  \"taxIdentificationNumber\": \"0000000023\"\n" +
              		  "}", json);
            }
        }
    }
    

    @Test
    void getAllPartiesSecondPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties?page=2&limit=20");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"hasMore\": false,\n" +
                        "  \"pageNumber\": 2,\n" +
                        "  \"recordsPerPage\": 20,\n" +
                        "  \"content\": []\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllPartiesInvalidPage() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties?page=0&limit=20");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Page number should be positive and starts with 1\"\n" +
                        "}", json);
            }
        }
    }

    @Test
    void getAllPartiesWithoutPagination() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpGet httpget = new HttpGet("http://localhost:9999/parties");
            try (CloseableHttpResponse response = httpClient.execute(httpget)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Invalid pagination parameters\"\n" +
                        "}", json);
            }
        }
    }

    @Test
    void transferMoneyWithTheSameAccount() throws Exception {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            final HttpPost httpPost = new HttpPost("http://localhost:9999/transactions");
            final TransactionPayload payload = new TransactionPayload(1L, 1L, BigDecimal.valueOf(123456, 2));
            final String jsonPayload = JsonUtils.make().toJson(payload);
            final StringEntity stringEntity = new StringEntity(jsonPayload);
            httpPost.setEntity(stringEntity);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
                assertEquals(HttpStatus.SC_BAD_REQUEST, response.getStatusLine().getStatusCode());
                final HttpEntity entity = response.getEntity();
                assertNotNull(entity);
                assertEquals("application/json", entity.getContentType().getValue());
                final String json = EntityUtils.toString(entity);
                assertEquals("{\n" +
                        "  \"errorCode\": 400,\n" +
                        "  \"errorMessage\": \"Accounts must be different\"\n" +
                        "}", json);
            }
        }
    }
}