CREATE TABLE public.author_book (
                               author_id int4 NOT NULL,
                               book_id int4 NOT NULL,
                               CONSTRAINT authors_book_pk PRIMARY KEY (author_id, book_id)
);

INSERT INTO public.author_book (author_id, book_id)
VALUES(1, 1),
      (1, 2),
      (2, 3),
      (3, 3),
      (3, 4),
      (3, 5),
      (4, 6),
      (4, 7),
      (4, 8),
      (1, 8),
      (2, 8);