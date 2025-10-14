--Tabela onde são armazenados os dados específicos da entidade que representa o adiministrador
create table admin (
id UUID PRIMARY key, --Id de identificação do adiministrador no banco de dados
auth_id UUID not null, --Id do usuário que esse adiministrador representa no banco de dados
clinic_id UUID not null,
name text , --Nome ligado ao adiministrador
email text unique not null, --Email ligado ao adiministrador
cpf CHAR(11) unique, --CPF ligado ao adiministrador
telephone VARCHAR(20) unique, --Número de telefone ligado ao adiministrador
CONSTRAINT fk_auth FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE, --Chave estrangeira ligando um adiministrador a um usuário
CONSTRAINT fk_clinic FOREIGN KEY (clinic_id) REFERENCES clinic (id) ON DELETE CASCADE
);