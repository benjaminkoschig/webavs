---------------------------------------------------------------
-----   NAOS.SQL
---------------------------------------------------------------
-- Script AC2
update SCHEMA.AFCOTIP set medfin = (case when meddeb <= 20221231 then 20221231 else meddeb end), metmot = 803035, PSPY = '20221115090700mmo'where MEICOT in (
    select cot.MEICOT
    from SCHEMA.AFCOTIP cot
             inner join SCHEMA.AFASSUP ass on (ass.MBIASS = cot.MBIASS and ass.MBTTYP = 812007)
    where (cot.MEDFIN > 20221231 or cot.MEDFIN = 0)
);