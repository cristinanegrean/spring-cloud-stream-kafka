DROP TABLE IF EXISTS schema_version;
DROP TABLE IF EXISTS dress_image;
DROP TABLE IF EXISTS rating;
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
  id             TEXT      NOT NULL PRIMARY KEY,
  status         TEXT      NOT NULL,
  brand          SERIAL    NOT NULL REFERENCES brand (id) ON DELETE CASCADE,
  average_rating SMALLINT  NOT NULL DEFAULT 0,
  name           TEXT,
  color          TEXT,
  season         TEXT,
  price          NUMERIC,
  created_at     TIMESTAMP NOT NULL DEFAULT now(),
  modified_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE dress_image (
  dress_id TEXT REFERENCES dress (id) ON DELETE CASCADE,
  thumb_url TEXT
);

CREATE TABLE rating (
  id          TEXT      NOT NULL PRIMARY KEY,
  dress_id    TEXT      NOT NULL,
  stars       SMALLINT  NOT NULL,
  event_time  TIMESTAMP NOT NULL,
  created_at  TIMESTAMP NOT NULL DEFAULT now(),
  modified_at TIMESTAMP NOT NULL DEFAULT now(),
  UNIQUE (id, dress_id)
);