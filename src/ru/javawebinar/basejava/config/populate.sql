DELETE FROM contact;
DELETE FROM resume;

INSERT INTO resume (uuid, full_name) VALUES
('uuid1', 'fullName1'),
('uuid2', 'fullName2'),
('uuid3', 'fullName3'),
('uuid4', 'fullName4');

INSERT INTO contact(id, resume_uuid, type, value) VALUES
('1','uuid1', 'PHONE', '12345'),
('2','uuid1', 'SKYPE', 'skype');