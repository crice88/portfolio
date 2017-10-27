<?php
namespace UCDavis\Credentials;

class SQLCredential implements ICredential
{
	const USERNAME = 'csc191';
	const PASSWORD = 'teamsb6';
	const HOST   = 'localhost';

	public function getUserName()
	{
		return self::USERNAME;
	}

	public function getPassword()
	{
		return self::PASSWORD;
	}

	public function getHost()
	{
		return self::HOST;
	}
}
?>
