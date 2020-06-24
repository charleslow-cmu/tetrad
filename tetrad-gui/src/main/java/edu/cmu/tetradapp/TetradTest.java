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

import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

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
        DataModel dataModel = null;
        Data data;
        DataColumn[] dataColumns;
        DataSet dataSet;

        // Load Data
        File file = new File(argv[0]);
        System.out.println("Data: " + file);
        TabularColumnReader columnReader = new TabularColumnFileReader(file.toPath(), Delimiter.COMMA);
        dataColumns = columnReader.readInDataColumns(new int[0], false);
        TabularDataReader dataReader = new TabularDataFileReader(file.toPath(), Delimiter.COMMA);
        data = dataReader.read(dataColumns, true);
        dataModel = DataConvertUtils.toDataModel(data);
        dataSet = (DataSet) dataModel;

        // Run PC
        IndependenceTest independenceTest = new IndTestFisherZ(dataSet, 0.001);
        Pc pc = new Pc(independenceTest);
        Graph graph = pc.search();
        System.out.println("Testing testing.");
    }

}

