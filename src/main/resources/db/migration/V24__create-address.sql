create table address (
id uuid primary key,
cep text unique,
logradouro text,
complemento text,
bairro text,
localidade text,
uf text

);