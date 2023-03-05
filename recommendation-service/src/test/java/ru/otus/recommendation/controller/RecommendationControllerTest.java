package ru.otus.recommendation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.common.dto.ProductCategoryDto;
import ru.otus.common.dto.ProductDto;
import ru.otus.common.dto.ProductManufacturerDto;
import ru.otus.common.dto.RecommendationDto;
import ru.otus.recommendation.config.SecurityConfig;
import ru.otus.recommendation.service.RecommendationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
@Import(SecurityConfig.class)
class RecommendationControllerTest {

    @MockBean
    private RecommendationService recommendationService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldGetRecommendationsIfAuthorized() throws Exception {
        List<ProductDto> products = List.of(
            new ProductDto("1", "test1", "100", new ProductManufacturerDto("11", "Test1 Test1"), List.of(new ProductCategoryDto("12", "Test cat1"))),
            new ProductDto("2", "test2", "200", new ProductManufacturerDto("21", "Test2 Test2"), List.of(new ProductCategoryDto("22", "Test cat2"))),
            new ProductDto("3", "test3", "300", new ProductManufacturerDto("31", "Test3 Test3"), List.of(new ProductCategoryDto("32", "Test cat3")))
        );
        RecommendationDto expected = new RecommendationDto("1234", products);

        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", "1234")
            .claim("user_authorities", "ROLE_RECOMMENDATION_USER")
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        String token = "Bearer " + jwt.getTokenValue();

        when(recommendationService.getRecommendation(eq("1234"), eq("123"), eq(token))).thenReturn(expected);

        mvc.perform(get("/recommendation?productCode=123")
                .with(jwt().jwt(jwt))
                .header(HttpHeaders.AUTHORIZATION, token))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().json(mapper.writeValueAsString(expected)));
    }

    @Test
    void shouldThrowGetRecommendationsIfNotAuthorized() throws Exception {
        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", "1234")
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        mvc.perform(get("/recommendation?productCode=123")
                .with(jwt().jwt(jwt))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.getTokenValue()))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}