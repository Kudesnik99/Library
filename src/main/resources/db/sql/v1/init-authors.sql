-- DROP TABLE public.authors;

CREATE SEQUENCE public.seq_author START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE TABLE public.author (
                                author_id int4 NOT NULL,
                                first_name varchar NULL,
                                last_name varchar NULL,
                                CONSTRAINT authors_pk PRIMARY KEY (author_id)
);

INSERT INTO public.author (author_id, first_name, last_name)
VALUES(nextval('seq_author'), 'Конан', 'Дойл'),
(nextval('seq_author'), 'Михаил', 'Булгаков'),
(nextval('seq_author'), 'Антон', 'Чехов'),
(nextval('seq_author'), 'Дэн', 'Браун');