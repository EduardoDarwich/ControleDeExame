create table exams_file (
id UUID primary key,
exams_request_id uuid not null,
patient_id uuid not null,
doctor_id uuid not null,
lab_id uuid not null,
file_name text,
file_path text,
upload_date timestamp,
FOREIGN KEY (exams_request_id) REFERENCES exams_request (id) ON DELETE CASCADE,
FOREIGN KEY (patient_id) REFERENCES patient (id) ON DELETE CASCADE,
FOREIGN KEY (doctor_id) REFERENCES doctor (id) ON DELETE CASCADE,
FOREIGN KEY (lab_id) REFERENCES laboratory (id) ON DELETE CASCADE
);
