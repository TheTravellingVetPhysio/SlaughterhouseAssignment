-- Seed-data til demonstration af sporbarhedskaeden:
-- Animal -> Part -> Tray -> ProductTray -> Product
--
-- Id'er udelades: databasen tildeler dem selv. Fordi den er tom ved hver
-- opstart (ddl-auto=create), bliver raekkerne nummereret 1, 2, 3 ... i
-- den raekkefoelge de staar her. Fremmednoegler nedenfor bygger paa det.
--
-- Scenarie:
--   DK-001 og DK-002 bidrager begge til tray 1 ("Ham"), som indgaar i produkt 1.
--   DK-001 bidrager ogsaa til tray 2 ("Chop"), som indgaar i produkt 2.
--   DK-003 er uafhaengig: tray 3 -> produkt 3.

INSERT INTO animal (registration_number, species, weight_kg, arrived_at) VALUES
                                                                             ('DK-001', 'Pig', 85.5, '2026-09-01T08:00:00Z'),
                                                                             ('DK-002', 'Pig', 90.2, '2026-09-01T08:15:00Z'),
                                                                             ('DK-003', 'Pig', 78.0, '2026-09-01T08:30:00Z');

INSERT INTO tray (part_type, max_weight_kg) VALUES
                                                ('Ham', 50.0),
                                                ('Chop', 30.0),
                                                ('Ham', 50.0);

INSERT INTO part (animal_id, tray_id, part_type, weight_kg) VALUES
                                                                (1, 1, 'Ham', 12.0),
                                                                (2, 1, 'Ham', 13.0),
                                                                (1, 2, 'Chop', 8.0),
                                                                (3, 3, 'Ham', 15.0);

INSERT INTO product (product_type, packed_at) VALUES
                                                  ('Half animal', '2026-09-02T10:00:00Z'),
                                                  ('Box of 20 chops', '2026-09-02T10:30:00Z'),
                                                  ('Ham package', '2026-09-02T11:00:00Z');

INSERT INTO product_tray (product_id, tray_id) VALUES
                                                   (1, 1),
                                                   (2, 2),
                                                   (3, 3);