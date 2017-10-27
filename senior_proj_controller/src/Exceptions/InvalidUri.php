<?php
namespace UCDavis\Exceptions;

class InvalidUri extends UCDavisException
{
	public function __construct($uri, $length)
	{
		$message = "Invalid uri $uri and length $length.";

		parent::_construct($message);
	}
}
?>
