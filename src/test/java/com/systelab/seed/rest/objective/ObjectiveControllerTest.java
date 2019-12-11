package com.systelab.seed.rest.objective;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.systelab.seed.infrastructure.authentication.TokenProvider;
import com.systelab.seed.objective.model.Objective;
import com.systelab.seed.objective.repository.ObjectiveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest()
public class ObjectiveControllerTest {
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private TokenProvider tokenProvider;

    @MockBean
    private ObjectiveRepository mockObjectiveRepository;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    public void testGetObjective() throws Exception {
        Optional<Objective> objective = Optional.of(createObjective("A"));

        when(mockObjectiveRepository.findById(isA(UUID.class))).thenReturn(objective);

        mvc.perform(get("/csw/v1/objectives/{id}", "a98b8fe5-7cc5-4348-8f99-4860f5b84b13")
                .header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is("a98b8fe5-7cc5-4348-8f99-4860f5b84b13")))
                .andExpect(jsonPath("$.name", is("ObjectiveA")));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    public void testInsertObjective() throws Exception {
        Objective objective = createObjective("A");

        when(mockObjectiveRepository.save(any())).thenReturn(objective);

        mvc.perform(post("/csw/v1/objectives/objective")
                .header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a")
                .contentType(MediaType.APPLICATION_JSON).content(createObjectiveInJson(objective)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @WithMockUser(roles = "User")
    public void testDeleteObjective() throws Exception {
        Optional<Objective> objective = Optional.of(createObjective("A"));
        when(mockObjectiveRepository.findById(isA(UUID.class))).thenReturn(objective);

        mvc.perform(delete("/csw/v1/objectives/{1}", "a98b8fe5-7cc5-4348-8f99-4860f5b84b13")
                .header("Authorization", "Bearer 5d1103e-b3e1-4ae9-b606-46c9c1bc915a"))
                .andExpect(status().is2xxSuccessful());
    }

    private static String createObjectiveInJson(Objective objective) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(objective);
    }


    private Objective createObjective(String objectiveName) {

        Objective objective = new Objective();
        objective.setId(UUID.fromString("a98b8fe5-7cc5-4348-8f99-4860f5b84b13"));
        objective.setName("Objective" + objectiveName);
        objective.setEvaluation(8);

        return objective;
    }
}
