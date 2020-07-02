<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');// root
define('PASS', 'RossHayden99!');// ''
define('DB','id12253777_easyshippingdb');// easyshipping1

$conn =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
if(!$conn){
die('could not connect'.mysql_error());
}

$currentDepot = $_GET["currentDepot"];
$query="SELECT * FROM consignments where currentDepot='{$currentDepot}' AND status = 'Ready For Delivery'";
//change status to = 'Ready For Delivery'

$result = mysqli_query($conn, $query);
while(($row = mysqli_fetch_assoc($result)) == true){
	$data[]=$row;
}
echo json_encode($data);
?>