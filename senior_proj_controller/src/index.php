<?php
namespace UCDavis;

require('../vendor/autoload.php');

use UCDavis\Routing\Route;
use UCDavis\Controllers\FrontController\FrontController;

$routing = new Route;
// Parse the incoming URI and pass it to the front controller.
$controller = new FrontController($routing->getCurrentUri());

// Fire off the routed class method.
$controller->run();
?>
