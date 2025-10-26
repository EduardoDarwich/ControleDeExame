--Tabela onde estão armazenados os dados do exame
create table support_ticket (
id uuid primary key, --Id de identificação do exame no banco de dados
auth_id uuid, --Código da doença (se for nescessário)
subject text, --Resultado do exame
message text, --Caminho URL para o arquivo do exame
response text, --Considerações do médico ou do laboratório
finished boolean,
foreign key (auth_id) references auth(id)
);