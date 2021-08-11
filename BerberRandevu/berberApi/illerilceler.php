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
			
	}
?>