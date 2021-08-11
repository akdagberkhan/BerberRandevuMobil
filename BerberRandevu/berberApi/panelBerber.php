<?php

include('baglanti.php');
	
	if(isset($_POST) and !empty($_POST))
	{	

		$islem=@$_POST["islem"];
		if($islem == "1")
		{
			$komut=$conn->prepare("SELECT * FROM iller ");
            $komut->execute();
			
			if($komut->rowCount() > 0)
            {
                $veri = $komut->fetchAll(PDO::FETCH_ASSOC);
                print_r(json_encode($veri));
            }
			
		}
		else if($islem == "2")
		{
			$ilNo=@$_POST['ilNo'];
			$komut=$conn->prepare("SELECT * FROM ilceler where il_no = ? ");
            $komut->execute([$ilNo]);
			
			if($komut->rowCount() > 0)
            {
                $veri = $komut->fetchAll(PDO::FETCH_ASSOC);
                print_r(json_encode($veri));
            }
			
		}
		else if($islem == "3")
		{
			$kulId=@$_POST['kulId'];
			$komut=$conn->prepare("select * from berber where kullaniciFK = ?");
			$komut->execute([$kulId]);
			$AdresFK;
			$berberId;
			if($komut->rowCount() > 0)
			{
				foreach( $komut as $row )
				{
					$result["basarili"] = "1";				
					$result["berberId"] = $row['id'];
					$berberId = $row['id'];
					$result["berberAd"] = $row['berberAd'];
					$result["berberTel"] = $row['berberTel'];
					$result["acilis"] = $row['acilis'];
					$result["kapanis"] = $row['kapanis'];
					$result["periyot"] = $row['periot'];
					$result["durum"] = $row['durum'];
					$AdresFK = $row['berberAdresFk'];
				}
				if(!empty($AdresFK))
				{
					$komut2=$conn->prepare("SELECT * FROM adres ADRES INNER JOIN ilceler ILCE ON ADRES.ilceFk= ILCE.ilce_no INNER JOIN iller IL ON ILCE.il_no=IL.il_no where ADRES.id = ?");
					$komut2->execute([$AdresFK]);
					foreach( $komut2 as $row2 )
					{
						$result["berberIlce"] = $row2['ilce_no'];
						$result["berberIl"] = $row2['il_no'];
						$result["berberDetay"] = $row2['detay'];
					}
				}
				
				$json=json_encode(array("Deger" => $result));
				echo $json ;
			}
			else{
				$result["basarili"] = "2";
				$json=json_encode(array("Deger" => $result));
				echo $json ;
			}
			
		}
		else if($islem == "4")
		{
			$acilis=@$_POST["acilis"];
			$kapanis=@$_POST["kapanis"];
			$berberAdi=@$_POST["berberAdi"];
			$periyot=@$_POST["periyot"];
			$telefon=@$_POST["telefon"];
			$ilceFk=@$_POST["ilceFk"];
			$detay=@$_POST["detay"];
			$kulId=@$_POST["kullaniciFk"];
			$komut=$conn->prepare("insert into adres(ilceFk,detay) values(?,?)");	
			if($komut->execute([$ilceFk,$detay]))
			{
				
				$komut2=$conn->prepare("SELECT * FROM adres where detay = ?");
				$komut2->execute([$detay]);
				if($komut2->rowCount() > 0)
				{
					
					$adresFk;
					foreach( $komut2 as $row )
					{
						$adresFk=$row['id'];
					}
					$komut3=$conn->prepare("update kullanicilar set berbermi = 1 where id = ?");
					if($komut3->execute([$kulId]))
					{
						
						$komut4=$conn->prepare("insert into berber(berberAd,berberTel,acilis,kapanis,periot,berberAdresFk,kullaniciFK,durum) values(?,?,?,?,?,?,?,?)");
						if($komut4->execute([$berberAdi,$telefon,$acilis,$kapanis,$periyot,$adresFk,$kulId,1]))
						{
							
							$result["basarili"] = "1";
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
				}

			}
			else
			{
				$result["basarili"] = "0";
				$json=json_encode(array("Deger" => $result));
				echo $json ;
			}
			
			
		}
		else if($islem == "0")
		{
			$kulId=@$_POST["kullaniciFk"];
			$komut2=$conn->prepare("update kullanicilar set berbermi = 0 where id = ?");	
			if($komut2->execute([$kulId]))
			{
				$result["basarili"] = "1";
			}
			else
			{
				$result["basarili"] = "0";
			}
			$json=json_encode(array("Deger" => $result));
			echo $json ;
		}
			
	}
?>