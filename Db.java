package com.company;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class Db {
    LinkedHashMap<String, String> registered = new LinkedHashMap(); //hashmap with the user/pass combos of users
    LinkedList<Player> userslist = new LinkedList(); //list of players waiting to join a team think of it as a lobby.
    int count = 0; //count of players that are waiting to join a team
    int index = 0;
    public LinkedList <Double> timelist= new LinkedList<> (); //temporary list for the user times
    public LinkedList<Double> recordBoard = new LinkedList (); //leaderboard

    public Db() {
    }

    public void quePlayer(Player player) {
        ++this.count;
        this.userslist.add(player); //put player in a list "lobby" with players ready to join a team
    }

    public Team createTeam() throws InterruptedException { //create team
        while(this.count < 2) {
            Thread.sleep(1000L); //wait until there are 2 players ready to join a team
        }

        Player p1 = (Player)this.userslist.get(this.index);
        Player p2 = (Player)this.userslist.get(this.index + 1);
        Team team = new Team(p1, p2);
        return team; //create a team with this 2 players
    }

    public void Listindex() {
        this.count = 0; //set the count of players waiting to join a team to 0
        ++this.index; //moves the index of the lobby to skip the players that joined and create teams from new players.

    }

    public void sortBoard(){
        Collections.sort(recordBoard); //sort leaderboard from the best time
    }

    public void playerTime (double t){
        timelist.add(t); //add the time of the player to a temporary list
    }
    public double teamTime (){ //get time of both players from the temporary list and add them together
        double t1 = timelist.get(0);
        double t2 = timelist.get(1);
        double total = t1+t2;
        return total;

    }

    public void removeTime (){ //remove time from the temporary list
        timelist.remove(0);
        timelist.remove(0);
    }


}
