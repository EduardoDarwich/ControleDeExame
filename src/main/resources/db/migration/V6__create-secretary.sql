--Tabela onde são armazenados os dados específicos da entidade que representa a secretaria
create table secretary (
id UUID PRIMARY key, --Id de identificação da secretaria no banco de dados
name text not null, --Nome ligado a secretaria no banco de dados
auth_id UUID not null, --Id do usuário que essa secretaria representa no banco de dados
email text unique not null, --Email ligado a secretaria no banco de dados
cpf CHAR(11) not null unique, --CPF ligado a secretaria no banco de dados
telephone VARCHAR(20) unique, --Número ligado a uma secretaria no banco de dados
sector text, --Setor ligado a uma secretaria no banco de dados
CONSTRAINT fk_auth FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE --Chave estrangeira ligando uma secretaria a um usuário
);