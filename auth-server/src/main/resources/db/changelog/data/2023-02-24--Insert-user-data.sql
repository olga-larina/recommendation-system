--liquibase formatted sql

--changeset olga:2023-02-24-001-user-data
insert into user_info (username, password)
values ('user1', '$2a$10$3DHpnBxoYCmcUl2DtT8wDuMx3AeOZeqcnwsNCHGH40afWo6n.lT2q'), -- password
       ('user2', '$2a$10$3DHpnBxoYCmcUl2DtT8wDuMx3AeOZeqcnwsNCHGH40afWo6n.lT2q'), -- password
       ('user3', '$2a$10$3DHpnBxoYCmcUl2DtT8wDuMx3AeOZeqcnwsNCHGH40afWo6n.lT2q'), -- password
       ('user4', '$2a$10$3DHpnBxoYCmcUl2DtT8wDuMx3AeOZeqcnwsNCHGH40afWo6n.lT2q') -- password
;

--changeset olga:2023-02-24-002-user-role-data
insert into user_role (user_id, authority)
values (1, 'RECOMMENDATION_USER'),
       (1, 'PRODUCT_USER'),
       (1, 'SHOP_USER'),
       (2, 'RECOMMENDATION_ADMIN'),
       (2, 'PRODUCT_ADMIN'),
       (2, 'SHOP_ADMIN'),
       (3, 'RECOMMENDATION_USER'),
       (3, 'PRODUCT_USER'),
       (3, 'SHOP_USER'),
       (4, 'RECOMMENDATION_ADMIN'),
       (4, 'PRODUCT_ADMIN'),
       (4, 'SHOP_ADMIN')
;

--changeset olga:2023-02-24-003-user-claim
insert into user_claim (username, client_id)
values ('user1', 'USER1_ID'),
       ('user2', 'USER2_ID'),
       ('user3', 'USER3_ID'),
       ('user4', 'USER4_ID')
;

