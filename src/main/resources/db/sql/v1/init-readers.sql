-- DROP TABLE public.readers;

CREATE SEQUENCE public.seq_reader START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE TABLE public.reader (
                              reader_id int4 NOT NULL,
                              first_name varchar NULL,
                              last_name varchar NULL,
                              CONSTRAINT readers_pk PRIMARY KEY (reader_id)
);