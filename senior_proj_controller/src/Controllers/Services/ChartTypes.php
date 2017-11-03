<?php
namespace UCDavis\Controllers\Services;

/**
 * Provides a map of each chart type. Useful
 * when chart type is received in an incorrect
 * (not camel case) format.
 */
class ChartTypes
{
	const CHART_MAP = [
		'barchart'       => 'BarChart'      ,
		'piechart'       => 'PieChart'      ,
		'donutchart'     => 'DonutChart'    ,
		'gaugechart'     => 'GaugeChart'    ,
		'geochart'       => 'GeoChart'      ,
		'linechart'      => 'LineChart'     ,
		'tablechart'     => 'TableChart'    ,
		'histogramchart' => 'HistogramChart'
	];
}
?>
