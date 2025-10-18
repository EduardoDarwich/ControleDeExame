create table pat_cli (
id_pat uuid not null,
id_clinic uuid not null,
primary key (id_pat, id_clinic),
foreign key (id_pat) references patient (id),
foreign key (id_clinic) references clinic (id)
);