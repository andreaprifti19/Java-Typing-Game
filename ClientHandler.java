package com.company;

import java.io.*;
import java.net.Socket;
import java.lang.*;

class ClientHandler extends Thread {
    final DataInputStream input;
    final DataOutputStream output;
    final Socket connection;
    public Db db;
    public Player player;
    public Team teamcreated;
    public ObjectOutputStream oos;
    double teamtime;

    public ClientHandler(Socket connection, DataInputStream input, DataOutputStream output, Db db, Player player) {
        this.connection = connection;
        this.input = input;
        this.output = output;
        this.db = db;
        this.player = player;
    }

    public void run() { //run thread
        try {
            OutputStream os = connection.getOutputStream();
            oos = new ObjectOutputStream(os);
        } catch (IOException e) {
            e.printStackTrace();
        }

        new BufferedReader(new InputStreamReader(System.in));
        boolean logged_in = false;
        boolean cont = true;
        String text = "According to all laws of aviation, there is no way a bee should be able to fly.";

        try {
            this.output.writeUTF("Welcome\n");

            label50:
            while(true) { //loop for main screen
                while(true) {
                    if (!cont) {
                        break label50;
                    }
                    this.output.writeUTF("Please choose one of the functionalities:\n1.Register     2.Log In     3.Join Team/Play");
                    this.output.flush();
                    int func = this.input.readInt();
                    switch(func) {
                        case 1: //register into server
                            String request_username = "Please enter your desired username: ";
                            this.output.writeUTF(request_username);
                            this.output.flush();
                            String username = this.input.readUTF(); //get username from user
                            String request_password = "Please enter your desired password: ";
                            this.output.writeUTF(request_password);
                            this.output.flush();
                            String password = this.input.readUTF(); //get password from user
                            this.db.registered.put(username, password); //put user/pass combos in the database hashmap
                            this.output.writeUTF("You are now registered! Please proceed to login in order to play the game. ");
                            this.output.flush();
                            break;
                        case 2: //log in
                            if (!logged_in) {  //check whether the player is logged in or not
                                this.output.writeUTF("n");
                                this.output.writeUTF("Enter your username: ");
                                this.output.flush();
                                String given_username = this.input.readUTF();
                                this.output.writeUTF("Enter your password: ");
                                this.output.flush();
                                String given_password = this.input.readUTF(); //get username and password for login
                                if (this.db.registered.containsKey(given_username)) { //if hashmap contains this username
                                    String upass = (String)this.db.registered.get(given_username);
                                    if (upass.equals(given_password)) { //and if this username matches the password
                                        this.output.writeUTF("You are now logged in, and may continue to play the game.");
                                        this.output.flush();
                                        logged_in = true; //player is logged in
                                    } else {
                                        this.output.writeUTF("Wrong credentials. Please try to login again, or register if you have not done so already!"); //else return player to main screen
                                    }
                                } else {
                                    output.writeUTF("Wrong credentials. Please try to login again, or register if you have not done so already!"); //else player is not logged in and returned to main screen
                                }
                                break;
                            } else {
                                this.output.writeUTF("y");
                            }
                        case 3: //join team
                            if (!logged_in) { //return player to main screen if he is not logged in
                                this.output.writeUTF("n");
                                break;
                            } else {
                                this.output.writeUTF("y");
                                this.output.flush();
                                this.output.writeUTF("Waiting for another player to join");
                                this.output.flush();
                                this.db.quePlayer(this.player);
                                teamcreated = this.db.createTeam(); //create team
                                this.output.writeUTF("Team Created!");
                                this.output.flush();
                                Thread.sleep(1000L);
                                this.db.Listindex();
                                if (this.input.readUTF().equals("y")) { //get confirmation that client is ready
                                    this.player.ready = true; //declare player as ready
                                    teamcreated.playersReady();
                                    this.output.writeUTF("Game Starting in:");
                                }

                                this.output.writeUTF("3");
                                this.output.flush();
                                Thread.sleep(1000L);
                                this.output.writeUTF("2");
                                this.output.flush();
                                Thread.sleep(1000L);
                                this.output.writeUTF("1");
                                this.output.flush();
                                Thread.sleep(1000L);
                                this.output.writeUTF("GO!");
                                this.output.flush(); //synchronised countdown
                                if (this.teamcreated.goFirst(player)) { //if client is the player that is chosen to go first
                                    this.output.writeUTF("y"); //send a string "y" to let the client know he is first if he is chosen to go first
                                    this.output.flush();
                                    this.output.writeUTF(text); //send the string of text the player has to type
                                    this.output.flush();
                                    double start1 = System.currentTimeMillis(); //start timer once the text is send
                                    String text1 = input.readUTF(); //receive text
                                    double end1 = System.currentTimeMillis(); //end timer once text has been received.
                                    if (text1.equals(text)) { //if the text the client submitted is correct, set player.correct to true
                                        this.teamcreated.p1.correct = true;
                                    }
                                    double time1 = end1 - start1;
                                    double time1s = time1 / 1000; //total time in seconds
                                    this.db.playerTime(time1s); //submit player time
                                    this.teamcreated.p1.finished = true; //declare the player has finished
                                    while (!(this.teamcreated.p2.finished)) { //sleep until the other player has finished typing
                                        Thread.sleep(1000L);
                                    }
                                }
                                else { //code for the second player
                                    this.output.writeUTF("n"); //send a string "n" to let the player know he is second player
                                    this.output.flush();
                                    this.output.writeUTF("n");
                                    this.output.flush();
                                    while (this.teamcreated.p1.finished == false) {
                                        Thread.sleep(500L);
                                        if (this.teamcreated.p1.finished == true) {
                                            break;
                                        } //sleep until the first player has finished
                                    }
                                    this.output.writeUTF(text);//send text to second player
                                    this.output.flush();
                                    double start2 = System.currentTimeMillis(); //start timer
                                    String text2 = input.readUTF();
                                    double end2 = System.currentTimeMillis();
                                    if (text2.equals(text)) { //check if the text received is correct
                                        this.teamcreated.p2.correct = true;
                                    }
                                    double time2 = end2 - start2;
                                    double time2s = time2 / 1000; //convert time to seconds
                                    this.db.playerTime(time2s); //add time of second player
                                    this.teamcreated.p2.finished = true; //declare second player as finished

                                }

                                if (this.teamcreated.p1.correct && this.teamcreated.p2.correct) { //if both player submitted valid text
                                    if (!(this.teamcreated.goFirst(player))) { //the first player declared the methods that calculate the time
                                        teamtime = this.db.teamTime();
                                        this.db.recordBoard.add(teamtime); //add time to leaderboard
                                        this.db.sortBoard(); //sort board from the best time.
                                    } else {
                                        Thread.sleep(2000L);
                                        teamtime = this.db.teamTime();
                                        this.db.removeTime(); //removes time from temporary list
                                    }
                                    this.oos.writeObject("Game Finished! \nYour team's time is: " + teamtime + " seconds." + "\nLeaderboard from all games: " + this.db.recordBoard);
                                    this.oos.flush(); //let the player know the game is finished and give him his time.
                                    if (teamtime == this.db.recordBoard.get(0)) { //check if the time of the player is the same as the top result in leaderboard
                                        this.output.writeUTF("Congratulations, your time is the new record!\nMaybe try beating the record again?"); //congratulate if first
                                        this.output.flush();
                                    } else {
                                        this.output.writeUTF("Thanks for playing!\nYou didnt get the first place,but you can try beating the current record with another game. ");
                                        this.output.flush(); break;
                                    }
                                }
                                else { //let the player know his text was not valid and return him to main screen
                                    this.oos.writeObject(("The text submitted was not correct, and thus was not counted as a result \nYou may start a new game."));
                                    this.oos.flush(); break;
                                }
                            }
                        default:
                            System.out.println("User entered invalid function"); //code if user enters another function other than the defined ones in the main screen
                    }
                }
            }
        } catch (InterruptedException | IOException var16) {
            System.out.println("Error");
        }

        try { //close sockets and streams
            this.input.close();
            this.output.close();
            this.connection.close();
        } catch (IOException var15) {
            System.out.println("User disconnected");
        }

    }
}
