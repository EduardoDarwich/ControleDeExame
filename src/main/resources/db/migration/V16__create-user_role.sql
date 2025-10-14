create table user_role (
id_user uuid not null,
id_role uuid not null,
primary key (id_user, id_role),
foreign key (id_user) references auth (id),
foreign key (id_role) references role (id)
);