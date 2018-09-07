/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.lb.AStar.model;

/**
 *
 * @author Philip
 */
public enum Direction {
    N, NE, E, SE, S, SW, W, NW;
    
    private Direction opposite;
    
    static {
        N.opposite = S;
        NE.opposite = SW;
        E.opposite = W;
        SE.opposite = NW;
        S.opposite = N;
        SW.opposite = NE;
        W.opposite = E;
        NW.opposite = SE;
    }
    
    public Direction inverse() {
        return opposite;
    }
    
    public Direction[] diagonal() {
        Direction[] diagonals = {NE, SE, SW, NW};
        return diagonals;
    }
    
    public Direction[] normal() {
        Direction[] normals = {N, S, W, E};
        return normals;
    }
}
