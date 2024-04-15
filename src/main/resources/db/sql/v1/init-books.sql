-- DROP TABLE public.books;

CREATE SEQUENCE public.seq_book START WITH 1 INCREMENT BY 1 NO CYCLE;
CREATE TABLE public.book (
                              book_id int4 NOT NULL,
                              title varchar NULL,
                              publication_date int NULL,
                              publisher varchar NULL,
                              isbn varchar NULL,
                              author_id int4 NULL,
                              reader_id int4 NULL,
                              CONSTRAINT books_pk PRIMARY KEY (book_id)
);


-- public.books внешние включи
ALTER TABLE public.book ADD CONSTRAINT books_author_fk FOREIGN KEY (author_id) REFERENCES public.author(author_id);

-- данные
INSERT INTO public.book (book_id, title, publication_date, publisher, isbn, author_id, reader_id)
VALUES(nextval('seq_book'), 'Знак черырёх', 1975, 'Детгиз', '12345678', 1, 1),
(nextval('seq_book'), 'Этюд в багровых тонах', 1975, 'Детгиз', '56789012', 1, 2),
(nextval('seq_book'), 'Мастер и Маргарита', 1980, 'Книга', '23456789', 2, NULL),
(nextval('seq_book'), 'Рассказы', 1982, 'Книга', '34567890', 3, NULL),
(nextval('seq_book'), 'Дама с собачкой', 1983, 'Прогресс', '45678901', 3, NULL);