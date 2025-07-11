package guru.springframework.controllers;

import guru.springframework.config.WebConfig;
import guru.springframework.domain.Recipe;
import guru.springframework.services.RecipeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.server.RouterFunction;
import reactor.core.publisher.Flux;

import static org.mockito.Mockito.when;

public class RouterFunctionTest {

    WebTestClient webTestClient;

    @Mock
    RecipeService recipeService;

    AutoCloseable closeable;

    @BeforeEach
    public void setUp() throws Exception {
        closeable = MockitoAnnotations.openMocks(this);

        WebConfig webConfig = new WebConfig();
        RouterFunction<?> routerFunction = webConfig.routes(recipeService);
        webTestClient = WebTestClient.bindToRouterFunction(routerFunction).build();
    }

    @AfterEach
    public void tearDown() throws Exception {
        closeable.close();
    }

    public void testGetRecipes() {

        when(recipeService.getRecipes()).thenReturn(Flux.just());

        webTestClient.get().uri("/api/recipes")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk();
    }

    public void testGetRecipesWithData() {

        when(recipeService.getRecipes()).thenReturn(Flux.just(new Recipe(), new Recipe()));

        webTestClient.get().uri("/api/recipes")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(Recipe.class);
    }


}
