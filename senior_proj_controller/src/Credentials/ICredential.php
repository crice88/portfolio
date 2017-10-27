<?php
namespace UCDavis\Credentials;

interface ICredential
{
	public function getUserName();
	public function getPassword();
	public function getHost();
}
?>
