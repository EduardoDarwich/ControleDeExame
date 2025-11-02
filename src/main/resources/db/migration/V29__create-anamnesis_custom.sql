create table anamnesis_custom (
id UUID primary key, --Id de identificação do pedido de exame no banco de dados
anamnesis_id uuid not null,
field_name text,
field_value text,
foreign key (anamnesis_id) references anamnesis(id)
);