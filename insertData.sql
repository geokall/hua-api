INSERT INTO hua.hua_role (id, name) values (1, 'ADMIN'), (2, 'READER');

CREATE EXTENSION pgcrypto;
INSERT INTO hua.hua_user(id, birth_date, changed_date, created_date, email, father_name, gender, mother_name, name, password, surname, username, is_verified)
VALUES (1, '1995-01-01', null, '1995-01-01 08:25:12.000000', 'itp1@hua.gr', 'Νίκος', 'MALE', 'Μαρία', 'Αλέξανδρος', crypt('temp', gen_salt('bf')), 'Χαροκόπειο', 'itp1', true);

INSERT INTO hua.user_role VALUES (1, 1);