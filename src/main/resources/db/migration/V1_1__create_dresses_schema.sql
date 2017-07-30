DROP TABLE IF EXISTS schema_version;
DROP TABLE IF EXISTS dress_image;
DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS dress;
DROP TABLE IF EXISTS brand;

CREATE TABLE brand (
  uid         SERIAL    NOT NULL PRIMARY KEY,
  name        TEXT      NOT NULL UNIQUE,
  logo_url    TEXT,
  created_at  TIMESTAMP NOT NULL DEFAULT now(),
  modified_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE dress (
  uid            SERIAL    NOT NULL PRIMARY KEY,
  id             TEXT      NOT NULL UNIQUE,
  status         TEXT      NOT NULL,
  brand          SERIAL REFERENCES brand (uid) ON DELETE CASCADE,
  average_rating SMALLINT  NOT NULL DEFAULT 0,
  name           TEXT,
  color          TEXT,
  season         TEXT,
  price          NUMERIC,
  created_at     TIMESTAMP NOT NULL DEFAULT now(),
  modified_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE dress_image (
  dress_uid SERIAL REFERENCES dress (uid) ON DELETE CASCADE,
  thumb_url TEXT
);

CREATE TABLE rating (
  uid         SERIAL    NOT NULL PRIMARY KEY,
  dress_id    TEXT      NOT NULL,
  rating_id   TEXT      NOT NULL,
  stars       SMALLINT  NOT NULL,
  event_time  TIMESTAMP NOT NULL,
  created_at  TIMESTAMP NOT NULL DEFAULT now(),
  modified_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE (dress_id, rating_id)
);