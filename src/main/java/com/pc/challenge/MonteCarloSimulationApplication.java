package com.pc.challenge;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Typical values for Aggressive Strategy : medianPerformance=243795.0,
 * bestPerformance=551464.0, worstPerformance=104285.0)
 * 
 * Typical values for Conservative Strategy: medianPerformance=158094.0,
 * bestPerformance=223599.0, worstPerformance=110820.0
 *
 */
public class MonteCarloSimulationApplication {

	public static void main(String[] args) {
		List<Portfolio> portfoliosToBeEvalauted = getPortfolios();

		int simulationsToRun = 10000;
		double inflation = 3.5 / 100;
		MonteCarloSimulator simulator = new MonteCarloSimulator(simulationsToRun, inflation);

		Random random = new Random();
		simulator.runSimulation(portfoliosToBeEvalauted, random);
	}

	/**
	 * generates an aggressive and a conservative portfolio with given data. The
	 * given values are percentages, so we divide by 100 for absolute calculations.
	 * 
	 * @return the list for portfolios.
	 */
	private static List<Portfolio> getPortfolios() {
		List<Portfolio> portfoliosToBeEvalauted = new ArrayList<>();
		Portfolio aggressive = Portfolio.builder().type("Aggressive").startingInvestment(100000.00).vestingTime(20)
				.mean(9.4324 / 100).standardDeviation(15.675 / 100).build();
		portfoliosToBeEvalauted.add(aggressive);

		Portfolio conservative = Portfolio.builder().type("Conservative").startingInvestment(100000.00).vestingTime(20)
				.mean(6.189 / 100).standardDeviation(6.3438 / 100).build();
		portfoliosToBeEvalauted.add(conservative);

		return portfoliosToBeEvalauted;
	}

}
