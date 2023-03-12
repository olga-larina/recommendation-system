--changeset olga:2023-02-24-001-oauth2-authorization
-- https://github.com/spring-projects/spring-authorization-server/blob/81e05c60fc5179bc32bd3b05e94582f9098309fc/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-schema.sql
-- If using PostgreSQL, update ALL columns defined with 'blob' to 'text', as PostgreSQL does not support the 'blob' data type.
CREATE TABLE oauth2_authorization (
    id varchar(100) NOT NULL,
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorization_grant_type varchar(100) NOT NULL,
    authorized_scopes varchar(1000) DEFAULT NULL,
    attributes blob DEFAULT NULL,
    state varchar(500) DEFAULT NULL,
    authorization_code_value blob DEFAULT NULL,
    authorization_code_issued_at timestamp DEFAULT NULL,
    authorization_code_expires_at timestamp DEFAULT NULL,
    authorization_code_metadata blob DEFAULT NULL,
    access_token_value blob DEFAULT NULL,
    access_token_issued_at timestamp DEFAULT NULL,
    access_token_expires_at timestamp DEFAULT NULL,
    access_token_metadata blob DEFAULT NULL,
    access_token_type varchar(100) DEFAULT NULL,
    access_token_scopes varchar(1000) DEFAULT NULL,
    oidc_id_token_value blob DEFAULT NULL,
    oidc_id_token_issued_at timestamp DEFAULT NULL,
    oidc_id_token_expires_at timestamp DEFAULT NULL,
    oidc_id_token_metadata blob DEFAULT NULL,
    refresh_token_value blob DEFAULT NULL,
    refresh_token_issued_at timestamp DEFAULT NULL,
    refresh_token_expires_at timestamp DEFAULT NULL,
    refresh_token_metadata blob DEFAULT NULL,
    PRIMARY KEY (id)
);

--changeset olga:2023-02-24-002-oauth2-registered-client
-- https://github.com/spring-projects/spring-authorization-server/blob/1ae4f7aa13e3ace33fc6fa484f3656782f00b310/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/client/oauth2-registered-client-schema.sql
CREATE TABLE oauth2_registered_client (
    id varchar(100) NOT NULL,
    client_id varchar(100) NOT NULL,
    client_id_issued_at timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL,
    client_secret varchar(200) DEFAULT NULL,
    client_secret_expires_at timestamp DEFAULT NULL,
    client_name varchar(200) NOT NULL,
    client_authentication_methods varchar(1000) NOT NULL,
    authorization_grant_types varchar(1000) NOT NULL,
    redirect_uris varchar(1000) DEFAULT NULL,
    scopes varchar(1000) NOT NULL,
    client_settings varchar(2000) NOT NULL,
    token_settings varchar(2000) NOT NULL,
    PRIMARY KEY (id)
);

--changeset olga:2023-02-24-003-oauth2-authorization-consent
-- https://github.com/spring-projects/spring-authorization-server/blob/81e05c60fc5179bc32bd3b05e94582f9098309fc/oauth2-authorization-server/src/main/resources/org/springframework/security/oauth2/server/authorization/oauth2-authorization-consent-schema.sql
CREATE TABLE oauth2_authorization_consent (
    registered_client_id varchar(100) NOT NULL,
    principal_name varchar(200) NOT NULL,
    authorities varchar(1000) NOT NULL,
    PRIMARY KEY (registered_client_id, principal_name)
);

--changeset olga:2023-02-24-004-user-info
create table user_info (
    id          bigserial,
    username    varchar(255),
    password    varchar(255),
    constraint user_info_pkey primary key (id),
    constraint user_info_username_unq unique (username)
);

--changeset olga:2023-02-24-004-user-role
create table user_role (
    id          bigserial,
    user_id     bigint,
    authority   varchar(255),
    constraint user_role_pkey primary key (id),
    constraint user_role_user_id_fkey foreign key (user_id) references user_info (id) on delete cascade,
    constraint user_role_unq unique (user_id, authority)
);

--changeset olga:2023-02-24-005-user-claim
create table user_claim (
    id          bigserial,
    username    varchar(255),
    client_id   varchar(255),
    constraint user_claim_pkey primary key (id),
    constraint user_claim_username_unq unique (username),
    constraint user_claim_client_id_unq unique (client_id),
    constraint user_claim_username_fkey foreign key (username) references user_info (username) on delete cascade
);
