<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
$fname = $_POST["fName"];
$lname = $_POST["lName"];
$email = $_POST["email"];
$addressline1 = $_POST["addressline1"];
$addressline2 = $_POST["addressline2"];
$towncity = $_POST["towncity"];
$countystate = $_POST["countystate"];
$country = $_POST["country"];
$postcode = $_POST["postcode"];
$phoneno = $_POST["phoneno"];

$fname = str_replace("'","''",$fname);
$lname = str_replace("'","''",$lname);
$email = str_replace("'","''",$email);
$addressline1 = str_replace("'","''",$addressline1);
$addressline2 = str_replace("'","''",$addressline2);
$towncity = str_replace("'","''",$towncity);
$countystate = str_replace("'","''",$countystate);
$country = str_replace("'","''",$country);

if(!$con){
die('could not connect'.mysql_error());
}

$sql = "INSERT INTO sendingcustomers (sender_id, fname, lname, email, addressline1, addressline2, towncity, countystate, country, postcode, phoneno) VALUES (NULL, '$fname', '$lname', '$email', '$addressline1', '$addressline2', '$towncity', '$countystate', '$country', '$postcode', '$phoneno')";

$result = mysqli_query( $con,$sql );

if($result) {
	echo "User Registered Successfully";
} 
else {
	echo 'Error: '. $con->error;
}

?>