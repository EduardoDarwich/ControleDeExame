--Tabela onde são armazenadas informações do usuário utilizadas na autenticação e controle de segurança
Create table auth (
id UUID PRIMARY key, --Id de identificação do usuário
username_key text unique not null, --Username do usuário usado para login
password_key text not null, --Password do usuário usado para login
active boolean default false, --Status do usuário para definir se está ativo no sistema ou não(usado para restringir acesso sem deletar a conta do banco)
token text, --Token usado para recuperação de senha e primeiro acesso
data_expiration_token timestamp, --Data de expiração do token
token_status boolean default true, --Status do token(usado para definir se ele é valido ou se ja foi utilizado)
locked boolean default false, --Verifica se a conta está bloqueada
failed_attempts int, --Contador para as tentativas falhas do usuário
lock_time timestamp --Hora de bloqueio da conta do usuário
);