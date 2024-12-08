CREATE TABLE IF NOT EXISTS words
(id SERIAL PRIMARY KEY,
word CHARACTER VARYING (100));

CREATE TABLE IF NOT EXISTS translations
(id SERIAL PRIMARY KEY,
word_id INTEGER,
word_translation CHARACTER VARYING,
CONSTRAINT FK_TRANSLATIONS_ON_WORD_ID FOREIGN KEY (word_id) REFERENCES words (id));

create table if not exists themes
(id serial primary key,
name character varying (100)
);

alter table words add column theme_id integer;

alter table words add constraint FK_WORD_ON_THEME_ID foreign key (theme_id) references themes (id);

alter table words add column "episode_number" integer

alter table words add column "theme_type" integer

alter table themes add column "prefix" character varying (10)

alter table themes add column "type" character varying (50)

create table if not exists episodes
(id serial primary key,
number integer,
theme_id integer,
constraint FK_EPISODES_ON_THEME_ID foreign key (theme_id) references themes (id))