CREATE DATABASE IF NOT EXISTS `id12253777_easyshippingdb` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci;
USE `id12253777_easyshippingdb`;

DROP TABLE IF EXISTS `consignments`;
CREATE TABLE `consignments` (
  `consignment_id` int(11) NOT NULL,
  `sender_id` int(11) DEFAULT NULL,
  `receiver_id` int(11) DEFAULT 0,
  `dispatchdate` date NOT NULL,
  `name` varchar(30) NOT NULL,
  `addressline1` varchar(20) NOT NULL,
  `addressline2` varchar(15) NOT NULL,
  `towncity` varchar(15) NOT NULL,
  `countystate` varchar(15) NOT NULL,
  `country` varchar(20) NOT NULL,
  `postcode` varchar(15) NOT NULL,
  `noOfParcels` int(11) NOT NULL,
  `totalWeight` varchar(10) NOT NULL,
  `phoneno` varchar(15) NOT NULL,
  `additionalNote` varchar(100) NOT NULL,
  `status` varchar(30) NOT NULL,
  `latestReport` varchar(1000) NOT NULL,
  `currentDepot` varchar(3) NOT NULL,
  `email` varchar(30) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `consignments` (`consignment_id`, `sender_id`, `receiver_id`, `dispatchdate`, `name`, `addressline1`, `addressline2`, `towncity`, `countystate`, `country`, `postcode`, `noOfParcels`, `totalWeight`, `phoneno`, `additionalNote`, `status`, `latestReport`, `currentDepot`, `email`) VALUES
(1, 1, 5, '2020-01-01', 'Ross Hayden', '51 Marian Pl', 'Kilcruttin', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'R35 X439', 1, '10kg', '0851234567', 'None', 'In Hub', 'Your parcel has been scanned at Depot 7 on 10/01/2\n26-03-2020: Your parcel has been scanned at Depot 1, Tipperary', '1', 'a00240194@student.ait.ie'),
(4, 1, 1, '2020-01-09', 'Jack Lynch', '32 Carraig Cluain', 'Collins Lane', 'Tullamore', 'County Offaly', 'Rep. of Ireland', 'N37 1243', 1, '2', '0851234567', 'None', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', NULL),
(8, 1, 0, '2020-01-10', 'Jack Daniel', '51 Marian Pl', 'Kilcruttin', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'R35 X439', 1, '4kg', '0861234567', 'Under Kennel', 'Ready For Delivery', 'Your parcel has been dispatched by the sender', '3', NULL),
(100000, 1, 3, '2020-01-21', 'Aaron O\'Toole', '119 The Green', 'Kilcoursey', 'Clara', 'Offaly', 'Rep of Ireland', 'R35 KX43', 1, '1', '0861234567', 'Note', 'Delivered', 'Your parcel has been delivered on 05-03-2020', '0', NULL),
(125095, 3, 6, '2020-04-03', 'Ronan Carroll', '60 Mayfield Grove', 'Curragh', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 P640', 5, '5kg', '0852948233', 'None', 'Delivered', '03-04-2020: Your parcel has been dispatched by the sender\n03-04-2020: Your parcel has been scanned at Depot 1, Westmeath\n03-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@gmail.com'),
(137916, 3, 0, '2020-02-29', 'Dean Flynn', '20 Caislean Riada', 'Coosan', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 TR64', 1, '4', '0852831466', 'None', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'rosshdn@gmail.com'),
(155905, 1, 3, '2020-01-22', 'Ryan Dunne', '46 Willow Park', 'Athlone', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 1234', 1, '5', '0852314566', 'Throw over fence if no answer', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'rosshdn@gmail.com'),
(183598, 3, 6, '2020-04-03', 'Ronan Carroll', '60 Mayfield Grove', 'Curragh', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 P640', 1, '5kg', '0852948233', 'None', 'Delivered', '03-04-2020: Your parcel has been dispatched by the sender\n03-04-2020: Your parcel has been scanned at Depot 1, Westmeath\n03-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@easyshipping.com'),
(220001, 3, 0, '2020-02-29', 'Tim Delaney', '23 Moreno', 'Cloghanboy', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 V6V6', 1, '5', '0852831466', 'None', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'rosshdn@gmail.com'),
(272211, 3, 0, '2020-03-29', 'Sharon Delaney', '44 Cypress Gardens', 'Garrycastle', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 K0N0', 1, '3kg', '0852831466', 'none', 'Delivered', '29-03-2020: Your parcel has been dispatched by the sender\r\n\n01-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@gmail.com'),
(317999, 3, 0, '2020-02-29', 'Ross Hayden', '14 Cypress Gardens', 'Garrycastle', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 K3C5', 1, '5', '0852831466', 'Throw into hedge', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'rosshdn@gmail.com'),
(322290, 3, 0, '2020-02-29', 'Mary Cutler', '18 Brawney Square', 'Lissywollen', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 V2P3', 1, '3', '0852831466', 'Just at the gate', 'Delivered', 'Your parcel has been dispatched by the sender\r\n\n03-04-2020: Your parcel has been scanned at Depot 1, Westmeath\n03-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@gmail.com'),
(360774, 3, 3, '2020-04-01', 'Joe Delaney', '90 Bloomfield Dr', 'Cloghanboy', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 YT53', 5, '5kg', '0852134569', 'In dog kennel', 'Dispatched', '01-04-2020: Your parcel has been dispatched by the sender', '0', 'rosshdn@mail3.com'),
(386832, 3, 0, '2020-04-03', 'Aaron Delaney', '4 Iona Villas', 'Bogganfin', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 VA48', 1, '4', '0852831466', 'RETURN TO SENDER', 'Dispatched', '03-04-2020: This parcel is being returned by the recipient', '0', 'rosshdn@gmail.com'),
(437281, 1, 3, '2020-03-29', 'Ross Hayden', '149 Ballin Ri', 'Collins Lane', 'Tullamore', 'Offaly', 'Ireland', 'R35 C967', 1, '1', '1978-02-14', 'RETURN TO SENDER', 'Dispatched', '29-03-2020: This parcel is being returned by the recipient', '0', ''),
(468841, 3, 0, '2020-03-29', 'Ciaran Pyke', '5 Roslevin Lawn', 'Cloghanboy West', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 R3N2', 1, '5kg', '0852832466', 'none', 'Delivered', '29-03-2020: Your parcel has been dispatched by the sender\r\n\n01-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@gmail.com'),
(534504, 3, 6, '2020-04-03', 'Ronan Carroll', '28 Sli An Aifrinn', 'Warrens Fields', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 CX38', 1, '5kg', '0852948233', 'None', 'Delivered', '03-04-2020: Your parcel has been dispatched by the sender\n03-04-2020: Your parcel has been scanned at Depot 1, Westmeath\n03-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@hotmail.com'),
(535539, 3, 0, '2020-03-09', 'Glenn Vickers', '43 Charleville View', 'Kilcruttin', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'R35 FA49', 1, '10Kg', '0859247394', 'Leave at door if not home', 'In Hub', '09-03-2020: Your parcel has been dispatched by the sender\n09-03-2020: Your parcel has been scanned at Depot 1, Offaly', '1', 'a00240194@student.ait.ie'),
(603045, 3, 0, '2020-02-29', 'Amy Keogh', '124 Sarsfield Square', 'Curragh', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 H298', 1, '3', '0852831466', 'None', 'Delivered', 'Your parcel has been dispatched by the sender\n03-04-2020: Your parcel has been scanned at Depot 1, Westmeath\n03-04-2020: Your parcel has been delivered by Driver: SEAN PARKER', '0', 'rosshdn@gmail.com'),
(727852, 3, 3, '2020-03-29', 'Joe Delaney', '90 Bloomfield Dr', 'Cloghanboy', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 YT53', 1, '5kg', '0852134569', 'In dog kennel', 'Ready For Delivery', '29-03-2020: Your parcel has been dispatched by the sender\n29-03-2020: Your parcel has been scanned at Depot 1, Westmeath', '1', 'rosshdn@mail3.com'),
(782955, 3, 0, '2020-03-08', 'Barry O\'Connor', '12 Cypress Gardens', 'Garrycastle', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 P086', 1, '10kg', '0862381748', 'None', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'a00240194@student.ait.ie'),
(791857, 3, 0, '2020-02-29', 'Sarah Benson', '5 Chestnut Ct', 'Cloghanboy', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 D625', 1, '4', '0852831466', 'Around the back', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'rosshdn@gmail.com'),
(867906, 3, 0, '2020-02-21', 'Mike McArdle', '7 Chancery Park Cl', 'Cloncollog', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'R35 C1H5\r\n', 1, '5', '0852831466', 'none', 'Ready For Delivery', 'Your parcel has been dispatched by the sender', '3', 'rosshdn@gmail.com'),
(874666, 1, 0, '2020-03-26', 'Roland Patrick', '5 Lenaboy Park', 'Salthill', 'Galway', 'Galway', 'Rep. of Ireland', 'H91 WC0F', 1, '10kg', '0851234567', 'Leave in garden', 'In Hub', '26-03-2020: Your parcel has been dispatched by the sender\r\n26-03-2020: Your parcel has been scanned at Depot 1, Westmeath', '1', 'rosshdn@gmail.com'),
(878607, 3, 0, '2020-03-08', 'John McCann', '16 Auburn Heights', 'Auburn Heights', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 TR83', 1, '4Kg', '0859264818', 'No', 'Dispatched', 'Your parcel has been dispatched by the sender', '0', 'a00240194@student.ait.ie'),
(936622, 1, 3, '2020-04-03', 'Ross Hayden', '149 Ballin Ri', 'Collins Lane', 'Tullamore', 'Offaly', 'Ireland', 'R35 C967', 1, '1', '1978-02-14', 'RETURN TO SENDER', 'Dispatched', '03-04-2020: This parcel is being returned by the recipient', '0', ''),
(938925, 3, 0, '2020-02-29', 'Glenn Hanlon', '7 Mitchells Terrace', 'Bellaugh', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 D4E6', 1, '12kg', '0852831466', 'None', 'Dispatched', 'Your parcel has been dispatched by the sender\r\n\r\n', '0', 'rosshdn@gmail.com');


DROP TABLE IF EXISTS `depots`;
CREATE TABLE `depots` (
  `depot` int(11) NOT NULL,
  `county` varchar(15) NOT NULL,
  `latitude` float NOT NULL,
  `longitude` float NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `depots` (`depot`, `county`, `latitude`, `longitude`) VALUES
(1, 'Westmeath', 53.4128, -7.89978),
(2, 'Tipperary', 52.3512, -7.41969),
(3, 'Offaly', 53.2837, -7.51218),
(4, 'Clare', 52.861, -8.98466),
(5, 'Galway', 53.5092, -8.87598),
(6, 'Mayo', 53.9369, -8.94306),
(7, 'Sligo', 54.1859, -8.48212),
(8, 'Roscommon', 53.7773, -8.24792),
(9, 'Waterford', 52.2571, -7.14748),
(10, 'Leitrim', 54.301, -8.16376),
(11, 'Derry', 54.9633, -7.71777),
(12, 'Longford', 53.6355, -8.17786),
(13, 'Cavan', 53.9965, -7.36729),
(14, 'Monaghan', 54.2571, -6.97974),
(15, 'Louth', 54.0034, -6.40692),
(16, 'Meath', 53.6443, -6.70716),
(17, 'Dublin', 53.317, -6.35665),
(18, 'Kildare', 53.1743, -6.79903),
(19, 'Portlaoise', 53.0256, -7.30605),
(20, 'Wicklow', 52.9863, -6.04488),
(21, 'Carlow', 52.8359, -6.93219),
(22, 'Kilkenny', 52.6564, -7.23368),
(23, 'Wexford', 52.329, -6.48465),
(24, 'Cork', 51.9115, -8.49199),
(25, 'Kerry', 52.2847, -9.70878),
(26, 'Limerick', 52.6636, -8.50494);


DROP TABLE IF EXISTS `driveremployees`;
CREATE TABLE `driveremployees` (
  `driver_id` int(11) NOT NULL,
  `depot` int(11) NOT NULL,
  `fName` varchar(15) NOT NULL,
  `lName` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `driveremployees` (`driver_id`, `depot`, `fName`, `lName`, `email`) VALUES
(6, 3, 'Ryan', 'McMahon', 'a00240194@student.ait.ie'),
(7, 1, 'Sean', 'Parker', 'driverdep1@mail.com'),
(8, 3, 'Shannon', 'Keogh', 'offalyDepDriver1@mail.com');


DROP TRIGGER IF EXISTS `newUser2`;
DELIMITER $$
CREATE TRIGGER `newUser2` AFTER INSERT ON `driveremployees` FOR EACH ROW INSERT INTO users(user_id, fName, lName, email, userType)
VALUES (NULL,
	NEW.fName,
	NEW.lName,
	NEW.email,
	'DriverEmployee'
	)
$$
DELIMITER ;


DROP VIEW IF EXISTS `driverUSERS`;
CREATE TABLE `driverUSERS` (
`user_id` int(11)
,`driver_id` int(11)
,`fName` varchar(15)
,`lName` varchar(20)
,`email` varchar(50)
);


DROP TABLE IF EXISTS `dummydeliveries`;
CREATE TABLE `dummydeliveries` (
  `consignment_id` int(11) NOT NULL,
  `sender_id` int(11) NOT NULL,
  `receiver_id` int(11) NOT NULL,
  `dispatchdate` date NOT NULL,
  `name` varchar(30) NOT NULL,
  `addressline1` varchar(20) NOT NULL,
  `addressline2` varchar(15) NOT NULL,
  `towncity` varchar(15) NOT NULL,
  `countystate` varchar(15) NOT NULL,
  `country` varchar(20) NOT NULL,
  `postcode` varchar(15) NOT NULL,
  `noOfParcels` int(11) NOT NULL,
  `totalWeight` varchar(10) NOT NULL,
  `phoneno` varchar(15) NOT NULL,
  `additionalNote` varchar(100) NOT NULL,
  `status` varchar(15) NOT NULL,
  `latestReport` varchar(100) NOT NULL,
  `qrImg` varchar(30) NOT NULL,
  `qrLink` varchar(120) NOT NULL,
  `currentDepot` varchar(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `dummydeliveries` (`consignment_id`, `sender_id`, `receiver_id`, `dispatchdate`, `name`, `addressline1`, `addressline2`, `towncity`, `countystate`, `country`, `postcode`, `noOfParcels`, `totalWeight`, `phoneno`, `additionalNote`, `status`, `latestReport`, `qrImg`, `qrLink`, `currentDepot`) VALUES
(9, 1, 1, '2020-01-14', 'Ross Hayden', '32 Thornbury Drive', 'Willow Park', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 F123', 1, '3', '0852831466', 'None', 'Delivered', 'Your parcel has been scanned at Depot 7 on 13-02-2020', '2.png', 'https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=2', '1'),
(10, 1, 1, '2020-01-14', 'Ross Hayden', '149 Ballin Ri', 'Collins Lane', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'N37 1234', 1, '3kg', '0861234567', 'Drop into reception', 'In Hub', 'Your parcel has been scanned at Depot 7 on 14-01-2', '2.png', 'https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=2', '1'),
(327528, 1, 1, '2020-01-21', 'QrToPdfTest2', '54 Carraig Cluain', 'Collins Lane', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'F37 1234', 1, '1', '0861234566', 'Under the gate door', 'In Hub', 'Your parcel has been scanned at Depot 7 on 21-01-2020', '2.png', 'https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=2', '1');

DROP VIEW IF EXISTS `employeeUSERS`;
CREATE TABLE `employeeUSERS` (
`user_id` int(11)
,`scanner_id` int(11)
,`fName` varchar(15)
,`lName` varchar(20)
,`email` varchar(50)
);

DROP TABLE IF EXISTS `receivingcustomers`;
CREATE TABLE `receivingcustomers` (
  `receiver_id` int(11) NOT NULL,
  `fName` varchar(15) NOT NULL,
  `lName` varchar(20) NOT NULL,
  `addressline1` varchar(20) NOT NULL,
  `addressline2` varchar(15) NOT NULL,
  `towncity` varchar(15) NOT NULL,
  `countystate` varchar(15) NOT NULL,
  `country` varchar(20) NOT NULL,
  `postcode` varchar(15) NOT NULL,
  `phoneno` varchar(15) NOT NULL,
  `hiddenloc` varchar(25) NOT NULL,
  `email` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `receivingcustomers` (`receiver_id`, `fName`, `lName`, `addressline1`, `addressline2`, `towncity`, `countystate`, `country`, `postcode`, `phoneno`, `hiddenloc`, `email`) VALUES
(1, 'Tom', 'Beades', '1 Ormonde Street', 'Gardens', 'Nenagh', 'Tipperary', 'Ireland', 'E45 DK57', '0852831466', 'Under the fence', ''),
(2, '', '', '', '', '', '', '', '', '', '', ''),
(3, 'Joe', 'Delaney', '90 Bloomfield Dr', 'Cloghanboy', 'Athlone', 'Westmeath', 'Ireland', 'N37 YT53', '0852134569', 'In dog kennel', 'rosshdn@mail3.com'),
(5, 'Ross', 'Hayden', '149 Ballin Ri', 'Collins Lane', 'Tullamore', 'Offaly', 'Rep. of Ireland', 'R35 C967', '0852831466', 'Over the gate', 'rosshdn@hotmail.com'),
(6, 'Ronan', 'Carroll', '60 Mayfield Grove', 'Curragh', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 P640', '0852948233', 'None', 'rosshdn@easyshipping.com');


DROP TRIGGER IF EXISTS `newUser`;
DELIMITER $$
CREATE TRIGGER `newUser` AFTER INSERT ON `receivingcustomers` FOR EACH ROW INSERT INTO users(user_id, fName, lName, email, userType)
VALUES (NULL,
	NEW.fName,
	NEW.lName,
	NEW.email,
	'ReceivingCustomer'
	)
$$
DELIMITER ;


DROP VIEW IF EXISTS `receivingUSERS`;
CREATE TABLE `receivingUSERS` (
`user_id` int(11)
,`receiver_id` int(11)
,`fName` varchar(15)
,`lName` varchar(20)
,`email` varchar(40)
);


DROP TABLE IF EXISTS `sendingcustomers`;
CREATE TABLE `sendingcustomers` (
  `sender_id` int(11) NOT NULL,
  `fName` varchar(15) NOT NULL,
  `lName` varchar(20) NOT NULL,
  `addressline1` varchar(20) NOT NULL,
  `addressline2` varchar(15) NOT NULL,
  `towncity` varchar(15) NOT NULL,
  `countystate` varchar(15) NOT NULL,
  `country` varchar(20) NOT NULL,
  `postcode` varchar(15) NOT NULL,
  `phoneno` varchar(15) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `sendingcustomers` (`sender_id`, `fName`, `lName`, `addressline1`, `addressline2`, `towncity`, `countystate`, `country`, `postcode`, `phoneno`, `email`) VALUES
(1, 'Ross', 'Hayden', '149 Ballin Ri', 'Collins Lane', 'Tullamore', 'Offaly', 'Ireland', 'R35 C967', '1978-02-14', ''),
(3, 'Aaron', 'Delaney', '4 Iona Villas', 'Bogganfin', 'Athlone', 'Westmeath', 'Rep. of Ireland', 'N37 VA48', '0852831466', 'rosshdn@gmail.com');


DROP TRIGGER IF EXISTS `newUser3`;
DELIMITER $$
CREATE TRIGGER `newUser3` AFTER INSERT ON `sendingcustomers` FOR EACH ROW INSERT INTO users(user_id, fName, lName, email, userType)
VALUES (NULL,
	NEW.fName,
	NEW.lName,
	NEW.email,
	'SendingCustomer'
	)
$$
DELIMITER ;


DROP VIEW IF EXISTS `sendingUSERS`;
CREATE TABLE `sendingUSERS` (
`user_id` int(11)
,`sender_id` int(11)
,`fName` varchar(15)
,`lName` varchar(20)
,`email` varchar(50)
);


DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL,
  `fName` varchar(15) NOT NULL,
  `lName` varchar(20) NOT NULL,
  `email` varchar(30) NOT NULL,
  `usertype` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


INSERT INTO `users` (`user_id`, `fName`, `lName`, `email`, `usertype`) VALUES
(1, 'Ross', 'Hayden', 'rosshdn@gmail.com', 'SendingCustomer'),
(2, 'Tom', 'Beades', 'tbeades@mail.com', 'ReceivingCustomer'),
(3, 'James', 'Connolly', 'jconnolly@mail.com', 'DriverEmployee'),
(4, 'Ryan', 'Smith', 'rsmith@mail.com', 'WarehouseEmployee'),
(5, 'Carol', 'Brock', 'cb@mail.com', 'Sending Customer'),
(10, 'Ross', 'Hayden', 'rosshdn@hotmail.com', 'ReceivingCustomer'),
(11, 'Ryan', 'McMahon', 'a00240194@student.ait.ie', 'DriverEmployee'),
(12, 'Aaron', 'Delaney', 'rosshdn@gmail.com', 'SendingCustomer'),
(13, 'Dean', 'Ganley', 'rosshdn@mail.com', 'WarehouseEmployee'),
(14, 'Sean', 'Parker', 'driverdep1@mail.com', 'DriverEmployee'),
(15, 'Thomas', 'Corcoran', 'offalyDepScan1@mail.com', 'WarehouseEmployee'),
(16, 'Shannon', 'Keogh', 'offalyDepDriver1@mail.com', 'DriverEmployee'),
(17, 'Ronan', 'Carroll', 'rosshdn@easyshipping.com', 'ReceivingCustomer');


DROP TABLE IF EXISTS `warehouseemployees`;
CREATE TABLE `warehouseemployees` (
  `scanner_id` int(11) NOT NULL,
  `depot` int(11) NOT NULL,
  `fName` varchar(15) NOT NULL,
  `lName` varchar(20) NOT NULL,
  `email` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

INSERT INTO `warehouseemployees` (`scanner_id`, `depot`, `fName`, `lName`, `email`) VALUES
(1, 1, 'Ryan', 'Smith', ''),
(3, 1, 'Dean', 'Ganley', 'rosshdn@mail.com'),
(4, 3, 'Thomas', 'Corcoran', 'offalyDepScan1@mail.com');


DROP TRIGGER IF EXISTS `newUser4`;
DELIMITER $$
CREATE TRIGGER `newUser4` AFTER INSERT ON `warehouseemployees` FOR EACH ROW INSERT INTO users(user_id, fName, lName, email, userType)
VALUES (NULL,
	NEW.fName,
	NEW.lName,
	NEW.email,
	'WarehouseEmployee'
	)
$$
DELIMITER ;

DROP TABLE IF EXISTS `driverUSERS`;

CREATE ALGORITHM=UNDEFINED DEFINER=`id12253777_easyshipping`@`%` SQL SECURITY DEFINER VIEW `driverUSERS`  AS  select `b`.`user_id` AS `user_id`,`a`.`driver_id` AS `driver_id`,`a`.`fName` AS `fName`,`a`.`lName` AS `lName`,`a`.`email` AS `email` from (`driveremployees` `a` join `users` `b`) where `a`.`fName` = `b`.`fName` and `a`.`lName` = `b`.`lName` ;

DROP TABLE IF EXISTS `employeeUSERS`;

CREATE ALGORITHM=UNDEFINED DEFINER=`id12253777_easyshipping`@`%` SQL SECURITY DEFINER VIEW `employeeUSERS`  AS  select `b`.`user_id` AS `user_id`,`a`.`scanner_id` AS `scanner_id`,`a`.`fName` AS `fName`,`a`.`lName` AS `lName`,`a`.`email` AS `email` from (`warehouseemployees` `a` join `users` `b`) where `a`.`fName` = `b`.`fName` and `a`.`lName` = `b`.`lName` ;

DROP TABLE IF EXISTS `receivingUSERS`;

CREATE ALGORITHM=UNDEFINED DEFINER=`id12253777_easyshipping`@`%` SQL SECURITY DEFINER VIEW `receivingUSERS`  AS  select `b`.`user_id` AS `user_id`,`a`.`receiver_id` AS `receiver_id`,`a`.`fName` AS `fName`,`a`.`lName` AS `lName`,`a`.`email` AS `email` from (`receivingcustomers` `a` join `users` `b`) where `a`.`fName` = `b`.`fName` and `a`.`lName` = `b`.`lName` ;

DROP TABLE IF EXISTS `sendingUSERS`;

CREATE ALGORITHM=UNDEFINED DEFINER=`id12253777_easyshipping`@`%` SQL SECURITY DEFINER VIEW `sendingUSERS`  AS  select `b`.`user_id` AS `user_id`,`a`.`sender_id` AS `sender_id`,`a`.`fName` AS `fName`,`a`.`lName` AS `lName`,`a`.`email` AS `email` from (`sendingcustomers` `a` join `users` `b`) where `a`.`fName` = `b`.`fName` and `a`.`lName` = `b`.`lName` ;

ALTER TABLE `consignments`
  ADD PRIMARY KEY (`consignment_id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`);

ALTER TABLE `depots`
  ADD PRIMARY KEY (`depot`);

ALTER TABLE `driveremployees`
  ADD PRIMARY KEY (`driver_id`),
  ADD KEY `depot` (`depot`);

ALTER TABLE `dummydeliveries`
  ADD PRIMARY KEY (`consignment_id`),
  ADD KEY `sender_id` (`sender_id`),
  ADD KEY `receiver_id` (`receiver_id`);

ALTER TABLE `receivingcustomers`
  ADD PRIMARY KEY (`receiver_id`);

ALTER TABLE `sendingcustomers`
  ADD PRIMARY KEY (`sender_id`);

ALTER TABLE `users`
  ADD PRIMARY KEY (`user_id`);

ALTER TABLE `warehouseemployees`
  ADD PRIMARY KEY (`scanner_id`),
  ADD KEY `depot` (`depot`);

ALTER TABLE `consignments`
  MODIFY `consignment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=964910;

ALTER TABLE `driveremployees`
  MODIFY `driver_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

ALTER TABLE `dummydeliveries`
  MODIFY `consignment_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=831976;

ALTER TABLE `receivingcustomers`
  MODIFY `receiver_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;

ALTER TABLE `sendingcustomers`
  MODIFY `sender_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

ALTER TABLE `users`
  MODIFY `user_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=18;

ALTER TABLE `warehouseemployees`
  MODIFY `scanner_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;

ALTER TABLE `driveremployees`
  ADD CONSTRAINT `driveremployees_ibfk_2` FOREIGN KEY (`depot`) REFERENCES `depots` (`depot`);

ALTER TABLE `warehouseemployees`
  ADD CONSTRAINT `warehouseemployees_ibfk_2` FOREIGN KEY (`depot`) REFERENCES `depots` (`depot`);
COMMIT;