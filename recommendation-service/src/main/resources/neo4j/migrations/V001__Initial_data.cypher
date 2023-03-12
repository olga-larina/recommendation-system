// MATCH p=()-[r:MIGRATED_TO]->() DETACH DELETE p; // удалить миграции

MATCH (n)
DETACH DELETE n;

CREATE
  (p1:Person { clientId: "USER1_ID" }),
  (p2:Person { clientId: "USER2_ID" }),
  (p3:Person { clientId: "USER3_ID" }),
  (p4:Person { clientId: "USER4_ID" });

CREATE
  (p01:Product { code: "100" }),
  (p02:Product { code: "150" }),
  (p03:Product { code: "200" }),
  (p04:Product { code: "250" }),
  (p05:Product { code: "300" }),
  (p06:Product { code: "350" }),
  (p07:Product { code: "400" }),
  (p08:Product { code: "450" }),
  (p09:Product { code: "500" }),
  (p10:Product { code: "550" }),
  (p11:Product { code: "600" }),
  (p12:Product { code: "650" });

// Создание связи между персоной USER2_ID и продуктами; присвоение баллов

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "500" AND pers.clientId = "USER2_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 10
  ON MATCH SET l.score = l.score + 10
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "550" AND pers.clientId = "USER2_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 10
  ON MATCH SET l.score = l.score + 10
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "600" AND pers.clientId = "USER2_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 50
  ON MATCH SET l.score = l.score + 50
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "650" AND pers.clientId = "USER2_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 100
  ON MATCH SET l.score = l.score + 100
RETURN 0;

// Создание связи между персоной USER3_ID и продуктами; присвоение баллов

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "100" AND pers.clientId = "USER3_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 10
  ON MATCH SET l.score = l.score + 10
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "150" AND pers.clientId = "USER3_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 10
  ON MATCH SET l.score = l.score + 10
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "200" AND pers.clientId = "USER3_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 50
  ON MATCH SET l.score = l.score + 50
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "250" AND pers.clientId = "USER3_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 50
  ON MATCH SET l.score = l.score + 50
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "300" AND pers.clientId = "USER3_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 100
  ON MATCH SET l.score = l.score + 100
RETURN 0;

// Создание связи между персоной USER4_ID и продуктами; присвоение баллов

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "350" AND pers.clientId = "USER4_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 10
  ON MATCH SET l.score = l.score + 10
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "400" AND pers.clientId = "USER4_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 50
  ON MATCH SET l.score = l.score + 50
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "450" AND pers.clientId = "USER4_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 50
  ON MATCH SET l.score = l.score + 50
RETURN 0;

MATCH (prod:Product),(pers:Person)
  WHERE prod.code = "500" AND pers.clientId = "USER4_ID"
MERGE (pers)-[l:LIKES]->(prod)
  ON CREATE SET l.score = 100
  ON MATCH SET l.score = l.score + 100
RETURN 0;

