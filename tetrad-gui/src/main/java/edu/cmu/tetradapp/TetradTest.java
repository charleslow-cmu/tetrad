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
package edu.cmu.tetradapp;

import edu.cmu.tetrad.data.*;
import edu.cmu.tetrad.graph.*;
import edu.cmu.tetrad.search.*;
import edu.cmu.tetrad.util.DataConvertUtils;
import edu.cmu.tetradapp.editor.LoadDataSettings;
import edu.pitt.dbmi.data.reader.*;
import edu.pitt.dbmi.data.reader.tabular.*;
import org.apache.commons.lang3.StringUtils;

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * <p>
 * Run Tetrad with custom commands.
 * </p>
 *
 * @author Joseph Ramsey jdramsey@andrew.cmu.edu
 */
public final class TetradTest {

    //==============================CONSTRUCTORS===========================//
    public TetradTest() {
    }

    //==============================PUBLIC METHODS=========================//

    /**
     * <p>
     * Launches Tetrad with custom data, knowledge etc.
     * </p>
     *
     * @param argv --skip-latest argument will skip checking for latest version.
     */
    public static void main(final String[] argv) throws IOException {

        // Avoid updates to swing code that causes comparison-method-violates-its-general-contract warnings
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        // This is needed to get numbers to be parsed and rendered uniformly, especially in the interface.
        Locale.setDefault(Locale.US);

        // Init
        DataModel dataModel;
        Data data;
        DataColumn[] dataColumns;
        DataSet dataSet;
        Graph graph = null;

        // Parameters
        String algorithm = argv[0];
        String dataFilePath = argv[1];
        String outFilePath = argv[2];

        // Load Data
        File file = new File(dataFilePath);
        TabularColumnReader columnReader = new TabularColumnFileReader(file.toPath(), Delimiter.COMMA);
        dataColumns = columnReader.readInDataColumns(new int[0], false);
        TabularDataReader dataReader = new TabularDataFileReader(file.toPath(), Delimiter.COMMA);
        data = dataReader.read(dataColumns, true);
        dataModel = DataConvertUtils.toDataModel(data);
        dataSet = (DataSet) dataModel;

        // Choose algorithm
        if (algorithm.equals("pc")) {
            // Arguments: alpha
            Double alpha = Double.parseDouble(argv[3]);
            IndependenceTest independenceTest = new IndTestFisherZ(dataSet, alpha);
            Pc pc = new Pc(independenceTest);
            graph = pc.search();
        } else if (algorithm.equals("fges")) {
            // Arguments: penaltyDiscount
            Double penaltyDiscount = Double.parseDouble(argv[3]);
            SemBicScore bicScore = new SemBicScore(dataSet);
            bicScore.setPenaltyDiscount(penaltyDiscount);
            Fges fges = new Fges(bicScore);
            graph = fges.search();
        } else if (algorithm.equals("lingam")) {
            // Arguments: penaltyDiscount
            Double penaltyDiscount = Double.parseDouble(argv[3]);
            Lingam lingam = new Lingam();
            lingam.setPenaltyDiscount(penaltyDiscount);
            graph = lingam.search(dataSet);
        }

        // Write out
        writeLearnedGraph(outFilePath, graph, dataColumns);
    }

    // Write edges of learned graph into an adjacency matrix for plotting in python
    public static void writeLearnedGraph(String filePath, Graph graph, DataColumn[] dataColumns) throws IOException {

        PrintWriter printWriter = new PrintWriter(new FileWriter(filePath));

        // Init data matrix
        int n = dataColumns.length;
        int[][] dataMatrix = new int[n][n];

        // Map Column Name to Number
        HashMap<String, Integer> columnNumber = new HashMap<String, Integer>();
        for (int i=0; i<n; i++) {
            DataColumn col = dataColumns[i];
            columnNumber.put(col.getName(), i);
        }

        // Get Edges
        Set edgesSet = graph.getEdges();
        for (Object obj: edgesSet.toArray()) {
            Edge edge = (Edge) obj;
            int i = columnNumber.get(edge.getNode1().getName());
            int j = columnNumber.get(edge.getNode2().getName());
            if (edge.getEndpoint2().toString().equals("Arrow")) {
                dataMatrix[i][j] = 1;  // Directed
            } else {
                dataMatrix[i][j] = 1;  // Undirected
                dataMatrix[j][i] = 1;
            }
        }

        // write it out for plotting in python
        for (int i=0; i<n; i++) {  // Write column names
            printWriter.print(dataColumns[i].getName());
            if (i<n-1) printWriter.print(",");
        }
        printWriter.print("\n");
        for (int i=0; i<n; i++) {  // Write values
            for (int j=0; j<n; j++) {
                printWriter.print(dataMatrix[i][j]);
                if (j<n-1) printWriter.print(",");
            }
            printWriter.print("\n");
        }
        printWriter.close();
    }
}

