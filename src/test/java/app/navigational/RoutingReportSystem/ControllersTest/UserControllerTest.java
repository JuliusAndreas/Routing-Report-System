package app.navigational.RoutingReportSystem.ControllersTest;

import app.navigational.RoutingReportSystem.Controllers.UserController;
import app.navigational.RoutingReportSystem.DTOs.UserDTO;
import app.navigational.RoutingReportSystem.Services.JwtUserDetailsService;
import app.navigational.RoutingReportSystem.Services.UserService;
import app.navigational.RoutingReportSystem.Utilities.JwtTokenUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WithMockUser(username = "user", password = "user", roles = "ADMIN")
public class UserControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService service;

    @MockBean
    private JwtTokenUtil jwtTokenUtil;

    @MockBean
    private JwtUserDetailsService jwtUserDetailsService;

    @Test
    public void fetchUserByIdTest() throws Exception {
        UserDTO userDTO = new UserDTO(1, "TestUser");
        given(service.getUserById(1)).willReturn(userDTO);
        mvc.perform(get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(userDTO.getUsername())));
    }

    @Test
    public void fetchUsersTest() throws Exception {
        UserDTO userDTO = new UserDTO(1, "TestUser");
        List<UserDTO> userDTOList = Arrays.asList(userDTO);
        given(service.getAllUsers()).willReturn(userDTOList);
        mvc.perform(get("/users/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is(userDTO.getUsername())));
    }


}
