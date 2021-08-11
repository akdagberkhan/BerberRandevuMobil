<?php
	include("baglanti.php");
	if(isset($_POST) and !empty($_POST))
	{
		
		$adSoyad =@$_POST["adSoyad"];
		$mail =@$_POST["mail"];
		$kuladi =@$_POST["kuladi"];
		$sifre =@$_POST["sifre"];
		$telefon =@$_POST["telefon"];
		$foto =@$_POST["foto"];
		
			
		$decodeImage=base64_decode("$foto");
		$fotoYol ="pFoto/$kuladi.jpg";
		unlink($fotoYol);
		file_put_contents($fotoYol,$decodeImage);
		
		if (file_exists( "pFoto/$kuladi.jpg" ) ) {
						
			$kayitdefault=0;
			$komutekle=$conn->prepare("UPDATE kullanicilar SET adsoyad = ? , mail = ? , sifre  = ? , telefon = ? , profilResim = ? where kuladi = ?");
			$komutekle->execute(array($adSoyad,$mail,$sifre,$telefon,$fotoYol,$kuladi));
			if($komutekle)
			{
				$result["basarili"] = "1";
				$json=json_encode(array("Deger" => $result));
				echo $json ;
			}
			else{
				$result["basarili"] = "4";
				$json=json_encode(array("Deger" => $result));
				echo $json ;
			}
							
		}
		else {
			$result["basarili"] = "3";
	
			$json=json_encode(array("Deger" => $result));
			echo $json ;
		}
		
	}
	else{
		$result["basarili"] = "2";
		
		$json=json_encode(array("Deger" => $result));
		echo $json ;
	}
?>