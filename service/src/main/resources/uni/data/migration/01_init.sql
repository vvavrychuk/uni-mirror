BEGIN;

CREATE TABLE news
(
  id bigserial PRIMARY KEY,
  url text UNIQUE ,
  title text
);

ALTER SEQUENCE news_id_seq RESTART WITH 2000000000;

CREATE TABLE version(version int);
INSERT INTO version VALUES (1);

COMMIT;
