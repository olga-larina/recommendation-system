package ru.otus.shop.controller;

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
import ru.otus.common.dto.ShopActionDto;
import ru.otus.common.dto.ShopRequestDto;
import ru.otus.shop.config.SecurityConfig;
import ru.otus.shop.service.ShopService;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ShopController.class)
@Import(SecurityConfig.class)
class ShopControllerTest {

    @MockBean
    private ShopService shopService;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private JwtDecoder jwtDecoder;

    @Test
    void shouldShopIfAuthorized() throws Exception {
        ShopRequestDto request = new ShopRequestDto(ShopActionDto.VIEW, "123");
        String clientId = "1234";

        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", clientId)
            .claim("user_authorities", "ROLE_SHOP_USER")
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        String token = "Bearer " + jwt.getTokenValue();

        when(shopService.process(eq(clientId), eq(request.getProductCode()), eq(request.getAction()))).thenReturn(true);

        mvc.perform(post("/shop")
                .with(jwt().jwt(jwt))
                .content(mapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().string("true"));
    }

    @Test
    void shouldThrowShopIfNotAuthorized() throws Exception {
        ShopRequestDto request = new ShopRequestDto(ShopActionDto.VIEW, "123");
        String clientId = "1234";

        Jwt jwt = Jwt.withTokenValue("token")
            .header("alg", "none")
            .claim("client_id", clientId)
            .issuer("http://localhost:9000")
            .build();
        when(jwtDecoder.decode(anyString())).thenReturn(jwt);

        String token = "Bearer " + jwt.getTokenValue();

        mvc.perform(post("/shop")
                .with(jwt().jwt(jwt))
                .content(mapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, token)
                .header(HttpHeaders.CONTENT_TYPE, "application/json"))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}