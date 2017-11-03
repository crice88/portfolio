<?php
namespace UCDavis\Controllers\Services;

/**
 * Provides several consts for use when
 * exporting various file types.
 */
class ContentTypes
{
	const BASE_STRING = 'Content-Type: ';

	const XLS  = self::BASE_STRING . 'application/vnd.ms-excel';
	const XLSX = self::BASE_STRING . 'application/vnd.openxmlformats-officedocument.'
		                             . 'spreadsheetml.sheet';
	const PDF  = self::BASE_STRING . 'application/pdf';
	const CSV  = self::BASE_STRING . 'text/csv';
	
	const CONTENT_DISPOSITION = 'Content-Disposition: attachment;filename=';
	const CONTENT_CACHE       = 'Cache-Control: max-age=0';

	const CONTENT_TYPE_MAP = [
		FileTypes::XLS  => self::XLS,
		FileTypes::XLSX => self::XLSX,
		FileTypes::PDF  => self::PDF,
		FileTypes::CSV  => self::CSV
	];
}
?>
