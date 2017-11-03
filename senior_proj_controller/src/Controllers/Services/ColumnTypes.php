<?php
namespace UCDavis\Controllers\Services;

/**
 * Maps SQL column types to Lavachart types.
 * Class declared abstract to prevent instantiation.
 */
abstract class ColumnTypes
{
	const COL_VARCHAR  = 'varchar';
	const COL_INT      = 'int';
	const COL_DECIMAL  = 'decimal';
	const COL_DATE     = 'date';
	const COL_DATETIME = 'datetime';
	const COL_STRING   = 'string';
	const COL_TINYTEXT = 'tinytext';
	const COL_NUMBER   = 'number';
	const COL_TEXT     = 'text';
}
?>
