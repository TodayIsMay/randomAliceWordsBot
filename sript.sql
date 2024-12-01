CREATE TABLE IF NOT EXISTS words
(id SERIAL PRIMARY KEY,
words CHARACTER VARYING (100));

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