drop table if exists Verwendung;
drop table if exists Verleih;
drop table if exists Rad;
drop table if exists Lager;
drop table if exists Kunde;
drop table if exists Mitarbeiter;

create table Mitarbeiter(
                            Mitarbeiter_name			varchar(50) Primary Key,
                            Mitarbeiter_handynummer		varchar(10) not null
);

create table Kunde(
                      Kunden_ID			INT PRIMARY KEY ,
                      Kunden_name			varchar(40) not null,
                      Kunden_adresse		varchar(70) not null,
                      Kunden_Telephonnummer varchar(70) not null,
                      Kunden_schaeden		INT CONSTRAINT cn_schaeden CHECK (Kunden_schaeden between 1 and 5)
);

create table Lager(
                      Lager_ID			int Primary key,
                      Lager_name		varchar(30)
);

create table Rad(
                    Rad_ID				serial PRIMARY KEY,
                    Rad_name			varchar(50) not null,
                    Rad_groese			char constraint cn_groese check (Rad_groese in ('S', 'M', 'L')),
                    Rad_marke			varchar(50) not null,
                    Rad_Kaufpreis		int not null,
                    Rad_Lager			int,
                    constraint FK_Rad_Lager foreign key(Rad_Lager) references Lager on Delete Cascade
);

alter sequence rad_rad_id_seq restart with 6;

create Table Verleih(
                        Verleih_ID				serial primary key,
                        Verleih_Dauer_In_Tagen	int not null,
                        Verleih_Preis			int not null,
                        Verleih_Anfangsdatum	date not null,
                        Verleih_Zusatz_Tools	varchar(50),
                        Verleih_Kunde			int not null,
                        Verleih_Mitarbeiter		varchar(50) not null,
                        constraint FK_Verleih foreign key (Verleih_Kunde) references Kunde,
                        constraint FK_Mitarbeiter foreign key (Verleih_Mitarbeiter) references Mitarbeiter
);

alter sequence verleih_verleih_id_seq restart with 6;

create table verwendung(
                           verw_Rad			int not null,
                           verw_Verleih		int not null,
                           Constraint FK_Rad_ID foreign key (verw_rad) references Rad on delete CASCADE,
                           Constraint FK_Verleih_ID foreign key (verw_verleih) references Verleih,
                           Constraint PK_verw primary key (verw_Rad, verw_Verleih)
);