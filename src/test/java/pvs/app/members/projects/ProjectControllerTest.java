package pvs.app.members.projects;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pvs.app.Application;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = Application.class)
@WebAppConfiguration
class ProjectControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .build();
    }

    @AfterEach
    void tearDown() {
    }

    // TODO: API Testing for Projects
    @Test
    @Disabled
    void createProject() throws Exception {
        String url = "/v1/projects";

        ProjectOfCreation dto = new ProjectOfCreation();
        dto.setProjectName("PVS");
        Gson gson = new Gson();
        String json = gson.toJson(dto);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(url).accept(MediaType.APPLICATION_JSON)
                        .content(json).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}