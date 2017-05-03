<html>
<body>

<?php

  $database = pg_connect("host=localhost port=5431 dbname=nic user=nic password=nic_gr8");

  if (!$database) {
    echo "failed to find the database";
    exit;
  }

  $epost = $_POST["email"];

  $result = pg_exec($database, "select name " . "from students " . "where email='$epost'");

  if ($result) {
    if (pg_NumRows($result) == 0) {
      echo "didn't find you";
      exit;
    }
    else {
      echo "Welcome as student ";
      for ($i = 0; $i < pg_NumRows($result); $i++)
      {
        echo pg_Result($result, $i , 0);
        echo "<br>";
      }
    }

  }

?>


</body>
</html>
