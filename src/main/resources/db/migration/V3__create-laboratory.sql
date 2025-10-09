--Tabela onde são armazenados os dados específicos da entidade que representa o laboratório
create table laboratory (
id UUID primary key, --Id de identificação do laboratório no banco de dados
auth_id UUID not null, --Id do usuário que esse laboratório representa no banco de dados
name text not null, --Nome ligado ao laboratório
email text not null unique, --Email ligado ao laboratório
cnpj CHAR(14) unique not null, --CNPJ ligado ao laboratório
address text not null, --Endereço ligado ao laboratório
telephone VARCHAR(20) unique not null, --Número de telefone ligado ao laboratório
CONSTRAINT fk_auth FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE --Chave estrangeira ligando um laboratório a um usuário
);