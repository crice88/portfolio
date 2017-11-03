<?php
namespace UCDavis\Routing;

use UCDavis\Exceptions\InvalidUri;

/**
 * Handles incoming route
 */
class Route
{
	const SPLIT_CHAR = '/';

	private $basepath;
	private $routes = array();
	
	/**
	 * Strips incoming URL of route
   * information.
	 *
	 * @return string URI
	 */
	public function getCurrentUri()
	{
	  // Retreive the base path
		$this->basepath = 
			implode(self::SPLIT_CHAR, 
			array_slice(
			explode(self::SPLIT_CHAR, $_SERVER['SCRIPT_NAME']), 0, -1)) . self::SPLIT_CHAR;
		$uri = substr($_SERVER['REQUEST_URI'], strlen($this->basepath));

		if ($uri === false) {
			throw new InvalidUri($_SERVER['REQUEST_URI'], strlen($this->basepath));
		}

		// Handle splitting on '?' char
		if (strstr($uri, '?')) {
			$uri = substr($uri, 0, strpos($uri, '?'));
		}

		$uri = self::SPLIT_CHAR . trim($uri, self::SPLIT_CHAR);
		
		return substr($uri, 1);
	}
}
?>
