<?php

include('baglanti.php');
$AdresFK = 8;
$komut2=$conn->prepare("SELECT * FROM adres ADRES INNER JOIN ilceler ILCE ON ADRES.ilceFk= ILCE.ilce_no INNER JOIN iller IL ON ILCE.il_no=IL.il_no where ADRES.id = ?");
$komut2->execute([$AdresFK]);
$veri = $komut2->fetchAll(PDO::FETCH_ASSOC);
 print_r(json_encode($veri));
?>