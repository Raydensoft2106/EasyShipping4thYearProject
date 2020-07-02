<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
// Check connection
if (!$con) {
    die("Connection failed: " . mysqli_connect_error());
}
 
 
$query=mysqli_query($con, "select * from depots");  //fetch all data from depots table
 
while($row=mysqli_fetch_array($query))
{
	$flag[]=$row;
}
 echo json_encode(array('FL' => $flag));  //json output

mysqli_close($con);
?>