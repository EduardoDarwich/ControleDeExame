--Tabela onde estão armazenados os dados de requisição de exames
create table exams_request (
id UUID primary key, --Id de identificação do pedido de exame no banco de dados
doctor_id uuid not null, --Id do médico que solicitou o exame
clinic_id uuid not null,
patient_id uuid not null,
consultation_id uuid not null,
complement text, -- possiveis complementos para o exame
exam_type text, --Tipo do exame
sample_type text, --Tipo de amostra
status text default 'pendente', --Informa o status do exame
cod_verific text unique,
request_date timestamp, --data que o pedido foi feito
executed_date timestamp, --data que o pedido foi entregue
foreign key (doctor_id) references doctor(id), --definindo a chave estrangeira de médico
CONSTRAINT fk_clinic FOREIGN KEY (clinic_id) REFERENCES clinic (id) ON DELETE CASCADE ,
foreign key (patient_id) references patient (id),
foreign key (consultation_id) references consultation(id)
);

