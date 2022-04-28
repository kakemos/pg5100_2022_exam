insert into users
values(nextval('users_user_id_seq'), 'admin@admin.com', '$2a$12$W1jj.Ebdm60zC5qxoZhVY.jEWrh.DzkWgKrDm.AKV1OYHXGey/Ob2', now(), true),
      (nextval('users_user_id_seq'), 'user@user.com', '$2a$12$LvmqyB.6yS7PD6e.oRgn8eke8oUF68gbHxYAlEQuEeRDdi.hJQhQO', now(), true);

insert into authorities
values(nextval('authorities_authority_id_seq'), 'USER'),
      (nextval('authorities_authority_id_seq'), 'ADMIN');

insert into users_authorities
values(1, 1),
      (1, 2),
      (2, 1);

insert into animals
values(nextval('animals_animal_id_seq'), 'Rufus', 'dog', 'chihuahua', 1, 2, false, now(), true, null),
      (nextval('animals_animal_id_seq'), 'Filippa', 'cat', 'persian', 0, 28, true, now(), true, 1),
      (nextval('animals_animal_id_seq'), 'Apple', 'horse', 'falabella', 0, 5, false, now(), true, 1),
      (nextval('animals_animal_id_seq'), 'Mais', 'rabbit', 'american fuzzy lop', 1, 1, false, now(), true, null),
      (nextval('animals_animal_id_seq'), 'Pablo', 'cow', 'limousin', 1, 10, true, now(), true, 2);



