<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$conn =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
if(!$conn){
die('could not connect'.mysql_error());
}

$dateStr=$_GET['dateStr'];
$myCounty = $_GET['myCounty'];

$query="SELECT * FROM consignments where status='Delivered' and countystate='{$myCounty}' and latestReport like '%{$dateStr}%';";
// SELECT * FROM `consignments` WHERE status='Delivered' and countystate='Westmeath' and latestReport like '%29-03-2020: Your parcel has been delivered%'
//echo $query;

$result = mysqli_query($conn, $query);
while(($row = mysqli_fetch_assoc($result)) == true){
	$data[]=$row;
}
echo json_encode($data);
?>