<?php

	include('baglanti.php');
	
	if(isset($_POST) and !empty($_POST))
	{
		
		$kuladi=@$_POST['kuladi'];
		$sifre=@$_POST['sifre'];
		$adSoyad=@$_POST['adSoyad'];
		$mail=@$_POST['mail'];
		$telefon=@$_POST['telefon'];
		$foto =@$_POST["foto"];
		
			$komut=$conn->prepare("SELECT * FROM kullanicilar where kuladi = ?  and sifre= ? ");
            $komut->execute([$kuladi,$sifre]);
			
			if($komut->rowCount() > 0)
			{

				$result["basarili"] = "01";
				$result["kuladi"] = $kuladi;				
				$json=json_encode(array("Deger" => $result));
				echo $json ;
	        }
			else
				{
					$decodeImage=base64_decode("$foto");
					$fotoYol ="pFoto/$kuladi.jpg";
					file_put_contents($fotoYol,$decodeImage);
					
					if ( file_exists( "pFoto/$kuladi.jpg" ) ) {
						
						$kayitdefault=0;
						$komutekle=$conn->prepare("INSERT INTO kullanicilar(adsoyad,mail,kuladi,sifre,telefon,profilResim,lastsession,durum,berbermi) VALUES(?,?,?,?,?,?,?,?,?)");
						//$komutekle->execute();
						
						if($komutekle->execute(array($adSoyad,$mail,$kuladi,$sifre,$telefon,$fotoYol,$kayitdefault,$kayitdefault,$kayitdefault)))
						{
							$result["basarili"] = "10";
							$result["kuladi"] = $kuladi;
							$json=json_encode(array("Deger" => $result));
							echo $json ;
						}
						else{
							$result["basarili"] = "04";
							$result["kuladi"] = $kuladi;
							unlink($fotoYol);
							$json=json_encode(array("Deger" => $result));
							echo $json ;
						}
							
					}
					else {
						$result["basarili"] = "03";
						$result["kuladi"] = $kuladi;
	
						$json=json_encode(array("Deger" => $result));
						echo $json ;
					}
				
				}	
	}
	else{
			$result["basarili"] = "02";
			$result["kuladi"] = $kuladi;
		
			$json=json_encode(array("Deger" => $result));
			echo $json ;
	}

?>