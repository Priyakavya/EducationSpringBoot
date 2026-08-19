-- ============================================================================
--  H2 SEED DATA - loaded automatically when the 'dev' profile is active.
--  Hibernate creates the tables (ddl-auto=create-drop), this file fills them.
--
--  Same rows as sql/02_seed_data.sql, minus the MySQL-only syntax.
-- ============================================================================

INSERT INTO teachers (id, name, email, department) VALUES
(1, 'Dr. Anand Krishnan', 'anand.krishnan@edu.in', 'Computer Science'),
(2, 'Prof. Meera Iyer',   'meera.iyer@edu.in',     'Mathematics'),
(3, 'Dr. Rakesh Sharma',  'rakesh.sharma@edu.in',  'Physics'),
(4, 'Prof. Divya Nair',   'divya.nair@edu.in',     'Computer Science'),
(5, 'Dr. Sanjay Gupta',   'sanjay.gupta@edu.in',   'Electronics');

INSERT INTO students (id, name, email, enrollment_date) VALUES
(1,  'Vishnu Vardhan', 'vishnu.vardhan@student.edu.in', '2025-07-15'),
(2,  'Priya Ramesh',   'priya.ramesh@student.edu.in',   '2025-07-15'),
(3,  'Arjun Menon',    'arjun.menon@student.edu.in',    '2025-07-18'),
(4,  'Sneha Kulkarni', 'sneha.kulkarni@student.edu.in', '2025-08-01'),
(5,  'Karthik Reddy',  'karthik.reddy@student.edu.in',  '2025-08-01'),
(6,  'Ananya Desai',   'ananya.desai@student.edu.in',   '2025-08-05'),
(7,  'Rohan Bhat',     'rohan.bhat@student.edu.in',     '2026-01-10'),
(8,  'Lakshmi Pillai', 'lakshmi.pillai@student.edu.in', '2026-01-10'),
(9,  'Aditya Joshi',   'aditya.joshi@student.edu.in',   '2026-01-12'),
(10, 'Nisha Verma',    'nisha.verma@student.edu.in',    '2026-02-01');

INSERT INTO courses (id, title, description, teacher_id) VALUES
(1, 'Data Structures and Algorithms', 'Arrays, linked lists, trees, graphs, sorting, and complexity analysis.', 1),
(2, 'Database Management Systems',    'Relational modelling, normalisation, SQL, transactions, and indexing.',   1),
(3, 'Linear Algebra',                 'Vector spaces, matrices, eigenvalues, and linear transformations.',       2),
(4, 'Quantum Mechanics I',            'Wave functions, the Schrodinger equation, and operator formalism.',       3),
(5, 'Web Application Development',    'HTTP, REST design, Spring Boot, and modern front-end integration.',       4),
(6, 'Digital Signal Processing',      'Sampling, Fourier transforms, filters, and z-transforms.',                5);

INSERT INTO enrollments (id, student_id, course_id, enrollment_date) VALUES
(1,  1, 1, '2025-07-20'), (2,  1, 2, '2025-07-20'), (3,  1, 5, '2025-07-22'),
(4,  2, 1, '2025-07-21'), (5,  2, 3, '2025-07-21'), (6,  3, 2, '2025-07-25'),
(7,  3, 5, '2025-07-25'), (8,  4, 1, '2025-08-05'), (9,  4, 4, '2025-08-05'),
(10, 5, 3, '2025-08-06'), (11, 5, 6, '2025-08-06'), (12, 6, 5, '2025-08-10'),
(13, 7, 1, '2026-01-15'), (14, 7, 2, '2026-01-15'), (15, 8, 4, '2026-01-16'),
(16, 9, 6, '2026-01-18'), (17, 10, 5, '2026-02-05');

INSERT INTO exams (id, course_id, exam_date, title) VALUES
(1, 1, '2025-09-15', 'DSA Mid-Term'),
(2, 1, '2025-12-10', 'DSA Final'),
(3, 2, '2025-09-18', 'DBMS Mid-Term'),
(4, 2, '2025-12-12', 'DBMS Final'),
(5, 3, '2025-09-20', 'Linear Algebra Mid-Term'),
(6, 4, '2025-09-22', 'Quantum Mechanics Mid-Term'),
(7, 5, '2025-10-05', 'Web Development Practical'),
(8, 5, '2025-12-15', 'Web Development Final'),
(9, 6, '2025-10-08', 'DSP Mid-Term');

INSERT INTO grades (id, enrollment_id, exam_id, score) VALUES
(1, 1, 1, 88.50), (2, 1, 2, 91.00), (3, 2, 3, 79.25), (4, 2, 4, 84.00),
(5, 3, 7, 95.00), (6, 4, 1, 72.00), (7, 4, 2, 68.50), (8, 5, 5, 81.75),
(9, 6, 3, 90.00), (10, 7, 7, 86.50), (11, 8, 1, 55.00), (12, 9, 6, 63.25),
(13, 10, 5, 77.00), (14, 11, 9, 82.00), (15, 12, 7, 74.50), (16, 13, 1, 45.00),
(17, 15, 6, 88.00), (18, 16, 9, 91.50);

-- ---------------------------------------------------------------------------
--  Restart the identity counters past the highest seeded id.
--  Without this, the first POST would try to reuse id 1 and fail on the PK.
-- ---------------------------------------------------------------------------
ALTER TABLE teachers    ALTER COLUMN id RESTART WITH 6;
ALTER TABLE students    ALTER COLUMN id RESTART WITH 11;
ALTER TABLE courses     ALTER COLUMN id RESTART WITH 7;
ALTER TABLE enrollments ALTER COLUMN id RESTART WITH 18;
ALTER TABLE exams       ALTER COLUMN id RESTART WITH 10;
ALTER TABLE grades      ALTER COLUMN id RESTART WITH 19;
