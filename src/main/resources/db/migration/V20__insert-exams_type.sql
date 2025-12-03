ALTER TABLE exams_type ALTER COLUMN id SET DEFAULT gen_random_uuid();

insert into exams_type (name)
values
('Hemograma'),
('Radiografia'),
('Ultrassom'),
('Raio-x'),
('Colesterol total'),
('HDL'),
('LDL')

