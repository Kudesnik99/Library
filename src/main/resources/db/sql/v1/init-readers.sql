-- DROP TABLE public.readers;

CREATE SEQUENCE public.seq_reader START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE TABLE public.reader (
                              reader_id int4 NOT NULL,
                              first_name varchar NULL,
                              last_name varchar NULL,
                              CONSTRAINT readers_pk PRIMARY KEY (reader_id)
);

INSERT INTO public.reader (reader_id, first_name, last_name)
VALUES(nextval('seq_reader'), 'Алексей', 'Ларистов'),
      (nextval('seq_reader'), 'Ольга', 'Афанасьева'),
      (nextval('seq_reader'), 'Константин', 'Федотов'),
      (nextval('seq_reader'), 'Катя', 'Собачкина');