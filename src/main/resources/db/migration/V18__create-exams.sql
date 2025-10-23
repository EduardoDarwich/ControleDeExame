--Tabela onde estão armazenados os dados do exame
create table exams (
id uuid primary key, --Id de identificação do exame no banco de dados
request_id uuid, --Id de referênncia a requisição do exame
cid text, --Código da doença (se for nescessário)
result_value text, --Resultado do exame
result_file_url text, --Caminho URL para o arquivo do exame
observation text, --Considerações do médico ou do laboratório
foreign key (request_id) references exams_request(id) --definindo a chave estrangeira de requisição

);