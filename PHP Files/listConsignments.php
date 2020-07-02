<?php
define('HOST','localhost');
define('USER','id12253777_easyshipping');
define('PASS', 'RossHayden99!');
define('DB','id12253777_easyshippingdb');

$con =mysqli_connect(HOST,USER,PASS,DB) or die('Failed');
 
if(!$con){
die('could not connect'.mysql_error());
}

$receiver_id=$_GET['receiver_id'];
// Select all of our stocks from table 'stock_tracker'
$sql = "SELECT consignment_id,dispatchdate,noOfParcels,status FROM consignments where receiver_id ='{$receiver_id}'";
 
// Confirm there are results
if ($result = mysqli_query($con, $sql))
{
 // We have results, create an array to hold the results
        // and an array to hold the data
 $resultArray = array();
 $tempArray = array();
 
 // Loop through each result
 while($row = $result->fetch_object())
 {
 // Add each result into the results array
 $tempArray = $row;
     array_push($resultArray, $tempArray);
 }
 
 // Encode the array to JSON and output the results
 echo json_encode($resultArray);
}
 
// Close connections
mysqli_close($con);
?>