package com.pc.challenge;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Portfolio {
	private String type;
	private double startingInvestment;
	private int vestingTime;
	private double mean;
	private double standardDeviation;
	private double medianPerformance;
	private double bestPerformance;
	private double worstPerformance;
	
}
