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

package edu.cmu.tetrad.test;

import edu.cmu.tetrad.graph.GraphNode;
import edu.cmu.tetrad.graph.IndependenceFact;
import edu.cmu.tetrad.graph.Node;
import junit.framework.AssertionFailedError;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * Implements some tests of the FDR (False Discovery Rate) test.
 *
 * @author Joseph Ramsey
 */
public class TestIndependenceFact {

    @Test
    public void testSimpleCase() {

        Node x = new GraphNode("X");
        Node y = new GraphNode("Y");
        Node w = new GraphNode("W");

        IndependenceFact fact1 = new IndependenceFact(x, y);
        IndependenceFact fact2 = new IndependenceFact(y, x);

        assertEquals(fact1, fact2);

        IndependenceFact fact3 = new IndependenceFact(x, w);

        assertNotEquals(fact1, fact3);

        List<IndependenceFact> facts = new ArrayList<IndependenceFact>();

        facts.add(fact1);

        assertTrue(facts.contains(fact2));
    }
}


