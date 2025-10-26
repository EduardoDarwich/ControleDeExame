--Tabela onde são armazenados os dados específicos da entidade que representa o paciente
create table support_user (
id UUID primary key, --Id de identificação do paciente no banco de dados
auth_id UUID NOT NULL, --Id do usuário que esse paciente representa no banco de dados
CONSTRAINT fk_auth FOREIGN KEY (auth_id) REFERENCES auth (id) ON DELETE CASCADE --Chave estrangeira ligando um paciente a um usuário
);