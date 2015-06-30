package com.hida.play.redmart


/**
 * Created by arief.hidayat on 30/06/2015.
 */
class SkiMap {

    Result process(String uri) {
        process(new MapDataReader().read(uri))
    }
    Result process(List<String> data) {
        process(new MapDataReader().read(data))
    }
    Result process(Graph graph) {
        LinkedList<Point> queue = new LinkedList<>()
        for(Point lowestPoint: graph.lowestPoints) queue.add(lowestPoint)

        while(!queue.isEmpty()) {
            Point p = queue.poll()

            for(Point higherPoint : p.edgesToHigherLevel) {
                boolean updateRef = higherPoint.refToLowerPoint == null
                int newPathLength = p.pathLength + 1
                long newPathVal = p.pathVal + (higherPoint.val - p.val)

                if(!updateRef) {
                    updateRef = (higherPoint.pathLength < newPathLength) ||
                            ((higherPoint.pathLength == newPathLength) && (higherPoint.pathVal < newPathVal) )
                }
                if(updateRef) {
                    higherPoint.refToLowerPoint = p
                    higherPoint.pathVal = newPathVal
                    higherPoint.pathLength = newPathLength
                }
                queue.add(higherPoint)
            }
        }

        List<Point> sortedHighestPoints = graph.highestPoints.sort()
        Point highestPoint = sortedHighestPoints.get(0)
        return new Result(maxVal: highestPoint.pathVal, maxLength: highestPoint.pathLength, description: highestPoint.printPath())
    }
}

class MapDataReader {

    /**
     *
     * @param input either URL String or List<String>
     * @return
     */
    Graph read(def input) {
        Point[][] points = null
        List<Point> highestPoints = [], lowestPoints = []
        int idx = 0;
        boolean firstTime = true
        Closure processLine = { String line ->
            List<Integer> rawVals = line.split(/\s+/).collect { Integer.parseInt(it) }
            if(firstTime) {
                points = new Point[rawVals.get(0)][rawVals.get(1)]
                firstTime = false
                idx++
                return
            }
            int X = points[0].length, Y = points.length
            points[idx - 1] = rawVals.collect { new Point(val : it) }.toArray(new Point[X])

            def rowIdxs = []
            if(idx - 2 >=0) rowIdxs.add(idx - 2)
            if(idx == Y) rowIdxs.add(idx - 1)
            rowIdxs.each { int i ->
                for(int j=0;j<X;j++) {
                    Point here = points[i][j]
                    int countHigher = 0, countLower = 0
                    if(i>0) {
                        Point top = points[i-1][j]
                        if(here.val > top.val) {
                            countLower ++
                        } else if (here.val < top.val) {
                            here.edgesToHigherLevel.add(top)
                            countHigher ++
                        }
                    }
                    if(i+1 < Y) {
                        Point bottom = points[i+1][j]
                        if(here.val > bottom.val) {
                            countLower ++
                        } else if (here.val < bottom.val) {
                            here.edgesToHigherLevel.add(bottom)
                            countHigher ++
                        }
                    }
                    if(j>0) {
                        Point left = points[i][j-1]
                        if(here.val > left.val) {
                            countLower ++
                        } else if (here.val < left.val) {
                            here.edgesToHigherLevel.add(left)
                            countHigher ++
                        }
                    }
                    if(j+1 < X) {
                        Point right = points[i][j+1]
                        if(here.val > right.val) {
                            countLower ++
                        } else if (here.val < right.val) {
                            here.edgesToHigherLevel.add(right)
                            countHigher ++
                        }
                    }

                    if(countLower == 0) lowestPoints.add(here)
                    if(countHigher == 0) highestPoints.add(here)
                }
            }
            idx++
        }
        if(input instanceof String) {
            input.toURL().eachLine(processLine)
        } else {
            input.each(processLine)
        }
        return new Graph(highestPoints: highestPoints, lowestPoints : lowestPoints)
    }
}

class Graph {
    List<Point> highestPoints, lowestPoints
}

class Point implements Comparable<Point> {
    private static final String TO = "->"
    int val

    long pathVal = 0
    int pathLength = 1
    Point refToLowerPoint = null

    List<Point> edgesToHigherLevel = []

    @Override
    int compareTo(Point o) {
        if(this.pathLength == o.pathLength) return o.pathVal <=> this.pathVal
        return o.pathLength <=> this.pathLength
    }

    String printPath() {
        StringBuffer sb = new StringBuffer()
        Point p = this
        while(p) {
            sb.append(p.val)
            p = p.refToLowerPoint
            if(p) sb.append(TO)
        }
        sb.toString()
    }
}

class Result {
    long maxVal
    int maxLength
    String description
}
