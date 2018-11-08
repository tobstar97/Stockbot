create table aktie
(
  Aktienname varchar(255) null,
  ISIN       varchar(255) not null
    primary key,
  WKN        decimal      null,
  Kurs       decimal      null,
  Umsatz     decimal      null,
  Gewinn     decimal      null
);


create table `index`
(
  Indexname         varchar(255) not null
    primary key,
  Unternehmenanzahl int          null,
  Kurs              decimal      null,
  Land              varchar(255) null,
  Waehrung          varchar(255) null
)
  comment 'Aktienindex (z.B. DAX)';
