--Tabela onde são armazenados os dados específicos da entidade que representa o paciente
create table patient (
id UUID primary key, --Id de identificação do paciente no banco de dados
auth_id UUID NOT NULL, --Id do usuário que esse paciente representa no banco de dados
date_birth date, --Data de nascimento ligada ao paciente
telephone VARCHAR(20) unique, --Número de telefone ligado ao paciente
cpf CHAR(11) not null unique, --CPF ligado ao paciente
address text , --Endereço ligado ao paciente
CONSTRAINT fk_auth FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE --Chave estrangeira ligando um paciente a um usuário
);