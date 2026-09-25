-- Seed-data til demonstration af sporbarhedskaeden:
-- Animal -> Part -> Tray -> ProductTray -> Product
--
-- Scenarie:
--   DK-001 og DK-002 bidrager begge til tray 1 ("Ham"), som indgaar i
--   produkt 1 ("Half animal"). Saa produkt 1 kan indeholde dele fra begge.
--
--   DK-001 bidrager ogsaa til tray 2 ("Chop"), som indgaar i produkt 2
--   ("Box of 20 chops"). Saa DK-001 indgaar i BAADE produkt 1 og produkt 2.
--
--   DK-003 er helt uafhaengig og bidrager kun til tray 3, som indgaar i
--   produkt 3. God til at teste at dyr IKKE fejlagtigt kobles paa forkerte
--   produkter.

INSERT INTO animal (id, registration_number, species, weight_kg, arrived_at) VALUES
  (1, 'DK-001', 'Pig', 85.5, '2026-09-01T08:00:00Z'),
  (2, 'DK-002', 'Pig', 90.2, '2026-09-01T08:15:00Z'),
  (3, 'DK-003', 'Pig', 78.0, '2026-09-01T08:30:00Z');

INSERT INTO tray (id, part_type, max_weight_kg) VALUES
  (1, 'Ham', 50.0),
  (2, 'Chop', 30.0),
  (3, 'Ham', 50.0);

INSERT INTO part (id, animal_id, tray_id, part_type, weight_kg) VALUES
  (1, 1, 1, 'Ham', 12.0),  -- DK-001 -> tray 1
  (2, 2, 1, 'Ham', 13.0),  -- DK-002 -> tray 1 (samme tray som DK-001)
  (3, 1, 2, 'Chop', 8.0),  -- DK-001 -> tray 2
  (4, 3, 3, 'Ham', 15.0);  -- DK-003 -> tray 3

INSERT INTO product (id, product_type, packed_at) VALUES
  (1, 'Half animal', '2026-09-02T10:00:00Z'),
  (2, 'Box of 20 chops', '2026-09-02T10:30:00Z'),
  (3, 'Ham package', '2026-09-02T11:00:00Z');

INSERT INTO product_tray (id, product_id, tray_id) VALUES
  (1, 1, 1),  -- produkt 1 pakket fra tray 1 (DK-001 + DK-002)
  (2, 2, 2),  -- produkt 2 pakket fra tray 2 (DK-001)
  (3, 3, 3);  -- produkt 3 pakket fra tray 3 (DK-003)
