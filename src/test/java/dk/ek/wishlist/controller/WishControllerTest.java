
package dk.ek.wishlist.controller;

import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
@WebMvcTest(WishController.class)
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishService wishService;

    @Test
    void showNewForm_whenNotLoggedIn_redirectsToLogin() throws Exception {

        mockMvc.perform(get("wishLists/{listId}/wishes/new", 1)).andExpect(status().is3xxRedirection()).andExpect(redirectedUrl("/login"));
    }

    @Test
    void addWish() throws Exception {
    }

    @Test
    void showEditForm() {
    }

    @Test
    void updateWish() {
    }

    @Test
    void deleteWish() {
    }
}