<?php
namespace UCDavis\Controllers\FrontController;

use ReflectionClass;

use UCDavis\Exceptions\InvalidController;
use UCDavis\Exceptions\InvalidAction;

/**
 * FrontController Class
 * 
 * Front controller class that handles routing a request
 * to the appropriate controller class and method. Pattern
 * taken from www.sitepoint.com/front-controller-pattern-1
 *
 * @author Alejandro Gervasio
 */
class FrontController implements IFrontController
{
	/**
	 * Default controller type.
	 *
	 * @var string
	 */
	const DEFAULT_CONTROLLER = 'IndexController';
	
	/**
	 * Default action.
	 *
	 * @var string
	 */
	const DEFAULT_ACTION = 'index';

	/**
	 * Base route that matches the controllers namespace.
	 *
	 * @var string
	 */
	const BASE_ROUTE = 'UCDavis\\Controllers\\';

	/**
	 * Holds the incoming route.
	 *
	 * @var string[]
	 */
	private $routes = array();
	
	/**
	 * Default routing values.
	 *
	 * @var string   $controller  Class to be called
	 * @var string   $action      Method to be fired off
	 * @var string[] $params      Parameters passed with route, seperated by '/'
	 */
	protected $controller = self::DEFAULT_CONTROLLER;
	protected $action = self::DEFAULT_ACTION;
	protected $params = array();
	
	/**
	 * FrontController Constructor
	 *
	 * @param string[] $routes
	 */
	public function __construct($routes)
	{			
		if (!empty($routes)) {
			$this->routes = $routes;
			$this->parseRoutes();
		}
	}

	/**
	 * Parses the incoming URI into controller, action and paramaters.
	 */
	private function parseRoutes()
	{
		@list($controller, $action, $params) = explode('/', $this->routes, 3);
		if (isset($controller)) {
			$this->setController($controller);	
		}
		if (isset($action)) {
			$this->setAction($action);
		}
		if (isset($params)) {
			$this->setParams(explode('/', $params));
		}
	}
	
	/**
	 * Checks if the controller exists and sets the $controller variable.
	 *
	 * @param  string $controller
	 * @return \UCDavis\Controllers\FrontController\FrontController
	 */
	public function setController($controller)
	{
		// Use the base route since controllers are in a different namespace.
		$controller = self::BASE_ROUTE . ucfirst(strtolower($controller)) . 'Controller';

		if (!class_exists($controller)) {
			throw new InvalidController($controller);
		}

		$this->controller = $controller;

		return $this;
	}

	/**
	 * Checks that the method exists within the routed controller.
	 *
	 * @param  string $action Method to be set
	 * @return \UCDavis\Controllers\FrontController\FrontController 
	 */
	public function setAction($action) 
	{
		$reflector = new ReflectionClass($this->controller);

		if (!$reflector->hasMethod($action)) {
			throw new InvalidAction($this->controller, $action);
		}
		
		$this->action = $action;

		return $this;
	}

	/**
     * Set the parameters.
	 *
	 * @param  string[] $params Array of parameters that have been split by '/'
	 *                          deliminator
	 * @return \UCDavis\Controllers\FrontController\FrontController
	 */
	public function setParams(array $params)
	{
		$this->params = $params;

		return $this;
	}
	
	/**
	 * Runs the specified controller and method with parameters.
	 */
	public function run() 
	{
		call_user_func_array(array(new $this->controller, $this->action), $this->params);		
	}
}
?>
