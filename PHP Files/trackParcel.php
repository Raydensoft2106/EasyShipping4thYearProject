<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$conn =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
if(!$conn){
die('could not connect'.mysql_error());
}

$consignment_id=$_GET['consignment_id'];
$query="SELECT * FROM consignments where consignment_id='{$consignment_id}'";

$result = mysqli_query($conn, $query);
while(($row = mysqli_fetch_assoc($result)) == true){
	$data[]=$row;
}
echo json_encode($data);
?>