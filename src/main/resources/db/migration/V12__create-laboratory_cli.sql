create table laboratory_cli(
id_laboratory uuid not null,
id_clinic uuid not null,
primary key (id_clinic, id_laboratory),
foreign key (id_clinic) references clinic(id),
foreign key (id_laboratory) references laboratory(id)
);