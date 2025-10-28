create table add_lab (
id_address uuid not null,
id_laboratory uuid not null,
primary key (id_laboratory, id_address),
foreign key (id_address) references address (id),
foreign key (id_laboratory) references laboratory (id)
);