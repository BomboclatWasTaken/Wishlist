package dk.ek.wishlist.controller;

import dk.ek.wishlist.exception.WishNotFoundException;
import dk.ek.wishlist.exception.WishlistNotFoundException;
import dk.ek.wishlist.model.User;
import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(WishController.class)
class WishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private WishService wishService;

    @MockitoBean
    private WishlistService wishlistService;

    @Test
    void showNewForm_whenNotLoggedIn_redirectsToLogin() throws Exception {

        mockMvc.perform(get("/wishlists/{listId}/wishes/new", 1))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        verify(wishlistService, never()).getSpecificList(anyInt(), anyInt());
    }


    @Test
    void showNewForm_whenListNotOwnedByUser_redirectsToWishlists() throws Exception {

        User user = new User(1, "Sebastian", "sebastian@IkkeSej.dk", "1234");

        when(wishlistService.getSpecificList(5, 1)).thenThrow(new WishlistNotFoundException(5));


        mockMvc.perform(get("/wishlists/{listId}/wishes/new",5)

                        .sessionAttr("user", user))

                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/wishlists"));

        verify(wishlistService).getSpecificList(5,1);
    }

    @Test
    void addWish_whenLoggedIn_savesWishWithListIdFromUrlAndRedirects() throws Exception {

        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        mockMvc.perform(post("/wishlists/{listId}/wishes", 5)
                .sessionAttr("user", user)
                .param("name", "Headset")
                .param("Description", "Razer Headset")
                .param("price", "2000")
                .param("wishlistId", "99"))
                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/wishlists/5"));
    }

    @Test
    void showEditForm_whenWishFound_redirectsToList() throws Exception {
        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        when(wishService.getWish(10, 1)).thenThrow(new WishNotFoundException(10));

        mockMvc.perform(get("/wishlists/{listId}/wishes/{wishId}/edit", 5, 10)
                .sessionAttr("user",user))
                        .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/wishlists/5"));
        verify(wishService).getWish(10,1);
    }

    @Test
    void updateWish_whenLoggedIn_updatesWishWithIdsFromUrlAndRedirects() throws Exception {
        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        mockMvc.perform(post("/wishlists/{listId}/wishes/{wishId}/edit", 5, 10)
                        .sessionAttr("user", user)
                .param("name","Nyt headset")
                .param("price","2500")
                .param("id","77")
                .param("wishlistId","99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishlists/5"));

        ArgumentCaptor<Wish> captor = ArgumentCaptor.forClass(Wish.class);

        verify(wishService).updateWish(captor.capture(), eq(1));

        Wish updatedWish = captor.getValue();

        assertEquals("Nyt headset", updatedWish.getName());

        assertEquals(10,updatedWish.getId());

        assertEquals(5, updatedWish.getWishlistId());


    }

    @Test
    void deleteWish() {
    }
}