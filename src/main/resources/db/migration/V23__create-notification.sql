--Tabela onde estão armazenados os dados do exame
create table notification (
id uuid primary key,
auth_id uuid,
title text,
message text,
read_file boolean,
foreign key (auth_id) references auth(id)
);