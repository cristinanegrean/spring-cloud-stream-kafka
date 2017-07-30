DROP TABLE IF EXISTS schema_version;
DROP TABLE IF EXISTS dress_image;
DROP TABLE IF EXISTS dress_rating;
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
  brand          SERIAL REFERENCES brand (uid) ON DELETE CASCADE,
  average_rating SMALLINT  NOT NULL DEFAULT 0,
  name           TEXT,
  color          TEXT,
  season         TEXT,
  price          NUMERIC,
  created_at     TIMESTAMP NOT NULL DEFAULT now(),
  modified_at    TIMESTAMP NOT NULL DEFAULT now()
);

CREATE TABLE dress_image_thumb (
  dress_uid SERIAL REFERENCES dress (uid) ON DELETE CASCADE,
  thumb_url TEXT
);

-- ratings are not updated anymore, so no need to support lookup based on rating message rating id, splitting data
-- for ease of aggregate/count calculations
CREATE TABLE dress_rating_stars (
  dress_uid TEXT     NOT NULL REFERENCES dress (id) ON DELETE CASCADE,
  stars     SMALLINT NOT NULL
);

-- for trending dresses, as of most often rated within a time window, store the rating message event time
CREATE TABLE dress_rating_event_time (
  dress_uid  TEXT      NOT NULL REFERENCES dress (id) ON DELETE CASCADE,
  event_time TIMESTAMP NOT NULL
);