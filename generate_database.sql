create table aktie
(
    ID         int(10)      null,
    ISIN       varchar(12)  not null
        primary key,
    WKN        int(6)       null,
    Branche    varchar(255) null,
    Aktienname varchar(255) null,
    Aktienlink varchar(255) null,
    `Index`    varchar(255) null
);

create table bilanz
(
    ID               int(10)      null,
    ISIN             varchar(12)  not null
        primary key,
    Jahr             time         null,
    Umsatz           double       null,
    Analystenmeinung varchar(255) null,
    Gewinn           double       null,
    EBIT             double       null,
    Eigenkapital     double       null,
    Fremdkapital     double       null,
    Waehrung         varchar(255) null,
    letztesUpdate    timestamp    null,
    constraint bilanz_aktie_ISIN_fk
        foreign key (ISIN) references aktie (ISIN)
);

create table `index`
(
    ID        int(10)      null,
    ISIN      varchar(12)  not null
        primary key,
    WKN       int(6)       null,
    Indexname varchar(255) null,
    Land      varchar(255) null
);

create table kurs
(
    ID            int(10)      null,
    ISIN          varchar(12)  not null
        primary key,
    Datum         time         null,
    Kurs          double       null,
    Waehrung      varchar(255) null,
    letztesUpdate timestamp    null,
    constraint kurs_aktie_ISIN_fk
        foreign key (ISIN) references aktie (ISIN)
);
