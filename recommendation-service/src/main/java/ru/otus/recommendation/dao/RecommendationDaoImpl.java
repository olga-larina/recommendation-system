package ru.otus.recommendation.dao;

import org.springframework.data.neo4j.core.Neo4jClient;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class RecommendationDaoImpl implements RecommendationDao {

    private final Neo4jClient neo4jClient;

    public RecommendationDaoImpl(Neo4jClient neo4jClient) {
        this.neo4jClient = neo4jClient;
    }

    @Override
    public Long matchProductAndPersonWithScore(String productCode, String clientId, long score) {
        return neo4jClient.query("""
                MATCH (prod:Product),(pers:Person)
                  WHERE prod.code = $productCode AND pers.clientId = $clientId
                MERGE (pers)-[l:LIKES]->(prod)
                  ON CREATE SET l.score = $score
                  ON MATCH SET l.score = l.score + $score
                RETURN l.score
                """)
            .bind(productCode).to("productCode")
            .bind(clientId).to("clientId")
            .bind(score).to("score")
            .fetchAs(Long.class)
            .mappedBy((ts, r) -> r.get(0).asLong())
            .one().orElse(0L);
    }

    @Override
    public Collection<String> findMatchesByProductPersonScore(String productCode, String clientId, long minScoreForProduct, long minScoreForOtherProducts) {
        return neo4jClient.query("""
                MATCH (me:Person)-[meLike:LIKES]->(myProd:Product)<-[l:LIKES]-(otherPerson:Person)-[otherLike:LIKES]->(otherProd:Product)
                  WHERE me.clientId = $clientId AND myProd.code = $productCode AND l.score >= $minScoreForProduct AND otherLike.score >= $minScoreForOthers
                RETURN otherProd.code
                ORDER BY otherLike.score DESC
                """)
            .bind(productCode).to("productCode")
            .bind(clientId).to("clientId")
            .bind(minScoreForProduct).to("minScoreForProduct")
            .bind(minScoreForOtherProducts).to("minScoreForOthers")
            .fetchAs(String.class)
            .mappedBy((ts, r) -> r.get(0).asString())
            .all();
    }
}
