-- ============================================================================
-- Quédate — Seed data (real rental listings near universities in Lima)
-- ============================================================================

-- ----------------------------------------------------------------------------
-- Roles
-- ----------------------------------------------------------------------------
INSERT INTO roles (id, name) VALUES (1, 'USER') ON CONFLICT (id) DO NOTHING;
INSERT INTO roles (id, name) VALUES (2, 'LANDLORD') ON CONFLICT (id) DO NOTHING;
INSERT INTO roles (id, name) VALUES (3, 'ADMIN') ON CONFLICT (id) DO NOTHING;

-- ----------------------------------------------------------------------------
-- Users (1 admin + 10 landlords + 5 students)
-- ----------------------------------------------------------------------------
INSERT INTO users (id, first_name, last_name, email, password, phone, created_at, updated_at) VALUES
(1, 'Admin', 'Quedate', 'admin@quedate.com', '$2a$10$VTIHU1/SvUe/YYzMVB2MAuLsvGR8Abf9FR/NoJKzA/Y/d1qMrUWGO', '999000001', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Carlos', 'Mendoza', 'landlord1@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000002', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Maria', 'Garcia', 'landlord2@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000003', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Jose', 'Ramirez', 'landlord3@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000004', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Ana', 'Torres', 'landlord4@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000005', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Luis', 'Fernandez', 'landlord5@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000006', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Carmen', 'Diaz', 'landlord6@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000007', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Pedro', 'Sanchez', 'landlord7@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000008', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Rosa', 'Lopez', 'landlord8@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000009', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Juan', 'Martinez', 'landlord9@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000010', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Lucia', 'Vargas', 'landlord10@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000011', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Student1', 'Apellido1', 'student1@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000012', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'Student2', 'Apellido2', 'student2@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000013', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'Student3', 'Apellido3', 'student3@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000014', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Student4', 'Apellido4', 'student4@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000015', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'Student5', 'Apellido5', 'student5@quedate.com', '$2a$10$hH.xvty1A7IY4S6ALThTLe8RdIu16KHNx4WmmDP8lAhvzhvIRE5Fi', '999000016', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- ----------------------------------------------------------------------------
-- User roles
-- ----------------------------------------------------------------------------
INSERT INTO user_roles (user_id, role_id) VALUES (1, 3) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (2, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (3, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (4, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (5, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (6, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (7, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (8, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (9, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (10, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (11, 2) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (12, 1) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (13, 1) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (14, 1) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (15, 1) ON CONFLICT DO NOTHING;
INSERT INTO user_roles (user_id, role_id) VALUES (16, 1) ON CONFLICT DO NOTHING;

-- ----------------------------------------------------------------------------
-- Landlords
-- ----------------------------------------------------------------------------
INSERT INTO landlords (id, user_id, dni, bio, is_verified, created_at) VALUES
(1, 2, '40000001', 'Arrendador demo 1 - Barranco', TRUE, CURRENT_TIMESTAMP),
(2, 3, '40000002', 'Arrendador demo 2 - San Miguel', TRUE, CURRENT_TIMESTAMP),
(3, 4, '40000003', 'Arrendador demo 3 - Rimac', FALSE, CURRENT_TIMESTAMP),
(4, 5, '40000004', 'Arrendador demo 4 - Surco', TRUE, CURRENT_TIMESTAMP),
(5, 6, '40000005', 'Arrendador demo 5 - La Molina', FALSE, CURRENT_TIMESTAMP),
(6, 7, '40000006', 'Arrendador demo 6 - San Isidro', TRUE, CURRENT_TIMESTAMP),
(7, 8, '40000007', 'Arrendador demo 7 - Breña', FALSE, CURRENT_TIMESTAMP),
(8, 9, '40000008', 'Arrendador demo 8 - Surquillo', TRUE, CURRENT_TIMESTAMP),
(9, 10, '40000009', 'Arrendador demo 9 - Jesús María', FALSE, CURRENT_TIMESTAMP),
(10, 11, '40000010', 'Arrendador demo 10 - Lince', TRUE, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- ----------------------------------------------------------------------------
-- Universities (10 real universities in Lima)
-- ----------------------------------------------------------------------------
INSERT INTO universities (id, name, campus_name, address, latitude, longitude, website) VALUES
(1, 'UTEC', 'UTEC Barranco', 'Jr. Medrano Silva 165, Barranco, Lima 15063', -12.146900, -77.022500, 'https://www.utec.edu.pe'),
(2, 'PUCP', 'Campus PUCP', 'Av. Universitaria 1801, San Miguel, Lima 15088', -12.069500, -77.079500, 'https://www.pucp.edu.pe'),
(3, 'UNI', 'Campus UNI', 'Av. Tupac Amaru 210, Rimac, Lima 15333', -12.023800, -77.050300, 'https://www.uni.edu.pe'),
(4, 'UPC', 'Campus Monterrico', 'Prolongacion Primavera 2390, Surco, Lima 15023', -12.104000, -76.963000, 'https://www.upc.edu.pe'),
(5, 'Universidad de Lima', 'Campus ULima', 'Av. Javier Prado Este 4600, Surco, Lima 15023', -12.084700, -76.973700, 'https://www.ulima.edu.pe'),
(6, 'USIL', 'Campus USIL', 'Av. La Fontana 550, La Molina, Lima 15024', -12.080000, -76.940000, 'https://www.usil.edu.pe'),
(7, 'Universidad Ricardo Palma', 'Campus URP', 'Av. Benavides 5440, Surco, Lima 15039', -12.121000, -76.998000, 'https://www.urp.edu.pe'),
(8, 'UNMSM', 'Ciudad Universitaria', 'Av. Venezuela s/n, Lima 15081', -12.056000, -77.084000, 'https://www.unmsm.edu.pe'),
(9, 'USMP', 'Campus USMP', 'Av. Tomas Marsano 242, Surquillo, Lima 15048', -12.110000, -77.010000, 'https://www.usmp.edu.pe'),
(10, 'Universidad del Pacifico', 'Campus UP', 'Av. Salaverry 2020, Jesus Maria, Lima 15076', -12.072000, -77.053000, 'https://www.up.edu.pe')
ON CONFLICT (id) DO NOTHING;

-- ----------------------------------------------------------------------------
-- Locations (real districts near each university)
-- ----------------------------------------------------------------------------
INSERT INTO locations (id, district, address, latitude, longitude, distance_to_university_km) VALUES
-- UTEC area (Barranco)
(1, 'Barranco', 'Jr. Medrano Silva 165, Barranco', -12.1469, -77.0225, 0.5),
(2, 'Miraflores', 'Av. Jose Pardo 123, Miraflores', -12.1210, -77.0300, 2.5),
(3, 'Surquillo', 'Av. Angamos Este 456, Surquillo', -12.1100, -77.0100, 3.0),
(4, 'Chorrillos', 'Av. Defensores del Morro 2481, Chorrillos', -12.1700, -77.0200, 2.8),
-- PUCP area (San Miguel)
(5, 'San Miguel', 'Av. La Marina 1500, San Miguel', -12.0760, -77.0850, 0.8),
(6, 'Pueblo Libre', 'Av. Brasil 1200, Pueblo Libre', -12.0750, -77.0650, 1.5),
(7, 'Magdalena', 'Jr. Leon Velarde 456, Magdalena', -12.0880, -77.0700, 2.0),
(8, 'Jesus Maria', 'Av. Salaverry 1800, Jesus Maria', -12.0720, -77.0530, 2.5),
-- UNI area (Rimac)
(9, 'Rimac', 'Jr. Trujillo 210, Rimac', -12.0238, -77.0503, 0.3),
(10, 'Independencia', 'Av. Tupac Amaru 1500, Independencia', -11.9910, -77.0550, 3.5),
(11, 'Los Olivos', 'Av. Carlos Izaguirre 800, Los Olivos', -11.9650, -77.0700, 6.0),
(12, 'San Martin de Porres', 'Av. Tomas Valle 500, SMP', -12.0050, -77.0750, 4.0),
-- UPC Monterrico (Surco)
(13, 'Surco', 'Prolongacion Primavera 2390, Surco', -12.1040, -76.9630, 0.5),
(14, 'La Molina', 'Av. La Molina 1200, La Molina', -12.0800, -76.9400, 3.0),
(15, 'San Borja', 'Av. San Luis 800, San Borja', -12.1080, -77.0000, 3.5),
(16, 'Santiago de Surco', 'Av. Benavides 3000, Surco', -12.1350, -76.9900, 4.0),
-- Universidad de Lima (Surco)
(17, 'Surco', 'Av. Javier Prado Este 4600, Surco', -12.0847, -76.9737, 0.5),
(18, 'La Molina', 'Av. La Molina 800, La Molina', -12.0800, -76.9400, 3.5),
(19, 'San Borja', 'Av. Aviacion 1200, San Borja', -12.1080, -77.0000, 3.0),
-- USIL (La Molina)
(20, 'La Molina', 'Av. La Fontana 550, La Molina', -12.0800, -76.9400, 0.5),
(21, 'Ate', 'Av. Nicolas Ayllon 1200, Ate', -12.0250, -76.9200, 6.0),
(22, 'Cieneguilla', 'Carretera Cieneguilla km 5', -12.0800, -76.8500, 10.0),
-- Universidad Ricardo Palma (Surco)
(23, 'Surco', 'Av. Benavides 5440, Surco', -12.1210, -76.9980, 0.5),
(24, 'Barranco', 'Av. Grau 456, Barranco', -12.1469, -77.0225, 3.0),
(25, 'Miraflores', 'Av. Larco 789, Miraflores', -12.1210, -77.0300, 3.5),
-- UNMSM (Lima/Breña)
(26, 'Breña', 'Jr. Huaraz 123, Breña', -12.0560, -77.0840, 0.5),
(27, 'Jesus Maria', 'Av. Brasil 1500, Jesus Maria', -12.0720, -77.0530, 2.0),
(28, 'Lima', 'Jr. de la Union 456, Cercado de Lima', -12.0464, -77.0428, 1.5),
(29, 'Pueblo Libre', 'Av. Sucre 789, Pueblo Libre', -12.0750, -77.0650, 2.5),
-- USMP (Surquillo)
(30, 'Surquillo', 'Av. Tomas Marsano 242, Surquillo', -12.1100, -77.0100, 0.5),
(31, 'San Luis', 'Av. Canada 1200, San Luis', -12.0800, -77.0000, 3.5),
(32, 'La Victoria', 'Av. Mexico 456, La Victoria', -12.0650, -77.0150, 4.5),
-- Universidad del Pacifico (Jesus Maria)
(33, 'Jesus Maria', 'Av. Salaverry 2020, Jesus Maria', -12.0720, -77.0530, 0.5),
(34, 'San Isidro', 'Av. Javier Prado Oeste 456, San Isidro', -12.0950, -77.0360, 2.5),
(35, 'Lince', 'Av. Arequipa 1234, Lince', -12.0850, -77.0350, 2.0),
(36, 'Magdalena', 'Jr. Leon Velarde 789, Magdalena', -12.0880, -77.0700, 2.5)
ON CONFLICT (id) DO NOTHING;

-- ----------------------------------------------------------------------------
-- Rooms (real listings near universities)
-- ----------------------------------------------------------------------------
INSERT INTO rooms (id, title, description, price, capacity, size_m2, status, is_verified, owner_id, university_id, location_id, created_at, updated_at) VALUES
-- UTEC (landlord 1, university 1)
(1, 'Cuarto privado amoblado en Barranco', 'Habitación con baño privado, cama queen, escritorio y closet. A 5 min caminando de UTEC. Servicios incluidos.', 850.00, 1, 16.0, 'AVAILABLE', TRUE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(2, 'Habitación compartida en Miraflores', 'Cuarto en departamento compartido con 2 roommates. Baño compartido, cocina equipada. A 10 min de UTEC en bus.', 550.00, 1, 12.0, 'AVAILABLE', TRUE, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(3, 'Estudio independiente en Surquillo', 'Estudio con baño privado, kitchenette, amoblado. Ideal para estudiante. A 15 min de UTEC.', 1200.00, 1, 22.0, 'AVAILABLE', TRUE, 1, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(4, 'Cuarto en Chorrillos cerca de UTEC', 'Habitación amoblada con baño compartido. Zona tranquila, cerca a paraderos. A 20 min de UTEC.', 450.00, 2, 14.0, 'AVAILABLE', FALSE, 1, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(5, 'Departamento compartido en Barranco', 'Habitación en depa compartido con vista al mar. Baño privado, cocina, lavandería. A 8 min de UTEC.', 950.00, 1, 18.0, 'AVAILABLE', TRUE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(6, 'Cuarto económico en Surquillo', 'Habitación básica con cama y closet. Baño y cocina compartidos. Ideal para presupuesto ajustado.', 380.00, 1, 10.0, 'AVAILABLE', FALSE, 1, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(7, 'Habitación premium en Miraflores', 'Cuarto amoblado con baño privado, smart TV, aire acondicionado. Edificio con seguridad 24/7.', 1100.00, 1, 20.0, 'AVAILABLE', TRUE, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(8, 'Cuarto compartido en Barranco', 'Habitación en casa compartida con 3 estudiantes. Baño compartido, cocina equipada, wifi.', 420.00, 1, 11.0, 'AVAILABLE', FALSE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(9, 'Estudio en Miraflores frente al mar', 'Estudio independiente con vista al mar, baño privado, kitchenette. A 12 min de UTEC.', 1350.00, 1, 25.0, 'AVAILABLE', TRUE, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(10, 'Habitación en Chorrillos con baño propio', 'Cuarto amoblado con baño privado, closet grande. Zona residencial cerca a UTEC.', 520.00, 1, 13.0, 'AVAILABLE', FALSE, 1, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(11, 'Cuarto privado en Barranco centro', 'Habitación en departamento compartido. Baño privado, cocina, lavandería. A 3 min de UTEC.', 780.00, 1, 15.0, 'AVAILABLE', TRUE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(12, 'Habitación amoblada en Surquillo', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a paraderos y supermercados.', 480.00, 1, 12.0, 'AVAILABLE', FALSE, 1, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(13, 'Departamento compartido en Miraflores', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 8 min de UTEC.', 650.00, 1, 14.0, 'AVAILABLE', TRUE, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(14, 'Cuarto económico en Barranco', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de UTEC.', 350.00, 1, 9.0, 'AVAILABLE', FALSE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(15, 'Estudio premium en Miraflores', 'Estudio amoblado con baño privado, kitchenette, balcón con vista. Edificio con seguridad.', 1450.00, 1, 28.0, 'AVAILABLE', TRUE, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(16, 'Habitación en Surquillo cerca de UTEC', 'Cuarto privado con baño compartido. Amoblado con cama, escritorio. Zona tranquila.', 500.00, 1, 13.0, 'AVAILABLE', FALSE, 1, 1, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(17, 'Cuarto en Barranco con vista al mar', 'Habitación en departamento con vista al mar. Baño privado, cocina, lavandería.', 920.00, 1, 17.0, 'AVAILABLE', TRUE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(18, 'Habitación compartida en Chorrillos', 'Cuarto en casa compartida con 2 roommates. Baño compartido, cocina, wifi.', 400.00, 1, 11.0, 'AVAILABLE', FALSE, 1, 1, 4, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(19, 'Estudio en Barranco cerca de UTEC', 'Estudio independiente amoblado. Baño privado, kitchenette, closet. A 6 min de UTEC.', 1150.00, 1, 20.0, 'AVAILABLE', TRUE, 1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(20, 'Cuarto en Miraflores zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 720.00, 1, 15.0, 'AVAILABLE', FALSE, 1, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- PUCP (landlord 2, university 2)
(21, 'Cuarto privado en San Miguel', 'Habitación amoblada con baño privado. A 5 min de PUCP. Servicios incluidos.', 750.00, 1, 15.0, 'AVAILABLE', TRUE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(22, 'Habitación compartida en Pueblo Libre', 'Cuarto en departamento compartido. Baño compartido, cocina. A 10 min de PUCP.', 500.00, 1, 12.0, 'AVAILABLE', FALSE, 2, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(23, 'Estudio en Magdalena', 'Estudio independiente con baño privado, kitchenette. A 15 min de PUCP.', 1100.00, 1, 20.0, 'AVAILABLE', TRUE, 2, 2, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(24, 'Cuarto en Jesús María cerca de PUCP', 'Habitación amoblada con baño compartido. Zona tranquila. A 12 min de PUCP.', 600.00, 1, 13.0, 'AVAILABLE', FALSE, 2, 2, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(25, 'Departamento compartido en San Miguel', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 7 min de PUCP.', 680.00, 1, 14.0, 'AVAILABLE', TRUE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(26, 'Cuarto económico en Pueblo Libre', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a PUCP.', 420.00, 1, 10.0, 'AVAILABLE', FALSE, 2, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(27, 'Habitación premium en San Miguel', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad 24/7. A 4 min de PUCP.', 950.00, 1, 18.0, 'AVAILABLE', TRUE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(28, 'Cuarto compartido en Magdalena', 'Habitación en casa compartida. Baño compartido, cocina, wifi. A 18 min de PUCP.', 450.00, 1, 11.0, 'AVAILABLE', FALSE, 2, 2, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(29, 'Estudio en Jesús María', 'Estudio independiente con baño privado, kitchenette, balcón. A 10 min de PUCP.', 1200.00, 1, 22.0, 'AVAILABLE', TRUE, 2, 2, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(30, 'Habitación en San Miguel zona residencial', 'Cuarto privado con baño compartido. Amoblado. Cerca a paraderos y supermercados.', 550.00, 1, 12.0, 'AVAILABLE', FALSE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(31, 'Cuarto privado en Pueblo Libre', 'Habitación con baño privado, cama, escritorio. A 8 min de PUCP. Servicios incluidos.', 720.00, 1, 14.0, 'AVAILABLE', TRUE, 2, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(32, 'Habitación amoblada en Magdalena', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a PUCP.', 580.00, 1, 13.0, 'AVAILABLE', FALSE, 2, 2, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(33, 'Departamento compartido en Jesús María', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 12 min de PUCP.', 650.00, 1, 15.0, 'AVAILABLE', TRUE, 2, 2, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(34, 'Cuarto económico en San Miguel', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de PUCP.', 380.00, 1, 9.0, 'AVAILABLE', FALSE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(35, 'Estudio premium en San Miguel', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 1350.00, 1, 25.0, 'AVAILABLE', TRUE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(36, 'Habitación en Pueblo Libre cerca de PUCP', 'Cuarto privado con baño compartido. Amoblado. Zona tranquila.', 480.00, 1, 12.0, 'AVAILABLE', FALSE, 2, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(37, 'Cuarto en Magdalena con vista al mar', 'Habitación en departamento con vista. Baño privado, cocina, lavandería.', 880.00, 1, 16.0, 'AVAILABLE', TRUE, 2, 2, 7, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(38, 'Habitación compartida en Jesús María', 'Cuarto en casa compartida. Baño compartido, cocina, wifi. Cerca a PUCP.', 420.00, 1, 11.0, 'AVAILABLE', FALSE, 2, 2, 8, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(39, 'Estudio en San Miguel cerca de PUCP', 'Estudio independiente amoblado. Baño privado, kitchenette. A 4 min de PUCP.', 1050.00, 1, 18.0, 'AVAILABLE', TRUE, 2, 2, 5, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(40, 'Cuarto en Pueblo Libre zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 620.00, 1, 14.0, 'AVAILABLE', FALSE, 2, 2, 6, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- UNI (landlord 3, university 3)
(41, 'Cuarto privado en Rimac', 'Habitación amoblada con baño privado. A 3 min de UNI. Servicios incluidos.', 550.00, 1, 14.0, 'AVAILABLE', TRUE, 3, 3, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(42, 'Habitación compartida en Independencia', 'Cuarto en departamento compartido. Baño compartido, cocina. A 15 min de UNI.', 380.00, 1, 11.0, 'AVAILABLE', FALSE, 3, 3, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(43, 'Cuarto en Los Olivos cerca de UNI', 'Habitación amoblada con baño compartido. Zona tranquila. A 20 min de UNI.', 320.00, 1, 10.0, 'AVAILABLE', FALSE, 3, 3, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(44, 'Habitación en San Martin de Porres', 'Cuarto privado con baño compartido. Amoblado. Cerca a paraderos.', 350.00, 1, 12.0, 'AVAILABLE', FALSE, 3, 3, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(45, 'Departamento compartido en Rimac', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 5 min de UNI.', 480.00, 1, 13.0, 'AVAILABLE', TRUE, 3, 3, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(46, 'Cuarto económico en Independencia', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a UNI.', 300.00, 1, 9.0, 'AVAILABLE', FALSE, 3, 3, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(47, 'Habitación amoblada en Rimac', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a UNI.', 420.00, 1, 11.0, 'AVAILABLE', FALSE, 3, 3, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(48, 'Cuarto compartido en Los Olivos', 'Habitación en casa compartida. Baño compartido, cocina, wifi. Cerca a UNI.', 280.00, 1, 10.0, 'AVAILABLE', FALSE, 3, 3, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(49, 'Estudio en Rimac cerca de UNI', 'Estudio independiente amoblado. Baño privado, kitchenette. A 4 min de UNI.', 650.00, 1, 16.0, 'AVAILABLE', TRUE, 3, 3, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(50, 'Cuarto en San Martin de Porres zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 380.00, 1, 12.0, 'AVAILABLE', FALSE, 3, 3, 12, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(51, 'Cuarto privado en Independencia', 'Habitación con baño privado, cama, escritorio. A 12 min de UNI. Servicios incluidos.', 480.00, 1, 13.0, 'AVAILABLE', TRUE, 3, 3, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(52, 'Habitación amoblada en Los Olivos', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a UNI.', 350.00, 1, 11.0, 'AVAILABLE', FALSE, 3, 3, 11, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(53, 'Habitación compartida en Rimac', 'Cuarto en depa compartido. Baño compartido, cocina, sala. A 6 min de UNI.', 320.00, 1, 10.0, 'AVAILABLE', FALSE, 3, 3, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(54, 'Cuarto económico en Independencia', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de UNI.', 260.00, 1, 9.0, 'AVAILABLE', FALSE, 3, 3, 10, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(55, 'Estudio en Rimac cerca de UNI', 'Estudio amoblado con baño privado, kitchenette. A 3 min de UNI.', 720.00, 1, 16.0, 'AVAILABLE', TRUE, 3, 3, 9, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- UPC Monterrico (landlord 4, university 4)
(56, 'Cuarto privado en Surco', 'Habitación amoblada con baño privado. A 5 min de UPC Monterrico. Servicios incluidos.', 900.00, 1, 16.0, 'AVAILABLE', TRUE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(57, 'Habitación compartida en La Molina', 'Cuarto en departamento compartido. Baño compartido, cocina. A 12 min de UPC.', 600.00, 1, 12.0, 'AVAILABLE', FALSE, 4, 4, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(58, 'Estudio en San Borja', 'Estudio independiente con baño privado, kitchenette. A 15 min de UPC.', 1250.00, 1, 22.0, 'AVAILABLE', TRUE, 4, 4, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(59, 'Cuarto en Santiago de Surco', 'Habitación amoblada con baño compartido. Zona tranquila. A 18 min de UPC.', 650.00, 1, 13.0, 'AVAILABLE', FALSE, 4, 4, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(60, 'Departamento compartido en Surco', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 7 min de UPC.', 750.00, 1, 15.0, 'AVAILABLE', TRUE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(61, 'Cuarto económico en La Molina', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a UPC.', 450.00, 1, 10.0, 'AVAILABLE', FALSE, 4, 4, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(62, 'Habitación premium en Surco', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de UPC.', 1050.00, 1, 18.0, 'AVAILABLE', TRUE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(63, 'Cuarto compartido en San Borja', 'Habitación en casa compartida. Baño compartido, cocina, wifi. A 12 min de UPC.', 500.00, 1, 11.0, 'AVAILABLE', FALSE, 4, 4, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(64, 'Estudio en Santiago de Surco', 'Estudio independiente amoblado. Baño privado, kitchenette, balcón. A 15 min de UPC.', 1300.00, 1, 24.0, 'AVAILABLE', TRUE, 4, 4, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(65, 'Habitación en Surco zona residencial', 'Cuarto privado con baño compartido. Amoblado. Cerca a paraderos.', 580.00, 1, 12.0, 'AVAILABLE', FALSE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(66, 'Cuarto privado en La Molina', 'Habitación con baño privado, cama, escritorio. A 10 min de UPC. Servicios incluidos.', 720.00, 1, 14.0, 'AVAILABLE', TRUE, 4, 4, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(67, 'Habitación amoblada en San Borja', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a UPC.', 620.00, 1, 13.0, 'AVAILABLE', FALSE, 4, 4, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(68, 'Departamento compartido en Santiago de Surco', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 12 min de UPC.', 700.00, 1, 15.0, 'AVAILABLE', TRUE, 4, 4, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(69, 'Cuarto económico en Surco', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de UPC.', 420.00, 1, 9.0, 'AVAILABLE', FALSE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(70, 'Estudio premium en Surco', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 1400.00, 1, 26.0, 'AVAILABLE', TRUE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(71, 'Habitación en La Molina cerca de UPC', 'Cuarto privado con baño compartido. Amoblado. Zona tranquila.', 520.00, 1, 12.0, 'AVAILABLE', FALSE, 4, 4, 14, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(72, 'Cuarto en San Borja con vista', 'Habitación en departamento con vista. Baño privado, cocina, lavandería.', 850.00, 1, 16.0, 'AVAILABLE', TRUE, 4, 4, 15, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(73, 'Habitación compartida en Surco', 'Cuarto en casa compartida. Baño compartido, cocina, wifi. Cerca a UPC.', 450.00, 1, 11.0, 'AVAILABLE', FALSE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(74, 'Estudio en Surco cerca de UPC', 'Estudio independiente amoblado. Baño privado, kitchenette. A 4 min de UPC.', 1100.00, 1, 19.0, 'AVAILABLE', TRUE, 4, 4, 13, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(75, 'Cuarto en Santiago de Surco zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 680.00, 1, 14.0, 'AVAILABLE', FALSE, 4, 4, 16, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Universidad de Lima (landlord 5, university 5)
(76, 'Cuarto privado en Surco (ULima)', 'Habitación amoblada con baño privado. A 5 min de Universidad de Lima. Servicios incluidos.', 850.00, 1, 16.0, 'AVAILABLE', TRUE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(77, 'Habitación compartida en La Molina', 'Cuarto en departamento compartido. Baño compartido, cocina. A 12 min de ULima.', 550.00, 1, 12.0, 'AVAILABLE', FALSE, 5, 5, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(78, 'Estudio en San Borja (ULima)', 'Estudio independiente con baño privado, kitchenette. A 15 min de ULima.', 1150.00, 1, 21.0, 'AVAILABLE', TRUE, 5, 5, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(79, 'Cuarto en Surco zona residencial', 'Habitación amoblada con baño compartido. Zona tranquila. A 8 min de ULima.', 600.00, 1, 13.0, 'AVAILABLE', FALSE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(80, 'Departamento compartido en Surco', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 6 min de ULima.', 700.00, 1, 15.0, 'AVAILABLE', TRUE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(81, 'Cuarto económico en La Molina', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a ULima.', 420.00, 1, 10.0, 'AVAILABLE', FALSE, 5, 5, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(82, 'Habitación premium en Surco (ULima)', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de ULima.', 1000.00, 1, 18.0, 'AVAILABLE', TRUE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(83, 'Cuarto compartido en San Borja', 'Habitación en casa compartida. Baño compartido, cocina, wifi. A 10 min de ULima.', 480.00, 1, 11.0, 'AVAILABLE', FALSE, 5, 5, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(84, 'Estudio en Surco (ULima)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 5 min de ULima.', 1200.00, 1, 20.0, 'AVAILABLE', TRUE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(85, 'Cuarto en La Molina cerca de ULima', 'Habitación privada con baño compartido. Amoblado. Zona tranquila.', 520.00, 1, 12.0, 'AVAILABLE', FALSE, 5, 5, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(86, 'Cuarto privado en San Borja', 'Habitación con baño privado, cama, escritorio. A 8 min de ULima. Servicios incluidos.', 750.00, 1, 14.0, 'AVAILABLE', TRUE, 5, 5, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(87, 'Habitación amoblada en Surco', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a ULima.', 550.00, 1, 12.0, 'AVAILABLE', FALSE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(88, 'Departamento compartido en La Molina', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 10 min de ULima.', 620.00, 1, 14.0, 'AVAILABLE', TRUE, 5, 5, 18, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(89, 'Cuarto económico en San Borja', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a ULima.', 400.00, 1, 9.0, 'AVAILABLE', FALSE, 5, 5, 19, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(90, 'Estudio premium en Surco (ULima)', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 1300.00, 1, 24.0, 'AVAILABLE', TRUE, 5, 5, 17, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- USIL (landlord 6, university 6)
(91, 'Cuarto privado en La Molina (USIL)', 'Habitación amoblada con baño privado. A 5 min de USIL. Servicios incluidos.', 700.00, 1, 15.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(92, 'Habitación compartida en Ate', 'Cuarto en departamento compartido. Baño compartido, cocina. A 15 min de USIL.', 420.00, 1, 11.0, 'AVAILABLE', FALSE, 6, 6, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(93, 'Cuarto en Cieneguilla cerca de USIL', 'Habitación amoblada con baño compartido. Zona tranquila. A 25 min de USIL.', 350.00, 1, 10.0, 'AVAILABLE', FALSE, 6, 6, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(94, 'Departamento compartido en La Molina', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 7 min de USIL.', 600.00, 1, 14.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(95, 'Cuarto económico en Ate', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a USIL.', 350.00, 1, 9.0, 'AVAILABLE', FALSE, 6, 6, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(96, 'Habitación premium en La Molina (USIL)', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de USIL.', 900.00, 1, 17.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(97, 'Cuarto compartido en La Molina', 'Habitación en casa compartida. Baño compartido, cocina, wifi. Cerca a USIL.', 400.00, 1, 10.0, 'AVAILABLE', FALSE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(98, 'Estudio en La Molina (USIL)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 5 min de USIL.', 1050.00, 1, 18.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(99, 'Cuarto en Ate zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 420.00, 1, 12.0, 'AVAILABLE', FALSE, 6, 6, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(100, 'Cuarto privado en La Molina', 'Habitación con baño privado, cama, escritorio. A 6 min de USIL. Servicios incluidos.', 650.00, 1, 13.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(101, 'Habitación amoblada en Ate', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a USIL.', 380.00, 1, 11.0, 'AVAILABLE', FALSE, 6, 6, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(102, 'Departamento compartido en La Molina', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 8 min de USIL.', 550.00, 1, 14.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(103, 'Cuarto económico en Cieneguilla', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a USIL.', 300.00, 1, 9.0, 'AVAILABLE', FALSE, 6, 6, 22, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(104, 'Estudio premium en La Molina (USIL)', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 1200.00, 1, 22.0, 'AVAILABLE', TRUE, 6, 6, 20, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(105, 'Habitación en Ate cerca de USIL', 'Cuarto privado con baño compartido. Amoblado. Zona tranquila.', 400.00, 1, 11.0, 'AVAILABLE', FALSE, 6, 6, 21, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Universidad Ricardo Palma (landlord 7, university 7)
(106, 'Cuarto privado en Surco (URP)', 'Habitación amoblada con baño privado. A 5 min de Universidad Ricardo Palma. Servicios incluidos.', 800.00, 1, 16.0, 'AVAILABLE', TRUE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(107, 'Habitación compartida en Barranco', 'Cuarto en departamento compartido. Baño compartido, cocina. A 12 min de URP.', 580.00, 1, 12.0, 'AVAILABLE', FALSE, 7, 7, 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(108, 'Estudio en Miraflores (URP)', 'Estudio independiente con baño privado, kitchenette. A 15 min de URP.', 1250.00, 1, 22.0, 'AVAILABLE', TRUE, 7, 7, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(109, 'Cuarto en Surco zona residencial', 'Habitación amoblada con baño compartido. Zona tranquila. A 8 min de URP.', 620.00, 1, 13.0, 'AVAILABLE', FALSE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(110, 'Departamento compartido en Surco', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 6 min de URP.', 720.00, 1, 15.0, 'AVAILABLE', TRUE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(111, 'Cuarto económico en Barranco', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a URP.', 450.00, 1, 10.0, 'AVAILABLE', FALSE, 7, 7, 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(112, 'Habitación premium en Surco (URP)', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de URP.', 980.00, 1, 18.0, 'AVAILABLE', TRUE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(113, 'Cuarto compartido en Miraflores', 'Habitación en casa compartida. Baño compartido, cocina, wifi. A 12 min de URP.', 520.00, 1, 11.0, 'AVAILABLE', FALSE, 7, 7, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(114, 'Estudio en Surco (URP)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 5 min de URP.', 1100.00, 1, 19.0, 'AVAILABLE', TRUE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(115, 'Cuarto en Barranco cerca de URP', 'Habitación privada con baño compartido. Amoblado. Zona tranquila.', 580.00, 1, 12.0, 'AVAILABLE', FALSE, 7, 7, 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(116, 'Cuarto privado en Miraflores', 'Habitación con baño privado, cama, escritorio. A 10 min de URP. Servicios incluidos.', 850.00, 1, 14.0, 'AVAILABLE', TRUE, 7, 7, 25, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(117, 'Habitación amoblada en Surco', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a URP.', 600.00, 1, 12.0, 'AVAILABLE', FALSE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(118, 'Departamento compartido en Barranco', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 8 min de URP.', 680.00, 1, 15.0, 'AVAILABLE', TRUE, 7, 7, 24, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(119, 'Cuarto económico en Surco', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de URP.', 420.00, 1, 9.0, 'AVAILABLE', FALSE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(120, 'Estudio premium en Surco (URP)', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 1250.00, 1, 23.0, 'AVAILABLE', TRUE, 7, 7, 23, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- UNMSM (landlord 8, university 8)
(121, 'Cuarto privado en Breña', 'Habitación amoblada con baño privado. A 5 min de UNMSM. Servicios incluidos.', 600.00, 1, 15.0, 'AVAILABLE', TRUE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(122, 'Habitación compartida en Jesus Maria', 'Cuarto en departamento compartido. Baño compartido, cocina. A 10 min de UNMSM.', 450.00, 1, 12.0, 'AVAILABLE', FALSE, 8, 8, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(123, 'Cuarto en Lima centro', 'Habitación amoblada con baño compartido. Zona céntrica. A 8 min de UNMSM.', 400.00, 1, 11.0, 'AVAILABLE', FALSE, 8, 8, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(124, 'Habitación en Pueblo Libre', 'Cuarto privado con baño compartido. Amoblado. Cerca a paraderos.', 420.00, 1, 12.0, 'AVAILABLE', FALSE, 8, 8, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(125, 'Departamento compartido en Breña', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 6 min de UNMSM.', 520.00, 1, 14.0, 'AVAILABLE', TRUE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(126, 'Cuarto económico en Jesus Maria', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a UNMSM.', 380.00, 1, 10.0, 'AVAILABLE', FALSE, 8, 8, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(127, 'Habitación premium en Breña', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de UNMSM.', 750.00, 1, 16.0, 'AVAILABLE', TRUE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(128, 'Cuarto compartido en Lima', 'Habitación en casa compartida. Baño compartido, cocina, wifi. Cerca a UNMSM.', 350.00, 1, 10.0, 'AVAILABLE', FALSE, 8, 8, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(129, 'Estudio en Breña (UNMSM)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 5 min de UNMSM.', 800.00, 1, 17.0, 'AVAILABLE', TRUE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(130, 'Cuarto en Pueblo Libre cerca de UNMSM', 'Habitación privada con baño compartido. Amoblado. Zona tranquila.', 400.00, 1, 11.0, 'AVAILABLE', FALSE, 8, 8, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(131, 'Cuarto privado en Jesus Maria', 'Habitación con baño privado, cama, escritorio. A 8 min de UNMSM. Servicios incluidos.', 550.00, 1, 13.0, 'AVAILABLE', TRUE, 8, 8, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(132, 'Habitación amoblada en Breña', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a UNMSM.', 420.00, 1, 11.0, 'AVAILABLE', FALSE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(133, 'Departamento compartido en Lima', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 10 min de UNMSM.', 480.00, 1, 13.0, 'AVAILABLE', TRUE, 8, 8, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(134, 'Cuarto económico en Pueblo Libre', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de UNMSM.', 320.00, 1, 9.0, 'AVAILABLE', FALSE, 8, 8, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(135, 'Estudio premium en Breña (UNMSM)', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 900.00, 1, 19.0, 'AVAILABLE', TRUE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(136, 'Habitación en Jesus Maria cerca de UNMSM', 'Cuarto privado con baño compartido. Amoblado. Zona tranquila.', 480.00, 1, 12.0, 'AVAILABLE', FALSE, 8, 8, 27, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(137, 'Cuarto en Breña con vista', 'Habitación en departamento con vista. Baño privado, cocina, lavandería.', 680.00, 1, 15.0, 'AVAILABLE', TRUE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(138, 'Habitación compartida en Pueblo Libre', 'Cuarto en casa compartida. Baño compartido, cocina, wifi. Cerca a UNMSM.', 350.00, 1, 10.0, 'AVAILABLE', FALSE, 8, 8, 29, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(139, 'Estudio en Lima centro (UNMSM)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 6 min de UNMSM.', 750.00, 1, 16.0, 'AVAILABLE', TRUE, 8, 8, 28, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(140, 'Cuarto en Breña zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 450.00, 1, 12.0, 'AVAILABLE', FALSE, 8, 8, 26, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- USMP (landlord 9, university 9)
(141, 'Cuarto privado en Surquillo (USMP)', 'Habitación amoblada con baño privado. A 5 min de USMP. Servicios incluidos.', 650.00, 1, 15.0, 'AVAILABLE', TRUE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(142, 'Habitación compartida en San Luis', 'Cuarto en departamento compartido. Baño compartido, cocina. A 12 min de USMP.', 420.00, 1, 11.0, 'AVAILABLE', FALSE, 9, 9, 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(143, 'Cuarto en La Victoria cerca de USMP', 'Habitación amoblada con baño compartido. Zona céntrica. A 15 min de USMP.', 380.00, 1, 10.0, 'AVAILABLE', FALSE, 9, 9, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(144, 'Departamento compartido en Surquillo', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 6 min de USMP.', 550.00, 1, 14.0, 'AVAILABLE', TRUE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(145, 'Cuarto económico en San Luis', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a USMP.', 350.00, 1, 9.0, 'AVAILABLE', FALSE, 9, 9, 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(146, 'Habitación premium en Surquillo (USMP)', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de USMP.', 800.00, 1, 16.0, 'AVAILABLE', TRUE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(147, 'Cuarto compartido en La Victoria', 'Habitación en casa compartida. Baño compartido, cocina, wifi. Cerca a USMP.', 320.00, 1, 10.0, 'AVAILABLE', FALSE, 9, 9, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(148, 'Estudio en Surquillo (USMP)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 5 min de USMP.', 900.00, 1, 17.0, 'AVAILABLE', TRUE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(149, 'Cuarto en San Luis zona residencial', 'Habitación en depa compartido. Baño privado, cocina, lavandería. Cerca a paraderos.', 400.00, 1, 11.0, 'AVAILABLE', FALSE, 9, 9, 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(150, 'Cuarto privado en La Victoria', 'Habitación con baño privado, cama, escritorio. A 10 min de USMP. Servicios incluidos.', 480.00, 1, 12.0, 'AVAILABLE', TRUE, 9, 9, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(151, 'Habitación amoblada en Surquillo', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a USMP.', 450.00, 1, 11.0, 'AVAILABLE', FALSE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(152, 'Departamento compartido en San Luis', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 8 min de USMP.', 520.00, 1, 13.0, 'AVAILABLE', TRUE, 9, 9, 31, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(153, 'Cuarto económico en Surquillo', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de USMP.', 320.00, 1, 9.0, 'AVAILABLE', FALSE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(154, 'Estudio premium en Surquillo (USMP)', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 950.00, 1, 18.0, 'AVAILABLE', TRUE, 9, 9, 30, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(155, 'Habitación en La Victoria cerca de USMP', 'Cuarto privado con baño compartido. Amoblado. Zona tranquila.', 380.00, 1, 10.0, 'AVAILABLE', FALSE, 9, 9, 32, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
-- Universidad del Pacifico (landlord 10, university 10)
(156, 'Cuarto privado en Jesus Maria (UP)', 'Habitación amoblada con baño privado. A 5 min de Universidad del Pacifico. Servicios incluidos.', 900.00, 1, 16.0, 'AVAILABLE', TRUE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(157, 'Habitación compartida en San Isidro', 'Cuarto en departamento compartido. Baño compartido, cocina. A 10 min de UP.', 650.00, 1, 12.0, 'AVAILABLE', FALSE, 10, 10, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(158, 'Estudio en Lince (UP)', 'Estudio independiente con baño privado, kitchenette. A 8 min de UP.', 1150.00, 1, 20.0, 'AVAILABLE', TRUE, 10, 10, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(159, 'Cuarto en Magdalena cerca de UP', 'Habitación amoblada con baño compartido. Zona tranquila. A 12 min de UP.', 600.00, 1, 13.0, 'AVAILABLE', FALSE, 10, 10, 36, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(160, 'Departamento compartido en Jesus Maria', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 6 min de UP.', 750.00, 1, 15.0, 'AVAILABLE', TRUE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(161, 'Cuarto económico en San Isidro', 'Habitación básica con cama y closet. Baño y cocina compartidos. Cerca a UP.', 500.00, 1, 10.0, 'AVAILABLE', FALSE, 10, 10, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(162, 'Habitación premium en Jesus Maria (UP)', 'Cuarto amoblado con baño privado, smart TV. Edificio con seguridad. A 4 min de UP.', 1100.00, 1, 18.0, 'AVAILABLE', TRUE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(163, 'Cuarto compartido en Lince', 'Habitación en casa compartida. Baño compartido, cocina, wifi. Cerca a UP.', 480.00, 1, 11.0, 'AVAILABLE', FALSE, 10, 10, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(164, 'Estudio en Jesus Maria (UP)', 'Estudio independiente amoblado. Baño privado, kitchenette. A 5 min de UP.', 1250.00, 1, 21.0, 'AVAILABLE', TRUE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(165, 'Cuarto en Magdalena cerca de UP', 'Habitación privada con baño compartido. Amoblado. Zona tranquila.', 550.00, 1, 12.0, 'AVAILABLE', FALSE, 10, 10, 36, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(166, 'Cuarto privado en San Isidro', 'Habitación con baño privado, cama, escritorio. A 8 min de UP. Servicios incluidos.', 850.00, 1, 14.0, 'AVAILABLE', TRUE, 10, 10, 34, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(167, 'Habitación amoblada en Jesus Maria', 'Cuarto con cama, escritorio, closet. Baño compartido. Cerca a UP.', 600.00, 1, 12.0, 'AVAILABLE', FALSE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(168, 'Departamento compartido en Lince', 'Habitación en depa de 3 cuartos. Baño compartido, cocina, sala. A 7 min de UP.', 680.00, 1, 14.0, 'AVAILABLE', TRUE, 10, 10, 35, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(169, 'Cuarto económico en Jesus Maria', 'Habitación básica con cama y closet. Baño y cocina compartidos. Muy cerca de UP.', 450.00, 1, 9.0, 'AVAILABLE', FALSE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
(170, 'Estudio premium en Jesus Maria (UP)', 'Estudio amoblado con baño privado, kitchenette, balcón. Edificio con seguridad.', 1350.00, 1, 24.0, 'AVAILABLE', TRUE, 10, 10, 33, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON CONFLICT (id) DO NOTHING;

-- ----------------------------------------------------------------------------
-- Room images (Unsplash photos of rooms/apartments)
-- ----------------------------------------------------------------------------
INSERT INTO room_images (room_id, image) VALUES
(1, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(2, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(3, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(4, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(5, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(6, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(7, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(8, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(9, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(10, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(11, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(12, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(13, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(14, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(15, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(16, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(17, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(18, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(19, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(20, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(21, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(22, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(23, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(24, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(25, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(26, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(27, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(28, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(29, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(30, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(31, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(32, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(33, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(34, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(35, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(36, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(37, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(38, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(39, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(40, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(41, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(42, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(43, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(44, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(45, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(46, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(47, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(48, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(49, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(50, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(51, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(52, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(53, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(54, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(55, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(56, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(57, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(58, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(59, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(60, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(61, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(62, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(63, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(64, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(65, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(66, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(67, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(68, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(69, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(70, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(71, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(72, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(73, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(74, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(75, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(76, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(77, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(78, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(79, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(80, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(81, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(82, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(83, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(84, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(85, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(86, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(87, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(88, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(89, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(90, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb'),
(91, 'https://images.unsplash.com/photo-1560448204-e02f11c3d0e2'),
(92, 'https://images.unsplash.com/photo-1555854855-1ff3b8b49a5e'),
(93, 'https://images.unsplash.com/photo-1560185007-cde436f6a4d0'),
(94, 'https://images.unsplash.com/photo-1586023492125-27b2c045efd7'),
(95, 'https://images.unsplash.com/photo-1502672260266-1c1ef2d93688'),
(96, 'https://images.unsplash.com/photo-1522771739844-6a9a6d99a55d'),
(97, 'https://images.unsplash.com/photo-1631049307264-da0ec9d70304'),
(98, 'https://images.unsplash.com/photo-1522708323590-d24dbb6b0267'),
(99, 'https://images.unsplash.com/photo-1505691723518-36a5ac3be353'),
(100, 'https://images.unsplash.com/photo-1493809842364-78817add7ffb')
ON CONFLICT (room_id, image) DO NOTHING;