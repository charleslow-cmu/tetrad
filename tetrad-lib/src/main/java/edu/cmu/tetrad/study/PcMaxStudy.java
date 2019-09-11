///////////////////////////////////////////////////////////////////////////////
// For information as to what this class does, see the Javadoc, below.       //
// Copyright (C) 1998, 1999, 2000, 2001, 2002, 2003, 2004, 2005, 2006,       //
// 2007, 2008, 2009, 2010, 2014, 2015 by Peter Spirtes, Richard Scheines, Joseph   //
// Ramsey, and Clark Glymour.                                                //
//                                                                           //
// This program is free software; you can redistribute it and/or modify      //
// it under the terms of the GNU General Public License as published by      //
// the Free Software Foundation; either version 2 of the License, or         //
// (at your option) any later version.                                       //
//                                                                           //
// This program is distributed in the hope that it will be useful,           //
// but WITHOUT ANY WARRANTY; without even the implied warranty of            //
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the             //
// GNU General Public License for more details.                              //
//                                                                           //
// You should have received a copy of the GNU General Public License         //
// along with this program; if not, write to the Free Software               //
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA //
///////////////////////////////////////////////////////////////////////////////

package edu.cmu.tetrad.study;

import edu.cmu.tetrad.algcomparison.Comparison;
import edu.cmu.tetrad.algcomparison.algorithm.Algorithms;
import edu.cmu.tetrad.algcomparison.algorithm.continuous.dag.Lingam;
import edu.cmu.tetrad.algcomparison.algorithm.multi.Fask;
import edu.cmu.tetrad.algcomparison.algorithm.oracle.pag.FciMax;
import edu.cmu.tetrad.algcomparison.algorithm.oracle.pattern.*;
import edu.cmu.tetrad.algcomparison.algorithm.pairwise.R3;
import edu.cmu.tetrad.algcomparison.algorithm.pairwise.RSkew;
import edu.cmu.tetrad.algcomparison.graph.RandomForward;
import edu.cmu.tetrad.algcomparison.independence.FisherZ;
import edu.cmu.tetrad.algcomparison.score.SemBicScore;
import edu.cmu.tetrad.algcomparison.simulation.LinearFisherModel;
import edu.cmu.tetrad.algcomparison.simulation.SemSimulation;
import edu.cmu.tetrad.algcomparison.simulation.Simulations;
import edu.cmu.tetrad.algcomparison.statistic.*;
import edu.cmu.tetrad.util.Parameters;
import edu.cmu.tetrad.util.Params;

/**
 * An example script to simulate data and run a comparison analysis on it.
 *
 * @author jdramsey
 */
public class PcMaxStudy {
    public static void main(String... args) {
        Statistics statistics = new Statistics();

//        statistics.add(new ParameterColumn(Params.SAMPLE_SIZE));
        statistics.add(new ParameterColumn(Params.PENALTY_DISCOUNT));
        statistics.add(new ParameterColumn(Params.ALPHA));
        statistics.add(new ParameterColumn(Params.COLLIDER_DISCOVERY_RULE));

        statistics.add(new AdjacencyPrecision());
        statistics.add(new AdjacencyRecall());
        statistics.add(new ArrowheadPrecision());
        statistics.add(new ArrowheadRecall());
        statistics.add(new ArrowheadPrecisionCommonEdges());
        statistics.add(new ArrowheadRecallCommonEdges());
        statistics.add(new ColliderPrecision());
        statistics.add(new ColliderRecall());
        statistics.add(new ColliderNumCoveringErrors());
        statistics.add(new ColliderNumUncoveringErrors());
        statistics.add(new NumberOfEdgesEst());
//        statistics.add(new F1All());
//        statistics.add(new GraphExactlyRight());
        statistics.add(new ElapsedTime());

        statistics.setWeight("AP", 1);
        statistics.setWeight("AR", 1);
        statistics.setWeight("AHP", 1);
        statistics.setWeight("AHR", 1);

        Algorithms algorithms = new Algorithms();

        algorithms.add(new CpcMaxAvg(new FisherZ()));
        algorithms.add(new CpcMax(new FisherZ()));
        algorithms.add(new PcAll(new FisherZ()));
        algorithms.add(new Fges(new SemBicScore()));

        Comparison comparison = new Comparison();

        comparison.setShowAlgorithmIndices(true);
        comparison.setShowSimulationIndices(true);
        comparison.setSortByUtility(false);
        comparison.setShowUtilities(false);
        comparison.setComparisonGraph(Comparison.ComparisonGraph.true_DAG);

        Simulations simulations = new Simulations();
        simulations.add(new SemSimulation(new RandomForward()));

        comparison.compareFromSimulations("pcmax", simulations, algorithms, statistics, getParameters());

    }

    private static Parameters getParameters() {
        Parameters parameters = new Parameters();

//        parameters.set(Params.VERBOSE, false);
//
//        parameters.set(Params.SAMPLE_SIZE, 1000);
////
//        parameters.set(Params.COEF_LOW, 0.2);
//        parameters.set(Params.COEF_HIGH, 0.7);
//        parameters.set(Params.VAR_LOW, .5);
//        parameters.set(Params.VAR_HIGH, .9);
//        parameters.set(Params.COEF_SYMMETRIC, true);
//        parameters.set(Params.SELF_LOOP_COEF, 0.0);
//        parameters.set(Params.RANDOMIZE_COLUMNS, true);
//
//        parameters.set(Params.INCLUDE_POSITIVE_COEFS, true);
//        parameters.set(Params.INCLUDE_NEGATIVE_COEFS, true);
//        parameters.set(Params.ERRORS_NORMAL, true);
//        parameters.set(Params.INTERVAL_BETWEEN_SHOCKS, 10);
//        parameters.set(Params.INTERVAL_BETWEEN_RECORDINGS, 10);
//        parameters.set(Params.FISHER_EPSILON, 0.001);
//
//
//
//        parameters.set(Params.STABLE_FAS, false);
//        parameters.set(Params.CONCURRENT_FAS, false);
//        parameters.set(Params.CONFLICT_RULE, 3);
//
//        parameters.set(Params.SYMMETRIC_FIRST_STEP, false);
//        parameters.set(Params.FAITHFULNESS_ASSUMED, true);
//        parameters.set(Params.MAX_DEGREE, 100);
//        parameters.set(Params.MAX_INDEGREE, 100);
//        parameters.set(Params.MAX_OUTDEGREE, 100);
//        parameters.set(Params.NUM_MEASURES, 10);
//
//
//        parameters.set(Params.ERRORS_NORMAL, true);
//        parameters.set(Params.RANDOMIZE_COLUMNS, true);
//        parameters.set(Params.DIFFERENT_GRAPHS, true);
//
//
//        parameters.set(Params.USE_SELLKE_ADJUSTMENT, false);

        parameters.set(Params.NUM_RUNS, 50);
        parameters.set(Params.DEPTH, 5);
        parameters.set(Params.ALPHA, 0.05);
        parameters.set(Params.PENALTY_DISCOUNT, 1, 2, 3);
        parameters.set(Params.AVG_DEGREE, 4);
        parameters.set(Params.COLLIDER_DISCOVERY_RULE, 1, 2, 3);


        return parameters;
    }

}



