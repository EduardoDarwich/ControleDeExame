ALTER TABLE role ALTER COLUMN id SET DEFAULT gen_random_uuid();

insert into role (name)
values
('Admin'),
('LaboratoryAdmin'),
('LaboratoryUser'),
('Doctor'),
('Patient'),
('Secretary')
