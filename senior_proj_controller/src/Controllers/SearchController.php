<?php

namespace UCDavis\Controllers;

use UCDavis\DataAccess\DatasetInfoDAO;

class SearchController
{
    public function searchFor($query)
    {
        $daoInfo = new DatasetInfoDAO('charts');

        $result = $daoInfo->runSearch($query);

        echo json_encode($result);

    }
}
