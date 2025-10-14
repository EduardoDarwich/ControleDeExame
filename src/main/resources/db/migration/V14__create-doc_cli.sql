create table doc_cli (
id_doctor uuid not null,
id_clinic uuid not null,
primary key (id_clinic, id_doctor),
foreign key (id_doctor) references doctor (id),
foreign key (id_clinic) references clinic (id)
);