package edu.cmu.tetrad.algcomparison.score;

import edu.cmu.tetrad.data.*;
import edu.cmu.tetrad.graph.Node;
import edu.cmu.tetrad.search.Score;
import edu.cmu.tetrad.util.Parameters;
import edu.cmu.tetrad.util.StatUtils;

import java.util.ArrayList;
import java.util.List;

import static edu.cmu.tetrad.util.StatUtils.skewness;

/**
 * Wrapper for Fisher Z test.
 *
 * @author jdramsey
 */
@edu.cmu.tetrad.annotation.Score(
        name = "Sem BIC Score",
        command = "sem-bic",
        dataType = {DataType.Continuous, DataType.Covariance}
)
public class SemBicScore implements ScoreWrapper {

    static final long serialVersionUID = 23L;
    private DataModel dataSet;

    @Override
    public Score getScore(DataModel dataSet, Parameters parameters) {
        this.dataSet = dataSet;


//        ICovarianceMatrix cov = dataSet instanceof ICovarianceMatrix ? (ICovarianceMatrix) dataSet
//                : new CovarianceMatrix((DataSet) dataSet);

        edu.cmu.tetrad.search.SemBicScore semBicScore
                = new edu.cmu.tetrad.search.SemBicScore((DataSet) this.dataSet);//parameters.getBoolean("biasCorrected"));
        semBicScore.setPenaltyDiscount(parameters.getDouble("penaltyDiscount"));
        semBicScore.setStructurePrior(parameters.getDouble("structurePrior"));
        return semBicScore;
    }

    @Override
    public String getDescription() {
        return "Sem BIC Score";
    }

    @Override
    public DataType getDataType() {
        return DataType.Continuous;
    }

    @Override
    public List<String> getParameters() {
        List<String> parameters = new ArrayList<>();
        parameters.add("penaltyDiscount");
        parameters.add("structurePrior");
        parameters.add("semBicDelta");
//        parameters.add("biasCorrected");
//        parameters.add("kevin");
        return parameters;
    }

    @Override
    public Node getVariable(String name) {
        return dataSet.getVariable(name);
    }

}
