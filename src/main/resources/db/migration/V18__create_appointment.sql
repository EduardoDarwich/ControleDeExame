create table appointment (
id uuid primary key,
id_pat uuid not null,
id_doc uuid not null,
id_cli uuid not null,
date_create timestamp not null,
open_appointment boolean,
date_end timestamp,
foreign key (id_pat) references patient (id),
foreign key (id_doc) references doctor (id),
foreign key (id_cli) references clinic (id)
);