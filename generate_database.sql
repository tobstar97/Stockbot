create table aktie
(
    ISIN       varchar(12)  not null
        primary key,
    WKN        varchar(255) null,
    Branche    varchar(255) null,
    Aktienname varchar(255) null,
    Aktienlink varchar(255) null
);

create table bilanz
(
    ISIN          varchar(12)  not null,
    Jahr          int          not null,
    Umsatz        double       null,
    Gewinn        double       null,
    EBIT          double       null,
    Eigenkapital  double       null,
    Fremdkapital  double       null,
    Waehrung      varchar(255) null,
    letztesUpdate varchar(255) null
);

create table `index`
(
    ISIN      varchar(12)  not null
        primary key,
    WKN       int(6)       null,
    Indexname varchar(255) null,
    Land      varchar(255) null
);

create table kurs
(
    ISIN          varchar(12)  not null,
    Datum         varchar(255) null,
    Kurs          double       null,
    Waehrung      varchar(255) null,
    letztesUpdate varchar(255) null
);
