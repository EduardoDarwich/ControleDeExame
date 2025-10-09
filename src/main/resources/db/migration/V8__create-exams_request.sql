--Tabela onde estão armazenados os dados de requisição de exames
create table exams_request (
id UUID primary key, --Id de identificação do pedido de exame no banco de dados
doctor_id uuid not null, --Id do médico que solicitou o exame
complement text, -- possiveis complementos para o exame
exam_type text, --Tipo do exame
sample_type text, --Tipo de amostra
status text default 'pendente', --Informa o status do exame,
request_date text, --data que o pedido foi feito
executed_date text, --data que o pedido foi entregue
foreign key (doctor_id) references doctor(id) --definindo a chave estrangeira de médico
);

