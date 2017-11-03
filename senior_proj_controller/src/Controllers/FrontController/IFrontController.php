<?php
namespace UCDavis\Controllers\FrontController;

/**
 * Taken from www.sitepoint.com/fron-controller-pattern-1
 *
 * @author Alejandro Gervasio
 */
interface IFrontController
{
	/**
     * Sets the route controller.
	 *
	 * @param string Controller to be set.
	 */
	public function setController($controller);

	/**
	 * Sets the action for the set controller.
	 *
	 * @param string Method to be run in set controller.
	 */
	public function setAction($action);

	/**
	 * Set the parameter array.
	 *
	 * @param string[] Set the array of parameters.
	 */
	public function setParams(array $params);

	/**
	 * Runs the parsed and set URI route.
	 */
	public function run();
}
?>
