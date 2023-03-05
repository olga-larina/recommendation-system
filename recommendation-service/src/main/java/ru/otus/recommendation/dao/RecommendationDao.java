package ru.otus.recommendation.dao;

import java.util.Collection;

public interface RecommendationDao {

    /**
     * Создать связь между продуктом и клиентом и увеличить у неё балл
     * @param productCode код продукта
     * @param clientId id клиента
     * @param score балл
     * @return новый балл
     */
    Long matchProductAndPersonWithScore(String productCode, String clientId, long score);

    /**
     * Найти соответствия продуктов.
     * Сначала отбираются клиенты, у которых есть связь с данным продуктом и её балл не ниже minScoreForProduct.
     * Затем у этих клиентов отбираются продукты с баллом не ниже minScoreForOtherProducts
     * @param productCode код продукта
     * @param clientId id клиента
     * @param minScoreForProduct минимальный балл у данного продукта у остальных клиентов
     * @param minScoreForOtherProducts минимальный балл у других продуктов у остальных клиентов
     * @return список кодов продуктов - рекомендаций
     */
    Collection<String> findMatchesByProductPersonScore(String productCode, String clientId, long minScoreForProduct, long minScoreForOtherProducts);
}
