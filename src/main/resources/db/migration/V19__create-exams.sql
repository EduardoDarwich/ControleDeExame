--Tabela onde estão armazenados os dados do exame
create table exams (
id uuid primary key, --Id de identificação do exame no banco de dados
request_id uuid, --Id de referênncia a requisição do exame
exams_type_id text,
justify text,
cid text,
foreign key (request_id) references exams_request(id) --definindo a chave estrangeira de requisição

);