--Tabela onde são armazenados os dados específicos da entidade que representa o médico
create table doctor (
id UUID primary key, --Id de identificação do médico no banco de dados
auth_id UUID not null, --Id do usuário que esse médico representa no banco de dados
crm text not null unique, --crm ligado ao médico
telephone VARCHAR(20) unique, --Número de telefone ligado ao médico
CONSTRAINT fk_auth FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE --Chave estrangeira ligando um médico a um usuário
);