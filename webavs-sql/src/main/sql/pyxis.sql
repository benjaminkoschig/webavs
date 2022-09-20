---------------------------------------------------------------
-----   PYXIS.SQL
---------------------------------------------------------------

CREATE TABLE SCHEMA.TIREFPA (
    REFPAID numeric (15,0) NOT NULL,
	REFPALIB VARCHAR(50),
	REFPAREF numeric (27,0),
	REFPADDB VARCHAR(25),
	REFPADDF VARCHAR(25),
	HITIAD numeric (15,0),
	HIIAPA numeric (15,0),
	PSPY CHAR(24) DEFAULT NULL,
	PRIMARY KEY( REFPAID ) ) ;

ALTER TABLE SCHEMA.TIADMIP ADD COLUMN HBSEID VARCHAR(14);
REORG table SCHEMA.TIADMIP;
-- call SYSPROC.ADMIN_CMD('reorg table TIADMIP');
