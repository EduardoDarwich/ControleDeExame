create table consultation (
id UUID primary key, --Id de identificação do pedido de exame no banco de dados
appointment_id uuid not null,
init time,
closed time,
diagnosis text,
prescription text,
returns boolean, --controle se é uma consulta necessita de um retorno ou não
finished boolean, --controle se a consulta ja foi finalizada ou não
duration int, --controle do tempo de duração da consulta
foreign key (appointment_id) references appointment(id)
);