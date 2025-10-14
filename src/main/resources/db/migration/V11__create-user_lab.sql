create table user_lab (
id_user uuid not null,
id_laboratory uuid not null,
primary key (id_user, id_laboratory),
email text not null unique,
foreign key (id_user) references auth(id),
foreign key (id_laboratory) references laboratory(id)
);