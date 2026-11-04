package dk.ek.wishlist.controller;

import dk.ek.wishlist.model.User;
import dk.ek.wishlist.model.Wish;
import dk.ek.wishlist.service.WishService;
import dk.ek.wishlist.service.WishlistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/// Fortæller Spring at kun web-laget for WishController skal startes op til denne test,
/// i stedet for hele applikationen. Gør testen hurtigere.
@WebMvcTest(WishController.class)
class WishControllerTest {

    /// MockMvc bliver injected af Spring og bruges til at simulere HTTP requests (GET, POST osv.)
    /// uden at der rent faktisk skal startes en server op.
    @Autowired
    private MockMvc mockMvc;

    /// MockitoBean erstatter den rigtige WishService med en simuleret (mocked) version,
    /// så testen ikke er afhængig af den rigtige logik eller databasen.
    @MockitoBean
    private WishService wishService;

    /// Samme som ovenfor, men for WishlistService. WishController kræver begge i konstruktøren,
    /// så Spring skal have begge, ellers kan controlleren ikke oprettes.
    @MockitoBean
    private WishlistService wishlistService;

    /// Test der tjekker at GET /wishlists/{listId}/wishes/new sender brugeren til /login,
    /// hvis ingen er logget ind.
    @Test
    void showNewForm_whenNotLoggedIn_redirectsToLogin() throws Exception {

        /// Simulerer en GET request til /wishlists/1/wishes/new. {listId} bliver erstattet af 1.
        mockMvc.perform(get("/wishlists/{listId}/wishes/new", 1))
                /// Tjekker at HTTP status er 3xx (redirect).
                .andExpect(status().is3xxRedirection())
                /// Tjekker at brugeren bliver sendt videre til /login.
                .andExpect(redirectedUrl("/login"));

        /// Bekræfter at getSpecificList(...) aldrig blev kaldt. Controlleren skal stoppe ved,
        /// login-tjekket og ikke kalde servicen. anyInt() bruges til at sige "uanset hvilke tal".
        verify(wishlistService, never()).getSpecificList(anyInt(), anyInt());
    }


    /// Test der tjekker at en logget ind bruger sendes til /wishlists,
    /// hvis listen ikke findes eller tilhører en anden bruger.
    @Test
    void showNewForm_whenListNotOwnedByUser_redirectsToWishlists() throws Exception {

        /// Opretter en bruger (id 1), der lægges i sessionen, så controlleren ser brugeren som logget ind.
        User user = new User(1, "Sebastian", "sebastian@IkkeSej.dk", "1234");

        /// Fortæller mock'en at når getSpecificList(5, 1) bliver kaldt (liste 5, bruger 1),
        /// skal den returnere null. null betyder "listen findes ikke eller tilhører en anden bruger".
        when(wishlistService.getSpecificList(5,1)).thenReturn(null);

        /// Simulerer en GET request til /wishlists/5/wishes/new.

        mockMvc.perform(get("/wishlists/{listId}/wishes/new",5)

                        /// Lægger brugeren i sessionen under nøglen "user".
                        /// Nøglen skal være identisk med SESSION_USER i controlleren.
                        .sessionAttr("user", user))

                /// Tjekker at HTTP status er 3xx (redirect).
                .andExpect(status().is3xxRedirection())

                /// Tjekker at brugeren bliver sendt videre til /wishlists.
                .andExpect(redirectedUrl("/wishlists"));

        /// Bekræfter at controlleren kaldte getSpecificList(5, 1) på wishlistService.
        verify(wishlistService).getSpecificList(5,1);
    }

    /// Test der tjekker at POST /wishlists/{listId}/wishes gemmer ønsket
    /// og sender brugeren videre til listen (redirect).
    @Test
    void addWish_whenLoggedIn_savesWishWithListIdFromUrlAndRedirects() throws Exception {

        /// Opretter en bruger, der lægges i sessionen, så controlleren ser brugeren som logget ind.

        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        /// Simulerer en POST request til /wishlists/5/wishes med formulardata.

        mockMvc.perform(post("/wishlists/{listId}/wishes", 5)
                        /// Lægger brugeren i sessionen under nøglen "user".
                .sessionAttr("user", user)

                        /// Formulardata. Hver .param(...) bliver bundet til det felt i Wish-objektet,
                        /// som har samme navn (name, description, price).
                .param("name", "Headset")
                .param("Description", "Razer Headset")
                .param("price", "2000")

                        /// Formularen siger liste 99, men URL'en siger liste 5.
                        /// Controlleren skal ignorere 99 og bruge 5 fra URL'en.
                .param("wishlistId", "99"))

                /// Tjekker at HTTP status er 3xx (redirect).

                .andExpect(status().is3xxRedirection())

                /// Tjekker at brugeren bliver sendt videre til listen.
                /// OBS: Der skal være en skråstreg foran: "/wishlists/5".
                .andExpect(redirectedUrl("wishlists/5"));
    }

    @Test
    void showEditForm_whenWishFound_redirectsToList() throws Exception {
        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        when(wishService.getWish(10,1)).thenReturn(null);

        mockMvc.perform(get("/wishlists/{listId}/wishes/{wishId}/edit", 5, 10)
                .sessionAttr("user",user))
                        .andExpect(status().is3xxRedirection())
                                .andExpect(redirectedUrl("/wishlists/5"));
        verify(wishService).getWish(10,1);
    }

    @Test
    void updateWish_whenLoggedIn_updatesWishWithIdsFromUrlAndRedirects() throws Exception {


    }

    @Test
    void deleteWish() {
    }
}