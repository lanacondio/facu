<?php
ini_set('display_errors', 'Off');
// get the HTTP method, path and body of the request
$method = $_SERVER['REQUEST_METHOD'];
$request = explode('/', trim($_SERVER['PATH_INFO'],'/'));
$input = json_decode(file_get_contents('php://input'),true);
 


// connect to the mysql database
$link = mysqli_connect('localhost', 'root', 'lvsha1987', 'gdicc');
mysqli_set_charset($link,'utf8');
 
// retrieve the table and key from the path
$table = preg_replace('/[^a-z0-9_]+/i','',array_shift($request));

//$key = array_shift($request[0])+0;
$key = $request[0];
 
// escape the columns and values from the input object
$columns = preg_replace('/[^a-z0-9_]+/i','',array_keys($input));
$values = array_map(function ($value) use ($link) {
  if ($value===null) return null;
  return mysqli_real_escape_string($link,(string)$value);
},array_values($input));
 
// build the SET part of the SQL command

$set = '';
for ($i=0;$i<count($columns);$i++) {
  $set.=($i>0?',':'').'`'.$columns[$i].'`=';
  $set.=($values[$i]===null?'NULL':'"'.$values[$i].'"');
}
 
// create SQL based on HTTP method
switch ($method) {
  case 'GET':
    $sql = "select * from `$table`".($key?" WHERE _id=$key":''); break;
  case 'PUT':
        
/*
 if (!($putData = fopen("php://input", "r"))) 
                throw new Exception("Can't get PUT data."); 


switch ($table) {
      case 'translation_update':
          
           $sql = "insert into `$table` (language_id, word, context, type, origin_device_id, translation, id_original_translation) values (".$putData["language_id"].",'".$putData["word"]."','".$putData["context"]."','".$putData["type"]."',".$putData["approved"].",'".$putData["origin_device_id"]."','".$putData["translation"]."',".$putData["id_original_translation"].")";
     var_dump($sql);

   while ($data = fread($putData, $CHUNK)) {var_dump($data); }

   
        break;
      }*/
      break;
  case 'POST':

    switch ($table) {
      case 'translation':
           
           $sql = "insert into `$table` (language_id, word, context, type, approved, origin_device_id, translation) values (".$_POST["language_id"].",'".$_POST["word"]."','".$_POST["context"]."','".$_POST["type"]."',".$_POST["approved"].",'".$_POST["origin_device_id"]."','".$_POST["translation"]."')";
     
        break;
      case 'evaluation':
          
          $sql = "insert into `$table` (id_translation, points, device_id) values (".$_POST["id_translation"].",".$_POST["points"].",'".$_POST["device_id"]."')";

        break;        
      }
      break;
  case 'DELETE':
      break;
}
 
// excecute SQL statement
$result = mysqli_query($link,$sql);
 
// die if SQL statement failed
if (!$result) {
  http_response_code(404);
  die(mysqli_error());
}
 


// print results, insert id or affected row count
if ($method == 'GET') {
  if (!$key) echo '[';
  for ($i=0;$i<mysqli_num_rows($result);$i++) {
    echo ($i>0?',':'').json_encode(mysqli_fetch_object($result));
  }
  if (!$key) echo ']';
} elseif ($method == 'POST') {
  echo mysqli_insert_id($link);
} else {
  echo mysqli_affected_rows($link);
}
 
// close mysql connection
mysqli_close($link);
?>