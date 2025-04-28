package com.company; //rei


import java.lang.reflect.Array;
import java.util.*;

public class Team {
    public com.company.Player p1;
    public com.company.Player p2;

    public Team(com.company.Player p1, com.company.Player p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void playersReady() throws InterruptedException {
        while(!this.p1.ready || !this.p2.ready) { //put thread to sleep until both players are ready
            Thread.sleep(1000L);
        }

    }
    public boolean goFirst (Player player){ //chooses which player goes first
        if (player.ID == p1.ID){ //if player 1 id is the same as the id of the first player in the database return true
            return true;
        }
        else return false;
    }

}
