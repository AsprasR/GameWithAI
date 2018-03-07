/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import Box.*;
import GUI.BoxColour;
import java.awt.Color;

/**
 *
 * @author Nightspire
 
public class Testowe {

    private int sum;
    private final int col, rows;
    private int maxMoves, removePos;
    private final BoxColour[][] points;
    private PosPoints posPoints;
    private final Coords coords;
    private final int depth = 6;
    private BoxColour point1;
    private BoxColour point2;
    private boolean check;
    private int result;
    private boolean noPoint = true;

    public Testowe(int col, int rows, BoxColour[][] points, Coords coords) {
        sum = 2 * (col * col - col);
        maxMoves = 0;
        removePos = 0;
        this.col = col;
        this.rows = rows;
        this.points = points;
        posPoints = new PosPoints();
        this.coords = coords;
    }

    private int check(Coords coords) {
        int wynik = 0;
        for (int i = 0; i < col; i++) {
            for (int j = 0; j < rows; j++) {
                if (coords.contains(points[i][j])) {
                    if (i + 1 < col && coords.contains(points[i + 1][j])) {
                        wynik++;
                    }
                    if (j + 1 < rows && coords.contains(points[i][j + 1])) {
                        wynik++;
                    }
                }
            }
        }
        maxMoves++;
        return wynik;
    }



    private int comPoint(int result, int k, int a, int b) {
        //       System.out.println("k " + k + " " + result);
        //     System.out.println("A PRZED: " + a + " " + b);
        if (k % 2 == 1) {
            if (a < result) {
                a = result;
                //            System.out.println("A PO " + a + " B " + b);
            }
            return a;
        } else {
            if (b > result) {
                b = result;
                //            System.out.println("A PO " + a + " B " + b);
            }
            return b;
        }
    }

    private void addPoint(int i, int j, BoxColour point) {
        point1 = points[i][j];
        point2 = point;
        noPoint = false;
    }

    private int alfaBeta(Coords coordsTmp, int i, int j, int level, BoxColour point, int alfa, int beta) {
        int tmp1, tmp2;
        coordsTmp.removeCoords(points[i][j]);
        //      if (noPoint) {
        //           point1 = points[i][j];
        //          point2 = point;
        //      }
        if (level == depth - 1 || depth == 0) {
            tmp1 = check(coordsTmp);
            tmp2 = comPoint(tmp1, level, alfa, beta);
            if (tmp1 == 0 && level % 2 == 1) {
                alfa = Integer.MAX_VALUE;
            } else if (level % 2 == 0) {
                beta = tmp2;
            } else {
                alfa = tmp2;
            }
        }
        if (level < depth - 1) {
            int diffValue = find(coordsTmp, level + 1, alfa, beta); 
            //    System.out.println("K = " + k);
            if (diffValue == -1) {    //Wartość -1 oznacza, że występuje wartość zerowa w danej gałęzi
                //          System.out.println("TUTAJ" + a + " " + b);
                alfa = Integer.MAX_VALUE;
                diffValue = Integer.MAX_VALUE;
            } else if (diffValue == -2) {
                beta = 0;
                diffValue = 0;
                addPoint(i, j, point);
            }
            //        System.out.println("AAA PRZED: " + a + " " + b);
            if (level % 2 == 0 && beta > diffValue) {
                beta = diffValue;
                addPoint(i, j, point);
            } else if (level % 2 == 1 && alfa < diffValue) {
                alfa = diffValue;
                addPoint(i, j, point);
            }
            //        System.out.println("AAA PO " + a + " B " + b);
        }
        coordsTmp.setCoords(points[i][j]);

        //     if (!check ) {
        //          depth = k;
        //     }
        //   if (k <= 2) {
        //       System.out.println(k + " " + tmp);
        //   }
        if (level % 2 == 1) {
            return alfa;
        } else {
            return beta;
        }
    }

    private int find(Coords coords, int level, int alfa, int beta) {
//        PosPoints posTmp = new PosPoints(posPoints.getList());
        check = false;
        if (level % 2 == 0) {
            result = beta;
        } else {
            result = alfa;
        }
        Coords coordsTmp = new Coords(coords.getCoords());
        for (BoxColour point : this.coords.getCoords()) {
            if (coordsTmp.contains(point)) {
                coordsTmp.removeCoords(point);
                int i = point.getCol();
                int j = point.getRow();
                if (i + 1 < col && coordsTmp.contains(points[i + 1][j])) {
                    if (level % 2 == 0) {
                        result = alfaBeta(coordsTmp, i + 1, j, level, point, alfa, result);
                    } else {
                        result = alfaBeta(coordsTmp, i + 1, j, level, point, result, beta);
                    }

                    check = true;
                }
                //        posPoints = posTmp;
                if (j + 1 < rows && coordsTmp.contains(points[i][j + 1])) {
                    if (level % 2 == 0) {
                        result = alfaBeta(coordsTmp, i, j + 1, level, point, alfa, result);
                    } else {
                        result = alfaBeta(coordsTmp, i, j + 1, level, point, result, beta);
                    }
                    check = true;
                }
                if (alfa >= beta || alfa == Integer.MAX_VALUE || beta == 0 ) {
                    break;
                }
                //          posPoints = posTmp;
                coordsTmp.setCoords(point);
            }
        }
        if (!check) {
            if (level % 2 == 0) {
                return -1;
            } else {
                return -2;
            }
        }
        //   System.out.println("Result " + result);
        return result;
    }

    public void findTheBestPoints() {
        long start = System.nanoTime();
        maxMoves = 0;
        check = false;
        int a = Integer.MIN_VALUE, b = Integer.MAX_VALUE;
        result = -1;
        //      removePos = 0;
        //   posPoints = bettercheck(point, coords, posPoints, sum, 0);
        //     sum -= removePos;
        //  check(coords, 0, 0);
        //   System.out.println(posPoints);
        int czy = find(coords, 0, a, b);
        System.out.println("Wybrane " + czy);
        coords.removeCoords(point1);
        coords.removeCoords(point2);
        point1.setState(Color.YELLOW);
        point2.setState(Color.YELLOW);
        System.out.println("sum " + maxMoves);
        System.out.println("Czas: " + (System.nanoTime() - start) / 10e6 + "ms");
    }
}

/*                    coordsTmp.removeCoords(points[i + 1][j]);
                    removePos = 0;
                    //                posPoints = bettercheck(point, coordsTmp, posPoints, sum, k+1);
                    if (k == depth - 1 || depth == 0) {
                        tmp1 = check(coordsTmp, tmp, k);
                        check = true;
                        if (tmp1 != tmp) {
                            point1 = points[i][j];
                            point2 = points[i + 1][j];
                            tmp = tmp1;
                        }
                    }
                    if (k < depth - 1) {
                        heh = loop(coordsTmp, k + 1, posPoints, sum - removePos);
                        if (tmp == -1 || tmp > heh && k % 2 == 0 || tmp < heh && k % 2 == 1) {
                            point1 = points[i][j];
                            point2 = points[i + 1][j];
                            tmp = heh;
                            if (tmp > heh && k % 2 == 0 && a >= b) {
                                return a;
                            } else if (tmp < heh && k % 2 == 1 && a >= b) {
                                return b;
                            }
                        }
                    }

                    coordsTmp.setCoords(points[i + 1][j]);*/
 /*                    coordsTmp.removeCoords(points[i][j + 1]);
                    removePos = 0;
                    //          posPoints = bettercheck(point, coordsTmp, posPoints, sum, k+1);
                    if (k == depth - 1 || depth == 0) {
                        tmp1 = check(coordsTmp, tmp, k);
                        check = true;
                        if (tmp1 != tmp) {
                            point1 = points[i][j];
                            point2 = points[i][j + 1];
                            tmp = tmp1;
                        }
                    }
                    if (k < depth - 1) {
                        heh = loop(coordsTmp, k + 1, posPoints, sum - removePos);
                        if (tmp == -1 || tmp > heh && k % 2 == 0 || tmp < heh && k % 2 == 1) {
                            point1 = points[i][j];
                            point2 = points[i][j + 1];
                            tmp = heh;
                            if (tmp > heh && k % 2 == 0 && a >= b) {
                                return a;
                            } else if (tmp < heh && k % 2 == 1 && a >= b) {
                                return b;
                            }
                        }
                    }

                    coordsTmp.setCoords(points[i][j + 1]);*/
