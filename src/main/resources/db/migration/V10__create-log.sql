--Tabela onde estão armazenados os dados para auditoria
create table log (
id UUID primary key, --Id de identificação do log no banco de dados
user_action text, --Ação que o usuario tentou fazer
entity_id UUID, --Id do usuario que tentou fazer
old_value text, --Campo para armazenar valores antigos em caso de mudança de dados
new_value text, --Campo para armazenar valores novos em caso de mudança de dados
hour_event text, --Armazena a hora em que o evento ocorreu
status text --Informa o status do evento, se foi bem sucedido ou não
);