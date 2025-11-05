create table file_path (
id UUID primary key,
exams_request_id uuid not null,
file_name text,
original_name text,
upload_date timestamp,
FOREIGN KEY (exams_request_id) REFERENCES exams_request (id) ON DELETE CASCADE
);
