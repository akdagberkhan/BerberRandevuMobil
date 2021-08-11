<?php 
	include("baglanti.php");
	if(isset($_POST) and !empty($_POST))
	{
		$islem=@$_POST["islem"];
		if($islem == "0")
		{
			$berberID=@$_POST["berberId"];
			$komut=$conn->prepare("select * from tarife where berberFk = ?");
			$komut->execute([$berberID]);
			$komut2=$conn->prepare("select * from resimler where berberFk = ?");
			$komut2->execute([$berberID]);
			if($komut->rowCount() > 0)
			{
				$result["islemTarife"] = "var";
				$t=0;
				foreach( $komut as $row )
				{
					$t = $t +1;
					$result["tarifeId".$t] = $row['id'];
					$result["tarifeAdi".$t] = $row['islemAdi'];				
					$result["tarifeUcret".$t] = $row['islemUcret'];	
					
				}
				$result["tarifeAdet"] =$t;
				if($komut2->rowCount() > 0)
				{
					$result["islemFoto"] = "var";
					$i=0;
					foreach( $komut2 as $row )
					{
						$i = $i+1;	
						$result["fotoSrc".$i] = $row['resimsrc'];
								
					}
					$result["fotoAdet"] =$i;
				}
				else
				{
					$result["islemFoto"] = "yok";
				}
			}
			else
			{
				$result["islemTarife"] = "yok";
				if($komut2->rowCount() > 0)
				{
					$result["islemFoto"] = "var";
					$i=0;
					foreach( $komut2 as $row )
					{
						$i = $i+1;
						$result["fotoSrc".$i] = $row['resimsrc'];
					}
					$result["fotoAdet"] =$i;
				}
				else
				{
					
					$result["islemFoto"] = "yok";
				}
			}
			$result["basarili"] = "1";
			$json=json_encode(array("Deger" => $result));
			echo $json ;
		}
		else if($islem == "1")
		{
			$tarifeUcret =@$_POST["islemUcret"];
			$tarifeAdi =@$_POST["islemAdi"];
			$berberId =@$_POST["berberId"];
			$komut=$conn->prepare("insert into tarife(islemAdi,islemUcret,berberFk) values(?,?,?)");
			if($komut->execute([$tarifeAdi,$tarifeUcret,$berberId]))
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
		else if($islem == "2")
		{
			$kuladi=@$_POST["kulAdi"];
			$berberId=@$_POST["berberId"];
			$FotoSayisi=@$_POST["FotoSayisi"];
			for ($i = 0; $i < $FotoSayisi; $i++)
			{
				$foto=@$_POST["foto".$i];
				$decodeImage=base64_decode("$foto");
				$fotoYol ="berberFoto/$kuladi".$i.".jpg";
				$komut=$conn->prepare("insert into resimler(resimsrc,berberFk) values(?,?)");
				$komut->execute([$fotoYol,$berberId]);
				file_put_contents($fotoYol,$decodeImage);
			}
			$result["basarili"] = "1";
			$json=json_encode(array("Deger" => $result));
			echo $json ;
			
		}
		else if($islem == "3")
		{
			
			$silinecekId =@$_POST["silinecekId"];
			$berberId =@$_POST["berberId"];
			$komut=$conn->prepare("delete from tarife where id = ?");
			if($komut->execute([$silinecekId]))
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