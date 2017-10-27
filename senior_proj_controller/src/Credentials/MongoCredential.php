<?php
namespace UCDavis\Credentials;

class MongoCredential implements ICredential
{
	const HOST = 'mongodb://localhost:27017';

	public function getUserName()
	{

	}

	public function getPassword()
	{

	}

	public function getHost()
	{
		return self::HOST;
	}
}
?>
