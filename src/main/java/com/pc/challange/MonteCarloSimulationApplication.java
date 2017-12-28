package com.pc.challange;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MonteCarloSimulationApplication {

	private static int simulationsToRun = 10000;
	private static double inflation = 3.5 / 100;

	public static void main(String[] args) {
		List<Portfolio> portfoliosToBeEvalauted = getPortfolios();
		Random random = new Random();

		runSimulation(portfoliosToBeEvalauted, random);
	}

	/**
	 * generates an aggressive and a conservative portfolio with given data. The
	 * given values are percentages, so we divide by 100 for absolute calculations.
	 * 
	 * @return the list for portfolios.
	 */
	private static List<Portfolio> getPortfolios() {
		Portfolio aggressive = Portfolio.builder().type("Aggressive").startingInvestment(100000.00).vestingTime(20)
				.mean(9.4324 / 100).standardDeviation(15.675 / 100).build();
		Portfolio conservative = Portfolio.builder().type("Conservative").startingInvestment(100000.00).vestingTime(20)
				.mean(6.189 / 100).standardDeviation(6.3438 / 100).build();

		List<Portfolio> portfoliosToBeEvalauted = new ArrayList<>();

		portfoliosToBeEvalauted.add(aggressive);
		portfoliosToBeEvalauted.add(conservative);
		return portfoliosToBeEvalauted;
	}

	/**
	 * For every simulation run, we compute the inflation adjusted value of
	 * investment over a period of 20 years and store in an array. These values are
	 * later used to calculate percentiles values.
	 * 
	 * @param portfoliosToBeEvalauted
	 *            list of portfolios to be evaluated.
	 * @param random
	 *            generated random
	 */
	private static void runSimulation(List<Portfolio> portfoliosToBeEvalauted, Random random) {
		for (Portfolio p : portfoliosToBeEvalauted) {
			double[] runArray = new double[simulationsToRun];

			for (int simCount = 0; simCount < simulationsToRun; simCount++) {
				double investmentInSimulation = p.getStartingInvestment();
				for (int yearCount = 0; yearCount < p.getVestingTime(); yearCount++) {
					double fittedRandom = getNextGaussianRandom(p.getMean(), p.getStandardDeviation(),
							random.nextGaussian());
					investmentInSimulation *= ((1 + fittedRandom) * (1 - inflation));
				}
				runArray[simCount] = investmentInSimulation;
			}

			computePerformance(runArray, p);
			log.info(p.toString());
		}
	}

	/**
	 * random.nextGaussian() returns the next pseudo-random which is Gaussian
	 * distributed, (double) with mean 0.0 and standard deviation 1.0 from random
	 * number generator's sequence. We then have to fit the result by desired
	 * standard deviation (by multiplication) and mean (by addition).
	 * 
	 * @param mean
	 *            given mean
	 * @param standardDeviation
	 *            given standard deviation
	 * @param gaussianValue
	 *            the normally distributed random obtained from
	 *            random.nextGaussian()
	 * @return the fitted value
	 */
	private static double getNextGaussianRandom(double mean, double standardDeviation, double gaussianValue) {
		return gaussianValue * standardDeviation + mean;
	}

	/**
	 * compute the 10th percentile, 90th percentile , and median (50th percentile)
	 * from the simulation run of a given portfolio
	 * 
	 * @param runArray
	 *            array containing simulations
	 * @param p
	 *            portfolio
	 */
	private static void computePerformance(double[] runArray, Portfolio p) {
		DescriptiveStatistics stats = new DescriptiveStatistics(runArray);

		p.setWorstPerformance(Math.floor(stats.getPercentile(10)));
		p.setMedianPerformance(Math.floor(stats.getPercentile(50)));
		p.setBestPerformance(Math.floor(stats.getPercentile(90)));
	}

}
