package ru.otus.recommendation.dao;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.neo4j.harness.Neo4j;
import org.neo4j.harness.Neo4jBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.neo4j.DataNeo4jTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DataNeo4jTest
@DirtiesContext
@Import(RecommendationDaoImpl.class)
class RecommendationDaoImplTest {

    private static Neo4j embeddedDatabaseServer;

    @Autowired
    private Neo4jClient neo4jClient;

    @Autowired
    private RecommendationDao recommendationDao;

    @BeforeAll
    static void setUp() {
        embeddedDatabaseServer = Neo4jBuilders.newInProcessBuilder()
            .withDisabledServer() // disable http server
            .build();
    }

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.neo4j.uri", embeddedDatabaseServer::boltURI);
        registry.add("spring.neo4j.authentication.username", () -> "neo4j");
        registry.add("spring.neo4j.authentication.password", () -> null);
    }

    @AfterAll
    static void tearDown() {
        embeddedDatabaseServer.close();
    }

    @Test
    void shouldCorrectMatchProductAndPersonWithScore() {
        neo4jClient.query("""
            CREATE (p1:Person { clientId: $clientId1 }),
                   (p2:Person { clientId: $clientId2 }),
                   (p3:Person { clientId: $clientId3 })
            """)
            .bind("USER1_ID").to("clientId1")
            .bind("USER2_ID").to("clientId2")
            .bind("USER3_ID").to("clientId3")
            .run();
        neo4jClient.query("""
                CREATE (p01:Product { code: $productCode1 }),
                       (p02:Product { code: $productCode2 }),
                       (p03:Product { code: $productCode3 }),
                       (p04:Product { code: $productCode4 })
                """)
            .bind("100").to("productCode1")
            .bind("150").to("productCode2")
            .bind("200").to("productCode3")
            .bind("250").to("productCode4")
            .run();

        assertThat(recommendationDao.matchProductAndPersonWithScore("100", "USER1_ID", 100)).isEqualTo(100);
        assertThat(recommendationDao.matchProductAndPersonWithScore("100", "USER2_ID", 100)).isEqualTo(100);
        assertThat(recommendationDao.matchProductAndPersonWithScore("150", "USER2_ID", 10)).isEqualTo(10);
        assertThat(recommendationDao.matchProductAndPersonWithScore("200", "USER2_ID", 50)).isEqualTo(50);
        assertThat(recommendationDao.matchProductAndPersonWithScore("100", "USER3_ID", 10)).isEqualTo(10);
        assertThat(recommendationDao.matchProductAndPersonWithScore("250", "USER3_ID", 50)).isEqualTo(50);
        assertThat(recommendationDao.findMatchesByProductPersonScore("100", "USER1_ID", 50, 50)).containsOnly("200");
        assertThat(recommendationDao.findMatchesByProductPersonScore("100", "USER1_ID", 110, 50)).isEmpty();
        assertThat(recommendationDao.findMatchesByProductPersonScore("100", "USER1_ID", 50, 110)).isEmpty();
        assertThat(recommendationDao.findMatchesByProductPersonScore("100", "USER1_ID", 10, 10)).containsOnly("150", "200", "250");
    }
}