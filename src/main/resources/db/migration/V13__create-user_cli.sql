create table user_cli (
id_user uuid not null,
id_clinic uuid not null,
primary key (id_user, id_clinic),
foreign key (id_user) references auth (id),
foreign key (id_clinic) references clinic (id)
);