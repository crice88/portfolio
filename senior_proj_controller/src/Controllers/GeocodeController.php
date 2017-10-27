<?php
namespace UCDavis\Controllers;

class GeocodeController
{
	public function getGeocode($address)
	{
		$url = 'https://geocoding.geo.census.gov/geocoder/locations/onelineaddress?address=' .
		  $address . '&benchmark=9&format=json';
		
		$json = file_get_contents($url);
		$jsonObj = json_decode($json, true);

		print_r($jsonObj['result']['addressMatches'][0]['coordinates']);
	}
}
?>
