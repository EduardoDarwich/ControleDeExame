create table add_cli (
id_address uuid not null,
id_clinic uuid not null,
primary key (id_clinic, id_address),
foreign key (id_address) references address (id),
foreign key (id_clinic) references clinic (id)
);