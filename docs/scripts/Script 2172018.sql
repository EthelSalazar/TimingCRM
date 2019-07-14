ALTER TABLE employee_repdetail
ADD user_telemktcont_id  integer,
ADD FOREIGN KEY (user_telemktcont_id) REFERENCES USERS (ID);

ALTER TABLE QUESTIONS
ADD SPECIAL_VALIDATION VARCHAR,
ADD depending BOOLEAN,
ADD question_dependent_id INTEGER,
ADD extra_label VARCHAR,
ADD FOREIGN KEY (question_dependent_id) REFERENCES QUESTIONS (ID);

ALTER TABLE QUESTION_VALUES
ADD desqualifying BOOLEAN,
ADD result_call_id INTEGER,
ADD FOREIGN KEY (result_call_id) REFERENCES result_call (ID);

ALTER TABLE APPOINTMENT
ADD REPDETAIL_COMMENT VARCHAR,
ADD APPOINTMENT_PARENT_ID INTEGER,
ADD COMMENT_SUMMARY VARCHAR,
ADD FOREIGN KEY (APPOINTMENT_PARENT_ID) REFERENCES APPOINTMENT (ID);

ALTER TABLE CALL
ADD FOLLOWUP_APPOINTMENT_ID INTEGER,
ADD FOREIGN KEY (FOLLOWUP_APPOINTMENT_ID) REFERENCES APPOINTMENT (ID);

ALTER TABLE EMPLOYEE_REPDETAIL
ADD user_telemktcont_id INTEGER,
ADD FOREIGN KEY (user_telemktcont_id) REFERENCES USERS (ID);


COMMIT;

DELETE FROM QUESTION_VALUES;
DELETE FROM QUESTIONS;
COMMIT;

INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(1,'CONOCE RP',2,NOW(),NOW(),TRUE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(2,'COMO CONOCIO RP',3,NOW(),NOW(),false,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(3,'TIENE PRODUCTOS RP',2,NOW(),NOW(),FALSE,1,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(4,'QUE PRODUCTOS TIENE DE RP',1,NOW(),NOW(),FALSE,1,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(5,'CUANDO FUE SU ULTIMA COMPRA',1,NOW(),NOW(),FALSE,1,'MESES','DATE_LAST_PURCHASE');
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(6,'ESTARIA INTERESADO EN COMPRAR PRODUCTOS RP',2,NOW(),NOW(),FALSE,NULL,NULL,'LEAD_INTERESTED');
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(7,'PARA QUIEN COCINA EN CASA',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(8,'LE GUSTA COCINAR',2,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(9,'EN QUE TIPO DE OLLAS COCINA',3,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(10,'CONSUME AGUA DE BOTELLA O DE ALGUN TIPO DE FILTRO',3,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(11,'USTED TIENE UN ACENTO DE <?> ES USTED DE',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(12,'QUE PLATO TIPICO COCINA USTED',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(13,'A QUE HORA LLEGA USTED DEL TRABAJO',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(14,'TRABAJA EN',1,NOW(),NOW(),FALSE,question_dependent_id_value,NULL,'extra_label_value');
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(15,'A QUE HORA SE ENCUENTRA USTED Y SU PAREJA EN CASA',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(16,'SU PAREJA TRABAJA EN',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);
INSERT INTO questions(id,description,TYPE,created_on,updated_on,depending,question_dependent_id,extra_label, SPECIAL_VALIDATION)
VALUES(17,'QUE PRODUCTO RP LE INTERESARIA',1,NOW(),NOW(),FALSE,NULL,NULL,NULL);

COMMIT;

INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(1,'SI',1,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(2,'NO',1,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(3,'AMIGO/FLIA',2,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(4,'TV',2,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(5,'DEMO',2,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(6,'HA ESCUCHADO',2,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(7,'OTRO',2,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(8,'SI',3,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(9,'NO',3,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(10,'SI',6,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(11,'NO',6,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(12,'SI',8,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(13,'NO',8,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(14,'A VECES',8,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(15,'ACERO',9,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(16,'VIDRIO',9,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(17,'TEFLON',9,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(18,'OTRO',9,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(19,'BOTELLA',10,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(20,'FILTRO NEVERA',10,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(21,'FILTRO NO RP',10,NOW(),NOW(),NULL,NULL);
INSERT INTO question_values(id,description,questions_id,created_on,updated_on,desqualifying,result_call_id)VALUES(22,'FILTRO RP',10,NOW(),NOW(),NULL,NULL);

COMMIT;

INSERT INTO result_call(id,description,created_on,updated_on)VALUES(9,'NO INTERESADO EN COMPRAR RP',NOW(),NOW());

COMMIT;
