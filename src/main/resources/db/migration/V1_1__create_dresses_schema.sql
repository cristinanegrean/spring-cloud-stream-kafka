DROP TABLE IF EXISTS schema_version;
DROP TABLE IF EXISTS dress_image;
DROP TABLE IF EXISTS dress_rating;
DROP TABLE IF EXISTS dress;
DROP TABLE IF EXISTS brand;

CREATE TABLE brand (
  id          SERIAL    NOT NULL PRIMARY KEY,
  name        TEXT      NOT NULL UNIQUE,
  logo_url    TEXT,
  created_at  TIMESTAMP NOT NULL DEFAULT now(),
  modified_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE dress (
  id             SERIAL    NOT NULL PRIMARY KEY,
  --dress_id       TEXT,
  --status         TEXT,
  brand          SERIAL REFERENCES brand (id) ON DELETE CASCADE,
  average_rating SMALLINT,
  name           TEXT,
  color          TEXT,
  season         TEXT,
  price          NUMERIC,
  created_at     TIMESTAMP NOT NULL DEFAULT now(),
  modified_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE dress_image (
  dress_id  SERIAL REFERENCES dress (id) ON DELETE CASCADE,
  image TEXT NOT NULL
);

CREATE TABLE dress_rating (
  dress_id  SERIAL NOT NULL REFERENCES dress (id) ON DELETE CASCADE,
  rating_id TEXT NOT NULL,
  stars     SMALLINT,
  PRIMARY KEY (dress_id, rating_id)
);