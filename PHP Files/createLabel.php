<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
$date = date('Y-m-d H:i:s');

$consignment_id = $_POST["consignment_id"];
$sender_id = $_POST["sender_id"];
$receiver_id = $_POST["receiver_id"];
$dispatchdate =  $date;
$currentDepot = $_POST["currentDepot"];
$name = $_POST["name"];
$addressline1 = $_POST["addressline1"];
$addressline2 = $_POST["addressline2"];
$towncity = $_POST["towncity"];
$countystate = $_POST["countystate"];
$country = $_POST["country"];
$postcode = $_POST["postcode"];
$noOfParcels = $_POST["noOfParcels"];
$totalWeight = $_POST["totalWeight"];
$phoneno = $_POST["phoneno"];
$email = $_POST["email"];
$additionalNote = $_POST["additionalNote"];
$status = $_POST["status"];
$latestReport = $_POST["latestReport"];

$name = str_replace("'","''",$name);
$addressline1 = str_replace("'","''",$addressline1);
$addressline2 = str_replace("'","''",$addressline2);
$towncity = str_replace("'","''",$towncity);
$countystate = str_replace("'","''",$countystate);
$country = str_replace("'","''",$country);
$email = str_replace("'","''",$email);
$additionalNote = str_replace("'","''",$additionalNote);

if(!$con){
die('could not connect'.mysql_error());
}

$sql = "INSERT INTO consignments (consignment_id, sender_id, receiver_id, dispatchdate, currentDepot, name, addressline1, addressline2, towncity, countystate, country, postcode, noOfParcels, totalWeight, phoneno, email, additionalNote, status, latestReport) VALUES ('$consignment_id', '$sender_id', '$receiver_id', '$dispatchdate', '$currentDepot', '$name', '$addressline1', '$addressline2', '$towncity', '$countystate', '$country', '$postcode', '$noOfParcels', '$totalWeight', '$phoneno', '$email', '$additionalNote', '$status', '$latestReport')";



$result = mysqli_query( $con,$sql );

if($result) {
	echo "Label Printed Successfully";
} 
else {
	echo 'Error: '. $con->error;
}

?>