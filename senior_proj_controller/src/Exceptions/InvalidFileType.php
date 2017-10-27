<?php
namespace UCDavis\Exceptions;

class InvalidFileType extends UCDavisException
{
	public function __construct($fileType)
	{
		$message = "The file type '$fileType' is unsupported or invalid. "
			. "Valid types are csv, xls, xlsx and pdf.";

		parent::__construct($message);
	}
}
?>
