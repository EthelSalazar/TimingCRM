ALTER TABLE leads ALTER COLUMN date_data DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN zip DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN city DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN approach_notes DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN approach_notes_414 DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN status DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN approach_type_id DROP NOT NULL;
ALTER TABLE leads ALTER COLUMN prospect_category_id DROP NOT NULL;


commit;