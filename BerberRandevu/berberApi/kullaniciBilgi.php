<?php

	include('baglanti.php');
	
	if(isset($_POST) and !empty($_POST))
	{				
		
		$kuladi=@$_POST['kuladi'];
			
			
			$komut=$conn->prepare("SELECT id,adsoyad,mail,kuladi,sifre,telefon,berbermi,profilResim FROM kullanicilar where kuladi = ?");
            $komut->execute([$kuladi]);
			
			if($komut->rowCount() > 0)
			{
				foreach( $komut as $row )
				{
					$result["basarili"] = "1";				
					$result["kulId"] = $row['id'];
					$result["adSoyad"] = $row['adsoyad'];
					$result["mail"] = $row['mail'];
					$result["sifre"] = $row['sifre'];
					$result["telefon"] = $row['telefon'];
					$result["berbermi"] = $row['berbermi'];
					$result["profilFoto"] = $row['profilResim'];
				}
				
				$json=json_encode(array("Deger" => $result));
				echo $json ;
	        }
			else
				{

					$result["basarili"] = "0";
			
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
