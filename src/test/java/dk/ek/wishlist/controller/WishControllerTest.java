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

//Tester kun WishController, sådan at resten af appen ikke startes, så testen køre hurtigere.
@WebMvcTest(WishController.class)
class WishControllerTest {

    //MockMvc laver "falske" HTTP-requests til controlleren sådan at vi kan køre tests uden at serveren skal startes op.
    @Autowired
    private MockMvc mockMvc;

    // Laver en falsk WishService, som kan hjælpe med at teste controlleren
    @MockitoBean
    private WishService wishService;

    // Falsk wishlist til test
    @MockitoBean
    private WishlistService wishlistService;

    // Tester at en bruger som ikke er logget ind, skal redirecte til en loginside.
    @Test
    void showNewForm_whenNotLoggedIn_redirectsToLogin() throws Exception {

        // Sender en GET-request ude at have user med i url, for at teste når de ikke er logget ind
        mockMvc.perform(get("/wishlists/{listId}/wishes/new", 1))
                // Der forvente en redirect til loginsiden.
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        //Service bliver ikke kaldt fordi den stopper når man ikke kan logge ind
        verify(wishlistService, never()).getSpecificList(anyInt(), anyInt());
    }


    // Tester hvis listen ikke findes eller tilhører en anden bruger.
    @Test
    void showNewForm_whenListNotOwnedByUser_redirectsToWishlists() throws Exception {

        //Opretter en bruger.
        User user = new User(1, "Sebastian", "sebastian@IkkeSej.dk", "1234");

        //Service kaster en exception, når den ikke kan finde ListId 5 på bruger id 1.
        when(wishlistService.getSpecificList(5, 1)).thenThrow(new WishlistNotFoundException(5));


        //Sender GET-request med en bruger
        mockMvc.perform(get("/wishlists/{listId}/wishes/new", 5)
                //Lægger brugeren med og ligger "user" som nøgle og user som værdi, det gør at det ligner brugeren er logget ind i testen.
                        .sessionAttr("user", user))

                .andExpect(status().is3xxRedirection())

                .andExpect(redirectedUrl("/wishlists"));

        //Tjekker så om servicen vil blive kaldt med de rigtige id'er.
        verify(wishlistService).getSpecificList(5, 1);
    }

    //Tester at når ønske gemmes, bruger den listId fra URL'en og ikke fra formularen
    @Test
    void addWish_whenLoggedIn_savesWishWithListIdFromUrlAndRedirects() throws Exception {

        //Opretter en bruger
        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        //Sender POST-request
        mockMvc.perform(post("/wishlists/{listId}/wishes", 5)
                        .sessionAttr("user", user)
                //Formularens felter sendes med som parametre
                        .param("name", "Headset")
                        .param("Description", "Razer Headset")
                        .param("price", "2000")
                //Tester med forkert id, for at se om testen bruger "5" fra url'en
                        .param("wishlistId", "99"))
                .andExpect(status().is3xxRedirection())

                //Redirecter til den liste ønsket blev lagt på (5, og ikke 99)
                .andExpect(redirectedUrl("/wishlists/5"));
    }

    // Hvis ønsker ikke findes, sendes brugeren tilbage til listen
    @Test
    void showEditForm_whenWishNNotFound_redirectsToList() throws Exception {

        //Opretter bruger
        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");

        //Falsk service kaster exception, fx at wishId 10 ikke findes eller ikke tilhører userId 1
        when(wishService.getWish(10, 1)).thenThrow(new WishNotFoundException(10));


        // Sender GET-request til redigeringssiden for ønske 10 på liste 5
        mockMvc.perform(get("/wishlists/{listId}/wishes/{wishId}/edit", 5, 10)
                        .sessionAttr("user", user))

                //Controlleren fanger exceptionen og redirecter til listen.
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishlists/5"));

        // Tjekker at servicen blev kaldt med de rigtige id'er
        verify(wishService).getWish(10, 1);
    }

    // Når et ønske opdateres, bruges id'erne fra URL'en og ikke fra formularen
    @Test
    void updateWish_whenLoggedIn_updatesWishWithIdsFromUrlAndRedirects() throws Exception {

        //Opretter bruger
        User user = new User(1, "Sebastian", "Sebastian@IkkeSej.dk", "1234");


        //Sender POST-request med de ændrede værdier fra formularen
        mockMvc.perform(post("/wishlists/{listId}/wishes/{wishId}/edit", 5, 10)
                        .sessionAttr("user", user)
                        .param("name", "Nyt headset")
                        .param("price", "2500")

                //Kaster forkerte id'er for at teste den retter det
                        .param("id", "77")
                        .param("wishlistId", "99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/wishlists/5"));


        //ArgumentCaptor fanger det Wsih-object controlleren har sendt videre til servicen.
        ArgumentCaptor<Wish> captor = ArgumentCaptor.forClass(Wish.class);

        //Tjekker at updateWish bliver kaldt, og fanger Wish-objektet (eq(1) = bruger-id'et skal være 1)
        verify(wishService).updateWish(captor.capture(), eq(1));


        //Henter det fangede Wish-objekt, så tjekkes værdierne
        Wish updatedWish = captor.getValue();

        //Navnet skal være det nye navn fra formularen
        assertEquals("Nyt headset", updatedWish.getName());

        //Id'et skal være 10 fra URL'en og ikke 77 fra formularen
        assertEquals(10, updatedWish.getId());

        //Samme som før, wishListId skal være 5 og ikke 99 fra formularen.
        assertEquals(5, updatedWish.getWishlistId());


    }
}