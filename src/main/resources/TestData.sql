delete from verwendung;
delete from Verleih;
delete from Rad;
delete from Lager;
delete from Kunde;
delete from Mitarbeiter;

insert into Mitarbeiter values ('Flo', '0660000');
insert into Mitarbeiter values ('Lukas', '94282058');
insert into Mitarbeiter values ('Nick', '9834021820');
insert into Mitarbeiter values ('Vici', '328053247');
insert into Mitarbeiter values ('Magda', '23473094');
insert into Mitarbeiter values ('Lucas', '3948203482');

insert into Kunde values (1, 'Kainrath', 'zuhause 1', '940385034', 5);
insert into Kunde values (2, 'LuxusKas', 'zuhause 2', '066412345', 4);
insert into Kunde values (3, 'Grashalm', 'dahamam 5', '239530931', 3);
insert into Kunde values (4, 'Lena', 'weit weg 2344', '943782305', 2);
insert into Kunde values (5, 'Sesselbauer', 'Weg 17', '303885845', 1);

insert into Lager values (1, 'Hauptlager');
insert into Lager values (2, 'BMX');
insert into Lager values (3, 'Trail');
insert into Lager values (4, 'MTB');
insert into Lager values (5, 'E-bikes');

insert into Rad values (1, 'rider', 'M', 'KTM', 1300, 4);
insert into Rad values (2, 'Kato', 'S', 'Ghost', 1800, 1);
insert into Rad values (3, 'treking', 'L', 'Trek', 3850, 2);
insert into Rad values (4, 'Torque', 'M', 'Canyon', 15000, 3);
insert into Rad values (5, 'bikers', 'M', 'Specialized', 6000, 5);

insert into Verleih values (1, 32, 325, '15-12-2004', null, 1, 'Flo');
insert into Verleih values (2, 1, 52, '17-12-2004', 'GPS', 3, 'Flo');
insert into Verleih values (3, 4, 234, '13-12-2004', 'Handy-Halterung', 2, 'Lukas');
insert into Verleih values (4, 43, 143, '12-12-2004', 'Flaschenhalterung', 4, 'Nick');
insert into Verleih values (5, 74, 314, '12-12-2004', null, 1, 'Lukas');

insert into verwendung values (1, 1);
insert into verwendung values (2, 1);
insert into verwendung values (3, 1);
insert into verwendung values (4, 1);
insert into verwendung values (1, 2);
insert into verwendung values (4, 3);
insert into verwendung values (2, 3);
insert into verwendung values (3, 4);
insert into verwendung values (1, 5);