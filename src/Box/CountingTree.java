package Box;

import GUI.BoxColour;
import java.awt.Color;
import java.util.Random;

public class CountingTree {

    private final int col, row;
    private int maxMoves;
    private final BoxColour[][] points;
    private final Coords coords;
    private int depth, equal;
    private BoxColour point1, point2;
    private boolean noPoint;
    private int posMoves, countElem;
    private boolean begin = false, end = false, start = false;
    private int x;
    private long startTime;

    public CountingTree(int rows, int col, BoxColour[][] points, Coords coords) {
        this.row = rows;
        this.col = col;
        this.points = points;
        this.coords = coords;
    }

    private int count(Coords coords) {
        int countPoss = 0;
        int i, j;
        Coords coordsTmp = new Coords(coords.getCoords());
        for (BoxColour point : this.coords.getCoords()) {
            if (coordsTmp.contains(point)) {
                i = point.getRow();
                j = point.getCol();
                if (i + 1 < row && coordsTmp.contains(points[i + 1][j])) {
                    countPoss++;
                }
                if (j + 1 < col && coordsTmp.contains(points[i][j + 1])) {
                    countPoss++;
                }
            }
        }
        maxMoves++;
        return countPoss;
    }

    private int countScales(Coords coords, int level) {
        int countPoss = 0;
        int countHoles = 0, blackPoles = 0, whitePoles = 0;
        countElem = 0;
        int i, j, countBlock, value = 15;
        Coords coordsTmp = new Coords(coords.getCoords());
        for (BoxColour point : this.coords.getCoords()) {
            if (coordsTmp.contains(point)) {
                i = point.getRow();
                j = point.getCol();
                countBlock = 0;
                if (i + 1 < row && coordsTmp.contains(points[i + 1][j])) {
                    countPoss++;
                    countBlock++;
                }
                if (j + 1 < col && coordsTmp.contains(points[i][j + 1])) {
                    countBlock++;
                    countPoss++;
                }
                if (j - 1 >= 0 && coordsTmp.contains(points[i][j - 1])) {
                    countBlock++;
                }
                if (i - 1 >= 0 && coordsTmp.contains(points[i - 1][j])) {
                    countBlock++;
                }
                if (countBlock == 0) {
                    countElem++;
                } else {
                    if (i % 2 == 0 && j % 2 == 0 || i % 2 == 1 && j % 2 == 1) {
                        whitePoles++;
                    }
                    if (i % 2 == 0 && j % 2 == 1 || i % 2 == 1 && j % 2 == 0) {
                        blackPoles++;
                    }
                }
                if (countBlock == 1) {
                    countHoles++;
                }
            }
        }
        int blocks = Math.min(whitePoles, blackPoles);
        int val = (countPoss - countHoles) % 2;
        if (countPoss - countHoles <= 0) {
            int poss = (coordsTmp.size() - countElem - countHoles) % 2;
            val = (countPoss - poss) % 2;
        }
        //    System.out.println("Blocks " + blocks);
        //     System.out.println("Holes " + countHoles);
        //    System.out.println("Elem " + countElem);
        if (level % 2 == 0) {
            if (val == 1) {
                value += 10;
            } else {
                value -= 10;
            }
            if (blocks % 2 == 1) {
                value += 5;
            } else if (blocks % 2 == 0) {
                value -= 5;
            }
        } else {
            if (val == 1) {
                value -= 10;
            } else {
                value += 10;
            }
            if (blocks % 2 == 0) {
                value += 5;
            } else if (blocks % 2 == 1) {
                value -= 5;
            }
        }
        return value;
    }

    private void addPoint(BoxColour tmpPoint, int level, int alfa, int beta, int a, int b) {
        if (level == 0) {
            if (posMoves > beta && alfa != Integer.MAX_VALUE || noPoint) {
                posMoves = beta;
                point1 = tmpPoint;
                point2 = points[tmpPoint.getRow() + a][tmpPoint.getCol() + b];
                System.out.println(point1);
                noPoint = false;
            }
        }
    }

    private int comPoint(int result, int level, int alfa, int beta) {
        //  System.out.println("AAA PRZED: " + alfa + " " + beta);
        if (level % 2 == 1) {
            if (alfa < result) {
                alfa = result;
            }
            //     System.out.println("AAA PO " + alfa + " B " + beta); 
            return alfa;
        } else {
            if (beta > result) {
                beta = result;
            }
            //      System.out.println("AAA PO " + alfa + " B " + beta); 
            return beta;
        }
    }

    public int diffValue(Coords coordsTmp, int level, int alfa, int beta) {
        int diffValue = find(coordsTmp, level + 1, alfa, beta);
        //    System.out.println("K = " + level);
        if (diffValue == -1) {
            alfa = Integer.MAX_VALUE;
            diffValue = Integer.MAX_VALUE;
        } else if (diffValue == -2) {
            diffValue = 0;
            beta = 0;
        }
        return comPoint(diffValue, level, alfa, beta);

    }

    private int alfaBeta(Coords coordsTmp, int i, int j, int level, int alfa, int beta) {
        int tmp1, tmp2, tmp3, value = 0;
        coordsTmp.removeCoords(points[i][j]);
        if (level == depth - 1 && !end) {
            tmp1 = countScales(coordsTmp, level);
            //      System.out.println("Wynik " + tmp1);
            tmp2 = comPoint(tmp1, level, alfa, beta);
            if (equal == depth && coordsTmp.size() - countElem < 60) {
                if (tmp2 > 5 &&  tmp2 < 20 && level % 2 == 0 || tmp2 > 15 && level % 2 == 1) {
                    depth++;
                    if (level % 2 == 0) {
                        tmp3 = find(coordsTmp, level + 1, tmp2, beta);
                    } else {
                        tmp3 = find(coordsTmp, level + 1, alfa, tmp2);
                    }
                    if (tmp2 < 15 && tmp3 > 15) {
                        return -1; 
                    } else if( tmp3 == 0 && level % 2 == 1 ) {
                        tmp2 = tmp3;
                    } else if (tmp3 < tmp2 && tmp3 < 10) {
                        tmp2 = tmp3;
                    }
                    start = true;
                    if (equal != depth) {
                        depth = equal;
                    } else if (start) {
                        if (level % 2 == 0) {
                            tmp2 = beta;
                        } else {
                            tmp2 = alfa;
                        }
                    }
                }
            }
            value = tmp2;
        }
        if (level < depth - 1) {
            value = diffValue(coordsTmp, level, alfa, beta);
        }
        coordsTmp.setCoords(points[i][j]);
        return value;
    }

    private int find(Coords coords, int level, int alfa, int beta) {
        boolean check = false;
        start = false;
        BoxColour tmpPoint = points[0][0];
        int i, j;
        Coords coordsTmp = new Coords(coords.getCoords());
        for (BoxColour point : this.coords.getCoords()) {
            if (coordsTmp.contains(point)) {
                coordsTmp.removeCoords(point);
                i = point.getRow();
                j = point.getCol();
                if (i + 1 < row && coordsTmp.contains(points[i + 1][j])) {
                    if (level == 0) {
                        tmpPoint = point;
                    }
                    if (level % 2 == 0) {
                        beta = alfaBeta(coordsTmp, i + 1, j, level, alfa, beta);
                    } else {
                        alfa = alfaBeta(coordsTmp, i + 1, j, level, alfa, beta);
                    }
                    begin = true;
                    check = true;
                    addPoint(tmpPoint, level, alfa, beta, 1, 0);
                }
                if( beta == -1 || alfa == -1 ) {
                    coordsTmp.setCoords(point);
                    return -1;
                } else if( beta == -2 || alfa == -2 ) {
                    coordsTmp.setCoords(point);
                    return -2;
                }
                if (j + 1 < col && coordsTmp.contains(points[i][j + 1])) {
                    if (level == 0) {
                        tmpPoint = point;
                    }
                    if (level % 2 == 0) {
                        beta = alfaBeta(coordsTmp, i, j + 1, level, alfa, beta);
                    } else {
                        alfa = alfaBeta(coordsTmp, i, j + 1, level, alfa, beta);
                    }
                    begin = true;
                    check = true;
                    addPoint(tmpPoint, level, alfa, beta, 0, 1);
                }
                coordsTmp.setCoords(point);
                if( beta == -1 || alfa == -1 ) {
                    return -1;
                } else if( beta == -2 || alfa == -2 ) {;
                    return -2;
                }
                if ((System.nanoTime() - startTime) / 10e5 >= 440) {
           //         end = true;
                }
                if (alfa >= beta || end ) {
                    check = true;
                    break;
                }  
            }
        }
        if (!check) {
            if (!begin) {
                depth = level;
                begin = true;
            }
            if (level % 2 == 0) {
                //   System.out.println("NLev" + level + " " + " Result " + Integer.MAX_VALUE);
                return -1;
            } else {
                //    System.out.println("GLev" + level + " " + " Result " + 0);
                return -2;
            }
        }
        if (level % 2 == 1) {
            //  System.out.println("Lev" + level + " " + " Result " + alfa);
            return alfa;
        } else {
            //  System.out.println("Lev" + level + " " + " Result " + beta);
            return beta;
        }
    }

    public void getRandom(Random r) {
        if (r.nextInt(10) % 2 == 0) {
            point1 = points[r.nextInt(row)][r.nextInt(col - 1)];
            point2 = points[point1.getRow()][point1.getCol() + 1];
        } else {
            point1 = points[r.nextInt(row - 1)][r.nextInt(col)];
            point2 = points[point1.getRow() + 1][point1.getCol()];
        }
    }

    public void findTheBestPoints() {
        startTime = System.nanoTime();
        maxMoves = 0;
        posMoves = countScales(coords, 0);
        int size = coords.size() - countElem;
        System.out.println(size);
        if (size >= 600) {
            Random r = new Random();
            getRandom(r);
            while (!coords.contains(point1) || !coords.contains(point2)) {
                getRandom(r);
            }
        } else {
            noPoint = true;
            if (size >= 225 && size < 600) {
                depth = 1;
            } else if (size >= 90 && size < 225) {
                depth = 2;
            } else if (size >= 51 && size < 90) {
                depth = 3;
            } else if (size >= 35 && size < 51) {
                depth = 4;
            } else if (size >= 22 && size < 35) {
                depth = 5;
            } else if (size >= 18 && size < 22) {
                depth = 6;
            } else if (size < 20) {
                depth = 7;
            }
            equal = depth;
            end = false;
            int alfa = Integer.MIN_VALUE, beta = Integer.MAX_VALUE;
            System.out.println("Wybrane " + find(coords, 0, alfa, beta));
        }
        coords.removeCoords(point1);
        coords.removeCoords(point2);
        //   System.out.println("Usunięto " + point1 + " " + point2);
        point1.setState(Color.YELLOW);
        point2.setState(Color.YELLOW);
        //   System.out.println("sum " + maxMoves);
        System.out.println("Czas: " + (System.nanoTime() - startTime) / 10e5 + "ms");
    }
}
