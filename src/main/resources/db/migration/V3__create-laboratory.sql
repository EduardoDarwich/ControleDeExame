--Tabela onde são armazenados os dados específicos da entidade que representa o laboratório
create table laboratory (
id UUID primary key, --Id de identificação do laboratório no banco de dados
name text not null, --Nome ligado ao laboratório
cnpj CHAR(14) unique not null, --CNPJ ligado ao laboratório
address text not null, --Endereço ligado ao laboratório
telephone VARCHAR(20) unique not null --Número de telefone ligado ao laboratório


);