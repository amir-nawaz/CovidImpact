package com.example.covidImpact;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith( SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsersControllerTests {
    
    @Autowired
    private MockMvc mockMvc;
   /*
    @Autowired
    private TestRestTemplate template;
    
    @WithMockUser(username = "")*/
    @Test
    public void getStatusTest() throws Exception {
        String expectedVal = "working on port number: 8080";
        mockMvc.perform( MockMvcRequestBuilders.get( "/users/status/check" ).header( "Authorization", "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBZGlsIiwiZXhwIjoxNjQxNDg5NTI5LCJpYXQiOjE2NDE0NzE1Mjl9.5YYs6JvZ2ClP0WZY83eW7sFBVGn95zVKeYh-Z1SxJ79ze1cVdU7DZ3e5TGImFPYDjm_lTYoZinCNKfrYTWLY1A" ).contentType( MediaType.APPLICATION_JSON )).andExpect(status().isOk());
        //Assertions.assertEquals(expectedVal, userController.getStatus());
    }
}
