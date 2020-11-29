package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.Owner;
import guru.springframework.sfgpetclinic.model.Pet;
import guru.springframework.sfgpetclinic.model.PetType;
import guru.springframework.sfgpetclinic.services.PetService;
import guru.springframework.sfgpetclinic.services.VisitService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VisitControllerTest {

    @Mock
    PetService petService;

    @Mock
    VisitService visitService;

    @InjectMocks
    VisitController visitController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(petService.findById(anyLong()))
                .thenReturn(Pet.builder()
                                .id(1L)
                                .birthday(LocalDate.of(2018,11, 11))
                                .name("Cutie").visits(new HashSet<>())
                                .owner(Owner.builder()
                                        .id(1L)
                                        .lastName("Doe")
                                        .firstName("John")
                                            .build())
                                        .petType(PetType.builder()
                                                .name("Dog")
                                                    .build())
                                    .build());

        mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();
    }

    @Test
    void initNewVisitForm() throws Exception {
        mockMvc.perform(get("/owners/1/pets/1/visits/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }



    @Test
    void processNewVisitForm() throws Exception {
        mockMvc.perform(post("/owners/1/pets/1/visits/new")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("date", "2018-11-11")
                        .param("description", "a visit"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/{ownerId}"))
                .andExpect(model().attributeExists("visit"));

    }
}