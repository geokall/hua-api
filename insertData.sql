drop schema if exists hua cascade;

create schema if not exists hua;

create table hua.hua_role (id  bigserial not null, name varchar(255) not null, primary key (id));
create table hua.hua_user (id  bigserial not null, address varchar(255), birth_date date, city varchar(255), changed_date timestamp, created_date timestamp not null, department varchar(255), direction varchar(255), email varchar(255) not null, father_name varchar(255), gender varchar(255), mobile_number varchar(20) not null, mother_name varchar(255), name varchar(255) not null, password varchar(255) not null, postal_code varchar(255), surname varchar(255), username varchar(255) not null, vat_number varchar(20) not null, is_verified boolean, primary key (id));
create table hua.user_role (user_id int8 not null, role_id int8 not null, primary key (user_id, role_id));
alter table hua.hua_user add constraint UK_lt7xr6y863ahhrwx42elvav1c unique (email);
alter table hua.hua_user add constraint UK_odu71lttaqh7ndnrs2vq8mg58 unique (mobile_number);
alter table hua.hua_user add constraint UK_d807qo5m5t0ndhoo7l5wgs5u3 unique (username);
alter table hua.hua_user add constraint UK_qvllljbwn8n54pf8icmu974cn unique (vat_number);
alter table hua.user_role add constraint FKk7s3w1wrcojurujwifa4q3sft foreign key (role_id) references hua.hua_role;
alter table hua.user_role add constraint FKqswmkbgi77vuh3scptrh71b1m foreign key (user_id) references hua.hua_user;

TRUNCATE TABLE hua.hua_user, hua.hua_role, hua.user_role RESTART IDENTITY;

INSERT INTO hua.hua_role (name) values ('ADMIN'), ('READER');

CREATE EXTENSION if not exists pgcrypto;

INSERT INTO hua.hua_user (address, birth_date, city, changed_date, created_date, department, direction, email, father_name, gender, mobile_number, mother_name, name, password, postal_code, surname, username, vat_number, is_verified)
VALUES ('Θησέως 70', '1995-01-01', 'Καλλιθέα', null, '1995-01-01 08:25:12.000000', 'ΤΜΗΜΑ ΠΛΗΡΟΦΟΡΙΚΗΣ ΚΑΙ ΤΗΛΕΜΑΤΙΚΗΣ (ΠΜΣ)', null, 'itp1@hua.gr', 'Κάποιο πατρώνυμο', null, '2109549101', 'Κάποιο μητρώνυμο', 'Γραμματεία', crypt('admin', gen_salt('bf')), 17671, 'Χαροκόπειο', 'itp1', '012345678', true);

INSERT INTO hua.user_role VALUES (1, 1);

create table hua.hua_event (id  bigserial not null, created_date timestamp not null, event_type varchar(255), user_id int8, primary key (id));

alter table hua.hua_event add constraint FK1vnyj16kn1oq80w11hd1d5ars foreign key (user_id) references hua.hua_user;

alter table hua.hua_event
    add is_admin_informed boolean;