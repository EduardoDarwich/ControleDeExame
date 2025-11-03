create table anamnesis (
--identificação e controle
id UUID primary key,
consultation_id uuid not null,
date_create date,
--queixa principal e doença atual
main_complaint text,
history_of_current_illness text,
--histórico parológico familiar
personal_medical_history text,
family_history text,
allergies text,
use_medications text,
previous_hospitalizations text,
previous_surgeries text,
--hábitos e estilo de vida
diet text,
sleep text,
physical_activity text,
smoking boolean,
alcoholism boolean,
--exame físico
blood_pressure text,
heart_rate text,
temperature double precision,
weight double precision,
height double precision,
bmi double precision,
--observações complementares e tratamento inicial
observations text,
diagnostic_hypothesis text,
treatment_plan text,
foreign key (consultation_id) references consultation(id)
);