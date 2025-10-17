create table clinic (
id UUID primary key,
name text unique not null,
cnpj CHAR(14) unique not null,
address text not null,
telephone VARCHAR(20) unique not null
);