<?php

	include('baglanti.php');
	
	if(isset($_POST))
	{
		
		$kuladi=@$_POST['kuladi'];
		$sifre=@$_POST['sifre'];
		
			$komut=$conn->prepare("SELECT * FROM kullanicilar where kuladi = ?  and sifre= ? ");
            $komut->execute([$kuladi,$sifre]);
			
			if($komut->rowCount() > 0)
			{

				$result["basarili"] = "1";				
				$result["kuladi"] = $kuladi;
				
				$json=json_encode(array("Deger" => $result));
			
				echo $json ;
	        }
			else
				{

					$result["basarili"] = "0";
					$result["kuladi"] = $kuladi;
		
					$json=json_encode(array("Deger" => $result));
			
					echo $json ;
				
				}	
	}

?>
