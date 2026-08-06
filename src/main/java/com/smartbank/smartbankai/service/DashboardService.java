package com.smartbank.smartbankai.service;

import com.smartbank.smartbankai.dto.DashboardResponse;

public interface DashboardService {

    

	DashboardResponse getDashboard(Long accountId);
}