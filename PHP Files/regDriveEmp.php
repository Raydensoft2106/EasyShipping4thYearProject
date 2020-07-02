<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
$fname = $_POST["fName"];
$lname = $_POST["lName"];
$email = $_POST["email"];
$depot = $_POST["depot"];

$fname = str_replace("'","''",$fname);
$lname = str_replace("'","''",$lname);
$email = str_replace("'","''",$email);

 
if(!$con){
die('could not connect'.mysql_error());
}

$sql = "INSERT INTO driveremployees (driver_id, fname, lname, email, depot) VALUES (NULL, '$fname', '$lname', '$email', '$depot')";

$result = mysqli_query( $con,$sql );

if($result) {
	echo "User Registered Successfully";
} 
else {
	echo 'Error: '. $con->error;
}

?>