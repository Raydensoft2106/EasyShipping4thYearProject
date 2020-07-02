<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
 $receiver_id = $_POST["receiver_id"];
 $consignment_id = $_POST["consignment_id"];
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

$sql = "UPDATE consignments SET name= '$name', addressline1 = '$addressline1',  addressline2= '$addressline2', towncity= '$towncity', countystate= '$countystate', country= '$country', postcode= '$postcode', noOfParcels= '$noOfParcels', totalWeight= '$totalWeight', phoneno= '$phoneno', email='$email', additionalNote= '$additionalNote' WHERE consignment_id = $consignment_id AND receiver_id = $receiver_id AND status <> 'Ready For Delivery' AND status <> 'Delivered'";

$result = mysqli_query( $con,$sql );

if($result) {
	echo "Consignment Updated Successfully";
} 
else {
	echo 'Error: '. $con->error;
}

?>