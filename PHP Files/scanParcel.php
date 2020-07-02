<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');


$consignment_id = $_POST["consignment_id"];
$status = $_POST["status"];
$latestReport = $_POST["latestReport"];
$currentDepot = $_POST["currentDepot"];
 
 if(!$con){
die('could not connect'.mysql_error());
}

$sql = "UPDATE consignments SET status= '$status', latestReport = CONCAT(latestReport,'$latestReport'), currentDepot= '$currentDepot' WHERE consignment_id = $consignment_id";
// = "UPDATE dummydeliveries SET status= '$status' WHERE consignment_id = $consignment_id";

$result = mysqli_query( $con,$sql );

if($result) {
	echo "Label Scanned Successfully";
} 
else {
	echo 'Error: '. $con->error;
}

?>