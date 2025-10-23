ALTER TABLE exams_type ALTER COLUMN id SET DEFAULT gen_random_uuid();

insert into exams_type (name)
values
('Exame de sangue'),
('Radiografia'),
('Ultrassom'),
('Raio-x')
