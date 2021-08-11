<?php
$mysqlsunucu = "localhost";
$mysqlkullanici = "root";
$mysqlsifre = "";
$baglankontrol = 0;
try {
    $conn = new PDO("mysql:host=$mysqlsunucu;dbname=dbberber", $mysqlkullanici, $mysqlsifre);
	$conn->exec("SET NAMES 'utf8'; SET CHARSET 'utf8'");
    $conn->setAttribute(PDO::ATTR_ERRMODE, PDO::ERRMODE_EXCEPTION);
		$baglankontrol=1;
    }
catch(PDOException $e)
    {
		$baglankontrol=0;
    }
?>