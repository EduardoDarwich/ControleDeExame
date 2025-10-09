--Tabela onde estão armazenados os dados do exame
create table exams (
id uuid primary key, --Id de identificação do exame no banco de dados
request_id uuid, --Id de referênncia a requisição do exame
patient_id uuid, --Id de referência ao paciente ligado ao exame
laboratory_id uuid, --Id de referência ao laboratório responsável pelo exame
doctor_id uuid, --Id de referência ao medico que solicitou esse exame
cid text, --Código da doença (se for nescessário)
status text default 'pendente', --Informa o status do exame,
result_value text, --Resultado do exame
result_file_url text, --Caminho URL para o arquivo do exame
observation text, --Considerações do médico ou do laboratório
foreign key (patient_id) references patient(id), --definindo a chave estrangeira de paciente
foreign key (request_id) references exams_request(id), --definindo a chave estrangeira de requisição
foreign key (laboratory_id) references laboratory(id), --definindo a chave estrangeira do laboratório
foreign key (doctor_id) references doctor(id) --define a chave estrangeira do médico
);