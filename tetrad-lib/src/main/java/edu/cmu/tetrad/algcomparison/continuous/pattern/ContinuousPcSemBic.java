package edu.cmu.tetrad.algcomparison.continuous.pattern;

import edu.cmu.tetrad.algcomparison.Algorithm;
import edu.cmu.tetrad.data.CovarianceMatrixOnTheFly;
import edu.cmu.tetrad.data.DataSet;
import edu.cmu.tetrad.graph.Graph;
import edu.cmu.tetrad.search.*;

import java.util.Map;

/**
 * Created by jdramsey on 6/4/16.
 */
public class ContinuousPcSemBic implements Algorithm {
    public Graph search(DataSet dataSet, Map<String, Number> parameters) {
        SemBicScore score = new SemBicScore(new CovarianceMatrixOnTheFly(dataSet));
        score.setPenaltyDiscount(parameters.get("penaltyDiscount").doubleValue());
        IndependenceTest test = new IndTestScore(score);
        Pc pc = new Pc(test);
        return pc.search();
    }

    public Graph getComparisonGraph(Graph dag) {
        return SearchGraphUtils.patternForDag(dag);
    }

    public String getDescription() {
        return "PC using the SEM BIC score";
    }}
